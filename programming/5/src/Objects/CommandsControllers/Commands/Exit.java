package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
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
        return "close the program";
    }

    @Override
    public void execute() {
        checkArgument();
        System.exit(0);
    }

}
