package Objects.CommandsControllers;

import Objects.CommandsControllers.Commands.Redo;

import java.util.LinkedList;
import java.util.Objects;

public class History {
    private final LinkedList<HistoryObject> history = new LinkedList<>();

    private int currentStep = 0;

    public synchronized boolean isAtStart() {
        return currentStep == 0;
    }

    public synchronized boolean isAtEnd() {
        return currentStep == history.size();
    }

    public synchronized void add(HistoryObject historyObject) {
        if (!Redo.redoFlag) {
            history.add(currentStep, historyObject);
            history.removeIf(p -> history.indexOf(p) > currentStep);

            currentStep++;
        }
    }

    public synchronized void moveBack() {
        if (--currentStep < 0) {
            currentStep = 0;
        }
    }

    public synchronized void moveForward() {
        if (currentStep + 1 != history.size() + 1) {
            currentStep++;
        }
    }

    public synchronized HistoryObject getHistoryObject() {
        return history.get(currentStep);
    }

    public record HistoryObject(RevertableCommand command, String simpleArg, Object complexArg) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            HistoryObject hObject = (HistoryObject) o;

            return command.getName().equals(hObject.command().getName()) && Objects.equals(simpleArg, hObject.simpleArg()) && Objects.equals(complexArg, hObject.complexArg());
        }

        @Override
        public int hashCode() {
            return Objects.hash(command.getName(), simpleArg, Objects.hash(complexArg));
        }
    }

}
