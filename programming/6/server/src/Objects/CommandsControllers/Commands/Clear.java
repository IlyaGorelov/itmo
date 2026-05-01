package Objects.CommandsControllers.Commands;

import Objects.Collection.Product;
import Objects.CommandsControllers.History;
import Objects.CommandsControllers.RevertableCommand;
import Objects.CommandsControllers.TrashBin;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;

/**
 * removes all elements from the collection
 */
public class Clear extends RevertableCommand {
    public Clear(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArg) {
        super(collectionManager, hasArgument, hasComplexArg);
    }

    public Clear(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();

        Product[] deleted = getCollectionManager().getElements().toArray(new Product[1]);

        TrashBin.add(deleted);

        getCollectionManager().clear();

        History.add(this, null, null);

        CustomPackage pkg = new CustomPackage(this.getName(), null, null);
        answer(pkg, "Collection successfully cleared");
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
    public void undo() {
        Product[] restoring = TrashBin.poll();

        for (Product product : restoring) {
            Product added = getCollectionManager().addElement(product.getId(), product);
            CustomPackage pkg = new CustomPackage(new Add(null).getName(), getArgument(), added);
            answer(pkg, "Added element with name " + added.getName());
        }
    }

}
