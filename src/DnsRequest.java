import java.nio.ByteBuffer;
import java.util.Random;


public class DnsRequest {

	private String domain;
	private QueryType type;
	public DnsRequest(String domain, QueryType type){
		this.domain = domain;
		this.type = type;
	}

	public byte[] getRequest(){
		int qNameLength = getQNameLength();
		ByteBuffer request = ByteBuffer.allocate(12 + 5 + qNameLength);
		request.put(createRequestHeader());
		request.put(createQuestionHeader(qNameLength));
        return request.array();
	}

	private byte[] createRequestHeader(){
		ByteBuffer header = ByteBuffer.allocate(12);
		byte[] randomID = new byte[2]; 
		new Random().nextBytes(randomID);
		header.put(randomID);
		header.put((byte)0x01);
		header.put((byte)0x00);
		header.put((byte)0x00);
		header.put((byte)0x01);
		
		//lines 3, 4, and 5 will be all 0s, which is what we want
		return header.array();
	}
	
	private int getQNameLength(){
		int byteLength = 0;
		String[] items = domain.split("\\.");
		for(int i=0; i < items.length; i ++){
			// 1 byte length for the number value and then another for each character
			//www.mcgill.ca = 3, w, w, w, 6, m, c, g, i, l, l, 2, c, a = 14 byteLength
			byteLength += items[i].length() + 1;
		}
		return byteLength;
	}

	private byte[] createQuestionHeader(int qNameLength){
		ByteBuffer question = ByteBuffer.allocate(qNameLength+5);
		
		//first calculate how many bytes we need so we know the size of the array
		String[] items = domain.split("\\.");
		for(int i=0; i < items.length; i ++){
			question.put((byte) items[i].length());
			for (int j = 0; j < items[i].length(); j++){
				question.put((byte) ((int) items[i].charAt(j)));
				
			}
		}

		question.put((byte) 0x00);

		//Add Query Type
		question.put(hexStringToByteArray("000" + hexValueFromQueryType(type)));
		question.put((byte) 0x00);
		//Add Query Class - always  0x0001 for internet addresses
		question.put((byte) 0x0001);

		return question.array();
	}
	
	private char hexValueFromQueryType(QueryType type){
		if (type == QueryType.A) {
			return '1';
		} else if (type == QueryType.NS) {
			return '2';
		} else {
			return 'F';
		}
	}

	private static byte[] hexStringToByteArray(String s) {
		int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
}
