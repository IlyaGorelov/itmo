package core.Objects.Connection;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import Commons.Collection.Product;
import Commons.CustomPackage;
import core.Objects.CommandsControllers.Commands.*;
import core.Objects.Managers.CommandManager;
import gui.Objects.Elements.Commons.ResultDialog;
import gui.Objects.Frames.ConnectingFrame;
import gui.Objects.Frames.LoginFrame;
import gui.Objects.Frames.MainFrame;

import javax.swing.*;

public class Client {
    private Socket socket = null;

    private static final int MAX_RETRY = 5;
    private static final int CONNECT_TIMEOUT = 5000;

    private InputStream in;
    private OutputStream out;

    private DataInputStream dis;
    private DataOutputStream dos;

    private Scanner scanner;

    private CommandManager commandManager;

    private static Mode mode;

    private volatile boolean listening = false;
    private Thread listenerThread;

    private static MainFrame mainFrame;

    public enum Mode {
        CLI, GUI
    }

    private static final BlockingQueue<CustomPackage> guiCommands = new LinkedBlockingQueue<>();
    private static final BlockingQueue<CustomPackage> guiAnswers = new LinkedBlockingQueue<>();

    private final BlockingQueue<CustomPackage[]> serverAnswers = new LinkedBlockingQueue<>();

    public static Mode getMode() {
        return mode;
    }

    private void initializeIO(Socket socket) throws IOException {
        in = socket.getInputStream();
        out = socket.getOutputStream();

        dis = new DataInputStream(in);
        dos = new DataOutputStream(out);

        scanner = new Scanner(System.in);
    }

    public void connect(String host, int port, Mode mode) {
        JFrame connectingFrame = new ConnectingFrame();
        connectingFrame.setVisible(true);

        while (true) {
            try {
                Client.mode = mode;
                CommandManager.setMode(mode);

                socket = getSocketWithRetry(host, port);

                if (socket == null) {
                    break;
                }

                initializeIO(socket);

                connectingFrame.dispose();

                work();

                break;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(e.getMessage());
                System.out.println("Trying to reconnect...");
            } catch (IOException e) {
                System.out.println("Connection lost: " + e.getMessage());
                System.out.println("Trying to reconnect...");
            }
        }

        connectingFrame.dispose();
    }

    private Socket getSocketWithRetry(String host, int port)
            throws IOException, InterruptedException {
        int retry = 0;
        int delay = 1000;

        while (retry < MAX_RETRY + 1) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), CONNECT_TIMEOUT);
                return socket;
            } catch (ConnectException e) {
                retry++;
                System.out.println("Server is unavailable. Attempt " + retry + "/" + MAX_RETRY);
                Thread.sleep(delay);
                delay *= 2;
            }
        }

        return null;
    }

    public static void openFrame(JFrame newFrame) {
        for (Frame frame : Frame.getFrames()) {
            frame.dispose();
        }

        if (newFrame instanceof MainFrame)
            mainFrame = (MainFrame) newFrame;

        newFrame.setVisible(true);
    }

    private void work() {
        new LoginFrame().setVisible(true);

        try {
            commandManager = new CommandManager(scanner, in, out);

            startServerListener();

            System.out.println("Client connected to server.");
            System.out.println();
            System.out.println("Log in to an existing account or create a new one to use all commands");
            System.out.println("Type \"help\" to see all available commands.");

            while (!socket.isOutputShutdown() && !socket.isClosed()) {
                CustomPackage customPackage = getCustomPackageForRequest();

                if (customPackage == null) {
                    continue;
                }

                if (isExitCommand(customPackage)) {
                    break;
                }

                if (isExecuteScriptCommand(customPackage) || isClientOnlyCommand(customPackage)) {
                    continue;
                }

                sendRequest(customPackage);

                String answer = getStringAnswer();

                System.out.println();
                System.out.println(answer);
            }

            closeConnection();

            System.out.println("Closing connections & channels on clientSide - DONE.");
        } catch (ConnectException e) {
            System.out.println("Server doesn't listen on this port or server is not started yet.");
        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            System.out.println("User input is not detected, but all data was saved");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            listening = false;
            for (Frame frame : Frame.getFrames()) {
                frame.dispose();
            }
        }
    }

    private void startServerListener() {
        listening = true;

        listenerThread = new Thread(() -> {
            while (listening && socket != null && !socket.isClosed()) {
                try {
                    CustomPackage[] packages = readServerPackages();

                    handleServerPackages(packages);
                } catch (EOFException | SocketException e) {
                    listening = false;
                    System.out.println("Server closed connection.");
                } catch (Exception e) {
                    listening = false;
                    System.out.println("Error while reading server message: " + e.getMessage());
                }
            }
        });

        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    private CustomPackage[] readServerPackages() throws IOException, ClassNotFoundException {
        int answerLength = dis.readInt();

        byte[] answerBytes = new byte[answerLength];

        dis.readFully(answerBytes);

        ByteArrayInputStream bais = new ByteArrayInputStream(answerBytes);
        ObjectInputStream ois = new ObjectInputStream(bais);

        Object[] rawPackages = (Object[]) ois.readObject();

        CustomPackage[] packages = new CustomPackage[rawPackages.length];

        for (int i = 0; i < rawPackages.length; i++) {
            packages[i] = (CustomPackage) rawPackages[i];
        }

        return packages;

    }

    private void handleServerPackages(CustomPackage[] packages) throws InterruptedException {
        List<CustomPackage> ordinaryAnswers = new ArrayList<>();

        for (CustomPackage pack : packages) {
            if (pack == null) {
                continue;
            }

            if (isCollectionUpdatedPackage(pack)) {
                updateTableFromPackage(pack);
            } else {
                ordinaryAnswers.add(pack);
            }
        }

        if (!ordinaryAnswers.isEmpty()) {
            serverAnswers.put(ordinaryAnswers.toArray(new CustomPackage[0]));
        }
    }

    private boolean isCollectionUpdatedPackage(CustomPackage pack) {
        return pack.getCommand().equals(new CollectionUpdated().getName());
    }

    private void updateTableFromPackage(CustomPackage pack) {
        Product[] products = extractProducts(pack.getObject());

        mainFrame.setActualProducts(products);
    }

    private Product[] extractProducts(Object object) {
        if (object == null) {
            return new Product[0];
        }

        if (object instanceof Object[] rawProducts) {
            return Arrays.copyOf(rawProducts, rawProducts.length, Product[].class);
        }

        return new Product[0];
    }

    private CustomPackage getCustomPackageForRequest() throws InterruptedException, IOException, ClassNotFoundException {
        CustomPackage clientCommand;

        switch (mode) {
            case CLI -> clientCommand = new CustomPackage(scanner.nextLine(), null, null);
            case GUI -> {
                return waitCommandFromGUI();
            }
            default -> {
                return null;
            }
        }

        try {
            return CommandManager.getRelevantPackage(clientCommand);
        } catch (Exception e) {
            if (e.getMessage() != null) {
                System.out.println(e.getMessage());
            }

            return null;
        }
    }

    private CustomPackage waitCommandFromGUI() throws InterruptedException, IOException, ClassNotFoundException {
        CustomPackage customPackage = guiCommands.take();

        if (isClientOnlyCommand(customPackage)) {
            return new CustomPackage(
                    customPackage.getCommand(),
                    null,
                    CommandManager.getRelevantPackage(customPackage).getObject()
            );
        }

        return customPackage;
    }

    public static void putCommand(CustomPackage c) {
        try {
            guiCommands.put(c);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(e.getMessage());
        }
    }

    public static void putAnswer(CustomPackage c) {
        try {
            guiAnswers.put(c);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(e.getMessage());
        }
    }

    private void sendRequest(CustomPackage customPackage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(customPackage);
        oos.flush();

        byte[] objectBytes = baos.toByteArray();

        synchronized (dos) {
            dos.writeInt(objectBytes.length);
            dos.write(objectBytes);
            dos.flush();
        }
    }

    private String getStringAnswer() throws IOException, InterruptedException {
        while (listening && socket != null && !socket.isClosed()) {
            CustomPackage[] answer = serverAnswers.poll(200, TimeUnit.MILLISECONDS);

            if (answer != null) {
                return commandManager.getRelevantAnswer(answer);
            }
        }

        throw new IOException("Connection lost while waiting for server answer.");
    }

    public static CustomPackage getAnswer() {
        try {
            return guiAnswers.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            ResultDialog.showError(e.getMessage());
            return null;
        }
    }

    private boolean isExitCommand(CustomPackage pkg) throws IOException, InterruptedException {
        if (pkg.getCommand().equals(new Exit().getName())) {
            sendRequest(pkg);

            System.out.println(getStringAnswer());

            return true;
        }

        return false;
    }

    private boolean isExecuteScriptCommand(CustomPackage rawPkg) throws IOException, ClassNotFoundException, InterruptedException {
        String commandName = rawPkg.getCommand();
        boolean isExecuteScript = commandName.startsWith(new ExecuteScript().getName());

        StringBuilder info = new StringBuilder();

        if (isExecuteScript) {
            ExecuteScript.isProcessing = true;

            CustomPackage pkg = CommandManager.getRelevantPackage(rawPkg);
            Object[] pkgs = (Object[]) pkg.getObject();

            for (Object pkgObject : pkgs) {
                CustomPackage singlePkg = (CustomPackage) pkgObject;

                sendRequest(singlePkg);

                String singleAnswer = getStringAnswer();

                info.append(singleAnswer);
                info.append("\n");

                System.out.println(singleAnswer);
            }

            ExecuteScript.isProcessing = false;

            ResultDialog.showInfo(info.toString());

            return true;
        }

        return false;
    }

    private boolean isClientOnlyCommand(CustomPackage pkg) throws IOException, ClassNotFoundException {
        String commandName = pkg.getCommand();

        boolean isHelp = commandName.equals(new Help().getName());
        boolean isLogout = commandName.equals(new Logout().getName());

        if (isHelp || isLogout) {
            String answer = (String) pkg.getObject();

            System.out.println(answer);

            return true;
        }

        return false;
    }

    private void closeConnection() throws IOException {
        listening = false;

        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}