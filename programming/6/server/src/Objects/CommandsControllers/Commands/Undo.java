package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.CommandBuffer;
import Objects.CommandsControllers.History;
import Objects.Managers.CollectionManager;
import Objects.Managers.CommandManager;

/** show information about collection */
public class Undo extends Command {

    public Undo(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public Undo(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();
        String answer = "";

        String[] undos = History.getLastCommand();
        if (undos.length == 0) {
            answer += ("Nothing to undo");
            getReceiver().addToAnswer(this, null, answer);
        } else if (undos.length > 0) {
            CommandManager.addUnrecordedCommands(undos.length);
            for (String string : undos)
                CommandBuffer.buffer.add(string);
        }
        getReceiver().addToAnswer(this, null, answer);

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
