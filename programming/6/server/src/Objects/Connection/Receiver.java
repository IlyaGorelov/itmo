package Objects.Connection;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Objects.CommandsControllers.Commands.Close;

public class Receiver {
    private Socket socket;
    private int port;
    private DataInputStream in;
    private ObjectOutputStream out;

    public Receiver(int port) {
        this.port = port;
    }

    public void connect() {
        try (ServerSocket server = new ServerSocket(3345)) {
            Socket socket = server.accept();
            System.out.print("Connection accepted.");

            in = new DataInputStream(socket.getInputStream());
            System.out.println("DataInputStream created");

            out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("ObjectOutputStream created");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receive() throws IOException {
        while (!socket.isClosed()) {
            System.out.println("Server reading from channel");
            String entry = in.readUTF();
            System.out.println("READ from client message - " + entry);
            return entry;
        }
        return null;

    }

    public void answer(Object object) {
        try {
            out.writeObject(object);
            System.out.println("Server send message to client.");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels.");
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
