package sharedAppPackage.models;

public class User {




    // SINGELTON
    private static User instance;
    private User() {
    }
    public static User getInstance(){
        if(instance==null)
            instance = new User();
        return instance;
    }
    // SINGELTON
}
