import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class DnsClient {

    public QueryType queryType = QueryType.A;
    public int MAX_DNS_PACKET_SIZE = 512;
    private int timeout = 5000;
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
        System.out.println("DnsClient sending pollRequest for " + name);
        System.out.println("Server: " + address);
        System.out.println("Request type: " + queryType);
        pollRequest(1);
    }

    private void pollRequest(int retryNumber) {
        if (retryNumber > maxRetries) {
            System.out.println("Max retry number exceeded.");
            return;
        }

        System.out.println("Attempt number: " + retryNumber);
        try {
            //TODO: I think a bunch of the below stuff shouldn't even be in the try catch
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(timeout);
            InetAddress inetaddress = InetAddress.getByAddress(server);
            DnsRequest request = new DnsRequest(name, queryType);

            byte[] requestBytes = request.getRequest();
            byte[] responseBytes = new byte[1024];
            DatagramPacket requestPacket = new DatagramPacket(requestBytes, requestBytes.length, inetaddress, port);
            DatagramPacket responsePacket = new DatagramPacket(responseBytes, responseBytes.length);

            long startTime = System.currentTimeMillis();
            socket.send(requestPacket);
            socket.receive(responsePacket);
            long endTime = System.currentTimeMillis();
            socket.close();

            System.out.println("Response received after " + (endTime - startTime)/1000. + " seconds");

            DnsResponse response = new DnsResponse(responsePacket.getData(), requestBytes.length);
            ResponseResult result = response.parseResponse();
            if (result.getANCount() > 0){
                System.out.println("***Answer Section (" + result.getANCount() + " records)***");
                String authString = result.isAA() ? "auth" : "nonauth";
                System.out.println("IP\t" + result.getIp_address() + "\t" + result.getAns_ttl() + "\t" + authString);

                //TODO: Right now this is hard-coded for IP (A-mode). This should work for all modes
            }

            if (result.getARCount() > 0){
                System.out.println("***Additional Section ([num-additional] records)***");
                //TODO:
            }

        } catch (SocketException e) {
            System.out.println("Error creating socket.");
        } catch (UnknownHostException e ) {
            System.out.println("Error: Unknown host.");
        } catch (SocketTimeoutException e) {
            System.out.println("Socket Timeout. Reattempting request...");
            pollRequest(++retryNumber);
        } catch (IOException e) {
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
                    timeout = Integer.parseInt(iterator.next()) * 1000;
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
                            int ipValue = Integer.parseInt(addressComponents[i]);
                            if (ipValue < 0 || ipValue > 255) {
                                throw new NumberFormatException("IP Address numbers must be between 0 and 255, inclusive.");
                            }
                            server[i] = (byte) ipValue;
                        }
                        name = iterator.next();
                    }
                    break;
            }
        }
    }

}
