package Objects.CommandsControllers;

import java.util.LinkedList;

/**
 * Class with command buffer - list of upcoming commands
 */
public class CommandBuffer {
    private static final LinkedList<String> commandBuffer = new LinkedList<>();
    private static final LinkedList<String> argBuffer = new LinkedList<>();
    private static final LinkedList<Object> complexArgBuffer = new LinkedList<>();

    public static void addInBuffer(String commandName, String arg, Object complexArg) {
        commandBuffer.add(commandName);
        argBuffer.add(arg);
        complexArgBuffer.add(complexArg);
    }

    public static void removeLast() {
        commandBuffer.removeLast();
        argBuffer.removeLast();
        complexArgBuffer.removeLast();
    }

    public static boolean isEmpty() {
        return commandBuffer.isEmpty();
    }

    public static String getCommand() {
        return commandBuffer.getLast();
    }

    public static String getArg() {
        return argBuffer.getLast();
    }

    public static Object getComplexArg() {
        return complexArgBuffer.getLast();
    }
}
