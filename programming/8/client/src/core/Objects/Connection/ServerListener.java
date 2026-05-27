package core.Objects.Connection;


import Commons.CustomPackage;

import java.io.EOFException;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

public class ServerListener implements Runnable {
    private volatile boolean listening = false;
    private Thread listenerThread;

    private final ConnectionManager connectionManager;

    private final PackageSerializer serializer;

    private final ResponseHandler responseHandler;

    private final BlockingQueue<CustomPackage[]>
            responseQueue;


    public ServerListener(
            ConnectionManager connectionManager,
            PackageSerializer serializer,
            ResponseHandler responseHandler,
            BlockingQueue<CustomPackage[]> responseQueue
    ) {

        this.connectionManager = connectionManager;
        this.serializer = serializer;
        this.responseHandler = responseHandler;
        this.responseQueue = responseQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                CustomPackage[] packages = serializer.read(connectionManager.getInput());

                responseHandler.handleServerPackages(packages, responseQueue);
            } catch (EOFException | SocketException e) {
                listening = false;
                System.out.println("Connection lost.");
                boolean reconnected = connectionManager.reconnect();
                if (!reconnected) {
                    break;
                }

            } catch (Exception e) {
                listening = false;

                System.out.println(
                        "Error while reading server message: "
                                + e.getMessage()
                );

                boolean reconnected = connectionManager.reconnect();

                if (!reconnected) {
                    break;
                }
            }
        }

        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void setListening(boolean listening){
        this.listening = listening;
    }

}
