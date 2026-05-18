package core.Objects.CommandsControllers.Commands;

import Commons.Collection.Product;
import core.Objects.CommandsControllers.AuthChecker;
import core.Objects.CommandsControllers.Command;
import Commons.CustomPackage;

/** show any element where unit of measure is minimal */
public class MinByUnit extends Command implements AuthChecker {

    public MinByUnit() {
        super();
    }

    @Override
    public String getName() {
        return "min_by_unit";
    }

    @Override
    public String getDescription() {
        return "show any element where unit of measure is minimal";
    }


    @Override
    public String getRelevantObject() {
        checkArgument();
        checkAuth();
        return null;
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object object = pack.getObject();

        if (object == null)
            return "Collection is empty.";
        if (object instanceof Product)
            return "An element with min unit:\n" + object + "\n";
        else
            return "Element wasn't removed as " + object + "\n";

    }

}
