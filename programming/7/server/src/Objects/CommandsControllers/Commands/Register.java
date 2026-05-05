package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.AuthCommand;
import Objects.Connection.CustomPackage;
import Objects.Managers.AuthManager;
import Objects.UserData.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class Register extends AuthCommand {
    private final static Logger logger = LoggerFactory.getLogger(Register.class);

    public Register(AuthManager authManager, boolean hasArg, boolean hasComplexArg) {
        super(authManager, hasArg, hasComplexArg);
    }

    @Override
    public void execute() {
        checkArgument();
        User newUser = (User) getComplexArgument();

        try {
            getAuthManager().register(newUser);
            CustomPackage pkg = new CustomPackage(this.getName(), null, newUser);
            answer(pkg, "Registered " + newUser.getLogin());
        } catch (SQLException e) {
            CustomPackage pkg = new CustomPackage(this.getName(), null, "This login is taken!");
            logger.error(e.getMessage());
            answer(pkg, e.getMessage());
        } catch (Exception e) {
            CustomPackage pkg = new CustomPackage(this.getName(), null, e);
            logger.error(e.getMessage());
            answer(pkg, e.getMessage());
        }

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
