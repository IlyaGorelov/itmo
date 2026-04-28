package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;
import Objects.Managers.CommandManager;

import java.util.TreeMap;

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
        checkArgument();
        CommandManager commandManager = !getIsCLIMode() ?
                new CommandManager(getCollectionManager(), getReceiver(), getCLient()) :
                new CommandManager(getCollectionManager(), getReceiver(), true);

        StringBuilder answer = new StringBuilder();
        new TreeMap<String, Command>(commandManager.getCommandMap()).forEach((name, command) -> {
            if (getIsCLIMode() || command.getClass() != GetById.class)
                answer.append(name).append(": ").append(command.getDescription()).append("\n");
        });

        String finalAnswer = answer.toString();

        var pkg = new CustomPackage(this.getName(), null, finalAnswer);
        answer(pkg, finalAnswer);
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
