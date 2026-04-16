package Objects.CommandsControllers.Commands;

import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.CommandBuffer;
import Objects.Connection.CustomPackage;

/** execute commands from the script file */
public class ExecuteScript extends Command {

    public ExecuteScript(boolean hasArgument) {
        super(hasArgument, false);
    }

    public ExecuteScript() {
        super();
    }

    @Override
    public String getName() {
        return "execute_script";
    }

    @Override
    public String getRelevantObject() {
        checkArgument();
        File script = new File(getArgument());
        setArgument(script.getAbsolutePath());
        return null;
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object object = (Object) pack.getObject();

        return object.toString() + "\n";
    }

}
