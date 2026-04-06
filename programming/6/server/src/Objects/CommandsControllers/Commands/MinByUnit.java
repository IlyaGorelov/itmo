package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Managers.CollectionManager;

/** show any element where unit of measure is minimal */
public class MinByUnit extends Command {
    public MinByUnit(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public MinByUnit(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();
        try {
            System.out.println("Any element with min unit of measure:\n");
            System.out.println(getCollectionManager().getMinByUnitOfMeasure());
            getReceiver().addToAnswer(this, null, getCollectionManager().getMinByUnitOfMeasure());
        } catch (Exception e) {
            getReceiver().addToAnswer(this, null, e);
        }
    }

    @Override
    public String getName() {
        return "min_by_unit_of_measure";
    }

    @Override
    public String getDescription() {
        return "show any element where unit of measure is minimal";
    }

}
