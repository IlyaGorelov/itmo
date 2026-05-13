package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;

/** show information about collection */
public class Info extends Command {

    public Info(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArg) {
        super(collectionManager, hasArgument,hasComplexArg);
    }

    public Info(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
            checkArgument();
            CustomPackage pkg = new CustomPackage(this.getName(), null, getCollectionManager().getCollectionInfo());
        answer(pkg,getCollectionManager().getCollectionInfo());

    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Show collection information";
    }

}
