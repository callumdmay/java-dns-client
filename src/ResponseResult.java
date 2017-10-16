import java.util.Arrays;


public class ResponseResult {
	private byte[] ID, ans_type, ans_class;
	private boolean QR, AA, TC, RD, RA;
	private byte OPCode;
	private int RCode, QDCount, ANCount, NSCount, ARCount, ans_ttl, ans_rdLength;
	private String ans_name, ip_address;
	
	@Override
	public String toString() {
		return "ResponseResult [ID=" + javax.xml.bind.DatatypeConverter.printHexBinary(ID) + ", ans_type="
				+ javax.xml.bind.DatatypeConverter.printHexBinary(ans_type) + ", ans_class="
				+ javax.xml.bind.DatatypeConverter.printHexBinary(ans_class) + ", QR=" + QR + ", AA=" + AA
				+ ", TC=" + TC + ", RD=" + RD + ", RA=" + RA + ", OPCode="
				+ OPCode + ", RCode=" + RCode + ", QDCount=" + QDCount
				+ ", ANCount=" + ANCount + ", NSCount=" + NSCount
				+ ", ARCount=" + ARCount + ", ans_ttl=" + ans_ttl
				+ ", ans_rdLength=" + ans_rdLength + ", ans_name=" + ans_name
				+ ", IP_address=" + ip_address
				+ "]";
	}

	public byte[] getAns_type() {
		return ans_type;
	}

	public void setAns_type(byte[] ans_type) {
		this.ans_type = ans_type;
	}

	public byte[] getAns_class() {
		return ans_class;
	}

	public void setAns_class(byte[] ans_class) {
		this.ans_class = ans_class;
	}

	public int getAns_ttl() {
		return ans_ttl;
	}

	public void setAns_ttl(int ans_ttl) {
		this.ans_ttl = ans_ttl;
	}

	public int getAns_rdLength() {
		return ans_rdLength;
	}

	public void setAns_rdLength(int ans_rdLength) {
		this.ans_rdLength = ans_rdLength;
	}

	public String getAns_name() {
		return ans_name;
	}

	public void setAns_name(String ans_name) {
		this.ans_name = ans_name;
	}

	public byte[] getID() {
		return ID;
	}

	public void setID(byte[] iD) {
		ID = iD;
	}

	public boolean isQR() {
		return QR;
	}

	public void setQR(boolean qR) {
		QR = qR;
	}

	public boolean isAA() {
		return AA;
	}

	public void setAA(boolean aA) {
		AA = aA;
	}

	public boolean isTC() {
		return TC;
	}

	public void setTC(boolean tC) {
		TC = tC;
	}

	public boolean isRD() {
		return RD;
	}

	public void setRD(boolean rD) {
		RD = rD;
	}

	public boolean isRA() {
		return RA;
	}

	public void setRA(boolean rA) {
		RA = rA;
	}

	public byte getOPCode() {
		return OPCode;
	}

	public void setOPCode(byte oPCode) {
		OPCode = oPCode;
	}

	public int getRCode() {
		return RCode;
	}

	public void setRCode(int rCode) {
		RCode = rCode;
	}

	public int getQDCount() {
		return QDCount;
	}

	public void setQDCount(int qDCount) {
		QDCount = qDCount;
	}

	public int getANCount() {
		return ANCount;
	}

	public void setANCount(int aNCount) {
		ANCount = aNCount;
	}

	public int getNSCount() {
		return NSCount;
	}

	public void setNSCount(int nSCount) {
		NSCount = nSCount;
	}

	public int getARCount() {
		return ARCount;
	}

	public void setARCount(int aRCount) {
		ARCount = aRCount;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
}
