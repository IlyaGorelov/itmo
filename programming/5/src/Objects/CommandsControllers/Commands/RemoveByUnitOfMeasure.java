package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Enums.UnitOfMeasure;
import Objects.Managers.CollectionManager;

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
        try {
            UnitOfMeasure unit = UnitOfMeasure.values()[Integer.parseInt(getArgument())];
            var ids = getCollectionManager().getIdsByUnitOfMeasure(unit);

            for (var i : ids) {
                getCollectionManager().deleteById(i);
                System.out.println("Successfully deleted");
            }
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(e.getMessage());
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
