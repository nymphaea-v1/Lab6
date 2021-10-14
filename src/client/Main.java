package client;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        ServerConnector serverConnector = new ServerConnector(new InetSocketAddress("localhost", 11111));
        InputReader inputReader = new InputReader();

        inputReader.startReading(serverConnector);
    }
}
