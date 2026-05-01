package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;

/** stop program */
public class Exit extends Command {

    public Exit(boolean hasArgument) {
        super(hasArgument, false);
    }

    public Exit() {
        super();
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String getRelevantObject() {
        checkArgument();
        return null;
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object object = (Object) pack.getObject();

        return object.toString() + "\n";
    }

}
