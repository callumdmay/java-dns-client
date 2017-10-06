import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class DnsClient {

    public QueryType queryType = QueryType.IP;
    private int timeout = 5;
    private int maxRetries = 3;
    private byte[] server = new byte[4];
    String address;
    private String name;
    private int port = 53;

    public DnsClient(String args[]) {
        this.parseInputArguments(args);
        if (server == null || name == null) {
            throw new IllegalArgumentException("Server IP and domain name must be provided.");
        }
    }

    public void makeRequest() {
        System.out.println("DnsClient sending request for " + name);
        System.out.println("Server: " + address);
        System.out.println("Request type: " + queryType);
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        InetAddress address;
        try {
            address = InetAddress.getByAddress(server);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    private void parseInputArguments(String args[]) {
        List<String> argsList = Arrays.asList(args);
        ListIterator<String> iterator = argsList.listIterator();

        while (iterator.hasNext()) {
            String arg = iterator.next();
            switch (arg) {
                case "-t":
                    timeout = Integer.parseInt(iterator.next());
                    break;
                case "-r":
                    maxRetries = Integer.parseInt(iterator.next());
                    break;
                case "-p":
                    port = Integer.parseInt(iterator.next());
                    break;
                case "-mx":
                    queryType = QueryType.MX;
                    break;
                case "-ns":
                    queryType = QueryType.NS;
                    break;
                default:
                    if (arg.contains("@")) {
                        address = arg.substring(1);
                        String[] addressComponents = address.split("\\.");

                        for (int i = 0; i < addressComponents.length; i++) {
                            server[i] = (byte) Integer.parseInt(addressComponents[i]);
                        }
                        name = iterator.next();
                    }
                    break;
            }
        }
    }

}
