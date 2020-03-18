package sharedAppPackage.models;

import de.ailis.pherialize.MixedArray;
import sharedAppPackage.models.enums.RoleEnum;

import java.util.HashSet;
import java.util.Set;

public final class UserSession {
    private static UserSession instance;
    private User user;

    private UserSession(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void cleanUserSession() {
        instance=null;
    }
    public static UserSession getInstace() {
        if (instance != null)
            return instance;
        return null;
    }
    public static UserSession getInstace(User user) {
        if(instance == null) {
            instance = new UserSession(user);
        }
        return instance;
    }

}