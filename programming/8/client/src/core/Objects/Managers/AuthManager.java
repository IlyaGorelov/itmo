package core.Objects.Managers;

import Commons.UserData.User;

public class AuthManager {
    private static volatile AuthManager instance;
    private User user;

    public static AuthManager getInstance() {
        AuthManager localInstance = instance;
        if (localInstance == null) {
            synchronized (AuthManager.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new AuthManager();
                }
            }
        }
        return localInstance;
    }

    public void setUser(User user){
        this.user = user;
    }
    public void removeUser(){this.user=null;}

    public User getUser(){
        return user;
    }

    public boolean isAuthorized(){
        return user!=null;
    }
}
