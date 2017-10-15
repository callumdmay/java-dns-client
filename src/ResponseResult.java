
public class ResponseResult {
	private byte[] ID;
	private boolean QR, AA, TC, RD, RA;
	private byte OPCode;
	private int RCode, QDCount, ANCount, NSCount, ARCount;
	
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
}
