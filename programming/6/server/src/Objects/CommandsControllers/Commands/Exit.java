package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.CommandExecutor;
import Objects.Managers.CollectionManager;

/** stop program */
public class Exit extends Command {

    public Exit(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public Exit(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String getDescription() {
        return "exit the program";
    }

    @Override
    public void execute() {
        checkArgument();
        var save = new Save(getCollectionManager());
        save.setReceiver(getReceiver());
        save.execute();

        getReceiver().addToAnswer(this, null, "Program successfully stopped");
        getReceiver().send();
        CommandExecutor.waitForNextCommand = false;
    }

    @Override
    public void executeFromScript(String complexArg) {
        execute();
    }

}
