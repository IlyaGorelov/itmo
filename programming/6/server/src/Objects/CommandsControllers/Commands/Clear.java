package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;

/** removes all elements from the collection */
public class Clear extends Command {
    public Clear(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public Clear(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();
        getCollectionManager().clear();

        if (!getIsCLIMode()) {
            CustomPackage pkg = new CustomPackage(this.getName(), null, null);
            getReceiver().addToAnswer(getCLient(), pkg);
        } else {
            getReceiver().addAnswerForCLI("Collection successfully cleared");
        }
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "remove all elements from the collection";
    }

    @Override
    public void executeFromScript(String complexArg) {
        execute();
    }

}
