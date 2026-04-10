package Objects.CommandsControllers.Commands;

import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.CommandBuffer;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;

/** execute commands from the script file */
public class ExecuteScript extends Command {

    public ExecuteScript(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public ExecuteScript(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() throws IllegalArgumentException {
        checkArgument();
        String answer = "";
        try {
            answer += ("Scanning script from: " + getArgument()) + "\n";
            File script = new File(getArgument());
            Scanner scanner = new Scanner(new FileReader(script));
            while (scanner.hasNextLine()) {
                String newCommand = scanner.nextLine();
                if (newCommand.isBlank())
                    continue;
                if (newCommand.contains(getName())) {
                    String path = newCommand.split(" ")[1];
                    if (new File(path).equals(script))
                        answer += ("Skip command " + newCommand + " because it refers to the same file") + "\n";
                    else {
                        answer += ("Add new command in queue: " + newCommand) + "\n";
                        CommandBuffer.buffer.add(newCommand);
                    }
                } else {
                    answer += ("Add new command in queue: " + newCommand) + "\n";
                    CommandBuffer.buffer.add(newCommand);
                }
            }
            scanner.close();
            answer += ("Executing") + "\n";

            CustomPackage pkg = new CustomPackage(this.getName(), getArgument(), answer);
            getReceiver().addToAnswer(getCLient(), pkg);

        } catch (Exception e) {
            CustomPackage pkg = new CustomPackage(this.getName(), getArgument(), e);
            getReceiver().addToAnswer(getCLient(), pkg);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "execute_script";
    }

    @Override
    public String getDescription() {
        return "execute commands from file line by line" +
                "\nWrite commands with pattern below. Type \"\" to set null value:" +
                "\n\tadd {String;int;double>-990;double>0;int;unit of measure;String;float>0;eye color;hair color;country;location x, loc y, loc z, loc name}"
                +
                "\n\tadd_if_min {same as for add}" +
                "\n\tadd_if_max {same as for add}" +
                "\n\tfilter_greater_than_owner {String;float>0;eye color;hair color;country;location x, loc y, loc z, loc name}"
                +
                "\n\tremove_greater {same as for add}" +
                "\n\tupdate id {same as for add}" +
                "\n\tother commands look the same as for user input";
    }

    @Override
    public void executeFromScript(String complexArg) {
        execute();
    }
}
