package client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        InputReader inputReader = new InputReader();

        inputReader.startReading(serverSocket);
    }
}
