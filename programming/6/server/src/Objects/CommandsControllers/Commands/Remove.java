package Objects.CommandsControllers.Commands;

import Objects.Collection.Product;
import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;

/** remove an element by id */
public class Remove extends Command {
    public Remove(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public Remove(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() throws IndexOutOfBoundsException {
        checkArgument();
        Product p = null;
        try {
            long id = Long.parseLong(getArgument());
            p = getCollectionManager().deleteById(id);

            if (!getIsCLIMode()) {
                CustomPackage pkg = new CustomPackage(this.getName(), getArgument(), p);
                getReceiver().addToAnswer(getCLient(), pkg);
            } else {
                getReceiver().addAnswerForCLI("Successfully removed " + p.getName());
            }

        } catch (IndexOutOfBoundsException e) {
            CustomPackage pkg = new CustomPackage(this.getName(), getArgument(), e.getMessage());
            getReceiver().addToAnswer(getCLient(), pkg);
            throw new IndexOutOfBoundsException(e.getMessage());
        } catch (Exception e) {
            CustomPackage pkg = new CustomPackage(this.getName(), getArgument(), e.getMessage());
            getReceiver().addToAnswer(getCLient(), pkg);
            // System.out.println(e.getMessage());
        }

    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "remove an element by id";
    }

}
