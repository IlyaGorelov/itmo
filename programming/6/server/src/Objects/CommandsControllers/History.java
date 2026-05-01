package Objects.CommandsControllers;

import java.util.ArrayList;

public class History {
    private static final ArrayList<RevertableCommand> commandsHistory = new ArrayList<>();
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
        commandsHistory.add(currentStep, command);
        argumentsHistory.add(currentStep, arg);
        complexArgHistory.add(currentStep, complexArg);
        currentStep++;
    }

    public static void moveBack() {
        if (--currentStep < 0) {
            currentStep = 0;
        }
    }

    public static void moveForward() {
        if (++currentStep == commandsHistory.size()) {
            currentStep--;
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
