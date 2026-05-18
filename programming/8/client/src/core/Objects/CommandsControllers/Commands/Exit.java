package core.Objects.CommandsControllers.Commands;

import core.Objects.CommandsControllers.Command;
import Commons.CustomPackage;

/** stop program */
public class Exit extends Command {

    public Exit() {
        super();
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String getDescription() {
        return "exit the program";
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
