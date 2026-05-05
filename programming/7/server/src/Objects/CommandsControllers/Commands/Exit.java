package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.CommandExecutor;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;

/**
 * stop program
 */
public class Exit extends Command {

    public Exit(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArg) {
        super(collectionManager, hasArgument, hasComplexArg);
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

        CustomPackage pkg = new CustomPackage(this.getName(), getArgument(), "Program successfully stopped", getCollectionManager().getCurrentUser());
        answer(pkg, "Program successfully stopped");

        CommandExecutor.waitForNextCommand = false;

    }

}
