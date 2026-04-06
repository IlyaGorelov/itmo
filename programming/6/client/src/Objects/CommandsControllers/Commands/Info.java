package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;

/** show information about collection */
public class Info extends Command {

    public Info(boolean hasArgument) {
        super(hasArgument, false);
    }

    public Info() {
        super();
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getRelevantObject() {
        checkArgument();
        return null;

    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object object = (Object) pack.getObject();

        return (String) object;
    }
}
