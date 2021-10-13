package client;

import general.Command;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ServerSocket {
    private final static int DISCONNECT = 0;
    private final static int EXECUTE = 1;
    private final static int EXECUTE_ANSWER = 2;

    private final OutputStream outputStream;
    private final BufferedInputStream inputStream;

    ServerSocket() throws IOException {
        Socket serverSocket = new Socket("localhost", 11111);
        outputStream = serverSocket.getOutputStream();
        inputStream = new BufferedInputStream(serverSocket.getInputStream());
        System.out.println("Connected to the server");
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
            ByteArrayOutputStream answer = new ByteArrayOutputStream();
            int answerLength = answerStream.readInt();

            for (int i = 0; i < answerLength; i++) answer.write(answerStream.read());

            System.out.println(answer.toString("UTF-8"));
        }
    }
}
