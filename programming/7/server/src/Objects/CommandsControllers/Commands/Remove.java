package Objects.CommandsControllers.Commands;

import Objects.Collection.Product;
import Objects.CommandsControllers.RevertableCommand;
import Objects.CommandsControllers.TrashBin;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;

/**
 * remove an element by id
 */
public class Remove extends RevertableCommand {
    public Remove(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArg) {
        super(collectionManager, hasArgument, hasComplexArg);
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
            TrashBin.add(p);

            addToHistory();

            CustomPackage pkg = new CustomPackage(this.getName(), getArgument(), p);
            answer(pkg, "Successfully removed " + p.getName());

        } catch (IndexOutOfBoundsException e) {
            CustomPackage pkg = new CustomPackage(this.getName(), getArgument(), e.getMessage());
            answer(pkg, e.getMessage());
            throw new IndexOutOfBoundsException(e.getMessage());
        } catch (Exception e) {
            CustomPackage pkg = new CustomPackage(this.getName(), getArgument(), e.getMessage());
            answer(pkg, e.getMessage());
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

    @Override
    public void checkArgument() {
        boolean actuallyHasArgument = getArgument() != null;
        boolean actuallyHasComplexArgument = getComplexArgument() != null;
        if (actuallyHasArgument != getHasArgument() || actuallyHasComplexArgument != getHasComplexArgument())
            throw new IllegalArgumentException("Invalid format, use:\n\tremove id");
    }

    @Override
    public void undo() {
        Product[] restoring = TrashBin.pop();

        for (Product product : restoring) {
            Product added = getCollectionManager().addElement(product.getId(), product);
            CustomPackage pkg = new CustomPackage(new Add(null).getName(), getArgument(), added);
            answer(pkg, "Added element with name " + added.getName());
        }
    }
}
