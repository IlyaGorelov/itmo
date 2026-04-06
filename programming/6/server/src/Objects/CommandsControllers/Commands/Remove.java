package Objects.CommandsControllers.Commands;

import Objects.Collection.Product;
import Objects.CommandsControllers.Command;
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
            getReceiver().addToAnswer(this, getArgument(), p);
        } catch (IndexOutOfBoundsException e) {
            getReceiver().addToAnswer(this, null, e.getMessage());
            throw new IndexOutOfBoundsException(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            getReceiver().addToAnswer(this, null, e.getMessage());
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
