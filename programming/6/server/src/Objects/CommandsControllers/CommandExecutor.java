package Objects.CommandsControllers;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.NoSuchElementException;

import Objects.CommandsControllers.Commands.Exit;
import Objects.CommandsControllers.Commands.Save;
import Objects.Connection.CustomPackage;
import Objects.Connection.Receiver;
import Objects.Managers.CollectionManager;
import Objects.Managers.CommandManager;

/** Class executes command from sysin or from buffer */
public class CommandExecutor {
    private CollectionManager collectionManager;
    public static boolean waitForNextCommand = true;
    private CommandManager commandManager;

    public CommandExecutor(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /** read command from sysin or buffer, then put command in commandManager */
    public void execute(Receiver receiver, SocketChannel client) {
        commandManager = new CommandManager(collectionManager, receiver, client);
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
            } catch (Exception e) {
                System.out.println(e);
                break;
            }

        }

        // try {
        // commandManager
        // .executeCommand(new
        // Exit(collectionManager).setClient(client).setReceiver(receiver).getName());
        // } catch (Exception e) {
        // System.out.println("ERROR ERROR ERROR");
        // }

    }

    public void stop() {
        try {
            new Save(collectionManager).execute();
        } catch (Exception e) {
            // System.out.println();
        }
    }

}
