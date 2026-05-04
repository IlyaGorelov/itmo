import Objects.Connection.Client;

public class App {
    private static final String HOST = "localhost";
    private static final int PORT = 1234;

    // Сделать чтобы регистрация была необязательной,
    // но когда залогинился эти команды исчезали
    // не авторизованные юзеры не могут юзать комманды
    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.connect(HOST, PORT);

    }
}