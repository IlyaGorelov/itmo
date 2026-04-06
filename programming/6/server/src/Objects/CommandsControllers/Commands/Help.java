package Objects.CommandsControllers.Commands;

import java.util.TreeMap;

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

        StringBuilder answer = new StringBuilder();
        new TreeMap<String, Command>(commandManager.getCommandMap()).forEach((name, command) -> {
            if (name != new Save(null).getName() && name != new GetById(null).getName())
                answer.append(name).append(": ").append(command.getDescription()).append("\n");
        });

        String finalAnswer = answer.toString();
        getReceiver().addToAnswer(new Help(null), null, finalAnswer);
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
