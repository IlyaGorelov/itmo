package Objects.Managers;

import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.CommandBuffer;
import Objects.CommandsControllers.Commands.*;
import Objects.Connection.Receiver;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * controls command
 */
public class CommandManager {
    /**
     * map of command kind of name - Command
     */
    private final HashMap<String, Command> commandMap = new HashMap<>();

    private final HashMap<String, Command> commandMapForCLI = new HashMap<>();

    private final Receiver receiver;
    private SocketChannel client;
    private boolean isCLIMode = false;

    /**
     * used for detecting then undo|redo cycle is over. While it>0 commands aren't
     * saved in the History
     */
    private static int countOfUnrecordingCommands = 0;

    /**
     * Constructor that fill command map with available commands
     *
     * @param collectionManager controls collection
     * @param receiver          to get input
     */
    public CommandManager(CollectionManager collectionManager, Receiver receiver, SocketChannel client) {
        this.receiver = receiver;
        this.client = client;
        ArrayList<Command> commands = new ArrayList<>();

        commands.add(new Add(collectionManager, false, true));
        commands.add(new AddIfMax(collectionManager, false, true));
        commands.add(new AddIfMin(collectionManager, false, true));
        commands.add(new Clear(collectionManager));
        commands.add(new Exit(collectionManager));
        commands.add(new ExecuteScript(collectionManager, true, false));
        commands.add(new GetById(collectionManager, true, false));
        commands.add(new GreaterThanOwner(collectionManager, false, true));
        commands.add(new Help(collectionManager));
        commands.add(new Info(collectionManager));
        commands.add(new MinByUnit(collectionManager));
        commands.add(new Show(collectionManager));
        commands.add(new Remove(collectionManager, true, false));
        commands.add(new RemoveGreater(collectionManager, false, true));
        commands.add(new RemoveByUnitOfMeasure(collectionManager, true, false));
        commands.add(new Undo(collectionManager));
        commands.add(new Redo(collectionManager));
        commands.add(new Update(collectionManager, true, true));

        for (Command command : commands) {
            commandMap.put(command.getName(), command);
            command.setReceiver(receiver);
            command.setClient(client);
        }
    }

    public CommandManager(
            CollectionManager collectionManager,
            Receiver receiver,
            boolean isCLIMode) {
        this.receiver = receiver;
        this.isCLIMode = isCLIMode;
        ArrayList<Command> commands = new ArrayList<>();

        commands.add(new Add(collectionManager, false, true));
        commands.add(new AddIfMax(collectionManager, false, true));
        commands.add(new AddIfMin(collectionManager, false, true));
        commands.add(new Clear(collectionManager));
        commands.add(new Exit(collectionManager));
        commands.add(new ExecuteScript(collectionManager, true, false));
        if (isCLIMode)
            commands.add(new GetById(collectionManager, true, false));
        commands.add(new GreaterThanOwner(collectionManager, false, true));
        commands.add(new Help(collectionManager));
        commands.add(new Info(collectionManager));
        commands.add(new MinByUnit(collectionManager));
        commands.add(new Show(collectionManager));
        commands.add(new Remove(collectionManager, true, false));
        commands.add(new RemoveGreater(collectionManager, false, true));
        commands.add(new RemoveByUnitOfMeasure(collectionManager, true, false));
        if (isCLIMode)
            commands.add(new Save(collectionManager));
        commands.add(new Undo(collectionManager));
        commands.add(new Redo(collectionManager));
        commands.add(new Update(collectionManager, true, true));

        for (Command command : commands) {
            commandMapForCLI.put(command.getName(), command);
            command.setReceiver(receiver);
            command.setClient(client);
            command.setCLIMode(isCLIMode);
        }
    }

    public HashMap<String, Command> getCommandMap() {
        return isCLIMode ? commandMapForCLI : commandMap;
    }

    /**
     * Executes commands and handles exceptions
     */
    public void executeCommand(String commandName) {
        var commandMap = getCommandMap();
        try {
            String[] commandWithArg = parseCommand(commandName);

            Command command = commandMap.get(commandWithArg[0]);
            var realArgs = Arrays.stream(commandWithArg).skip(1).toArray(String[]::new);

            String simpleArg = Arrays.stream(realArgs).filter(x -> !isComplexArg(x)).findFirst().orElse(null);
            String complexArg = Arrays.stream(realArgs).filter(x -> isComplexArg(x)).findFirst().orElse(null);

            command.setArgument(simpleArg);
            command.setComplexArgument(complexArg);
            command.executeInline();

        } catch (NullPointerException e) {
            System.out.println("Unknown command!");
            System.out.println("Skip command");
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

    private boolean isComplexArg(String arg) {
        return arg.startsWith("{") && arg.endsWith("}");
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
