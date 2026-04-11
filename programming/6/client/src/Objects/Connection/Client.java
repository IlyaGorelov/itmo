package Objects.Connection;

import java.io.BufferedReader;
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
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import Objects.CommandsControllers.Commands.Exit;
import Objects.Managers.CommandManager;

public class Client {

    private Socket socket = null;

    private static final int MAX_RETRY = 5;
    private static final int CONNECT_TIMEOUT = 5000;

    public void connect(String host, int port) {

        while (true) {
            try {
                socket = getSocketWithRetry(host, port);
                if (socket == null)
                    break;

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
            Scanner reader = new Scanner(System.in);
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            System.out.println("Client connected to server.");
            System.out.println();
            System.out.println("Type \"help\" to see all available commands.");
            CommandManager commandManager = new CommandManager(reader, in, out);

            while (!socket.isOutputShutdown()) {
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);

                String clientCommand = reader.nextLine();
                CustomPackage realCommand = null;
                try {
                    realCommand = commandManager.getRelevantPackage(clientCommand);
                } catch (Exception e) {
                    if (e.getMessage() != null)
                        System.out.println(e.getMessage());
                    continue;
                }

                oos.writeObject(realCommand);

                oos.flush();
                byte[] objectBytes = baos.toByteArray();

                dos.writeInt(objectBytes.length);
                dos.write(objectBytes);
                dos.flush();

                // System.out.println("Client sent message \"" + realCommand + "\" to server.");

                if (realCommand.getCommand().equals(new Exit().getName())) {

                    System.out.println("Client kills connections");

                    System.out.println();
                    int answerLength = dis.readInt();
                    byte[] answerBytes = new byte[answerLength];
                    dis.readFully(answerBytes);

                    ByteArrayInputStream bais = new ByteArrayInputStream(answerBytes);
                    ObjectInputStream ois = new ObjectInputStream(bais);

                    Object[] answer = (Object[]) ois.readObject();
                    String realAnswer = commandManager.getRelevantAnswer(answer);
                    System.out.println(realAnswer);

                    break;
                }

                System.out.println();
                int answerLength = dis.readInt();
                byte[] answerBytes = new byte[answerLength];
                dis.readFully(answerBytes);

                ByteArrayInputStream bais = new ByteArrayInputStream(answerBytes);
                ObjectInputStream ois = new ObjectInputStream(bais);

                Object[] answer = (Object[]) ois.readObject();
                String realAnswer = commandManager.getRelevantAnswer(answer);
                System.out.println(realAnswer);

            }
            socket.close();
            System.out.println("Closing connections & channels on clentSide - DONE.");

        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (ConnectException e) {
            System.out.println("Server doesn't listen on this port or server is not started yet.");
        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            System.out.println("User input is not detected, but all data was saved");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } // catch (ClassNotFoundException e) {
          // System.out.println(e.getMessage());
          // }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
