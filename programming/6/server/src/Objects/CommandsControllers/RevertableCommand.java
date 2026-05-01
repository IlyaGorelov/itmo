package Objects.CommandsControllers;

import Objects.Managers.CollectionManager;

public abstract class RevertableCommand extends Command {
    public RevertableCommand(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArgument) {
        super(collectionManager, hasArgument, hasComplexArgument);
    }

    public RevertableCommand(CollectionManager collectionManager) {
        super(collectionManager);
    }

    public abstract void undo();
}
