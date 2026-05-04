package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.AuthCommand;
import Objects.Connection.CustomPackage;
import Objects.Managers.AuthManager;
import Objects.UserData.User;

public class Register extends AuthCommand {

    public Register(AuthManager authManager) {
        super(authManager);
    }

    @Override
    public void execute() {
        checkArgument();
        User newUser = (User) getComplexArgument();
        getAuthManager().register(newUser);

        CustomPackage pkg = new CustomPackage(this.getName(), null, newUser);
        answer(pkg, "Successfully added " + newUser.getLogin());
    }

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public String getDescription() {
        return "register";
    }

}
