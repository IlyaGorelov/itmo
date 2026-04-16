package Objects.CommandsControllers.Commands;

import java.util.TreeMap;

import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
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
        CommandManager commandManager =  !getIsCLIMode() ?
                new CommandManager(getCollectionManager(), getReceiver(), getCLient()) :
                new CommandManager(getCollectionManager(),getReceiver(),true);

        StringBuilder answer = new StringBuilder();
        new TreeMap<String, Command>(commandManager.getCommandMap()).forEach((name, command) -> {
            if (name != new Save(null).getName() && name != new GetById(null).getName())
                answer.append(name).append(": ").append(command.getDescription()).append("\n");
        });

        String finalAnswer = answer.toString();

        if (!getIsCLIMode()) {
            var pkg = new CustomPackage(this.getName(), null, finalAnswer);
            getReceiver().addToAnswer(getCLient(), pkg);
        } else
            getReceiver().addAnswerForCLI(finalAnswer);
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
