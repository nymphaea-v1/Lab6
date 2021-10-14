package client;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) {
        int port = 11111;

        InetSocketAddress socketAddress;
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            socketAddress = new InetSocketAddress(inetAddress, port);
        } catch (UnknownHostException e) {
            socketAddress = new InetSocketAddress("localhost", port);
        }

        ServerConnector serverConnector = new ServerConnector(socketAddress);
        InputReader inputReader = new InputReader();

        inputReader.startReading(serverConnector);
    }
}
