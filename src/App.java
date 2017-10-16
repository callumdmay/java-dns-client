public class App {
    public static void main(String args[]) throws Exception {
        try {
            DnsClient client = new DnsClient(args);
            client.makeRequest();
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }
}


