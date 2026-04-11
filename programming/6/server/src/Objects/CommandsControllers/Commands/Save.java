package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
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
        // System.out.println("Successfully saved");
        CustomPackage pkg = new CustomPackage(this.getName(), getArgument(), "Successfully saved");
        getReceiver().addToAnswer(getCLient(), pkg);

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
