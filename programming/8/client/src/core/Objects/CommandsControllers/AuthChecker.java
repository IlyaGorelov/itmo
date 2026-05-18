package core.Objects.CommandsControllers;

import core.Objects.Managers.AuthManager;

public interface AuthChecker {
    default void checkAuth() throws SecurityException {
        if (!AuthManager.getInstance().isAuthorized())
            throw new SecurityException("User is not authenticated");
    }
}

