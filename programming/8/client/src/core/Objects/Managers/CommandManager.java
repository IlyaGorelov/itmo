package core.Objects.Managers;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import core.Objects.CommandsControllers.Command;
import core.Objects.CommandsControllers.Commands.*;
import core.Objects.Connection.Client;
import Commons.CustomPackage;
import gui.Objects.Elements.Commons.ResultDialog;

/**
 * controls command
 */
public class CommandManager {
    /**
     * map of command kind of name - Command
     */
    private static final HashMap<String, Command> commandMap = new HashMap<>();
    private static final HashMap<String, Command> hiddenCommandMap = new HashMap<>();

    private static Scanner reader;

    private static Client.Mode mode = Client.Mode.CLI;

    /**
     * Constructor that fill command map with available commands
     *
     * @param reader to get input
     */
    public CommandManager(Scanner reader) {
        CommandManager.reader = reader;

        putCommandForUnAuthenticatedUsers();
        hiddenCommandMap.put(new GetById().getName(),new GetById());
    }

    public static void setMode(Client.Mode mode){
        CommandManager.mode = mode;
    }

    public static void putCommandForUnAuthenticatedUsers(){
        commandMap.clear();
        ArrayList<Command> commands = new ArrayList<>();

        commands.add(new Register());
        commands.add(new Login());
        commands.add(new Help());
        commands.add(new Exit());

        for (Command command : commands) {
            commandMap.put(command.getName(), command);
            command.setScanner(reader);
        }
    }

    public static void putCommandsForAuthenticatedUsers() {
        commandMap.clear();

        ArrayList<Command> commands = new ArrayList<>();
        commands.add(new Info());
        commands.add(new Show());
        commands.add(new Logout());
        commands.add(new Add(false));
        commands.add(new AddIfMax(false));
        commands.add(new AddIfMin(false));
        commands.add(new Update(true));
        commands.add(new Remove(true));
        commands.add(new Clear());
        commands.add(new Help());
        commands.add(new Exit());
        commands.add(new ExecuteScript(true));
        commands.add(new RemoveGreater(false));
        commands.add(new RemoveByUnitOfMeasure(true));
        commands.add(new MinByUnit());
        commands.add(new GreaterThanOwner(false));
        commands.add(new Undo());
        commands.add(new Redo());

        for (Command command : commands) {
            commandMap.put(command.getName(), command);
            command.setScanner(reader);
        }

        commandMap.remove(new Register().getName());
        commandMap.remove(new Login().getName());
    }

    public static HashMap<String, Command> getCommandMap() {
        return commandMap;
    }

    public static CustomPackage getRelevantPackage(CustomPackage commandPack) {
        try {
            String[] commandWithArg = parseCommand(commandPack.getCommand());
            Object relevantObject = null;

            var command = commandMap.get(commandWithArg[0]);
            var realArgs = Arrays.stream(commandWithArg).skip(1).toArray(String[]::new);

            String simpleArg = Arrays.stream(realArgs).filter(x -> !isComplexArg(x)).findFirst().orElse(null);
            String complexArg = Arrays.stream(realArgs).filter(CommandManager::isComplexArg).findFirst().orElse(null);

            command.setArgument(simpleArg);

            command.setComplexArgument(complexArg);

            relevantObject = command.getRelevantObject();
            return new CustomPackage(command.getName(), command.getArgument(), relevantObject, AuthManager.getInstance().getUser());
        } catch (NullPointerException e) {
            throw new NullPointerException("Unknown command");
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

    private static boolean isComplexArg(String arg) {
        return arg.startsWith("{") && arg.endsWith("}");
    }

    public String getRelevantAnswer(Object[] answer) {
        try {
            StringBuilder relevant = new StringBuilder();

            for (Object single : answer) {
                CustomPackage pack = (CustomPackage) single;

                var command = commandMap.get(pack.getCommand());
                if(command==null) command=hiddenCommandMap.get(pack.getCommand());

                if (command == null) {
                    relevant.append(pack.getObject()).append("\n");
                    continue;
                }
                relevant.append(command.getRelevantAnswer(pack));
            }

            if(relevant.toString().isBlank())
                return "";

            if(ExecuteScript.isProcessing)
                return relevant.toString();

            return ResultDialog.showInfo(relevant.toString());
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong while parsing command!");
        }
    }
}
