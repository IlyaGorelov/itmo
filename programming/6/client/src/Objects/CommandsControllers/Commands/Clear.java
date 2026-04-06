package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;

/** removes all elements from the collection */
public class Clear extends Command {

    public Clear() {
        super();
    }

    @Override
    public String getRelevantObject() {
        checkArgument();
        return getName();
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object object = pack.getObject();

        if (object instanceof Exception)
            return "Collection wasn't cleared as " + object.toString() + "\n";
        else
            return "Collection was succesfully cleared" + "\n";
    }

}
