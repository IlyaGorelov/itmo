package core.Objects.CommandsControllers.Commands;

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
public class Register extends Command {

    public Register() {
        super();
    }

    @Override
    public Object getRelevantObject() {
        checkArgument();

        System.out.println("Type values to register.");

        UserBuilder userBuilder = new UserBuilder();
        return userBuilder.build(getScanner());
    }

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public String getDescription() {
        return "create a new account";
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object object = pack.getObject();
        String result="";

        if (object instanceof User registered) {
            AuthManager.getInstance().setUser(registered);
            result = "User " + registered.getLogin() + " was registered." + "\n";
            CommandManager.putCommandsForAuthenticatedUsers();
            result +="Now all commands are accessible";

            Client.openFrame(new MainFrame());
            return result;
        }
        else {
          //  ErrorMessageDeliverer.add(new RuntimeException(object.toString()), ErrorMessageDeliverer.response);
            ResultDialog.showError(getName(),object.toString());
            return object.toString() + "\n";
        }
    }
}
