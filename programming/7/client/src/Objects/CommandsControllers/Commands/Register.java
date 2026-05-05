package Objects.CommandsControllers.Commands;

import Objects.Builders.ProductBuilder;
import Objects.Builders.UserBuilder;
import Objects.Collection.Product;
import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Managers.AuthManager;
import Objects.Managers.CommandManager;
import Objects.UserData.User;

/**
 * Adds an element to the collection
 */
public class Register extends Command {

    public Register(boolean hasArgument) {
        super(hasArgument);
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
            CommandManager.allowCommandsForAuthenticatedUsers();
            result +="Now all commands are accessible";
            return result;
        }
        else
            return object.toString() + "\n";
    }
}
