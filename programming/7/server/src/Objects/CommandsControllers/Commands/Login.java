package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.AuthCommand;
import Objects.Connection.CustomPackage;
import Objects.Managers.AuthManager;
import Objects.UserData.User;

public class Login extends AuthCommand {

    public Login(AuthManager authManager, boolean hasArg, boolean hasComplexArg) {
        super(authManager, hasArg, hasComplexArg);
    }

    @Override
    public void execute() {
        checkArgument();
        User user = (User) getComplexArgument();
        User logged = getAuthManager().login(user);

        CustomPackage pkg = new CustomPackage(this.getName(), null, logged);
        if (logged != null) {
            answer(pkg, "Logged in " + logged.getLogin());
        } else {
            answer(pkg, "Wrong login or password!");
        }
    }

    @Override
    public String getName() {
        return "login";
    }

    @Override
    public String getDescription() {
        return "login";
    }

}
