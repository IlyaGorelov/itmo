package Objects.CommandsControllers.Commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Managers.CommandManager;

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
    public Object getRelevantObject() {
        checkArgument();
        try {
            File script = new File(getArgument());
            Scanner scanner = new Scanner(script);
            ArrayList<CustomPackage> pkgs = new ArrayList<>();

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                pkgs.add(CommandManager.getRelevantPackage(line));
            }
            return pkgs.toArray(new CustomPackage[1]);
        }catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found");
        }
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object object = (Object) pack.getObject();
        return object.toString() + "\n";
    }

}
