package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;

/** show information about collection */
public class Redo extends Command {

    public Redo() {
        super();
    }

    @Override
    public String getName() {
        return "redo";
    }

    @Override
    public String getRelevantObject() {
        checkArgument();
        return null;
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object arg = (Object) pack.getObject();
        return arg.toString() + "\n";
    }

}
