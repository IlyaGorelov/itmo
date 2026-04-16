package Objects.CommandsControllers.Handlers;

import Objects.CommandsControllers.Command;

public class CommandWithArgHandler extends  CommandHandler{
    private Command command;

    public CommandWithArgHandler(Command command){
        this.command = command;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) throw new IllegalArgumentException("Argument expected");
        if (args[0].startsWith("{")) {
            command.executeFromScript(args[0]);
        } else {
            command.setArgument(args[0]);
            command.execute();
        }
    }
}
