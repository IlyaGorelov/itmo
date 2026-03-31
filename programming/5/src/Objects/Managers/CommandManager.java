package Objects.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.CommandBuffer;
import Objects.CommandsControllers.Commands.*;

/** controls command */
public class CommandManager {
    /** map of command kind of name - Command */
    private HashMap<String, Command> commandMap = new HashMap<>();

    private Scanner scanner;

    /**
     * used for detecting then undo|redo cycle is over. While it>0 commands aren't
     * saved in the History
     */
    private static int countOfUnrecordingCommands = 0;

    /**
     * Constructor that fill command map with available commands
     * 
     * @param collectionManager controls collection
     * @param scanner           to get input
     */
    public CommandManager(CollectionManager collectionManager, Scanner scanner) {
        this.scanner = scanner;
        ArrayList<Command> commands = new ArrayList<>();

        commands.add(new Help(collectionManager));
        commands.add(new Info(collectionManager));
        commands.add(new Show(collectionManager));
        commands.add(new Add(collectionManager));
        commands.add(new Update(collectionManager, true));
        commands.add(new Remove(collectionManager, true));
        commands.add(new Clear(collectionManager));
        commands.add(new Save(collectionManager));
        commands.add(new ExecuteScript(collectionManager, true));
        commands.add(new Close(collectionManager));
        commands.add(new AddIfMax(collectionManager));
        commands.add(new AddIfMin(collectionManager));
        commands.add(new RemoveGreater(collectionManager));
        commands.add(new RemoveByUnitOfMeasure(collectionManager, true));
        commands.add(new MinByUnit(collectionManager));
        commands.add(new GreaterThanOwner(collectionManager));
        commands.add(new Undo(collectionManager));
        commands.add(new Redo(collectionManager));

        for (Command command : commands) {
            commandMap.put(command.getName(), command);
        }
    }

    public HashMap<String, Command> getCommandMap() {
        return commandMap;
    }

    /** Executes commands and handles exceptions */
    public void executeCommand(String commandName) {
        try {
            String[] commandWithArg = parseCommand(commandName);
            if (commandMap.containsKey(commandWithArg[0])) {
                var command = commandMap.get(commandWithArg[0]);
                switch (commandWithArg.length) {
                    case 3:
                        if (commandWithArg[2].startsWith("{")) {
                            command.setScanner(scanner);
                            command.setArgument(commandWithArg[1]);
                            command.executeFromScript(commandWithArg[2]);
                            return;
                        } else
                            throw new IllegalArgumentException("There is no such command: " + commandName);
                    case 2:
                        if (commandWithArg[1].startsWith("{")) {
                            command.setScanner(scanner);
                            command.executeFromScript(commandWithArg[1]);
                            return;
                        } else {
                            command.setScanner(scanner);
                            command.setArgument(commandWithArg[1]);
                            command.execute();
                        }
                        break;
                    case 1:
                        command.setScanner(scanner);
                        command.execute();
                }
            } else
                throw new IllegalArgumentException("There is no such command: " + commandName);
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println(e.getMessage() + "\n" + "Skip command");
            else
                System.out.println("Skip command");
        } finally {
            CommandBuffer.buffer.remove(commandName);
        }
    }

    public static String[] parseCommand(String input) {
        List<String> parts = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\{[^}]*}|\\S+");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String match = matcher.group();
            parts.add(match);
        }

        return parts.toArray(new String[0]);
    }

    public static void addUnrecordedCommands(int i) {
        countOfUnrecordingCommands += i;
    }

    public static void minusUnrecordedCommand() {
        if (countOfUnrecordingCommands > 0)
            countOfUnrecordingCommands--;
    }

    public static int getCountOfUnrecorded() {
        return countOfUnrecordingCommands;
    }
}
