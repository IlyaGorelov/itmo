package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.CommandBuffer;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;

import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

/**
 * execute commands from the script file
 */
public class ExecuteScript extends Command {

    public ExecuteScript(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArg) {
        super(collectionManager, hasArgument, hasComplexArg);
    }

    public ExecuteScript(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() throws IllegalArgumentException {
        checkArgument();
        return;
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
    public void checkArgument() {
        boolean actuallyHasArgument = getArgument() != null;
        boolean actuallyHasComplexArgument = getComplexArgument() != null;
        if (actuallyHasArgument != getHasArgument() || actuallyHasComplexArgument != getHasComplexArgument())
            throw new IllegalArgumentException(String.format("Invalid format, use:\n\t%s path_to_file", getName()));
    }
}
