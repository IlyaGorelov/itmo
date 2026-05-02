package Objects.CommandsControllers.Commands;

import Objects.Collection.Product;
import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;

/** remove an element by id */
public class Remove extends Command {
    public Remove(boolean hasArgument) {
        super(hasArgument, false);
    }

    @Override
    public String getRelevantObject() {
        checkArgument();
        return null;
    }

    @Override
    public String getName() {
        return "remove";
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
