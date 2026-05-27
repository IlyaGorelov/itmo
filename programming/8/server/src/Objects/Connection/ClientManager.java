package Objects.Connection;

import Commons.Collection.Product;
import Commons.CustomPackage;
import Commons.UserData.User;
import Objects.CommandsControllers.CommandExecutor;
import Objects.CommandsControllers.Commands.CollectionUpdated;
import Objects.Managers.HistoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Phaser;

public class ClientManager {
    private final static Logger logger = LoggerFactory.getLogger(ClientManager.class);

    private final Map<SocketChannel, ByteBuffer> buffers = new ConcurrentHashMap<>();
    private final Map<SocketChannel, Queue<CustomPackage>> requests = new ConcurrentHashMap<>();
    private final Map<SocketChannel, Queue<CustomPackage>> answers = new ConcurrentHashMap<>();
    private final Set<SocketChannel> processingClients = ConcurrentHashMap.newKeySet();
    private final Set<SocketChannel> clients = new HashSet<>();

    private Selector selector;
    private final Phaser phaser;
    private final CommandExecutor commandExecutor;
    private final Server server;

    public ClientManager(Phaser phaser,
                         CommandExecutor commandExecutor,
                         Server server) {
        this.phaser = phaser;
        this.commandExecutor = commandExecutor;
        this.server = server;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public void register(SocketChannel clientChannel) {
        clients.add(clientChannel);
        buffers.put(clientChannel, ByteBuffer.allocate(4096));
        answers.put(clientChannel, new ConcurrentLinkedQueue<>());
        requests.put(clientChannel, new ConcurrentLinkedQueue<>());
    }

    public void read(SocketChannel clientChannel,
                     ExecutorService requestExecutor) throws IOException, ClassNotFoundException {
        ByteBuffer buffer = buffers.get(clientChannel);

        int bytes = clientChannel.read(buffer);
        if (bytes == -1) {
            logger.info("Client disconnected: {}", clientChannel.getRemoteAddress());
            closeClient(clientChannel);
            return;
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

                if (pkg.getAuthor() != null)
                    HistoryManager.registerNewHistory(pkg.getAuthor());

                submitCommandExecuting(clientChannel,
                        pkg.getAuthor(),
                        clientChannel.keyFor(selector),
                        requestExecutor);
            }

        }
        buffer.compact();
    }

    private void submitCommandExecuting(SocketChannel channel, User user, SelectionKey key, ExecutorService requestExecutor) {
        if (!processingClients.add(channel)) {
            return;
        }

        requestExecutor.submit(() -> {
            try {
                phaser.register();
                commandExecutor.execute(server, channel, user, false);
            } finally {
                phaser.arriveAndDeregister();
                processingClients.remove(channel);

                server.addInterestOps(key, SelectionKey.OP_WRITE);
            }
        });
    }

    public void write(SocketChannel clientCannel) throws IOException {
        Queue<CustomPackage> answerQueue = answers.get(clientCannel);

        List<CustomPackage> packages = new ArrayList<>();

        CustomPackage pkg;
        while ((pkg = answerQueue.poll()) != null) {
            packages.add(pkg);
        }

        answerQueue.clear();

        byte[] bytes;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(packages.toArray());
        oos.flush();
        bytes = baos.toByteArray();

        ByteBuffer buffer = ByteBuffer.allocate(4 + bytes.length);
        buffer.putInt(bytes.length);
        buffer.put(bytes);
        buffer.flip();

        while (buffer.hasRemaining()) {
            clientCannel.write(buffer);
        }
        logger.info("Sent an answer: {}", packages);
    }

    public void closeClient(SocketChannel client) {
        try {
            clients.remove(client);
            answers.remove(client);
            requests.remove(client);
            buffers.remove(client);
            logger.info("Closed connection with client: {}", client.getRemoteAddress());
            client.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public CustomPackage getPackage(SocketChannel client) {
        Queue<CustomPackage> queue = requests.get(client);
        return queue == null ? null : queue.poll();
    }

    public void addToAnswer(SocketChannel client, CustomPackage pkg) {
        Queue<CustomPackage> queue = answers.get(client);
        if (queue != null) {
            queue.add(pkg);
        }
    }

    public void broadcastCollectionUpdate(Product[] products) {
        phaser.arriveAndAwaitAdvance();
        CustomPackage updatePackage = new CustomPackage(
                new CollectionUpdated().getName(),
                null,
                products
        );

        for (SocketChannel client : clients) {
            addToAnswer(client, updatePackage);

            SelectionKey key = client.keyFor(selector);

            if (key != null && key.isValid()) {
                key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
            }
        }

        selector.wakeup();
    }
}
