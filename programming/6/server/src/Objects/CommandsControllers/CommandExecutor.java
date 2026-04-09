package Objects.CommandsControllers;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.NoSuchElementException;

import Objects.Connection.CustomPackage;
import Objects.Connection.Receiver;
import Objects.Managers.CollectionManager;
import Objects.Managers.CommandManager;

/** Class executes command from sysin or from buffer */
public class CommandExecutor {
    private CollectionManager collectionManager;
    public static boolean waitForNextCommand = true;

    public CommandExecutor(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /** read command from sysin or buffer, then put command in commandManager */
    public void execute(Receiver receiver, SocketChannel client) {
        CommandManager commandManager = new CommandManager(collectionManager, receiver, client);
        waitForNextCommand = true;

        while (waitForNextCommand) {
            try {
                if (CommandBuffer.buffer.size() > 0) {
                    commandManager.executeCommand(CommandBuffer.buffer.get(0));
                    continue;
                }
                // // receiver.write(null);
                CustomPackage pack = receiver.getPackage(client);
                if (pack != null) {
                    CommandBuffer.buffer.add(pack.toString());
                    System.out.println();
                } else {
                    break;
                }

                // commandManager.executeCommand(CommandBuffer.buffer.get(0));
            } catch (IndexOutOfBoundsException | NoSuchElementException e) {
                System.out.println("User input is not detected");
                break;
            }

        }

        // try {
        // commandManager.executeCommand(new Exit(collectionManager).getName());
        // } catch (Exception e) {
        // System.out.println("ERROR ERROR ERROR");
        // }
        // receiver.closeClient(client);
    }

}
