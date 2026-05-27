package core.Objects.Connection;

import core.Objects.Managers.CommandManager;
import gui.Objects.Frames.ConnectingFrame;
import gui.Objects.Frames.LoginFrame;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnectionManager {

    private Socket socket;

    private String host;
    private int port;

    private static final int MAX_RETRY = 5;
    private static final int CONNECT_TIMEOUT = 5000;

    private DataInputStream dis;
    private DataOutputStream dos;

    private final Object connectionLock = new Object();

    public ConnectionManager(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
      @return {@code result}. {@code true} if success, {@code false} if fail
     */
    public boolean connect(Client.Mode mode) {
        JFrame connectingFrame = new ConnectingFrame();
        connectingFrame.setVisible(true);
        while (true) {
            try {
                CommandManager.setMode(mode);
                socket = getSocketWithRetry(host, port);
                if (socket == null) {
                    connectingFrame.dispose();
                    return false;
                }
                initializeIO(socket);
                connectingFrame.dispose();
                new LoginFrame().setVisible(true);

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
        return true;
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

    private void initializeIO(Socket socket) throws IOException {
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        dis = new DataInputStream(in);
        dos = new DataOutputStream(out);
    }

    public synchronized boolean reconnect() {
        System.out.println("Trying to reconnect to server...");
        Frame connectingFrame = new ConnectingFrame();
        connectingFrame.setVisible(true);
        int retry = 0;
        int delay = 1000;
        while (retry < MAX_RETRY) {
            try {
                closeConnection();
                Socket newSocket = new Socket();
                newSocket.connect(new InetSocketAddress(host, port), CONNECT_TIMEOUT);
                synchronized (connectionLock) {
                    socket = newSocket;
                    initializeIO(socket);
                }
                System.out.println("Reconnected to server.");
                connectingFrame.dispose();
                return true;
            } catch (Exception e) {
                retry++;
                System.out.println("Reconnect attempt " + retry + "/" + MAX_RETRY + " failed.");
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    return false;
                }
                delay *= 2;
            }
        }
        System.out.println("Failed to reconnect.");
        return false;
    }

    public void closeConnection() throws IOException {
        synchronized (connectionLock) {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    public DataInputStream getInput(){
        return dis;
    }

    public DataOutputStream getOutput(){
        return dos;
    }
}