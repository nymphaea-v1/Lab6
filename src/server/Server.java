package server;

import general.Command;
import general.exceptions.NoSuchCommandException;
import server.commands.CommandManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
    private SocketChannel clientChannel;
    private final CommandManager commandManager;

    public Server(CommandManager commandManager) throws IOException {
        this.commandManager = commandManager;

        selector = Selector.open();

        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(11111));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public boolean isConnected() {
        return clientChannel != null && clientChannel.isConnected();
    }

    public void accept() throws IOException {
        if (isConnected()) return;

        clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);

        serverChannel.keyFor(selector).cancel();

        System.out.println("Connected to the client");
    }

    public void serveClient() throws IOException {
        if (clientChannel == null || !clientChannel.isConnected()) return;

        ByteBuffer messageCodeBuffer = ByteBuffer.allocate(4);
        try {
            if (clientChannel.read(messageCodeBuffer) != 4) return;
        } catch (SocketException e) {
            disconnect();
            return;
        }

        int messageCode = messageCodeBuffer.getInt(0);
        System.out.println("Received message with code " + messageCode);
        switch (messageCode) {
            case DISCONNECT:
                disconnect();
            case EXECUTE:
                execute();
        }
    }

    private void disconnect() throws IOException {
        System.out.println("Client has disconnected");

        clientChannel.keyFor(selector).cancel();
        clientChannel.close();

        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void execute() throws IOException {
        ByteBuffer messageLengthBuffer = ByteBuffer.allocate(4);
        clientChannel.read(messageLengthBuffer);
        int messageLength = messageLengthBuffer.getInt(0);

        ByteBuffer messageBuffer = ByteBuffer.allocate(messageLength);
        clientChannel.read(messageBuffer);

        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(messageBuffer.array()));
        String result;

        try {
            Command command = (Command) objectInputStream.readObject();
            result = commandManager.execute(command);
        } catch (ClassNotFoundException | ClassCastException | NoSuchCommandException e) {
            result = "Incorrect command or argument";
        }

        byte[] resultBytes = result.getBytes();
        ByteBuffer resultBuffer = ByteBuffer.allocate(resultBytes.length + 4);
        resultBuffer.putInt(resultBytes.length).put(resultBytes);

        sendExecuteAnswer(resultBuffer.array());
    }

    private void sendExecuteAnswer(byte[] answer) throws IOException {
        ByteBuffer answerBuffer = ByteBuffer.allocate(answer.length + 4);
        answerBuffer.putInt(Server.EXECUTE_ANSWER);
        answerBuffer.put(answer);
        answerBuffer.flip();

        clientChannel.write(answerBuffer);
        System.out.println("Answer send: " + Arrays.toString(answerBuffer.array()));
    }
}
