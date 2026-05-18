package core.Objects.CommandsControllers.Commands;

import Commons.Collection.Product;
import core.Objects.CommandsControllers.AuthChecker;
import core.Objects.CommandsControllers.Command;
import Commons.CustomPackage;

/** remove an element by id */
public class Remove extends Command implements AuthChecker {
    public Remove(boolean hasArgument) {
        super(hasArgument);
    }

    @Override
    public String getRelevantObject() {
        checkArgument();
        checkAuth();
        return null;
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "remove an element by id";
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object object = pack.getObject();

        if (object instanceof Product)
            return "Element with name \"" + ((Product) object).getName() + "\" was succesfully removed" + "\n";
        else
            return "Element wasn't removed as " + object.toString() + "\n";
    }

}
