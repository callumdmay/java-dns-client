public class App {
    public static void main(String args[]) throws Exception {
        try {
            DnsClient client = new DnsClient(args);
            client.makeRequest();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


