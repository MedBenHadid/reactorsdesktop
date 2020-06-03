package Main.Controllers;

import Main.Entities.User;
import Main.Services.UserService;
import javafx.collections.ObservableMap;

import java.sql.SQLException;

public class UserController {
    private static UserController instance;

    public ObservableMap<Integer, User> getUsers() {
        return users;
    }

    public void setUsers(ObservableMap<Integer, User> users) {
        this.users = users;
    }

    private ObservableMap<Integer, User> users;

    public boolean add(User a) throws SQLException {
        if(users.containsKey(a))
            return false;
        else{
            UserService.getInstace().create(a);
            users.put(a.getId(),a);
            return true;
        }
    }
    public void update(int uId,User u){
        users.computeIfPresent(uId, (k, v) -> {
            try {
                return v = UserService.getInstace().update(u);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return null;
            }
        });
    }
    public void delete(User u) throws SQLException {
        UserService.getInstace().delete(u);
        this.users.remove(u.getId(),u);
    }
    private UserController() throws SQLException {
        this.users= UserService.getInstace().readAllHashed();
    }
    public static UserController getInstance() throws SQLException {
        if(instance==null) return instance = new UserController();
        return instance;
    }
}
