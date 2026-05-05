package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.AuthChecker;
import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;

/** show information about collection */
public class Redo extends Command implements AuthChecker {

    public Redo() {
        super();
    }

    @Override
    public String getName() {
        return "redo";
    }

    @Override
    public String getDescription() {
        return "undo(undo)";
    }

    @Override
    public String getRelevantObject() {
        checkArgument();
        checkAuth();
        return null;
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object arg = pack.getObject();
        return arg.toString() + "\n";
    }

}
