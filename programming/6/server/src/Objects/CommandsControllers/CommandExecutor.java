package Objects.CommandsControllers;

import java.io.IOException;
import java.util.NoSuchElementException;

import Objects.Connection.Receiver;
import Objects.Managers.CollectionManager;
import Objects.Managers.CommandManager;

/** Class executes command from sysin or from buffer */
public class CommandExecutor {
    public static boolean waitForNextCommand = true;

    /** read command from sysin or buffer, then put command in commandManager */
    public void execute(CollectionManager collectionManager, Receiver receiver) {
        CommandManager commandManager = new CommandManager(collectionManager, receiver);

        while (waitForNextCommand) {
            try {
                if (CommandBuffer.buffer.size() > 0) {
                    commandManager.executeCommand(CommandBuffer.buffer.get(0));
                    continue;
                }
                String line = receiver.receive();
                if (line != null) {
                    if (line.isEmpty())
                        continue;
                    CommandBuffer.buffer.add(line);
                    System.out.println();
                }
                commandManager.executeCommand(CommandBuffer.buffer.get(0));
            } catch (IndexOutOfBoundsException | NoSuchElementException e) {
                System.out.println("User input is not detected");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        receiver.close();
    }
}
