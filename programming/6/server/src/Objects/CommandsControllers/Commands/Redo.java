package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.History;
import Objects.CommandsControllers.RevertableCommand;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;

/**
 * show information about collection
 */
public class Redo extends Command {

    public Redo(CollectionManager collectionManager, boolean hasArgument, boolean hasCOmplexArg) {
        super(collectionManager, hasArgument, hasCOmplexArg);
    }

    public Redo(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();

        if (History.isAtEnd()) {
            // System.out.println("Nothing to redo");
            CustomPackage pkg = new CustomPackage(this.getName(), null, "Nothing to redo");
            answer(pkg, "Nothing to redo");

        } else {
            History.moveForward();
            RevertableCommand command = History.getCommand();

            command.setArgument(History.getArg());
            command.setComplexArgument(History.getComplexArg());
            command.execute();
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
