package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.History;
import Objects.CommandsControllers.RevertableCommand;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;
import Objects.Managers.HistoryManager;

/**
 * show information about collection
 */
public class Redo extends Command {

    public Redo(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();


        if (HistoryManager.isAtEnd(getUser())) {
            CustomPackage pkg = new CustomPackage(this.getName(), null, "Nothing to redo");
            answer(pkg, "Nothing to redo");

        } else {
            History.HistoryObject historyObject = HistoryManager.getHistoryObject(getUser());


            RevertableCommand command = historyObject.command();

            command.setArgument(historyObject.simpleArg());
            command.setComplexArgument(historyObject.complexArg());
            command.execute();

            HistoryManager.moveForward(getUser());
        }

    }

    @Override
    public String getName() {
        return "redo";
    }

    @Override
    public String getDescription() {
        return "undo(undo)";
    }

}
