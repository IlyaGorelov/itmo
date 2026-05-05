package Objects.CommandsControllers.Commands;

import Objects.Collection.Product;
import Objects.CommandsControllers.RevertableCommand;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;

/**
 * Adds element to a collection if this element gonna be max
 */
public class AddIfMax extends RevertableCommand {
    public AddIfMax(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArg) {
        super(collectionManager, hasArgument, hasComplexArg);
    }

    public AddIfMax(CollectionManager collectionManager) {
        super(collectionManager);
    }

    /**
     * Asks for required fields then creates new element - Product, then put it into
     * collection if it's gonna be max
     */
    @Override
    public void execute() {
        checkArgument();
        Product newProduct = (Product) getComplexArgument();
        if (getCollectionManager().isMax(newProduct)) {
            newProduct = getCollectionManager().addElement(newProduct);

            addToHistory();

            CustomPackage pkg = new CustomPackage(this.getName(), null, newProduct, getCollectionManager().getCurrentUser());
            answer(pkg, "Successfully added");
        } else {
            CustomPackage pkg = new CustomPackage(this.getName(), null, newProduct, getCollectionManager().getCurrentUser());
            answer(pkg, "Not added");
        }
    }

    @Override
    public void checkArgument() {
        boolean actuallyHasArgument = getArgument() != null;
        boolean actuallyHasComplexArgument = getComplexArgument() != null;
        if (actuallyHasArgument != getHasArgument() || actuallyHasComplexArgument != getHasComplexArgument())
            throw new IllegalArgumentException("Invalid format, use:\n\tadd_if_max {Name(String);X(int);Y(double>-990);Price(double>0 | null);Man Cost(int);unit of measure | null;Owner name(String) | null;Height(float>0);eye color | null;hair color;country;location x|null;loc y;loc z;loc name}");
    }

    @Override
    public String getName() {
        return "add_if_max";
    }

    @Override
    public String getDescription() {
        return "add new element if the new is bigger than the max of collection";
    }

    @Override
    public void undo() {
        Product complexArg = (Product) getComplexArgument();

        long id = complexArg.getId();
        Product deleted = getCollectionManager().deleteById(id);

        CustomPackage pkg = new CustomPackage(new Remove(null).getName(), getArgument(), deleted, getCollectionManager().getCurrentUser());
        answer(pkg, "Successfully removed " + deleted.getName());
    }
}
