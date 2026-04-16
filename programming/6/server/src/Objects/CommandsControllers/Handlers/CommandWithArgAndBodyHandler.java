package Objects.CommandsControllers.Handlers;

import Objects.CommandsControllers.Command;

public class CommandWithArgAndBodyHandler extends  CommandHandler{
    private Command command;

    public CommandWithArgAndBodyHandler(Command command) {
        this.command = command;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2 || !args[1].startsWith("{"))
            throw new IllegalArgumentException("Body expected as a second arg");
        command.setArgument(args[0]);
        command.executeFromScript(args[1]);
    }
}
