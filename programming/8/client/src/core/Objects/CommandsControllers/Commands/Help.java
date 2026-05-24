package core.Objects.CommandsControllers.Commands;

import core.Objects.CommandsControllers.Command;
import Commons.CustomPackage;
import core.Objects.Managers.CommandManager;

import java.util.TreeMap;

/** shows all available commands with description */
public class Help extends Command {
    public Help() {
        super();
    }

    @Override
    public Object getRelevantObject() {
        checkArgument();

        StringBuilder answer = new StringBuilder();
        new TreeMap<>(CommandManager.getCommandMap()).forEach((name, command) -> {
                answer.append(name).append(": ").append(command.getDescription()).append("\n");
        });

        return answer.toString();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Show descriptions of available commands";
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object object = (Object) pack.getObject();


        return (String) object;
    }

}
