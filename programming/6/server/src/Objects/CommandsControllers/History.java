package Objects.CommandsControllers;

import java.util.ArrayDeque;

import Objects.Managers.CommandManager;

public class History {
    private static ArrayDeque<String> commandsHistory = new ArrayDeque<>();
    private static ArrayDeque<String> antiCommandsHistory = new ArrayDeque<>();
    private static ArrayDeque<String> undoHistory = new ArrayDeque<>();
    private static ArrayDeque<String> antiUndoHistory = new ArrayDeque<>();

    public static void add(String command, String antiCommand) {
        if (commandsHistory.size() == 0 || !command.equals(undoHistory.peekLast())
                && !antiCommand.equals(antiUndoHistory.peekLast())) {
            {
                if (CommandManager.getCountOfUnrecorded() == 0) {
                    commandsHistory.add(command);
                    antiCommandsHistory.add(antiCommand);
                }
            }
        }
    }

    public static String[] getLastCommand() {
        try {
            undoHistory.add(antiCommandsHistory.getLast());
            antiUndoHistory.add(commandsHistory.getLast());

            commandsHistory.removeLast();
            String lastAntiCommand = antiCommandsHistory.getLast();
            antiCommandsHistory.removeLast();
            return lastAntiCommand.split("\n");
        } catch (Exception e) {
            String[] empty = {};
            return empty;
        }
    }

    public static String[] getLastUndo() {
        try {
            commandsHistory.add(antiUndoHistory.getLast());
            antiCommandsHistory.add(undoHistory.getLast());

            undoHistory.removeLast();
            String lastCommand = antiUndoHistory.getLast();
            antiUndoHistory.removeLast();
            return lastCommand.split("\n");
        } catch (Exception e) {
            String[] empty = {};
            return empty;
        }
    }

    public static void clearUndoHistory() {
        undoHistory.clear();
        antiUndoHistory.clear();
    }
}
