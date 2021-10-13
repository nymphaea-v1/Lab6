package server;

import server.commands.CommandManager;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;

public class Main {
    public static void main(String[] args) throws IOException {
        CollectionManager collectionManager = new CollectionManager("input.csv");
        CommandManager commandManager = new CommandManager(collectionManager);
        ByteBuffer commandBuffer = ByteBuffer.allocate(4);

        Server server = new Server(commandManager);
        Selector selector = server.selector;

        ConsoleReaderThread consoleReaderThread = new ConsoleReaderThread("consoleInputThread");
        consoleReaderThread.getPipeSource().register(selector, SelectionKey.OP_READ);
        consoleReaderThread.start();

        boolean isRunning = true;

        while (isRunning) {
            selector.select();
            System.out.println(selector.selectedKeys());

            for (SelectionKey selectionKey : selector.selectedKeys()) {
                if (selectionKey.isAcceptable()) {
                    System.out.println("Accepting new client");
                    server.accept();
                } else if (selectionKey.isReadable()) {
                    SelectableChannel selectableChannel = selectionKey.channel();

                    if (selectableChannel instanceof SocketChannel) {
                        System.out.println("Reading request from client");
                        server.serveClient();
                        continue;
                    }

                    if (selectableChannel instanceof Pipe.SourceChannel) {
                        System.out.println("Reading console input");

                        Pipe.SourceChannel pipeSource = (Pipe.SourceChannel) selectableChannel;
                        commandBuffer.clear();
                        pipeSource.read(commandBuffer);
                        String command = new String(commandBuffer.array());

                        if (command.equals("save")) {
                            collectionManager.save();
                            System.out.println("Collection has been save");
                        } else if (command.equals("exit")) {
                            collectionManager.save();
                            isRunning = false;
                        }
                    }
                }

                selector.selectedKeys().remove(selectionKey);
            }
        }
    }
}
