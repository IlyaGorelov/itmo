package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;

/*save collection in a file */
public class Save extends Command {

    public Save(CollectionManager collectionManager, boolean hasArgument,boolean hasComplexArg) {
        super(collectionManager, hasArgument,hasComplexArg);
    }

    public Save(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();

        getCollectionManager().setCollection();
            CustomPackage pkg = new CustomPackage(this.getName(), getArgument(), "Successfully saved");
            answer(pkg,"Saved");


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
