package Objects.CommandsControllers.Commands;

import Commons.Collection.Product;
import Commons.CustomPackage;
import Objects.CommandsControllers.RevertableCommand;
import Objects.CommandsControllers.TrashBin;
import Objects.Managers.CollectionManager;

/**
 * update an element
 */
public class Update extends RevertableCommand {
    public Update(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArg) {
        super(collectionManager, hasArgument, hasComplexArg);
    }

    public Update(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() throws IndexOutOfBoundsException {
        checkArgument();

        Product newProduct = (Product) getComplexArgument();
        Product prevElement = getCollectionManager().getById(Long.parseLong(getArgument())).clone();
        Product updatedElement = getCollectionManager().updateElement(Long.parseLong(getArgument()), newProduct);

        TrashBin.add(prevElement);

        addToHistory();

        getReceiver().broadcastCollectionUpdate(getCollectionManager().getElements().toArray(new Product[0]));

        CustomPackage pkg = new CustomPackage(getName(), getArgument(), updatedElement);
        answer(pkg, "Updated an element with id " + pkg.getArgument());
    }


    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getDescription() {
        return "update an element";
    }

    @Override
    public void checkArgument() {
        boolean actuallyHasArgument = getArgument() != null;
        boolean actuallyHasComplexArgument = getComplexArgument() != null;
        if (actuallyHasArgument != getHasArgument() || actuallyHasComplexArgument != getHasComplexArgument())
            throw new IllegalArgumentException("Invalid format, use:\n\tupdate {Name(String);X(int);Y(double>-990);Price(double>0 | null);Man Cost(int);unit of measure | null;Owner name(String) | null;Height(float>0);eye color | null;hair color;country;location x|null;loc y;loc z;loc name}");
    }

    @Override
    public void undo() {
        Product[] restoring = TrashBin.pop();

        for (Product product : restoring) {
            Product previous = getCollectionManager().updateElement(product.getId(), product);
            CustomPackage pkg = new CustomPackage(new Update(null).getName(), getArgument(), previous);
            answer(pkg, "Element with id " + previous.getId() + " was returned to prev state ");
        }
    }
}
