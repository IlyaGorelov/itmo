package Objects.Managers;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.Commands.*;
import Objects.Connection.CustomPackage;

/** controls command */
public class CommandManager {
    /** map of command kind of name - Command */
    private HashMap<String, Command> commandMap = new HashMap<>();

    /**
     * used for detecting then undo|redo cycle is over. While it>0 commands aren't
     * saved in the History
     */
    private static int countOfUnrecordingCommands = 0;

    /**
     * Constructor that fill command map with available commands
     * 
     * @param receiver to get input
     */
    public CommandManager(Scanner reader, ObjectInputStream in, ObjectOutputStream writer) {
        ArrayList<Command> commands = new ArrayList<>();
        IdManager.setIO(in, writer);

        commands.add(new Help());
        commands.add(new Info());
        commands.add(new Show());
        commands.add(new Add(false, true));
        commands.add(new AddIfMax(false, true));
        commands.add(new AddIfMin(false, true));
        commands.add(new Update(true, true));
        commands.add(new Remove(true));
        commands.add(new Clear());
        commands.add(new ExecuteScript(true));
        commands.add(new Exit());
        commands.add(new RemoveGreater(false, true));
        commands.add(new RemoveByUnitOfMeasure(true));
        commands.add(new MinByUnit());
        commands.add(new GreaterThanOwner(false, true));
        commands.add(new Undo());
        commands.add(new Redo());

        for (Command command : commands) {
            commandMap.put(command.getName(), command);
            command.setScanner(reader);
        }
    }

    public HashMap<String, Command> getCommandMap() {
        return commandMap;
    }

    /** Executes commands and handles exceptions */
    public CustomPackage getRelevantPackage(String commandName) {
        try {
            String[] commandWithAStrings = parseCommand(commandName);
            Object relevantObject = null;
            Command command = null;
            String argument = null;
            switch (commandWithAStrings.length) {
                case 1:
                    if (commandMap.containsKey(commandWithAStrings[0])) {
                        command = commandMap.get(commandWithAStrings[0]);
                        if (command.getHasComplexArgument()) {
                            relevantObject = command.getRelevantObject();
                        }
                        if (command.getHasArgument())
                            throw new IllegalArgumentException("Invalid number of arguments!");

                    } else
                        throw new IllegalArgumentException("There is no such command!");
                    break;
                case 2:
                    command = commandMap.get(commandWithAStrings[0]);
                    if (command == null)
                        throw new IllegalArgumentException("There is no such command!");
                    else {
                        command.setArgument(commandWithAStrings[1]);
                        argument = commandWithAStrings[1];
                    }
                    relevantObject = command.getRelevantObject();
                    break;
                case 3:
                    if (commandMap.containsKey(commandWithAStrings[0])) {
                        command = commandMap.get(commandWithAStrings[0]);
                        if (command.getHasComplexArgument()) {
                            relevantObject = command.getRelevantObject();
                            argument = commandWithAStrings[1];
                        }

                    } else
                        throw new IllegalArgumentException("There is no such command!");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid number of arguments!");
            }
            return new CustomPackage(command, argument, relevantObject);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong while parsing command!");
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

    public String getRelevantAnswer(Object[] answer) {
        try {
            String relevant = "";
            for (Object single : answer) {
                CustomPackage pack = (CustomPackage) single;
                var command = commandMap.get(pack.getCommand());
                if (command == null) {
                    relevant += (String) pack.getObject() + "\n";
                    continue;
                }
                relevant += command.getRelevantAnswer(pack);
            }
            return relevant;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong while parsing command!");
        }
    }
}
