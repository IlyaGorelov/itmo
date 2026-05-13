package Objects.CommandsControllers.Commands;

import Objects.Builders.UserBuilder;
import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Managers.AuthManager;
import Objects.Managers.CommandManager;
import Objects.UserData.User;

/**
 * Adds an element to the collection
 */
public class Login extends Command {

    public Login(boolean hasArgument) {
        super(hasArgument);
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

        if(object==null)
            return "Wrong login or password!";

        if (object instanceof User registered) {
            AuthManager.getInstance().setUser(registered);
            result = "User " + registered.getLogin() + " was logged in." + "\n";
            CommandManager.allowCommandsForAuthenticatedUsers();
            result +="Now all commands are accessible";
            return result;
        }
        else
            return object.toString() + "\n";
    }

}
