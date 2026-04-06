package Objects.CommandsControllers.Commands;

import Objects.Collection.Product;
import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Enums.UnitOfMeasure;
import Objects.Validators.UnitValidator;

/* remove all elements with the same unit of measure*/
public class RemoveByUnitOfMeasure extends Command {
    public RemoveByUnitOfMeasure(boolean hasArgument) {
        super(hasArgument, false);
    }

    public RemoveByUnitOfMeasure() {
        super();
    }

    @Override
    public String getName() {
        return "remove_all_by_unit_of_measure";
    }

    @Override
    public String getRelevantObject() {
        checkArgument();
        return null;
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object object = pack.getObject();
        if (object instanceof Exception)
            return ((Exception) object).getMessage() + "\n";

        Object[] products = (Object[]) object;
        String relevant = "";
        if (products.length == 0)
            return "There are no elements with this unit: " + pack.getArgument() + " \n";

        for (Object product : products) {
            relevant += "Element with id " + ((Product) product).getId() + " was removed\n";
        }
        return relevant;
    }

}
