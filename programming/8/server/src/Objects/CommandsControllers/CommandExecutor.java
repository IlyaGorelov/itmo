package Objects.CommandsControllers;

import Commons.CustomPackage;
import Commons.UserData.User;
import Objects.Connection.Receiver;
import Objects.Managers.AuthManager;
import Objects.Managers.CollectionManager;
import Objects.Managers.CommandManager;

import java.nio.channels.SocketChannel;
import java.util.NoSuchElementException;

public class CommandExecutor {
    private final CollectionManager collectionManager;
    private final AuthManager authManager;

    public static boolean waitForNextCommand = true;
    private CommandManager commandManager;

    public CommandExecutor(CollectionManager collectionManager, AuthManager authManager) {
        this.collectionManager = collectionManager;
        this.authManager = authManager;
    }

    public void execute(Receiver receiver, SocketChannel client, User user, boolean isCliMode) {
        commandManager = getCommandManager(receiver, client, user, isCliMode);

        try {
            while (true) {
                if (tryExecuteSingleCommand()) continue;

                CustomPackage pack = getRequest(receiver, client, isCliMode);

                if (!tryAddInBuffer(pack, isCliMode)) break;
            }
        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            System.out.println("User input is not detected");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private CommandManager getCommandManager(Receiver receiver,
                                             SocketChannel client,
                                             User user,
                                             boolean isCliMode) {
        return !isCliMode ? new CommandManager(collectionManager, authManager, receiver, client, user) :
                new CommandManager(collectionManager, receiver, true);
    }

    private boolean tryExecuteSingleCommand() {
        if (!CommandBuffer.isEmpty()) {
            commandManager.executeCommand(CommandBuffer.getCommand());
            return true;
        }
        return false;
    }

    private CustomPackage getRequest(Receiver receiver, SocketChannel client, boolean isCLIMode) {
        if (isCLIMode) {
            String command = receiver.getCLICommand();
            return new CustomPackage(command, null, null);
        } else {
            return receiver.getPackage(client);
        }
    }

    private boolean tryAddInBuffer(CustomPackage pack, boolean isCLIMode) {
        if (pack != null) {
            CommandBuffer.addInBuffer(pack);
            return true;
        } else {
            return false;
        }
    }

}
