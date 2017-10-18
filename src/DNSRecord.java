	
public class DNSRecord {
	 private int ans_ttl, ans_rdLength, ans_mx_preference;
	 private String ans_name, ans_domain;
	 private byte[] ans_class;
	 private QueryType ans_type;
	 private boolean auth;
	 private int byteLength;
	 
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
	public int getAns_mx_preference() {
		return ans_mx_preference;
	}
	public void setAns_mx_preference(int ans_mx_preference) {
		this.ans_mx_preference = ans_mx_preference;
	}
	public String getAns_name() {
		return ans_name;
	}
	public void setAns_name(String ans_name) {
		this.ans_name = ans_name;
	}
	public String getAns_domain() {
		return ans_domain;
	}
	public void setAns_domain(String ans_domain) {
		this.ans_domain = ans_domain;
	}
	public byte[] getAns_class() {
		return ans_class;
	}
	public void setAns_class(byte[] ans_class) {
		this.ans_class = ans_class;
	}
	public QueryType getAns_type() {
		return ans_type;
	}
	public void setAns_type(QueryType ans_type) {
		this.ans_type = ans_type;
	}
	public boolean isAuth() {
		return auth;
	}
	public void setAuth(boolean auth) {
		this.auth = auth;
	}
	public DNSRecord(boolean auth){
		 this.auth = auth;
	 }
	 public void outputRecord() {
        switch(this.ans_type) {
            case A:
                this.outputATypeRecords();
                break;
            case NS:
                this.outputNSTypeRecords();
                break;
            case MX:
                this.outputMXTypeRecords();
                break;
			case CNAME:
				this.outputCNameTypeRecords();
				break;
			default:
				break;
        }
	}

	private void outputATypeRecords() {
        String authString = this.auth ? "auth" : "nonauth";
        System.out.println("IP\t" + this.ans_domain + "\t" + this.ans_ttl + "\t" + authString);
    }

    private void outputNSTypeRecords() {
        String authString = this.auth ? "auth" : "nonauth";
    	System.out.println("NS\t" + this.ans_domain + "\t" + this.ans_ttl + "\t" + authString);
    }

    private void outputMXTypeRecords() {
    	String authString = this.auth ? "auth" : "nonauth";
    	System.out.println("MX\t" + this.ans_domain + "\t" + ans_mx_preference + "\t" + this.ans_ttl + "\t" + authString);
    }
    
    private void outputCNameTypeRecords() {
		String authString = this.auth ? "auth" : "nonauth";
		System.out.println("CNAME\t" + this.ans_domain + "\t" + this.ans_ttl + "\t" + authString);
    }
	
    public int getByteLength() {
		return byteLength;
	}
	
	public void setByteLength(int byteLength) {
		this.byteLength = byteLength;
	}

}
