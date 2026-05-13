import Objects.CommandsControllers.CommandExecutor;
import Objects.Connection.Receiver;
import Objects.Helpers.PasswordHasher;
import Objects.Managers.AuthManager;
import Objects.Managers.CollectionManager;
import Objects.Managers.DBManager;

public class App {
    private static final String URL_POSTGRES = "url_postgres";
    private static final String PROPS_POSTGRES = "props_postgres";
    private static final String PEPPER = "lab7_pepper";

    private static final int PORT = 1234;

    public static void main(String[] args) throws Exception {
        try {
            DBManager.setDbURL(URL_POSTGRES);
            DBManager.setPathToProps(PROPS_POSTGRES);

            PasswordHasher.setPEPPER(PEPPER);
            
            CollectionManager collectionManager = new CollectionManager();
            AuthManager authManager = new AuthManager();

            CommandExecutor executor = new CommandExecutor(collectionManager, authManager);
            Receiver receiver = new Receiver(PORT, executor);

            receiver.connect();
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }
}
