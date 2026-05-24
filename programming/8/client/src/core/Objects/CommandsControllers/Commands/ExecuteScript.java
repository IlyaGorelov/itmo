package core.Objects.CommandsControllers.Commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import Localization.I18n;
import core.Objects.CommandsControllers.AuthChecker;
import core.Objects.CommandsControllers.Command;
import Commons.CustomPackage;
import core.Objects.Managers.CommandManager;
import gui.Objects.Elements.Commons.ResultDialog;

/**
 * execute commands from the script file
 */
public class ExecuteScript extends Command implements AuthChecker {

    String infoText;

    public static boolean isProcessing = false;

    public ExecuteScript(boolean hasArgument) {
        super(hasArgument);
    }

    public ExecuteScript() {
        super();
    }

    @Override
    public String getName() {
        return "execute_script";
    }

    @Override
    public String getDescription() {
        return """
                execute commands from file line by line
                Write commands with pattern below. Type "" to set null value:
                \tadd {Name(String);X(int);Y(double>-990);Price(double>0 | null);Man Cost(int);unit of measure | null;Owner name(String) | null;Height(float>0);eye color | null;hair color;country;location x|null;loc y;loc z;loc name}
                \tadd_if_min {same as for add}
                \tadd_if_max {same as for add}
                \tfilter_greater_than_owner {name(String);height(float>0)}
                \tremove_greater {Price(double>0 | null);Man Cost(int)}
                \tupdate id {same as for add}
                \tother commands look the same as for user input""";
    }

    @Override
    public Object getRelevantObject() {
        checkArgument();
        checkAuth();
        infoText="";
        try {
            File script = new File(getArgument());
            Scanner scanner = new Scanner(script);
            ArrayList<CustomPackage> pkgs = new ArrayList<>();

            infoText+= I18n.get("info.execute1")+" " + script.getPath()+"\n\n";
            System.out.println("Reading script: " + script.getPath());

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (isLineCorrect(line)) {
                    try {
                        CustomPackage pkg = CommandManager.getRelevantPackage(new CustomPackage(line, null, null));

                        addPkgToPkgs(pkg, pkgs);
                    }catch (Exception e){
                        infoText+=e.getMessage()+'\n';
                    }
                }
            }

            ResultDialog.showInfo(infoText);
            return pkgs.toArray(new CustomPackage[1]);
        } catch (FileNotFoundException e) {
            ResultDialog.showError(I18n.get("error.execute"));
            throw new IllegalArgumentException("File not found");
        }
    }

    private boolean isLineCorrect(String line) {
        if (line.startsWith(new ExecuteScript().getName())) {
            File script = new File(getArgument());

            String pathToSubScript = line.split(" ")[1];

            File subScript = new File(pathToSubScript);

            infoText+=I18n.get("info.execute2")+" " + line.trim() + "\nSkipping line\n";
            System.out.println("Recursive reference detected: " + line.trim() + "\nSkipping line\n");
            return !script.equals(subScript);
        }
        infoText+=I18n.get("info.execute3")+" " + line.trim()+"\n\n";
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
