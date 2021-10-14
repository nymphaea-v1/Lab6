package client;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) {
        int port = 11111;
        try {
            if (args.length != 0) port = Integer.parseInt(args[0]);
        } catch (NumberFormatException ignored) {}

        InetAddress inetAddress = null;
        try {
            if (args.length > 1) inetAddress = InetAddress.getByName(args[1]);
        } catch (UnknownHostException ignored) {}

        InetSocketAddress socketAddress = inetAddress == null
                ? new InetSocketAddress("localhost", port)
                : new InetSocketAddress(inetAddress, port);
        ServerConnector serverConnector = new ServerConnector(socketAddress);
        InputReader inputReader = new InputReader();

        inputReader.startReading(serverConnector);
    }
}
