package Objects.CommandsControllers.Handlers;

import Objects.CommandsControllers.Command;

public class SingleCommandHandler extends  CommandHandler{
    private  Command command;

    public SingleCommandHandler(Command command){
        this.command = command;
    }

    @Override
    public void execute(String[] args) {
        command.execute();
    }
}
