package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;

/** show any element where unit of measure is minimal */
public class MinByUnit extends Command {
    public MinByUnit(CollectionManager collectionManager, boolean hasArgument,boolean hasComplexArg) {
        super(collectionManager, hasArgument,hasComplexArg);
    }

    public MinByUnit(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();
        try {

            CustomPackage pkg = new CustomPackage(this.getName(), null, getCollectionManager().getMinByUnitOfMeasure());
            answer(pkg,getCollectionManager().getMinByUnitOfMeasure());
        } catch (Exception e) {
            CustomPackage pkg = new CustomPackage(this.getName(), null, e);
            answer(pkg,e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "min_by_unit";
    }

    @Override
    public String getDescription() {
        return "show any element where unit of measure is minimal";
    }

}
