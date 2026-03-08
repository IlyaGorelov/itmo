package Objects.CommandsControllers;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

import Objects.Managers.CollectionManager;
import Objects.Managers.CommandManager;

/** Class executes command from sysin or from buffer */
public class CommandExecutor {
    public static boolean waitForNextCommand = true;

    /** read command from sysin or buffer, then put command in commandManager */
    public void execute(CollectionManager collectionManager) {
        InputStream inputStream = System.in;
        Scanner scanner = new Scanner(inputStream);
        CommandManager commandManager = new CommandManager(collectionManager, scanner);

        while (waitForNextCommand) {
            try {
                if (CommandBuffer.buffer.size() > 0) {
                    commandManager.executeCommand(CommandBuffer.buffer.get(0));
                    continue;
                }
                if (scanner.hasNext()) {
                    String line = scanner.nextLine().trim();
                    if (line.isEmpty())
                        continue;
                    CommandBuffer.buffer.add(line);
                    System.out.println();
                }
                commandManager.executeCommand(CommandBuffer.buffer.get(0));
            } catch (IndexOutOfBoundsException | NoSuchElementException e) {
                System.out.println("User input is not detected");
                break;
            }
        }

        scanner.close();
    }
}
