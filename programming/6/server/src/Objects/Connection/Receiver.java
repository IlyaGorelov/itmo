package Objects.Connection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Objects.CommandsControllers.CommandExecutor;

public class Receiver {
    private final static Logger logger = LoggerFactory.getLogger(Receiver.class);

    private int port;
    private Selector selector;
    private ServerSocketChannel serverChannel;

    private HashMap<SocketChannel, ByteBuffer> buffers = new HashMap<>();
    private HashMap<SocketChannel, Queue<CustomPackage>> requests = new HashMap<>();
    private HashMap<SocketChannel, Queue<CustomPackage>> answers = new HashMap<>();

    private CommandExecutor commandExecutor;

    public Receiver(int port, CommandExecutor commandExecutor) {
        this.port = port;
        this.commandExecutor = commandExecutor;
    }

    public void connect() {
        try {

            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false);

            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("Server started on port {}", port);

            while (true) {
                selector.select();
                var keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (!key.isValid())
                        continue;
                    try {
                        if (key.isAcceptable()) {
                            accept(key);
                        } else if (key.isReadable()) {
                            read(key);
                        } else if (key.isWritable()) {
                            write(key);
                        }
                    } catch (SocketException e) {
                        logger.error("SocketException for client, closing: {}", key.channel());
                        closeClient((SocketChannel) key.channel());
                    } catch (IOException e) {
                        logger.error("IOException for client, closing: ", key.channel());
                        e.printStackTrace();
                        closeClient((SocketChannel) key.channel());
                    } catch (ClassNotFoundException e) {
                        logger.error("Received unknown object from client, skipping");
                        e.printStackTrace();
                    }
                }
            }

        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            logger.error("User input is not detected");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            commandExecutor.stop();
        }
    }

    public void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);

        buffers.put(clientChannel, ByteBuffer.allocate(4096));
        answers.put(clientChannel, new LinkedList<>());
        requests.put(clientChannel, new LinkedList<>());

        // commandExecutor.execute(this, clientChannel);

        logger.info("Client connected: {}", clientChannel.getRemoteAddress());
    }

    public void read(SelectionKey key) throws IOException, ClassNotFoundException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = buffers.get(clientChannel);

        int bytes = clientChannel.read(buffer);
        if (bytes == -1) {
            logger.info("Client disconnected: {}", clientChannel.getRemoteAddress());
            closeClient(clientChannel);
        }

        if (bytes == 0) {
            return;
        }

        buffer.flip();

        while (true) {
            if (buffer.remaining() < Integer.BYTES) {
                break;
            }

            buffer.mark();
            int length = buffer.getInt();

            if (buffer.remaining() < length) {
                buffer.reset();
                break;
            }

            byte[] objectBytes = new byte[length];
            buffer.get(objectBytes);

            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(objectBytes))) {
                CustomPackage pkg = (CustomPackage) ois.readObject();
                requests.get(clientChannel).add(pkg);
                logger.info("Received a request: {}", pkg.getCommand());
            }

            commandExecutor.execute(this, clientChannel);
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }

        buffer.compact();
    }

    public CustomPackage getPackage(SocketChannel client) {
        var queue = requests.get(client);
        if (queue != null && !queue.isEmpty()) {
            return queue.poll();
        }
        return null;
    }

    public void write(SelectionKey key) throws IOException {
        SocketChannel clientCannel = (SocketChannel) key.channel();
        Queue<CustomPackage> answerQueue = answers.get(clientCannel);

        Object[] pkg = answerQueue.toArray();
        answerQueue.clear();

        byte[] bytes;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(pkg);
        oos.flush();
        bytes = baos.toByteArray();

        ByteBuffer buffer = ByteBuffer.allocate(4 + bytes.length);
        buffer.putInt(bytes.length);
        buffer.put(bytes);
        buffer.flip();

        while (buffer.hasRemaining()) {
            clientCannel.write(buffer);
        }
        logger.info("Sended an answer: {}", pkg);

        key.interestOps(SelectionKey.OP_READ);
    }

    public void addToAnswer(SocketChannel client, CustomPackage pkg) {
        Queue<CustomPackage> queue = answers.get(client);
        if (queue != null) {
            queue.add(pkg);
        }
    }

    public void closeClient(SocketChannel client) {
        try {
            answers.remove(client);
            requests.remove(client);
            buffers.remove(client);
            logger.info("Closed connection with client: {}", client.getRemoteAddress());
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
