package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

public class ConsoleReaderThread extends Thread {
    private final Pipe pipe;
    private final Pipe.SourceChannel pipeSource;

    ConsoleReaderThread(String name) throws IOException {
        super(name);
        setDaemon(true);

        pipe = Pipe.open();
        pipeSource = pipe.source();
        pipeSource.configureBlocking(false);
    }

    public Pipe.SourceChannel getPipeSource() {
        return pipeSource;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (System.in.available() >= 4) {
                    byte[] commandByte = new byte[4];

                    System.in.read(commandByte);
                    System.in.skip(System.in.available());

                    pipe.sink().write(ByteBuffer.wrap(commandByte));
                }
            } catch (IOException ignored) {}
        }
    }
}