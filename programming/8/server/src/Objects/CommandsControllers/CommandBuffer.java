package Objects.CommandsControllers;

import Commons.CustomPackage;

import java.util.LinkedList;

/**
 * Class with command buffer - list of upcoming commands
 */
public class CommandBuffer {
    private static final LinkedList<CustomPackage> commandBuffer = new LinkedList<>();

    public static void addInBuffer(CustomPackage pack) {
        commandBuffer.add(pack);
    }

    public static void removeLast() {
        commandBuffer.removeLast();
    }

    public static boolean isEmpty() {
        return commandBuffer.isEmpty();
    }

    public static CustomPackage getCommand() {
        return commandBuffer.getLast();
    }
}
