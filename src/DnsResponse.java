import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class DnsResponse{
	private byte[] response;
	private int requestSize;
    private byte[] ID;
    private boolean QR, AA, TC, RD, RA;
    private int RCode, QDCount, ANCount, NSCount, ARCount;
    private DNSRecord[] records;
    private QueryType queryType;
    //TODO: MX, NS, Parse multiple answers

	public DnsResponse(byte[] response, int requestSize, QueryType queryType) throws Exception {
		this.response = response;
		this.requestSize = requestSize;
		this.queryType = queryType;

        this.validateResponseQuestionType();
        this.parseHeader();
        
        records = new DNSRecord[ANCount];
        int offSet = requestSize;
        for(int i = 0; i < ANCount; i ++){
        	records[i] = this.parseAnswer(offSet);
        	offSet += records[i].getByteLength();
        }

        
        this.checkRCodeForErrors();
        this.validateQueryTypeIsResponse();
    }

    public void outputResponse() {
        if (this.ANCount <= 0) {
            System.out.println("NOTFOUND");
            return;
        }

        System.out.println("***Answer Section (" + this.ANCount + " records)***");

        for (DNSRecord record : records){
        	record.outputRecord();	
        }

        if (this.ARCount > 0) {
            System.out.println("***Additional Section ([num-additional] records)***");
            //TODO additional sections
        }
    }

	
	private void checkRCodeForErrors() {
	    switch( this.RCode) {
            case 0:
                //No error
                break;
            case 1:
                throw new RuntimeException("Format error: the name server was unable to interpret the query");
            case 2:
                throw new RuntimeException("Server failure: the name server was unable to process this query due to a problem with the name server");
            case 3:
                throw new MissingDomainException();
            case 4:
                throw new RuntimeException("Not implemented: the name server does not support the requested kind of query");
            case 5:
                throw new RuntimeException("Refused: the name server refuses to perform the requested operation for policy reasons");
        }
    }

    private void parseHeader(){
        //ID
        byte[] ID = new byte[2];
        ID[0] = response[0];
        ID[1] = response[1];
        this.ID = ID;

        //QR
        this.QR = getBit(response[2], 7) == 1;

        //AA
        this.AA = getBit(response[2], 2) == 1;

        //TC
        this.TC = getBit(response[2], 1) == 1;

        //RD
        this.RD = getBit(response[2], 0) == 1;

        //RA
        this.RA = getBit(response[3], 7) == 1;

        //RCODE
        this.RCode = response[3] & 0x0F;

        //QDCount
        byte[] QDCount = { response[4], response[5] };
        ByteBuffer wrapped = ByteBuffer.wrap(QDCount);
        this.QDCount = wrapped.getShort();

        //ANCount
        byte[] ANCount = { response[6], response[7] };
        wrapped = ByteBuffer.wrap(ANCount);
        this.ANCount = wrapped.getShort();

        //NSCount
        byte[] NSCount = { response[8], response[9] };
        wrapped = ByteBuffer.wrap(NSCount);
        this.NSCount = wrapped.getShort();

        //ARCount
        byte[] ARCount = { response[10], response[11] };
        wrapped = ByteBuffer.wrap(ARCount);
        this.ARCount = wrapped.getShort();
    }

    private DNSRecord parseAnswer(int index){
    	DNSRecord result = new DNSRecord(this.AA);
    	
        String domain = "";
        int countByte = index;

        rDataEntry domainResult = getDomainFromIndex(countByte);
        countByte += domainResult.getBytes();
        domain = domainResult.getDomain();
        
        //Name
        result.setAns_name(domain);

        //TYPE
        byte[] ans_type = new byte[2];
        ans_type[0] = response[countByte];
        ans_type[1] = response[countByte + 1];
        
        result.setAns_type(getQTYPEFromByteArray(ans_type));

        countByte += 2;
        //CLASS
        byte[] ans_class = new byte[2];
        ans_class[0] = response[countByte];
        ans_class[1] = response[countByte + 1];
        if (ans_class[0] != 0 && ans_class[1] != 1) {
            throw new RuntimeException(("ERROR\tThe class field in the response answer is not 1"));
        }
        result.setAns_class(ans_class);

        countByte +=2;
        //TTL
        byte[] TTL = { response[countByte], response[countByte + 1], response[countByte + 2], response[countByte + 3] };
        ByteBuffer wrapped = ByteBuffer.wrap(TTL);
        result.setAns_ttl(wrapped.getInt());

        countByte +=4;
        //RDLength
        byte[] RDLength = { response[countByte], response[countByte + 1] };
        wrapped = ByteBuffer.wrap(RDLength);
        int rdLength = wrapped.getShort();
        result.setAns_rdLength(rdLength);

        countByte +=2;
        switch (result.getAns_type()) {
            case A:
                result.setAns_domain(parseATypeRDATA(rdLength, countByte));
                break;
            case NS:
                result.setAns_domain(parseNSTypeRDATA(rdLength, countByte));
                break;
            case MX:
                result.setAns_domain(parseMXTypeRDATA(rdLength, countByte));
                break;
            case CNAME:
                this.parseCNAMETypeRDATA(rdLength, countByte);
                break;
        }
        result.setByteLength(countByte + rdLength - index);
        return result;
    }

    private String parseATypeRDATA(int rdLength, int countByte) {
        String address = "";
        byte[] byteAddress= { response[countByte], response[countByte + 1], response[countByte + 2], response[countByte + 3] };
        try {
            InetAddress inetaddress = InetAddress.getByAddress(byteAddress);
            address = inetaddress.toString().substring(1);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address;
        
    }

    private String parseNSTypeRDATA(int rdLength, int countByte) {
		rDataEntry result = getDomainFromIndex(countByte);
		String nameServer = result.getDomain();
    	
    	return nameServer;
    }

    private String parseMXTypeRDATA(int rdLength, int countByte) {
        //TODO add support for parsing this type of RDATA
    	return null;
    }

    private void parseCNAMETypeRDATA(int rdLength, int countByte) {
        //TODO add support for parsing this type of RDATA
    }

    private void validateQueryTypeIsResponse(){
        if (!this.QR) {
            throw new RuntimeException("ERROR\tInvalid response from server: Message is not a response");
        }
    }

    private void validateResponseQuestionType() {
        //Question starts at byte 13 (indexed at 11)
        int index = 12;

        while (this.response[index] != 0) {
            index++;
        }
        byte[] qType = {this.response[index + 1], this.response[index + 2]};

        if (this.getQTYPEFromByteArray(qType) != this.queryType) {
            throw new RuntimeException("ERROR\tResponse query type does not match request query type");
        }
    }

    private rDataEntry getDomainFromIndex(int index){
    	rDataEntry result = new rDataEntry();
    	int wordSize = response[index];
    	String domain = "";
    	boolean start = true;
    	int count = 0;
    	while(wordSize != 0){
			if (!start){
				domain += ".";
			}
	    	if ((wordSize & 0xC0) == (int) 0xC0) {
	    		byte[] offset = { (byte) (response[index] & 0x3F), response[index + 1] };
	            ByteBuffer wrapped = ByteBuffer.wrap(offset);
	            domain += getDomainFromIndex(wrapped.getShort()).getDomain();
	            index += 2;
	            count +=2;
	            wordSize = 0;
	    	}else{
	    		domain += getWordFromIndex(index);
	    		index += wordSize + 1;
	    		count += wordSize + 1;
	    		wordSize = response[index];
	    	}
            start = false;
            
    	}
    	result.setDomain(domain);
    	result.setBytes(count);
    	return result;
    }
    private String getWordFromIndex(int index){
    	String word = "";
    	int wordSize = response[index];
    	for(int i =0; i < wordSize; i++){
    		word += (char) response[index + i + 1];
		}
    	return word;
    }

    private int getBit(byte b, int position) {
    	return (b >> position) & 1;
    }

    private QueryType getQTYPEFromByteArray(byte[] qType) {
        if (qType[0] == 0) {
            if (qType[1] == 1) {
                return QueryType.A;
            } else if (qType[1] == 2) {
                return QueryType.NS;
            } else if (qType[1] == 15) {
                return  QueryType.MX;
            } else if (qType[1] == 5) {
            	return QueryType.CNAME;
            }else {
                throw new RuntimeException("ERROR\tUnrecognized query type in response");
            }
        } else {
            throw new RuntimeException("ERROR\tUnrecognized query type in response");
        }
    }
}
