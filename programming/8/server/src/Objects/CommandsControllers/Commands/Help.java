package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Managers.CollectionManager;

/**
 * shows all available commands with description
 */
public class Help extends Command {
    public Help(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArg) {
        super(collectionManager, hasArgument, hasComplexArg);
    }

    public Help(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Show descriptions of available commands";
    }

}
