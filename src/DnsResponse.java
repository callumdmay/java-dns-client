import java.nio.ByteBuffer;

public class DnsResponse {
	private byte[] response;
	private ResponseResult result;

	public DnsResponse(byte[] response){
		this.response = response;
    	result = new ResponseResult();
    }

    public ResponseResult parseResponse() {
    	parseHeader();
    	parseQuestion();
    	
    	return result;
    }
    private void parseAnswer(){
    	
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
    	
    	//RCODE TODO check this
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

    private int getBit(byte b, int position) {
    	return (b >> position) & 1;
    }
}
