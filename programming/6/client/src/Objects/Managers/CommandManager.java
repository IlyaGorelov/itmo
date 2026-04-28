package Objects.Managers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.*;
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
     * Constructor that fill command map with available commands
     * 
     * @param receiver to get input
     */
    public CommandManager(Scanner reader, InputStream in, OutputStream out) {
        ArrayList<Command> commands = new ArrayList<>();
        IdManager.setIO(in, out);

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

    public CustomPackage getRelevantPackage(String commandName) {
        try {
            String[] commandWithArg = parseCommand(commandName);
            Object relevantObject = null;

            var command = commandMap.get(commandWithArg[0]);
            var realArgs = Arrays.stream(commandWithArg).skip(1).toArray(String[]::new);

            String simpleArg = Arrays.stream(realArgs).filter(x->!isComplexArg(x)).findFirst().orElse(null);

            command.setArgument(simpleArg);
            relevantObject = command.getRelevantObject();
            return new CustomPackage(command.getName(), command.getArgument(), relevantObject);
        } catch (NullPointerException e) {
            throw new NullPointerException("Unknown command");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        catch (Exception e) {
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

    private  boolean isComplexArg(String arg){
        return arg.startsWith("{") && arg.endsWith("}");
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
