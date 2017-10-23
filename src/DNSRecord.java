	
public class DNSRecord {
	 private int timeToLive, rdLength, mxPreference;
	 private String name, domain;
	 private byte[] queryClass;
	 private QueryType queryType;
	 private boolean auth;
	 private int byteLength;
	 
	public DNSRecord(boolean auth){
		 this.auth = auth;
	 }

	 public void outputRecord() {
        switch(this.queryType) {
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
        System.out.println("IP\t" + this.domain + "\t" + this.timeToLive + "\t" + authString);
    }

    private void outputNSTypeRecords() {
        String authString = this.auth ? "auth" : "nonauth";
    	System.out.println("NS\t" + this.domain + "\t" + this.timeToLive + "\t" + authString);
    }

    private void outputMXTypeRecords() {
    	String authString = this.auth ? "auth" : "nonauth";
    	System.out.println("MX\t" + this.domain + "\t" + mxPreference + "\t" + this.timeToLive + "\t" + authString);
    }
    
    private void outputCNameTypeRecords() {
		String authString = this.auth ? "auth" : "nonauth";
		System.out.println("CNAME\t" + this.domain + "\t" + this.timeToLive + "\t" + authString);
    }
	
    public int getByteLength() {
		return byteLength;
	}
	
	public void setByteLength(int byteLength) {
		this.byteLength = byteLength;
	}

	public int getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(int timeToLive) {
		this.timeToLive = timeToLive;
	}

	public int getRdLength() {
		return rdLength;
	}

	public void setRdLength(int rdLength) {
		this.rdLength = rdLength;
	}

	public int getMxPreference() {
		return mxPreference;
	}

	public void setMxPreference(int mxPreference) {
		this.mxPreference = mxPreference;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public byte[] getQueryClass() {
		return queryClass;
	}

	public void setQueryClass(byte[] queryClass) {
		this.queryClass = queryClass;
	}

	public QueryType getQueryType() {
		return queryType;
	}

	public void setQueryType(QueryType queryType) {
		this.queryType = queryType;
	}

	public boolean isAuth() {
		return auth;
	}

	public void setAuth(boolean auth) {
		this.auth = auth;
	}
}
