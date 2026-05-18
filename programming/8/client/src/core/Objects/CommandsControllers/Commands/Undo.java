package core.Objects.CommandsControllers.Commands;

import core.Objects.CommandsControllers.AuthChecker;
import core.Objects.CommandsControllers.Command;
import Commons.CustomPackage;

/** show information about collection */
public class Undo extends Command implements AuthChecker {

    public Undo() {
        super();
    }

    @Override
    public String getName() {
        return "undo";
    }

    @Override
    public String getDescription() {
        return "undo prev command";
    }

    @Override
    public String getRelevantObject() {
        checkArgument();
        checkAuth();
        return null;
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object arg = (Object) pack.getObject();

        return arg.toString() + "\n";
    }

}
