import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class DnsResponse {
	private byte[] response;
	private ResponseResult result;
	private int requestSize;

	public DnsResponse(byte[] response, int requestSize){
		this.response = response;
		this.requestSize = requestSize;
    	result = new ResponseResult();
    }

    public ResponseResult parseResponse() {
    	parseHeader();
    	parseQuestion();
    	parseAnswer();
    	
    	return result;
    }

    private void parseAnswer(){
    	String domain = "";
    	int countByte = requestSize; //start byte
    	
    	//check if offset
    	if((response[countByte] & 0xC0) == (int) 0xC0){
        	byte[] offset = { (byte) (response[countByte] & 0x3F), response[countByte + 1] };
        	ByteBuffer wrapped = ByteBuffer.wrap(offset);
    		//get offset and then get name starting at that point
    		domain = getDomainFromIndex(wrapped.getShort());
    		countByte += 2;
    	}else{
    		domain = getDomainFromIndex(countByte);
    		countByte += domain.length();
    	}
    	
    	//Name
    	result.setAns_name(domain);
    	
    	
    	//TYPE
    	byte[] ans_type = new byte[2];
    	ans_type[0] = response[countByte];
    	ans_type[1] = response[countByte + 1];
    	result.setAns_type(ans_type);
    	
    	countByte += 2;
    	//CLASS
    	byte[] ans_class = new byte[2];
    	ans_class[0] = response[countByte];
    	ans_class[1] = response[countByte + 1];
    	result.setAns_class(ans_class);
    	
    	countByte +=2;
    	//TTL
    	byte[] TTL = { response[countByte], response[countByte + 1], response[countByte + 2], response[countByte + 3] };
    	ByteBuffer wrapped = ByteBuffer.wrap(TTL);
    	result.setAns_ttl(wrapped.getShort());
    	
    	countByte +=4;
    	//RDLength
    	byte[] RDLength = { response[countByte], response[countByte + 1] };
    	wrapped = ByteBuffer.wrap(RDLength);
    	int rdLength = wrapped.getShort();
    	result.setAns_rdLength(rdLength);
    	
    	countByte +=2;
    	
    	if (rdLength == 4){
    		byte[] IPAddress = { response[countByte], response[countByte + 1], response[countByte + 2], response[countByte + 3] };
    		try {
				InetAddress inetaddress = InetAddress.getByAddress(IPAddress);
				result.setIp_address(inetaddress.toString().substring(1));
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }

    private void parseQuestion(){
    	//TODO
    }

    private void parseHeader(){
    	//ID
    	byte[] ID = new byte[2];
    	ID[0] = response[0];
    	ID[1] = response[1];
    	result.setID(ID);
    	
    	//QR
    	result.setQR(getBit(response[2], 7) == 1);
    	
    	//OPCODE TODO
    	
    	//AA
    	result.setAA(getBit(response[2], 2) == 1);
    	
    	//TC
    	result.setTC(getBit(response[2], 1) == 1);
    	
    	//RD
    	result.setRD(getBit(response[2], 0) == 1);
    	
    	//RA
    	result.setRA(getBit(response[3], 7) == 1);
    	
    	//RCODE
    	result.setRCode(response[3] & 0x0F);
    	
    	//QDCount
    	byte[] QDCount = { response[4], response[5] };
    	ByteBuffer wrapped = ByteBuffer.wrap(QDCount);
    	result.setQDCount(wrapped.getShort());
    	
    	//ANCount
    	byte[] ANCount = { response[6], response[7] };
    	wrapped = ByteBuffer.wrap(ANCount);
    	result.setANCount(wrapped.getShort());
    	
    	//NSCount
    	byte[] NSCount = { response[8], response[9] };
    	wrapped = ByteBuffer.wrap(NSCount);
    	result.setNSCount(wrapped.getShort());
    	
    	//ARCount
    	byte[] ARCount = { response[10], response[11] };
    	wrapped = ByteBuffer.wrap(ARCount);
    	result.setARCount(wrapped.getShort());
    }

    private String getDomainFromIndex(int index){
    	int wordSize = response[index];
    	String domain = "";

    	while(wordSize > 0){
    		for(int i =0; i < wordSize; i++){
    			domain += (char) response[index + i + 1];
    		}
    		index += wordSize + 1;
    		wordSize = response[index];
    			
    		if (wordSize != 0){
    			domain += ".";	
    		}
    	}
    	return domain;
    }
    private int getBit(byte b, int position) {
    	return (b >> position) & 1;
    }
}
