package Objects.CommandsControllers;

import Objects.Managers.AuthManager;

public abstract class AuthCommand extends Command {
    private AuthManager authManager;


    public AuthCommand(AuthManager authManager, boolean hasArg, boolean hasComplexArg) {
        super(null, hasArg, hasComplexArg);
        setAuthManager(authManager);
    }

    public void setAuthManager(AuthManager authManager) {
        this.authManager = authManager;
    }

    public AuthManager getAuthManager() {
        return authManager;
    }
}
