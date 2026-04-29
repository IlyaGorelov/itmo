import Objects.Connection.Client;

public class App {
    1. Убрать магические числа
    2. Код должен соответствовать всем правилам чистого кода (нужно будет привести примеры до/после)
    3. Сделать undo/redo через интерфейс (Revertable)
    4. Клиент должен читать скрипт и отправлять команды на сервер

    private static final String HOST = "localhost";
    private static final int PORT = 1234;

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        // System.out.println("Trying to connect to server...");
        client.connect(HOST, PORT);

    }
}