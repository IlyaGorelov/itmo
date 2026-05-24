package core.Objects.CommandsControllers.Commands;

import Localization.I18n;
import core.Objects.Builders.UserBuilder;
import core.Objects.CommandsControllers.Command;
import Commons.CustomPackage;
import core.Objects.Connection.Client;
import core.Objects.Managers.AuthManager;
import core.Objects.Managers.CommandManager;
import Commons.UserData.User;
import gui.Objects.Elements.Commons.ResultDialog;
import gui.Objects.Frames.MainFrame;
import gui.Objects.Helpers.ErrorMessageDeliverer;

/**
 * Adds an element to the collection
 */
public class Login extends Command {
    public Login(){
        super();
    }

    @Override
    public Object getRelevantObject() {
        checkArgument();

        System.out.println("Type values to log in.");

        UserBuilder userBuilder = new UserBuilder();
        return userBuilder.build(getScanner());
    }

    @Override
    public String getName() {
        return "login";
    }

    @Override
    public String getDescription() {
        return "log in to an account";
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object object = pack.getObject();
        String result="";

        if(object==null) {
            sendError(I18n.get("error.login"));
            return "";
        }

        if (object instanceof User registered) {
            AuthManager.getInstance().setUser(registered);
            result = I18n.get("info.login1").formatted(registered.getLogin()) + "\n";
            CommandManager.putCommandsForAuthenticatedUsers();
            result += I18n.get("info.login2");

            Client.openFrame(new MainFrame());
            return "";
        }
        else
            return object.toString() + "\n";
    }


}
