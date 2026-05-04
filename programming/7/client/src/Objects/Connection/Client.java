package Objects.Connection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import Objects.CommandsControllers.Commands.ExecuteScript;
import Objects.CommandsControllers.Commands.Exit;
import Objects.Managers.CommandManager;

public class Client {
    private Socket socket = null;

    private static final int MAX_RETRY = 5;
    private static final int CONNECT_TIMEOUT = 5000;

    InputStream in;
    OutputStream out;

    DataInputStream dis;
    DataOutputStream dos;

    Scanner scanner;

    CommandManager commandManager;

    private void initializeIO(Socket socket) throws IOException {
        in = socket.getInputStream();
        out = socket.getOutputStream();

        dis = new DataInputStream(in);
        dos = new DataOutputStream(out);

        scanner = new Scanner(System.in);
    }

    public void connect(String host, int port) {
        while (true) {
            try {
                socket = getSocketWithRetry(host, port);
                if (socket == null)
                    break;

                initializeIO(socket);

                work();
                break;
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                System.out.println("Trying to reconnect...");
            } catch (IOException e) {
                System.out.println("Connection lost: " + e.getMessage());
                System.out.println("Trying to reconnect...");
            }
        }

    }

    private Socket getSocketWithRetry(String host, int port)
            throws IOException, InterruptedException {
        int retry = 0;
        int delay = 1000;

        while (retry < MAX_RETRY) {
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
        return socket;

    }

    private void work() {
        try {
            commandManager = new CommandManager(scanner, in, out);

            System.out.println("Client connected to server.");
            System.out.println();
            System.out.println("Type \"help\" to see all available commands.");

            while (!socket.isOutputShutdown()) {
                CustomPackage customPackage = getCustomPackage();

                if (customPackage == null) continue;

                if (isExitCommand(customPackage)) {
                    break;
                }

                if (isExecuteScriptCommand(customPackage)) {
                    continue;
                }

                sendRequest(customPackage);

                System.out.println();
                System.out.println(getAnswer());
            }
            socket.close();
            System.out.println("Closing connections & channels on clentSide - DONE.");

        } catch (ConnectException e) {
            System.out.println("Server doesn't listen on this port or server is not started yet.");
        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            System.out.println("User input is not detected, but all data was saved");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private CustomPackage getCustomPackage() {
        String clientCommand = scanner.nextLine();
        CustomPackage customPackage = null;

        try {
            customPackage = CommandManager.getRelevantPackage(clientCommand);
            return customPackage;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    private void sendRequest(CustomPackage customPackage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(customPackage);
        oos.flush();

        byte[] objectBytes = baos.toByteArray();

        dos.writeInt(objectBytes.length);
        dos.write(objectBytes);
        dos.flush();
    }

    private String getAnswer() throws IOException, ClassNotFoundException {
        int answerLength = dis.readInt();
        byte[] answerBytes = new byte[answerLength];

        dis.readFully(answerBytes);

        ByteArrayInputStream bais = new ByteArrayInputStream(answerBytes);
        ObjectInputStream ois = new ObjectInputStream(bais);

        Object[] answer = (Object[]) ois.readObject();
        return commandManager.getRelevantAnswer(answer);
    }

    private boolean isExitCommand(CustomPackage pkg) throws IOException, ClassNotFoundException {
        if (pkg.getCommand().equals(new Exit().getName())) {
            System.out.println("Client kills connections");
            System.out.println();

            sendRequest(pkg);

            System.out.println(getAnswer());
            return true;
        }
        return false;
    }

    private boolean isExecuteScriptCommand(CustomPackage pkg) throws IOException, ClassNotFoundException {
        String commandName = pkg.getCommand();
        boolean isExecuteScript = commandName.equals(new ExecuteScript().getName());

        if (isExecuteScript) {
            Object[] pkgs = (Object[]) pkg.getObject();

            for (Object pkgObject : pkgs) {
                CustomPackage singlePkg = (CustomPackage) pkgObject;

                sendRequest(singlePkg);
                System.out.println(getAnswer());
            }
            return true;
        }
        return false;
    }
}
