package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;

/** shows all available commands with description */
public class Help extends Command {
    public Help() {
        super();
    }

    @Override
    public String getRelevantObject() {
        checkArgument();
        return null;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object object = (Object) pack.getObject();

        return (String) object;
    }

}
