package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.AuthChecker;
import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;

/**
 * show information about collection
 */
public class Info extends Command implements AuthChecker {

    public Info() {
        super();
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Show collection information";
    }

    @Override
    public String getRelevantObject() {
        checkArgument();
        checkAuth();
        return null;
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object object = (Object) pack.getObject();

        return (String) object;
    }
}
