package client;

import java.io.IOException;
import java.net.ConnectException;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            ServerConnector serverConnector = new ServerConnector();
            InputReader inputReader = new InputReader();

            inputReader.startReading(serverConnector);
        } catch (ConnectException e) {
            System.out.println(e.getMessage());
        }
    }
}
