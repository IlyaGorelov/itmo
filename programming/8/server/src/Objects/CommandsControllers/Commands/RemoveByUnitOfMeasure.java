package Objects.CommandsControllers.Commands;

import Commons.Collection.Product;
import Commons.CustomPackage;
import Commons.Enums.UnitOfMeasure;
import Objects.CommandsControllers.RevertableCommand;
import Objects.CommandsControllers.TrashBin;
import Objects.Managers.CollectionManager;
import Objects.Validators.UnitValidator;

/* remove all elements with the same unit of measure*/
public class RemoveByUnitOfMeasure extends RevertableCommand {
    public RemoveByUnitOfMeasure(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArg) {
        super(collectionManager, hasArgument, hasComplexArg);
    }

    @Override
    public void execute() {
        checkArgument();
        Product[] result = null;
        try {
            UnitValidator unitValidator = new UnitValidator();
            if (unitValidator.isValid(String.valueOf(getArgument()), true)) {

                UnitOfMeasure unit = getArgument() != null ? UnitOfMeasure.valueOf(getArgument().toUpperCase())
                        : null;
                result = getCollectionManager().removeByUnitOfMeasure(unit, getUser());

                TrashBin.add(result);
                addToHistory();

                CustomPackage pkg = new CustomPackage(this.getName(), getArgument(), result);
                answer(pkg, "Removed all elements with " + unit);

            } else
                throw new IllegalArgumentException("Unknown unit of measure");
        } catch (IllegalArgumentException e) {
            CustomPackage pkg = new CustomPackage(this.getName(), getArgument(), e);
            answer(pkg, e.getMessage());
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String getName() {
        return "remove_all_by_unit";
    }

    @Override
    public String getDescription() {
        return "remove all elements with the same unit of measure";
    }

    @Override
    public boolean getHasArgument() {
        return false;
    }

    @Override
    public void checkArgument() {
        boolean actuallyHasComplexArgument = getComplexArgument() != null;
        if (actuallyHasComplexArgument != getHasComplexArgument())
            throw new IllegalArgumentException("Invalid format, use:\n\tremove_all_by_unit UNIT");
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
