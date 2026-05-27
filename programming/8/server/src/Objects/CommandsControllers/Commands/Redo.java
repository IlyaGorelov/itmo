package Objects.CommandsControllers.Commands;

import Commons.CustomPackage;
import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.History;
import Objects.CommandsControllers.RevertableCommand;
import Objects.Managers.CollectionManager;
import Objects.Managers.HistoryManager;

public class Redo extends Command {

    public static boolean redoFlag = false;

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
            redoFlag = true;
            History.HistoryObject historyObject = HistoryManager.getHistoryObject(getUser());

            RevertableCommand command = historyObject.command();

            command.setArgument(historyObject.simpleArg());
            command.setComplexArgument(historyObject.complexArg());
            command.execute();

            HistoryManager.moveForward(getUser());
            redoFlag = false;
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
