package Objects.CommandsControllers.Commands;

import Objects.Collection.Product;
import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Enums.UnitOfMeasure;
import Objects.Managers.CollectionManager;
import Objects.Validators.UnitValidator;

/* remove all elements with the same unit of measure*/
public class RemoveByUnitOfMeasure extends Command {
    public RemoveByUnitOfMeasure(CollectionManager collectionManager, boolean hasArgument,boolean hasComplexArg) {
        super(collectionManager, hasArgument,hasComplexArg);
    }

    public RemoveByUnitOfMeasure(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();
        Product[] result = null;
        try {
            UnitValidator unitValidator = new UnitValidator();
            if (unitValidator.isValid(getArgument(), true)) {
                // System.out.println("Removing all products with this unit of measure\n");

                UnitOfMeasure unit = getArgument() != null ? UnitOfMeasure.valueOf(getArgument().toUpperCase())
                        : null;
                result = getCollectionManager().removeByUnitOfMeasure(unit);

                CustomPackage pkg = new CustomPackage(this.getName(), getArgument(), result);
               answer(pkg,"Removed all elements with "+unit);

            } else
                throw new IllegalArgumentException("Unknown unit of measure");
        } catch (IllegalArgumentException e) {
            CustomPackage pkg = new CustomPackage(this.getName(), getArgument(), e);
           answer(pkg,e.getMessage());
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
    public  void checkArgument(){
        boolean actuallyHasComplexArgument = getComplexArgument() != null;
        if ( actuallyHasComplexArgument != getHasComplexArgument())
            throw new IllegalArgumentException("Invalid format, use:\n\tremove_all_by_unit UNIT");
    }
}
