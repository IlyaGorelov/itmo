package Objects.CommandsControllers;

import java.util.LinkedList;

public class History {
    private final LinkedList<HistoryObject> history = new LinkedList<>();

    private int currentStep = 0;

    public boolean isAtStart() {
        return currentStep == 0;
    }

    public boolean isAtEnd() {
        return currentStep == history.size();
    }

    public void add(HistoryObject historyObject) {
        if (!history.contains(historyObject)) {
            history.add(currentStep, historyObject);
            history.removeIf(p -> history.indexOf(p) > currentStep);

            currentStep++;
        }
    }

    public void moveBack() {
        if (--currentStep < 0) {
            currentStep = 0;
        }
    }

    public void moveForward() {
        if (currentStep + 1 != history.size() + 1) {
            currentStep++;
        }
    }

    public HistoryObject getHistoryObject() {
        return history.get(currentStep);
    }

    public record HistoryObject(RevertableCommand command, String simpleArg, Object complexArg) {
    }

}
