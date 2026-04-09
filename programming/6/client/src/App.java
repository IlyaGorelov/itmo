
import Objects.Connection.Client;

public class App {
    private static final String HOST = "localhost";
    private static final int PORT = 1234;

    // TODO: add logger
    // TODO:Для обмена данными на сервере необходимо использовать сетевой канал
    // Для обмена данными на клиенте необходимо использовать потоки ввода-вывода
    // Сетевые каналы должны использоваться в неблокирующем режиме.
    public static void main(String[] args) throws Exception {
        Client client = new Client();
        // System.out.println("Trying to connect to server...");
        client.connect(HOST, PORT);
    }
}