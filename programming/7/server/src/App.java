import Objects.CommandsControllers.CommandExecutor;
import Objects.Connection.Receiver;
import Objects.Managers.AuthManager;
import Objects.Managers.CollectionManager;

/**
 * Main class of the program, it starts the execution
 *
 * @author Ilya Gorelov
 */
public class App {
    private static final String ENV_KEY = "postgres";
    private static final String PROPS = "props_7";

    private static final int PORT = 1234;

    public static void main(String[] args) throws Exception {
        CollectionManager collectionManager = new CollectionManager(ENV_KEY, PROPS);
        AuthManager authManager = new AuthManager(ENV_KEY, PROPS);
        CommandExecutor executor = new CommandExecutor(collectionManager, authManager);
        Receiver receiver = new Receiver(PORT, executor);
        receiver.connect();
    }
}
