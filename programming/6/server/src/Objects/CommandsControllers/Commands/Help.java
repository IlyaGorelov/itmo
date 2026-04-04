package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Managers.CollectionManager;
import Objects.Managers.CommandManager;

/** shows all available commands with description */
public class Help extends Command {
    public Help(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public Help(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();
        CommandManager commandManager = new CommandManager(getCollectionManager(), getReceiver());
        commandManager.getCommandMap()
                .forEach((name, command) -> System.out.println(name + ": " + command.getDescription()));
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
