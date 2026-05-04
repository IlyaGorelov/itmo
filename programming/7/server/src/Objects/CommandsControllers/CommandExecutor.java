package Objects.CommandsControllers;

import Objects.CommandsControllers.Commands.Save;
import Objects.Connection.CustomPackage;
import Objects.Connection.Receiver;
import Objects.Managers.AuthManager;
import Objects.Managers.CollectionManager;
import Objects.Managers.CommandManager;

import java.nio.channels.SocketChannel;
import java.util.NoSuchElementException;

/**
 * Class executes command from sysin or from buffer
 */
public class CommandExecutor {
    private final CollectionManager collectionManager;
    private final AuthManager authManager;

    public static boolean waitForNextCommand = true;
    public static boolean waitForNextCommandForCLI = false;
    private CommandManager commandManager;

    public CommandExecutor(CollectionManager collectionManager, AuthManager authManager) {
        this.collectionManager = collectionManager;
        this.authManager = authManager;
    }

    /**
     * read command from sysin or buffer, then put command in commandManager
     */
    public void execute(Receiver receiver, SocketChannel client) {
        commandManager = new CommandManager(collectionManager, authManager, receiver, client);
        waitForNextCommand = true;

        try {
            while (waitForNextCommand) {

                if (!CommandBuffer.isEmpty()) {
                    commandManager.executeCommand(
                            CommandBuffer.getCommand(),
                            CommandBuffer.getArg(),
                            CommandBuffer.getComplexArg()
                    );
                    continue;
                }
                CustomPackage pack = receiver.getPackage(client);
                if (pack != null) {
                    CommandBuffer.addInBuffer(pack.getCommand(), pack.getArgument() != null ? pack.getArgument().toString() : null, pack.getObject());
                    System.out.println();
                } else {
                    break;
                }
            }
        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            System.out.println("User input is not detected");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void executeFromCLI(Receiver receiver) {
        commandManager = new CommandManager(collectionManager, receiver, true);
        waitForNextCommandForCLI = true;

        try {
            while (waitForNextCommandForCLI) {
                if (!CommandBuffer.isEmpty()) {
                    commandManager.executeCommand(
                            CommandBuffer.getCommand(),
                            CommandBuffer.getArg(),
                            CommandBuffer.getComplexArg()
                    );
                    continue;
                }

                String command = receiver.getCLICommand();
                if (command != null) {
                    CommandBuffer.addInBuffer(command, null, null);
                    System.out.println();
                } else {
                    break;
                }


            }
        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            System.out.println("User input is not detected");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void stop() {
        try {
            new Save(collectionManager).execute();
        } catch (Exception e) {
            // System.out.println();
        }
    }

}
