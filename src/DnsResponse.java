public class DnsResponse {
    private String domain;
    private QueryType type;

    public DnsResponse(QueryType type){
        this.domain = domain;
        this.type = type;
    }

    public String[] parseAndFormatAnswer(byte[][] answer) {

    }
}
