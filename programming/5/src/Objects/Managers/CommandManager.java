package Objects.Managers;

import java.util.HashMap;
import java.util.Scanner;

import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.CommandBuffer;
import Objects.CommandsControllers.Commands.*;

/** controls command */
public class CommandManager {
    /** map of command kind of name - Command */
    private HashMap<String, Command> commandMap = new HashMap<>();
    private Scanner scanner;

    /**
     * Constructor that fill command map with available commands
     * 
     * @param collectionManager controls collection
     * @param scanner           to get input
     */
    public CommandManager(CollectionManager collectionManager, Scanner scanner) {
        this.scanner = scanner;
        commandMap.put("help", new Help(collectionManager));
        commandMap.put("info", new Info(collectionManager));
        commandMap.put("show", new Show(collectionManager));
        commandMap.put("add", new Add(collectionManager));
        commandMap.put("update", new Update(collectionManager, true));
        commandMap.put("remove_by_id", new Remove(collectionManager, true));
        commandMap.put("clear", new Clear(collectionManager));
        commandMap.put("save", new Save(collectionManager));
        commandMap.put("execute_script", new ExecuteScript(collectionManager, true));
        commandMap.put("exit", new Exit(collectionManager));
        commandMap.put("add_if_max", new AddIfMax(collectionManager));
        commandMap.put("add_if_min", new AddIfMin(collectionManager));
        commandMap.put("remove_greater", new RemoveGreater(collectionManager));
        commandMap.put("remove_all_by_unit_of_measure", new RemoveByUnitOfMeasure(collectionManager, true));
        commandMap.put("min_by_unit_of_measure", new MinByUnit(collectionManager));
        commandMap.put("filter_greater_than_owner", new GreaterThanOwner(collectionManager, true));
    }

    public HashMap<String, Command> getCommandMap() {
        return commandMap;
    }

    /** Executes commands and handles exceptions */
    public void executeCommand(String commandName) {
        try {
            String[] commandWithArg = commandName.split(" ");
            if (commandMap.containsKey(commandWithArg[0])) {
                var command = commandMap.get(commandWithArg[0]);
                if (commandWithArg.length > 1)
                    command.setArgument(commandWithArg[1]);
                command.setScanner(scanner);
                command.execute();
            } else
                throw new IllegalArgumentException("There is no such command: " + commandName);
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n" + "Skip command");
        } finally {
            CommandBuffer.buffer.remove(commandName);
        }

    }

}
