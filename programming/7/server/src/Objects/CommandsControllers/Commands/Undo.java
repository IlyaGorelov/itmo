package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.History;
import Objects.CommandsControllers.RevertableCommand;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;

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

        if (History.isAtStart()) {
            answer += ("Nothing to undo");
        } else {
            History.moveBack();
            RevertableCommand command = History.getCommand();

            command.setArgument(History.getArg());
            command.setComplexArgument(History.getComplexArg());
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
