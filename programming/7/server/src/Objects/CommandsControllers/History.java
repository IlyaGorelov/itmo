package Objects.CommandsControllers;

import java.util.ArrayList;
import java.util.LinkedList;

public class History {
    private static final LinkedList<RevertableCommand> commandsHistory = new LinkedList<>();
    private static final ArrayList<String> argumentsHistory = new ArrayList<>();
    private static final ArrayList<Object> complexArgHistory = new ArrayList<>();

    private static int currentStep = 0;

    public static boolean isAtStart() {
        return currentStep == 0;
    }

    public static boolean isAtEnd() {
        return currentStep == commandsHistory.size();
    }

    public static void add(RevertableCommand command, String arg, Object complexArg) {
        if (!commandsHistory.contains(command)) {
            commandsHistory.add(currentStep, command);
            commandsHistory.removeIf(p -> commandsHistory.indexOf(p) > currentStep);

            argumentsHistory.add(currentStep, arg);
            argumentsHistory.removeIf(a -> argumentsHistory.indexOf(a) > currentStep);

            complexArgHistory.add(currentStep, complexArg);
            complexArgHistory.removeIf(p -> complexArgHistory.indexOf(p) > currentStep);

            currentStep++;
        }
    }

    public static void moveBack() {
        if (--currentStep < 0) {
            currentStep = 0;
        }
    }

    public static void moveForward() {
        if (currentStep + 1 != commandsHistory.size()) {
            currentStep++;
        }
    }

    public static RevertableCommand getCommand() {
        return commandsHistory.get(currentStep);
    }

    public static String getArg() {
        return argumentsHistory.get(currentStep);
    }

    public static Object getComplexArg() {
        return complexArgHistory.get(currentStep);
    }
}
