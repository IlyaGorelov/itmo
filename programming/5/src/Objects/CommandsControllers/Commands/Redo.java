package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.CommandBuffer;
import Objects.CommandsControllers.History;
import Objects.Managers.CollectionManager;
import Objects.Managers.CommandManager;

/** show information about collection */
public class Redo extends Command {

    public Redo(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public Redo(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();

        String[] redos = History.getLastUndo();
        if (redos.length == 0)
            System.out.println("Nothing to redo");
        else if (redos.length > 0) {
            CommandManager.addUnrecordedCommands(redos.length);
            for (String string : redos)
                CommandBuffer.buffer.add(string);
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
