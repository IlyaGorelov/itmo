package Objects.Connection;

import Commons.Collection.Product;
import Commons.CustomPackage;
import Objects.CommandsControllers.CommandExecutor;
import Objects.Managers.CLIManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class Server {
    private final static Logger logger = LoggerFactory.getLogger(Server.class);
    private final CLIManager cliManager = new CLIManager();

    private final int port;

    Phaser phaser = new Phaser();
    private final CommandExecutor commandExecutor;

    ClientManager clientManager;
    private Selector selector;

    private final List<String> answersForCLI = new ArrayList<>();
    private final Queue<String> requestsForCLI = new LinkedList<>();


    private final ExecutorService requestExecutor = Executors.newCachedThreadPool();
    private final ExecutorService responseExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public Server(int port, CommandExecutor commandExecutor) {
        this.port = port;
        this.commandExecutor = commandExecutor;
        clientManager = new ClientManager(phaser, commandExecutor, this);
    }

    public void connect() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Stopping server...");

            requestExecutor.shutdown();
            responseExecutor.shutdown();
        }));


        try {
            selector = Selector.open();
            clientManager.setSelector(selector);

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
                    clientManager.closeClient((SocketChannel) key.channel());
                } catch (IOException e) {
                    logger.error("IOException for client, closing: {}", key.channel());
                    clientManager.closeClient((SocketChannel) key.channel());
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

        clientManager.register(clientChannel);

        logger.info("Client connected: {}", clientChannel.getRemoteAddress());
    }

    private void readWithNewThread(SelectionKey key) {
        key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);

        new Thread(() -> {
            try {
                read(key);
            } catch (Exception e) {
                logger.error("Error while reading from client: {}", e.getMessage());
                clientManager.closeClient((SocketChannel) key.channel());
            } finally {
                addInterestOps(key, SelectionKey.OP_READ);
            }
        }).start();
    }

    private void read(SelectionKey key) throws IOException, ClassNotFoundException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        clientManager.read(clientChannel, requestExecutor);
    }

    private void writeAsync(SelectionKey key) {
        key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);

        responseExecutor.submit(() -> {
            try {
                write(key);
            } catch (IOException e) {
                logger.error("Error while sending answer: {}", e.getMessage());
                clientManager.closeClient((SocketChannel) key.channel());
            } finally {
                addInterestOps(key, SelectionKey.OP_READ);
            }
        });
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel clientCannel = (SocketChannel) key.channel();
        clientManager.write(clientCannel);
    }

    public CustomPackage getPackage(SocketChannel client) {
        return clientManager.getPackage(client);
    }

    public void addToAnswer(SocketChannel client, CustomPackage pkg) {
        clientManager.addToAnswer(client, pkg);
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

    public void addInterestOps(SelectionKey key, int ops) {
        synchronized (key) {
            if (key.isValid()) {
                key.interestOps(key.interestOps() | ops);
            }
        }

        selector.wakeup();
    }

    public void broadcastCollectionUpdate(Product[] products) {
        clientManager.broadcastCollectionUpdate(products);
    }
}
