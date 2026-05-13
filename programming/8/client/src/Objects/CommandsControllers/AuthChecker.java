package Objects.CommandsControllers;

import Objects.Managers.AuthManager;

public interface AuthChecker {
    default void checkAuth() throws SecurityException {
        if (!AuthManager.getInstance().isAuthorized())
            throw new SecurityException("User is not authenticated");
    }
}

