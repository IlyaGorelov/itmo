package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.CommandExecutor;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;

/** stop program */
public class Exit extends Command {

    public Exit(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public Exit(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String getDescription() {
        return "exit the program";
    }

    @Override
    public void execute() {
        checkArgument();
        var save = new Save(getCollectionManager());
        save.setReceiver(getReceiver());
        save.setClient(getCLient());
        save.execute();

        if (!getIsCLIMode()) {
            CustomPackage pkg = new CustomPackage(this.getName(), getArgument(), "Program successfully stopped");
            getReceiver().addToAnswer(getCLient(), pkg);
        } else {
            getReceiver().addAnswerForCLI("Program successfully stopped");
        }

        CommandExecutor.waitForNextCommand = false;

    }

    @Override
    public void executeFromScript(String complexArg) {
        execute();
    }

}
