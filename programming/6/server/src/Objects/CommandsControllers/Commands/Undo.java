package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.CommandBuffer;
import Objects.CommandsControllers.History;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;
import Objects.Managers.CommandManager;

import java.util.Arrays;

/** show information about collection */
public class Undo extends Command {

    public Undo(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArg) {
        super(collectionManager, hasArgument, hasComplexArg);
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
        } else {
            CommandManager.addUnrecordedCommands(undos.length);
            CommandBuffer.buffer.addAll(Arrays.asList(undos));
        }
            CustomPackage pkg = new CustomPackage(this.getName(), null, answer);
       answer(pkg,answer);


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
