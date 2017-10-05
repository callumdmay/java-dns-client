import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class App {

    public static void main(String args[]) throws Exception
    {
        DnsClient client = new DnsClient(args);
        client.makeRequest();
    }
}

