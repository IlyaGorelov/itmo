package Objects.Connection;

import Commons.CustomPackage;
import Commons.UserData.User;
import Objects.CommandsControllers.CommandExecutor;
import Objects.Managers.CLIManager;
import Objects.Managers.HistoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Receiver {
    private final static Logger logger = LoggerFactory.getLogger(Receiver.class);
    private final CLIManager cliManager = new CLIManager();

    private final int port;

    private Selector selector;

    private final List<String> answersForCLI = new ArrayList<>();
    private final Queue<String> requestsForCLI = new LinkedList<>();

    private final Map<SocketChannel, ByteBuffer> buffers = new ConcurrentHashMap<>();
    private final Map<SocketChannel, Queue<CustomPackage>> requests = new ConcurrentHashMap<>();
    private final Map<SocketChannel, Queue<CustomPackage>> answers = new ConcurrentHashMap<>();
    private final Set<SocketChannel> processingClients = ConcurrentHashMap.newKeySet();

    private final CommandExecutor commandExecutor;

    private final ExecutorService requestExecutor = Executors.newCachedThreadPool();
    private final ExecutorService responseExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public Receiver(int port, CommandExecutor commandExecutor) {
        this.port = port;
        this.commandExecutor = commandExecutor;
    }

    public void connect() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Stopping server...");

            requestExecutor.shutdown();
            responseExecutor.shutdown();
        }));

        try {
            selector = Selector.open();
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false);

            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("Server started on port {}", port);

            selectKeys();
        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            logger.error("User input is not detected");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void selectKeys() throws IOException {
        while (true) {
            selector.select(50);
            var keys = selector.selectedKeys().iterator();

            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();

                if (!key.isValid()) continue;

                try {
                    if (key.isAcceptable()) {
                        accept(key);
                    } else if (key.isReadable()) {
                        readWithNewThread(key);
                    } else if (key.isWritable()) {
                        writeAsync(key);
                    }
                } catch (SocketException e) {
                    logger.error("SocketException for client, closing: {}", key.channel());
                    closeClient((SocketChannel) key.channel());
                } catch (IOException e) {
                    logger.error("IOException for client, closing: {}", key.channel());
                    closeClient((SocketChannel) key.channel());
                }
            }

            processCLI();
        }
    }

    public void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);

        buffers.put(clientChannel, ByteBuffer.allocate(4096));
        answers.put(clientChannel, new ConcurrentLinkedQueue<>());
        requests.put(clientChannel, new ConcurrentLinkedQueue<>());

        logger.info("Client connected: {}", clientChannel.getRemoteAddress());
    }

    private void readWithNewThread(SelectionKey key) {
        key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);

        new Thread(() -> {
            try {
                read(key);
            } catch (Exception e) {
                logger.error("Error while reading from client: {}", e.getMessage());
                closeClient((SocketChannel) key.channel());
            } finally {
                addInterestOps(key, SelectionKey.OP_READ);
            }
        }).start();
    }

    private void read(SelectionKey key) throws IOException, ClassNotFoundException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
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

                submitCommandExecuting(clientChannel, pkg.getAuthor(), key);
            }


        }
        buffer.compact();
    }

    private void submitCommandExecuting(SocketChannel channel, User user, SelectionKey key) {
        if (!processingClients.add(channel)) {
            return;
        }

        requestExecutor.submit(() -> {
            try {
                commandExecutor.execute(this, channel, user, false);
            } finally {
                processingClients.remove(channel);

                addInterestOps(key, SelectionKey.OP_WRITE);
            }
        });
    }

    private void writeAsync(SelectionKey key) {
        key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);

        responseExecutor.submit(() -> {
            try {
                write(key);
            } catch (IOException e) {
                logger.error("Error while sending answer: {}", e.getMessage());
                closeClient((SocketChannel) key.channel());
            } finally {
                addInterestOps(key, SelectionKey.OP_READ);
            }
        });
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel clientCannel = (SocketChannel) key.channel();
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

    public void closeClient(SocketChannel client) {
        try {
            answers.remove(client);
            requests.remove(client);
            buffers.remove(client);
            logger.info("Closed connection with client: {}", client.getRemoteAddress());
            client.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void addAnswerForCLI(String answer) {
        answersForCLI.add(answer);
    }

    private void processCLI() throws IOException {
        String line;

        while ((line = cliManager.pollLine()) != null) {
            line = line.trim();

            if (line.isEmpty()) {
                continue;
            }

            requestsForCLI.add(line);

            logger.info("CLI command: {}", line);

            commandExecutor.execute(this, null, null, true);
            for (String a : answersForCLI)
                cliManager.writeLine(a);

            answersForCLI.clear();
        }
    }

    public String getCLICommand() {
        return requestsForCLI.poll();
    }

    private void addInterestOps(SelectionKey key, int ops) {
        synchronized (key) {
            if (key.isValid()) {
                key.interestOps(key.interestOps() | ops);
            }
        }

        selector.wakeup();
    }
}
