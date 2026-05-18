package Objects.Managers;

import Commons.CustomPackage;
import Commons.UserData.User;
import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.CommandBuffer;
import Objects.CommandsControllers.Commands.*;
import Objects.Connection.Receiver;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * controls command
 */
public class CommandManager {
    /**
     * map of command kind of name - Command
     */
    private final HashMap<String, Command> commandMap = new HashMap<>();

    private final HashMap<String, Command> commandMapForCLI = new HashMap<>();

    private SocketChannel client;
    private boolean isCLIMode = false;

    private CollectionManager collectionManager;

    /**
     * Constructor that fills command map with available commands
     *
     * @param collectionManager controls collection
     * @param receiver          to get input
     */
    public CommandManager(CollectionManager collectionManager,
                          AuthManager authManager,
                          Receiver receiver,
                          SocketChannel client,
                          User user
    ) {
        this.client = client;
        this.collectionManager = collectionManager;

        ArrayList<Command> commands = new ArrayList<>();

        addAllAuthCommandsInMap(authManager, commands);
        addAllCollectionCommandsInMap(commands);

        for (Command command : commands) {
            commandMap.put(command.getName(), command);
            command.setReceiver(receiver)
                    .setClient(client)
                    .setUser(user);
        }
    }

    public CommandManager(
            CollectionManager collectionManager,
            Receiver receiver,
            boolean isCLIMode) {
        this.isCLIMode = isCLIMode;
        ArrayList<Command> commands = new ArrayList<>();

        if (!this.isCLIMode)
            addAllCollectionCommandsInMap(commands);

        for (Command command : commands) {
            if (this.isCLIMode) {
                commandMapForCLI.put(command.getName(), command);
            } else {
                commandMap.put(command.getName(), command);
            }

            command.setReceiver(receiver);
            command.setClient(client);
            command.setCLIMode(this.isCLIMode);
        }
    }

    private void addAllAuthCommandsInMap(AuthManager authManager, ArrayList<Command> commands) {
        commands.add(new Register(authManager, false, true));
        commands.add(new Login(authManager, false, true));
    }

    private void addAllCollectionCommandsInMap(ArrayList<Command> commands) {
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
    }

    public HashMap<String, Command> getCommandMap() {
        return isCLIMode ? commandMapForCLI : commandMap;
    }

    /**
     * Executes commands and handles exceptions
     */
    public void executeCommand(
            CustomPackage pack
    ) {
        var commandMap = getCommandMap();
        try {
            Command command = commandMap.get(pack.getCommand());

            command.setArgument((String) pack.getArgument());
            command.setComplexArgument(pack.getObject());
            command.execute();

        } catch (NullPointerException e) {
            System.out.println("Unknown command!");
            System.out.println("Skip command");
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println(e.getMessage() + "\n" + "Skip command");
            else
                System.out.println("Skip command");
        } finally {
            CommandBuffer.removeLast();
        }
    }
}
