package sharedAppPackage.services;

import sharedAppPackage.models.User;
import sharedAppPackage.utils.connector.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {
    private Connection connection;

    public void createUser(User user){
        String request ="INSERT INTO user VALUES ()";
        Statement st;
        try {
            st = connection.createStatement();
            st.executeQuery(request);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUserLastName(int id,String firstName){
        try {
            PreparedStatement pt = connection.prepareStatement("UPDATE user SET nom=? WHERE id=?");
            pt.setString(1,firstName);
            pt.setInt(2,id);
            pt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    public User readUser(int id){
        User user = null;
        return user;
    }

    public List<User> listUsers(){
        List<User> userList = new ArrayList<>();
        try {
            PreparedStatement pt = connection.prepareStatement("SELECT * FROM user");
            ResultSet rs = pt.executeQuery();
            while(rs.next()){
                // TO:DO : Add parameters !
                User user = User.getInstance();
                userList.add(user);

            }
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteUser(int id){
        PreparedStatement ps ;
        try {
            ps = connection.prepareStatement("DELETE FROM user WHERE id=?");
            ps.setInt(1,id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean validateLogin(String email, String password){

        return true;
    }
    public UserService() {
        connection = ConnectionUtil.conDB().conn;
    }
}
