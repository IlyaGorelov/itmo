
import Objects.Connection.Client;

public class App {
    // TODO: add logger
    // TODO:Для обмена данными на сервере необходимо использовать сетевой канал
    // Для обмена данными на клиенте необходимо использовать потоки ввода-вывода
    // Сетевые каналы должны использоваться в неблокирующем режиме.
    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.connect();
    }
}