import Objects.CommandsControllers.CommandExecutor;
import Objects.Managers.CollectionManager;

/**
 * Main class of the program, it starts the execution
 * 
 * @author Ilya Gorelov
 */
public class App {
        /** Environment key which stores a path to csv file */
        private static final String ENV_KEY = "lab5";

        /**
         * Main method
         * 
         * @param args an array of command-line arguments
         */
        public static void main(String[] args) throws Exception {
                CollectionManager collectionManager = new CollectionManager();
                collectionManager.loadCollection(ENV_KEY);
                CommandExecutor executor = new CommandExecutor();
                System.out.println("Enter \"help\" to see all commands");
                System.out.println();
                executor.execute(collectionManager);
        }
}
