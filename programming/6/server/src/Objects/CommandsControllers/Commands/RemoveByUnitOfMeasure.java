package Objects.CommandsControllers.Commands;

import Objects.Collection.Product;
import Objects.CommandsControllers.Command;
import Objects.Enums.UnitOfMeasure;
import Objects.Managers.CollectionManager;
import Objects.Validators.UnitValidator;

/* remove all elements with the same unit of measure*/
public class RemoveByUnitOfMeasure extends Command {
    public RemoveByUnitOfMeasure(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public RemoveByUnitOfMeasure(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();
        Product[] removes = null;
        try {
            UnitValidator unitValidator = new UnitValidator();
            if (unitValidator.isValid(getArgument(), false)) {
                System.out.println("Removing all products with this unit of measure\n");
                UnitOfMeasure unit = UnitOfMeasure.valueOf(getArgument().toUpperCase());
                removes = getCollectionManager().removeByUnitOfMeasure(unit);
                getReceiver().addToAnswer(this, getArgument(), removes);

            } else
                throw new IllegalArgumentException("Unknown unit of measure");
        } catch (IllegalArgumentException e) {
            getReceiver().addToAnswer(this, getArgument(), e);
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String getName() {
        return "remove_all_by_unit_of_measure";
    }

    @Override
    public String getDescription() {
        return "remove all elements with the same unit of measure";
    }

}
