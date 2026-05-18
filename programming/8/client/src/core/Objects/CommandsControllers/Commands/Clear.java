package core.Objects.CommandsControllers.Commands;

import core.Objects.CommandsControllers.AuthChecker;
import core.Objects.CommandsControllers.Command;
import Commons.CustomPackage;

/** removes all elements from the collection */
public class Clear extends Command implements AuthChecker {

    public Clear() {
        super();
    }

    @Override
    public String getRelevantObject() {
        checkArgument();
        checkAuth();
        return null;
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "remove all your elements from the collection";
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object object = pack.getObject();

        if (object instanceof Exception)
            return "Collection wasn't cleared as " + object.toString() + "\n";

        Object[] productObjects = (Object[]) object;

        return "Deleted "+productObjects.length+" elements";
    }

}
