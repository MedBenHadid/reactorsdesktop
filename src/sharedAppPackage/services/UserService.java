package sharedAppPackage.services;

import sharedAppPackage.models.User;
import sharedAppPackage.models.UserSession;
import sharedAppPackage.utils.BinaryValidator.EmailValidator;
import sharedAppPackage.utils.connector.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {
    private static UserService instance;
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

    public User readUserById(int id){
        User user=null;
        try {
            PreparedStatement pt = connection.prepareStatement("SELECT * FROM user WHERE id = ?");
            pt.setInt(1,id);
            user = resultSetToUser(pt.executeQuery());
        } catch (SQLException e){
            e.printStackTrace();
        }
        return user;
    }

    public List<User> listUsers(){
        List<User> userList = new ArrayList<>();
        try {
            PreparedStatement pt = connection.prepareStatement("SELECT * FROM user");
            ResultSet rs = pt.executeQuery();
            while(rs.next()){
                // TO:DO : Add parameters !
                User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setUsername(rs.getString("username"));
                    u.setUsername(rs.getString("email"));
                    u.setPlainPassword(rs.getString("password_plain"));
                    u.setEnabled(rs.getBoolean("enabled"));
                    u.setLast_login(rs.getTimestamp("last_login"));
                    u.setIs_admin(rs.getBoolean("is_admin"));
                    u.setIs_ass_admin(rs.getBoolean("is_ass_admin"));
                    u.setIs_member(rs.getBoolean("is_member"));
                userList.add(u);
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

    public boolean validateLogin(String credential, String password) throws SQLException {
        String sql;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        boolean t;
        if (EmailValidator.isEmail(credential))
            sql = "SELECT * FROM user Where email = ? and password_plain = ?";
        else
            sql = "SELECT * FROM user Where username =? and password_plain = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, credential);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            t = resultSet.next();
            if (t) {
                User current = UserService.getInstace().resultSetToUser(resultSet);
                UserSession.getInstace(current.getUsername(), current.getIs_admin(), current.getIs_ass_admin(), current.getIs_member());
            }
            return t;
    }

    public User resultSetToUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setUsername(rs.getString("email"));
        u.setPlainPassword(rs.getString("password_plain"));
        u.setEnabled(rs.getBoolean("enabled"));
        u.setLast_login(rs.getTimestamp("last_login"));
        u.setIs_admin(rs.getBoolean("is_admin"));
        u.setIs_ass_admin(rs.getBoolean("is_ass_admin"));
        u.setIs_member(rs.getBoolean("is_member"));
        return u;
    }

    private UserService() {
        connection = ConnectionUtil.conDB().conn;
    }
    public static UserService getInstace() {
        if(instance == null) {
            instance = new UserService();
        }
        return instance;
    }
}
