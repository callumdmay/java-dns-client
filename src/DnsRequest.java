import java.util.Random;


public class DnsRequest {

	private Random rand;
	private String domain;
	private QUERYTYPE type;

	public enum QUERYTYPE { HOSTADDRESS, NAMESERVER, MAILSERVER }

	public DnsRequest(String domain, QUERYTYPE type){
		rand = new Random();
		this.domain = domain;
		this.type = type;
	}

	public byte[][] getRequest(){
		byte[][] header = createRequestHeader();
		byte[][] question = createQuestionHeader();
		byte[][] request = new byte[header.length + question.length][];
        System.arraycopy(header, 0, request, 0, header.length);
        System.arraycopy(question, 0, request, header.length, request.length);
        return request;
	}

	private byte[][] createRequestHeader(){
		byte[][] header = new byte[6][16];
		header[0] = randomID();
		header[1] = hexStringToByteArray("8100");
		header[2] = hexStringToByteArray("0001");
		//lines 3, 4, and 5 will be all 0s, which is what we want

		return header;
	}
	
	private byte[][] createQuestionHeader(){
		
		String hexQName = "";
		
		//first calculate how many bytes we need so we know the size of the array
		String[] items = domain.split(".");
		for(int i=0; i < items.length; i ++){
			if (i % 2 == 0){
				hexQName += Integer.toHexString(items[i].length());
			}else{
				for (int j = 0; j < items[i].length(); j++){
					hexQName += Integer.toHexString((int) items[i].charAt(j));
				}
			}
		}
		hexQName += "00";

		//Length of QName affects how many rows we need
		int QNameRows = (int) Math.ceil(hexQName.length() / 4.);
		
		byte[][] question = new byte[QNameRows + 2][16];
		
		//go through QNameRows and fill the question byte array
		for(int i = 0; i < QNameRows; i ++){
			question[i] = hexStringToByteArray(hexQName.substring(i*2, i*2 + 1));
		}
		question[QNameRows] = hexStringToByteArray("000" + hexFromType(type));
		question[QNameRows + 1] = hexStringToByteArray("0001");
		
		return question;
	}
	
	private char hexFromType(QUERYTYPE type){
		if(type == QUERYTYPE.HOSTADDRESS){
			return '1';
		}else if(type == QUERYTYPE.MAILSERVER){
			return '2';
		}else {
			return 'F';
		}
	}
	private byte[] randomID(){
		//65536 is max value for 16 bit no.
		return hexStringToByteArray(Integer.toHexString(rand.nextInt(65536)));
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
