package sharedAppPackage.models;

import java.util.HashSet;
import java.util.Set;

public final class UserSession {
    //UserSession.getInstace(userName, privileges)
    // and when you do log out: UserSession.cleanUserSession()
    private static UserSession instance;

    private String userName;
    private boolean is_admin,is_ass_admin,is_member;

    public boolean isIs_admin() {
        return is_admin;
    }

    public boolean isIs_ass_admin() {
        return is_ass_admin;
    }

    public boolean isIs_member() {
        return is_member;
    }

    private UserSession(String userName, boolean is_admin, boolean is_ass_admin, boolean is_member) {
        this.userName = userName;
        this.is_admin=is_admin;
        this.is_ass_admin=is_ass_admin;
        this.is_member=is_member;
    }

    public static UserSession getInstace(){
        return instance;
    }
    public static UserSession getInstace(String userName, boolean is_admin, boolean is_ass_admin, boolean is_member) {
        if(instance == null) {
            instance = new UserSession(userName, is_admin, is_ass_admin, is_member);
        }
        return instance;
    }

    public String getUserName() {
        return userName;
    }



    public void cleanUserSession() {
        instance=null;
    }

    @Override
    public String toString() {
        String privileges;
        if(is_admin)
            privileges="Super admin";
        else if (is_ass_admin)
            privileges="Association admin";
        else if (is_member)
            privileges="Association member";
        else
            privileges="Simple user";
        return "UserSession{" +
                "userName='" + userName + '\'' +
                ", privileges=" +
                    privileges
                +
                '}';
    }
}