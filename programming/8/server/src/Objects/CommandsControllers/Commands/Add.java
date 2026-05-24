package Objects.CommandsControllers.Commands;

import Commons.Collection.Product;
import Commons.CustomPackage;
import Objects.CommandsControllers.RevertableCommand;
import Objects.Managers.CollectionManager;

public class Add extends RevertableCommand {
    public Add(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArgument) {
        super(collectionManager, hasArgument, hasComplexArgument);
    }

    public Add(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();
        Product newProduct = (Product) getComplexArgument();
        Product added = getCollectionManager().addElement(newProduct);

        getReceiver().broadcastCollectionUpdate(getCollectionManager().getElements().toArray(new Product[0]));

        setComplexArgument(added);
        addToHistory();

        CustomPackage pkg = new CustomPackage(this.getName(), null, newProduct, getUser());

        answer(pkg, "Successfully added " + newProduct.getName());
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "add new element";
    }

    @Override
    public void undo() {
        Product complexArg = (Product) getComplexArgument();

        long id = complexArg.getId();
        Product deleted = getCollectionManager().deleteById(id, getUser());

        CustomPackage pkg = new CustomPackage(new Remove(null).getName(), getArgument(), deleted, getUser());
        answer(pkg, "Successfully removed " + deleted.getName());
    }
}
