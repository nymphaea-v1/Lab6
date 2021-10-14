package server;

import general.Command;
import general.ExecutionResult;
import general.exceptions.NoSuchCommandException;
import server.commands.CommandManager;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class Server {
    private final static int DISCONNECT = 0;
    private final static int EXECUTE = 1;
    private final static int EXECUTE_ANSWER = 2;

    public final Selector selector;

    private final ServerSocketChannel serverChannel;
    private final CommandManager commandManager;

    public Server(CommandManager commandManager, int port) throws IOException {
        this.commandManager = commandManager;

        selector = Selector.open();

        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void accept() throws IOException {
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);

        System.out.println("Client has connected");
    }

    public void serveClient(SocketChannel clientChannel) throws IOException {
        if (clientChannel == null || !clientChannel.isConnected()) return;

        ByteBuffer messageCodeBuffer = ByteBuffer.allocate(4);
        try {
            while (messageCodeBuffer.position() != 4) clientChannel.read(messageCodeBuffer);
        } catch (SocketException e) {
            disconnect(clientChannel);
            return;
        }

        int messageCode = messageCodeBuffer.getInt(0);
        System.out.println("Received message with code " + messageCode);
        switch (messageCode) {
            case DISCONNECT:
                disconnect(clientChannel);
            case EXECUTE:
                execute(clientChannel);
        }
    }

    private void disconnect(SocketChannel clientChannel) throws IOException {
        System.out.println("Client has disconnected");

        clientChannel.keyFor(selector).cancel();
        clientChannel.close();
    }

    private void execute(SocketChannel clientChannel) throws IOException {
        ByteBuffer messageLengthBuffer = ByteBuffer.allocate(4);
        while (messageLengthBuffer.position() != 4) clientChannel.read(messageLengthBuffer);
        int messageLength = messageLengthBuffer.getInt(0);

        ByteBuffer messageBuffer = ByteBuffer.allocate(messageLength);
        while (messageBuffer.position() != messageLength) clientChannel.read(messageBuffer);

        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(messageBuffer.array()));
        ExecutionResult<?> result;

        try {
            Command command = (Command) objectInputStream.readObject();
            result = commandManager.execute(command);
        } catch (ClassNotFoundException | ClassCastException | NoSuchCommandException e) {
            result = new ExecutionResult<>(false, "Incorrect command or argument");
        }

        ByteArrayOutputStream resultBytesStream = new ByteArrayOutputStream();
        ObjectOutputStream resultObjectStream = new ObjectOutputStream(resultBytesStream);
        resultObjectStream.writeObject(result);
        byte[] resultBytes = resultBytesStream.toByteArray();

        ByteBuffer resultBuffer = ByteBuffer.allocate(resultBytes.length + 4);
        resultBuffer.putInt(resultBytes.length).put(resultBytes);

        sendAnswer(EXECUTE_ANSWER, clientChannel, resultBuffer.array());
    }

    private void sendAnswer(int messageCode, SocketChannel clientChannel, byte[] answer) throws IOException {
        ByteBuffer answerBuffer = ByteBuffer.allocate(answer.length + 4);
        answerBuffer.putInt(messageCode);
        answerBuffer.put(answer);
        answerBuffer.flip();

        clientChannel.write(answerBuffer);
        System.out.println("Answer send: " + Arrays.toString(answerBuffer.array()));
    }
}
