package client;

import java.io.IOException;
import java.net.ConnectException;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket();
            InputReader inputReader = new InputReader();

            inputReader.startReading(serverSocket);
        } catch (ConnectException e) {
            System.out.println(e.getMessage());
        }
    }
}
