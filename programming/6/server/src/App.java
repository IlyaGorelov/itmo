import Objects.CommandsControllers.CommandExecutor;
import Objects.Connection.Receiver;
import Objects.Managers.CollectionManager;

/**
 * Main class of the program, it starts the execution
 * 
 * @author Ilya Gorelov
 */
public class App {

        /** Environment key which stores a path to csv file */
        private static final String ENV_KEY = "lab5";

        private static final int PORT = 1234;

        /**
         * Main method
         * 
         * @param args an array of command-line arguments
         */
        public static void main(String[] args) throws Exception {
                CollectionManager collectionManager = new CollectionManager();
                collectionManager.loadCollection(ENV_KEY);
                CommandExecutor executor = new CommandExecutor(collectionManager);
                Receiver receiver = new Receiver(PORT, executor);
                receiver.connect();
        }
}
