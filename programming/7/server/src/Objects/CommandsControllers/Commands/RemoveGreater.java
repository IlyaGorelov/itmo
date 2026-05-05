package Objects.CommandsControllers.Commands;

import Objects.Collection.Product;
import Objects.CommandsControllers.RevertableCommand;
import Objects.CommandsControllers.TrashBin;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;

/*remove elements greater than input */
public class RemoveGreater extends RevertableCommand {
    public RemoveGreater(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArg) {
        super(collectionManager, hasArgument, hasComplexArg);
    }

    public RemoveGreater(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();

        try {
            Product[] greaters = getCollectionManager().removeGreater((Product) getComplexArgument());

            TrashBin.add(greaters);
            addToHistory();

            CustomPackage pkg = new CustomPackage(this.getName(), null, greaters);
            answer(pkg, "Removed");

        } catch (IndexOutOfBoundsException e) {
            CustomPackage pkg = new CustomPackage(this.getName(), null, e);
            answer(pkg, "Invalid number of arguments!");
        } catch (Exception e) {
            if (e.getMessage() != null) {
                CustomPackage pkg = new CustomPackage(this.getName(), null, e);
                answer(pkg, e.getMessage());
            }
            // System.out.println("Skip\n");
        }
    }


    @Override
    public String getName() {
        return "remove_greater";
    }

    @Override
    public String getDescription() {
        return "remove elements greater than input ";
    }

    @Override
    public void checkArgument() {
        boolean actuallyHasArgument = getArgument() != null;
        boolean actuallyHasComplexArgument = getComplexArgument() != null;
        if (actuallyHasArgument != getHasArgument() || actuallyHasComplexArgument != getHasComplexArgument())
            throw new IllegalArgumentException("Invalid format, use:\n\tremove_greater {Price(double>0 | null);Man Cost(int)}");
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
