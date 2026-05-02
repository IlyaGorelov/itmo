package Objects.CommandsControllers.Commands;

import Objects.Collection.Product;
import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;

/** show any element where unit of measure is minimal */
public class MinByUnit extends Command {
    public MinByUnit(boolean hasArgument) {
        super(hasArgument, false);
    }

    public MinByUnit() {
        super();
    }

    @Override
    public String getName() {
        return "min_by_unit_of_measure";
    }

    @Override
    public String getRelevantObject() {
        checkArgument();
        return null;
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object object = pack.getObject();

        if (object == null)
            return "Collection is empty.";
        if (object instanceof Product)
            return "An element with min unit:\n" + ((Product) object).toString() + "\n";
        else
            return "Element wasn't removed as " + object.toString() + "\n";

    }

}
