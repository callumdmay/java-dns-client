import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class DnsClient {

    private enum Query  {
        IP,
        MX,
        NS
    }

    private Query query = Query.IP;
    private int timeout = 5;
    private int maxRetries = 3;
    private String server;
    private String name;
    private int port = 53;

    public DnsClient(String args[]) {
        this.parseInputArguments(args);
        System.out.println(query);
        System.out.println(timeout);
        System.out.println(maxRetries);
        System.out.println(server);
        System.out.println(name);
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
                    query = Query.MX;
                    break;
                case "-ns":
                    query = Query.NS;
                    break;
                default:
                    if (arg.contains("@")) {
                        server = arg.substring(1);
                        name = iterator.next();
                    }
                    break;
            }
        }
    }

}
