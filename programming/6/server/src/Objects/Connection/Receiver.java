package Objects.Connection;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.Commands.Exit;

public class Receiver {
    private Socket socket;
    private boolean wasAsked = false;
    private int port;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ArrayList<CustomPackage> answer = new ArrayList<>();

    public Receiver(int port) {
        this.port = port;
    }

    public void connect() {
        try (ServerSocket server = new ServerSocket(3345)) {
            socket = server.accept();
            System.out.print("Connection accepted.\n");

            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("DataInputStream created");

            out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("ObjectOutputStream created");

        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            System.out.println("User input is not detected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CustomPackage receive() throws IOException, ClassNotFoundException {

        while (!socket.isClosed()) {
            wasAsked = true;
            System.out.println("Server reading from channel");
            CustomPackage entry = (CustomPackage) in.readObject();
            System.out.println("READ from client message - " + entry);
            return entry;
        }
        return null;
    }

    private void clearAnswer() {
        answer.clear();
    }

    public void addToAnswer(Command command, Object arg, Object object) {
        answer.add(new CustomPackage(command, arg, object));
    }

    public void send() {
        try {
            if (wasAsked) {
                out.reset();
                out.writeObject(answer.toArray());
                System.out.println("Server send message to client.");
                out.flush();
                clearAnswer();
                wasAsked = false;
            }
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
