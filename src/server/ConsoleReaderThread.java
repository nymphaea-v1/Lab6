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
        byte[] commandBuffer = new byte[4];
        while (true) {
            try {
                if (System.in.read(commandBuffer) != 4) continue;
                System.in.skip(System.in.available());
                pipe.sink().write(ByteBuffer.wrap(commandBuffer));
            } catch (IOException ignored) {}
        }
    }
}