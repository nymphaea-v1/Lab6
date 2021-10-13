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

            for (SelectionKey selectionKey : selector.selectedKeys()) {
                if (selectionKey.isAcceptable()) server.accept();
                else if (selectionKey.isReadable()) {
                    SelectableChannel selectableChannel = selectionKey.channel();

                    if (selectableChannel instanceof SocketChannel) server.serveClient((SocketChannel) selectableChannel);

                    if (selectableChannel instanceof Pipe.SourceChannel) {
                        System.out.println("Reading console input");

                        Pipe.SourceChannel pipeSource = (Pipe.SourceChannel) selectableChannel;
                        commandBuffer.clear();
                        pipeSource.read(commandBuffer);
                        String command = new String(commandBuffer.array()).trim();

                        if (command.equals("save")) {
                            collectionManager.save();
                            System.out.println("Collection has been save");
                        } else if (command.equals("exit")) {
                            collectionManager.save();
                            isRunning = false;
                        } else System.out.println(command + " is not a valid command. Available commands: save, exit");
                    }
                }

                selector.selectedKeys().remove(selectionKey);
            }
        }
    }
}
