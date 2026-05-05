package Objects.CommandsControllers.Commands;

import Objects.Collection.Product;
import Objects.CommandsControllers.History;
import Objects.CommandsControllers.RevertableCommand;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;

/**
 * Adds element to a collection if this element gonna be max
 */
public class AddIfMin extends RevertableCommand {

    public AddIfMin(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArg) {
        super(collectionManager, hasArgument, hasComplexArg);
    }

    public AddIfMin(CollectionManager collectionManager) {
        super(collectionManager);
    }

    /**
     * Asks for required fields then creates new element - Product, then put it into
     * collection if it's gonna be min
     */
    @Override
    public void execute() {
        checkArgument();
        Product newProduct = (Product) getComplexArgument();
        if (getCollectionManager().isMin(newProduct)) {
            newProduct = getCollectionManager().addElement(newProduct);

            History.add(this, getArgument(), getComplexArgument());

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
            throw new IllegalArgumentException("Invalid format, use:\n\tadd_if_min {Name(String);X(int);Y(double>-990);Price(double>0 | null);Man Cost(int);unit of measure | null;Owner name(String) | null;Height(float>0);eye color | null;hair color;country;location x|null;loc y;loc z;loc name}");
    }

    @Override
    public String getName() {
        return "add_if_min";
    }

    @Override
    public String getDescription() {
        return "add new element if the new is smaller than the min of collection";
    }

    @Override
    public void undo() {
        Product complexArg = (Product) getComplexArgument();

        long id = complexArg.getId();
        Product deleted = getCollectionManager().deleteById(id);

        CustomPackage pkg = new CustomPackage(new Remove(null).getName(), getArgument(), deleted);
        answer(pkg, "Successfully removed " + deleted.getName());
    }
}
