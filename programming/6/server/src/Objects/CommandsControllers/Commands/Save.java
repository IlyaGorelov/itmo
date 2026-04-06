package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Managers.CollectionManager;

/*save collection in a file */
public class Save extends Command {

    public Save(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public Save(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();
        getCollectionManager().setCollection();
        getReceiver().addToAnswer(this, null, "Successfully saved");
        System.out.println("Successfully saved");
    }

    @Override
    public String getName() {
        return "save";
    }

    @Override
    public String getDescription() {
        return "save collection in a file";
    }

}
