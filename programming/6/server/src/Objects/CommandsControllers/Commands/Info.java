package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;

/** show information about collection */
public class Info extends Command {

    public Info(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public Info(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();
        System.out.println("Information about collection:\n");
        System.out.println(getCollectionManager().getCollectionInfo());
        CustomPackage pkg = new CustomPackage(this.getName(), null, getCollectionManager().getCollectionInfo());
        getReceiver().addToAnswer(getCLient(), pkg);
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
