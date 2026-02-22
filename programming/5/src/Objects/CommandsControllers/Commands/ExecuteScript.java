package Objects.CommandsControllers.Commands;

import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.CommandBuffer;
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
        try {
            File script = new File(getArgument());
            Scanner scanner = new Scanner(new FileReader(script));
            while (scanner.hasNextLine())
                CommandBuffer.buffer.add(scanner.nextLine());
            scanner.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("There is no such file");
        }
    }

    @Override
    public String getName() {
        return "execute_script";
    }

    @Override
    public String getDescription() {
        return "execute commands from file";
    }
}
