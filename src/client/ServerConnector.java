package client;

import general.Command;
import general.ExecutionResult;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class ServerConnector {
    private final static int DISCONNECT = 0;
    private final static int EXECUTE = 1;
    private final static int EXECUTE_ANSWER = 2;

    private final SocketAddress socketAddress;
    private Socket socket;
    private OutputStream outputStream;
    private BufferedInputStream inputStream;

    ServerConnector(SocketAddress socketAddress) throws IOException {
        this.socketAddress = socketAddress;
        connect();
    }

    public void connect() throws IOException {
        if (socket != null && !socket.isConnected()) {
            System.out.println("Already connected to the server");
            return;
        }

        while (true) {
            System.out.println("Connecting to the server");

            try {
                socket = new Socket();
                socket.connect(socketAddress, 0);

                outputStream = socket.getOutputStream();
                inputStream = new BufferedInputStream(socket.getInputStream());

                System.out.println("Successfully connected to the server");
                break;
            } catch (IOException e) {
                System.out.println(e.getMessage());

                System.out.println("Type 1 to try connecting again");
                String answer = new Scanner(System.in).next();
                if (!answer.equals("1")) throw new IOException();
            }
        }
    }

    public void sendMessage(int messageCode, Serializable object) throws IOException {
        ByteArrayOutputStream messageStream = new ByteArrayOutputStream();
        messageStream.write(ByteBuffer.allocate(4).putInt(messageCode).array());

        if (messageCode == EXECUTE) {
            if (!(object instanceof Command)) return;
            Command command = (Command) object;

            ByteArrayOutputStream commandStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(commandStream);
            objectOutputStream.writeObject(command);
            byte[] commandByteArray = commandStream.toByteArray();

            messageStream.write(ByteBuffer.allocate(4).putInt(commandByteArray.length).array());
            messageStream.write(commandByteArray);

            outputStream.write(messageStream.toByteArray());
            System.out.println("Request " + command.name + " has been sent");
        }

        proceedAnswer();
    }

    private void proceedAnswer() throws IOException {
        DataInputStream answerStream = new DataInputStream(inputStream);
        int answerCode = answerStream.readInt();

        if (answerCode == EXECUTE_ANSWER) {
            int answerLength = answerStream.readInt();
            ObjectInputStream resultObjectStream = new ObjectInputStream(inputStream);
            try {
                ExecutionResult<?> executionResult = (ExecutionResult<?>) resultObjectStream.readObject();
                executionResult.printResult();
            } catch (ClassNotFoundException ignored) {}
        }
    }
}
