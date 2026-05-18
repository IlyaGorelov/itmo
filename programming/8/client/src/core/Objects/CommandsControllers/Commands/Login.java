package core.Objects.CommandsControllers.Commands;

import core.Objects.Builders.UserBuilder;
import core.Objects.CommandsControllers.Command;
import Commons.CustomPackage;
import core.Objects.Connection.Client;
import core.Objects.Managers.AuthManager;
import core.Objects.Managers.CommandManager;
import Commons.UserData.User;
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
            ErrorMessageDeliverer.add(new IllegalArgumentException("Wrong login or password!"),ErrorMessageDeliverer.response);
            return "Wrong login or password!";
        }

        if (object instanceof User registered) {
            AuthManager.getInstance().setUser(registered);
            result = "User " + registered.getLogin() + " was logged in." + "\n";
            CommandManager.putCommandsForAuthenticatedUsers();
            result +="Now all commands are accessible";

            Client.openFrame(new MainFrame());
            return result;
        }
        else
            return object.toString() + "\n";
    }


}
