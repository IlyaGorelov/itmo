package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.CommandExecutor;
import Objects.Managers.CollectionManager;

/** stop program */
public class Close extends Command {

    public Close(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public Close(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public String getName() {
        return "close";
    }

    @Override
    public String getDescription() {
        return "close the program";
    }

    @Override
    public void execute() {
        checkArgument();
        CommandExecutor.waitForNextCommand = false;
    }

    @Override
    public void executeFromScript(String complexArg) {
        execute();
    }

}
