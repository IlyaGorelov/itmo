package core.Objects.CommandsControllers.Commands;

import Commons.CustomPackage;
import core.Objects.CommandsControllers.Command;
import core.Objects.Connection.Client;
import core.Objects.Managers.AuthManager;
import core.Objects.Managers.CommandManager;
import gui.Objects.Frames.LoginFrame;

/** shows all available commands with description */
public class Logout extends Command {
    public Logout() {
        super();
    }

    @Override
    public Object getRelevantObject() {
        checkArgument();

        StringBuilder answer = new StringBuilder();
        AuthManager.getInstance().removeUser();
        CommandManager.putCommandForUnAuthenticatedUsers();

        Client.openFrame(new LoginFrame());

        return answer.toString();
    }

    @Override
    public String getName() {
        return "logout";
    }

    @Override
    public String getDescription() {
        return "logout";
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object object = (Object) pack.getObject();

        return (String) object;
    }

}
