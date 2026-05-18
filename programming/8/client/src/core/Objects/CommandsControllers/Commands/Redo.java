package core.Objects.CommandsControllers.Commands;

import core.Objects.CommandsControllers.AuthChecker;
import core.Objects.CommandsControllers.Command;
import Commons.CustomPackage;

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
