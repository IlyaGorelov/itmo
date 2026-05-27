package Objects.Connection;

import Commons.CustomPackage;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Phaser;

public class ClientSession {

    private final SocketChannel channel;
    private final ByteBuffer buffer = ByteBuffer.allocate(8192); // larger buffer

    private final Queue<CustomPackage> incoming = new ConcurrentLinkedQueue<>();
    private final Queue<CustomPackage> outgoing = new ConcurrentLinkedQueue<>();

    private final Phaser phaser;
    private final Selector selector;

    public ClientSession(SocketChannel channel, Phaser phaser, Selector selector) {
        this.channel = channel;
        this.phaser = phaser;
        this.selector = selector;
    }

    public void registerInPhaser() {
        phaser.register();
    }

    public void deregisterFromPhaser() {
        phaser.arriveAndDeregister();
    }

    public void read() throws IOException, ClassNotFoundException {
        int bytesRead = channel.read(buffer);
        if (bytesRead == -1) {
            throw new IOException("Client disconnected");
        }
        if (bytesRead == 0) return;

        buffer.flip();

        while (buffer.remaining() >= Integer.BYTES) {
            buffer.mark();
            int length = buffer.getInt();

            if (buffer.remaining() < length) {
                buffer.reset();
                break;
            }

            byte[] data = new byte[length];
            buffer.get(data);

            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
                CustomPackage pkg = (CustomPackage) ois.readObject();
                incoming.add(pkg);
            }
        }

        buffer.compact();
    }

    public void write() throws IOException {
        if (outgoing.isEmpty()) return;

        List<CustomPackage> toSend = new ArrayList<>();
        CustomPackage pkg;
        while ((pkg = outgoing.poll()) != null) {
            toSend.add(pkg);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(toSend.toArray());
        }

        byte[] data = baos.toByteArray();
        ByteBuffer writeBuffer = ByteBuffer.allocate(4 + data.length);
        writeBuffer.putInt(data.length);
        writeBuffer.put(data);
        writeBuffer.flip();

        while (writeBuffer.hasRemaining()) {
            channel.write(writeBuffer);
        }
    }

    public void requestWrite() {
        SelectionKey key = channel.keyFor(selector);
        if (key != null && key.isValid()) {
            key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
            selector.wakeup();
        }
    }

    public CustomPackage getNextRequest() {
        return incoming.poll();
    }

    public void addAnswer(CustomPackage pkg) {
        outgoing.add(pkg);
    }
}
