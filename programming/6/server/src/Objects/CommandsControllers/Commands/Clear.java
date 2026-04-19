package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;

/** removes all elements from the collection */
public class Clear extends Command {
    public Clear(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArg) {
        super(collectionManager, hasArgument, hasComplexArg);
    }

    public Clear(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();
        getCollectionManager().clear();

            CustomPackage pkg = new CustomPackage(this.getName(), null, null);
        answer(pkg,"Collection successfully cleared");
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "remove all elements from the collection";
    }

}
