import Objects.Connection.Client;

public class App {

    private static final String HOST = "localhost";
    private static final int PORT = 1234;

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        // System.out.println("Trying to connect to server...");
        client.connect(HOST, PORT);

    }
}