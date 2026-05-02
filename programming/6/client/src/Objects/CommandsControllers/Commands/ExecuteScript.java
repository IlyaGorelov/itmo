package Objects.CommandsControllers.Commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Managers.CommandManager;

/**
 * execute commands from the script file
 */
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

            System.out.println("Reading script: " + script.getPath());

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (isLineCorrect(line)) {
                    CustomPackage pkg = CommandManager.getRelevantPackage(line);

                    addPkgToPkgs(pkg,pkgs);
                }
            }
            return pkgs.toArray(new CustomPackage[1]);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found");
        }
    }

    private boolean isLineCorrect(String line) {

        if (line.startsWith(new ExecuteScript().getName())) {
            File script = new File(getArgument());

            String pathToSubScript = line.split(" ")[1];

            File subScript = new File(pathToSubScript);

            System.out.println("Recursive reference detected: " + line.trim() + "\nSkipping line\n");
            return !script.equals(subScript);
        }
        System.out.println("Adding new command: " + line.trim());
        return true;
    }

    private void addPkgToPkgs(CustomPackage pkg, ArrayList<CustomPackage> pkgs) {
        if (pkg.getCommand().equals(new ExecuteScript().getName())) {
            Object[] pkgsOfExecuteScript = (Object[]) pkg.getObject();

            for (Object pkgObject : pkgsOfExecuteScript) {
                pkgs.add((CustomPackage) pkgObject);
            }
        } else {
            pkgs.add(pkg);
        }
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object object = (Object) pack.getObject();
        return object.toString() + "\n";
    }

}
