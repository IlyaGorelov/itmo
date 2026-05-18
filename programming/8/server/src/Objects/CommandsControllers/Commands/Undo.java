package Objects.CommandsControllers.Commands;

import Commons.CustomPackage;
import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.History;
import Objects.CommandsControllers.RevertableCommand;
import Objects.Managers.CollectionManager;
import Objects.Managers.HistoryManager;

/**
 * show information about collection
 */
public class Undo extends Command {

    public Undo(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();
        String answer = "";

        if (HistoryManager.isAtStart(getUser())) {
            answer += ("Nothing to undo");
        } else {
            HistoryManager.moveBack(getUser());

            History.HistoryObject historyObject = HistoryManager.getHistoryObject(getUser());

            RevertableCommand command = historyObject.command();

            command.setArgument(historyObject.simpleArg());
            command.setComplexArgument(historyObject.complexArg());
            command.undo();

        }
        CustomPackage pkg = new CustomPackage(this.getName(), null, answer);
        answer(pkg, answer);


    }

    @Override
    public String getName() {
        return "undo";
    }

    @Override
    public String getDescription() {
        return "undo prev command";
    }

}
