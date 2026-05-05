package Objects.CommandsControllers;

import Objects.Managers.CollectionManager;
import Objects.Managers.HistoryManager;

public abstract class RevertableCommand extends Command {
    public RevertableCommand(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArgument) {
        super(collectionManager, hasArgument, hasComplexArgument);
    }

    public RevertableCommand(CollectionManager collectionManager) {
        super(collectionManager);
    }

    public abstract void undo();

    public void addToHistory() {
        History.HistoryObject historyObject = new History.HistoryObject(this, getArgument(), getComplexArgument());
        HistoryManager.add(getCollectionManager().getCurrentUser(), historyObject);
    }
}
