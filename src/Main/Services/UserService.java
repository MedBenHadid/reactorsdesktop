package Main.Services;

import de.ailis.pherialize.Pherialize;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Main.Entities.User;
import Main.Entities.UserSession;
import utils.Utils.BCrypt.BCrypt;
import utils.Utils.Connector.ConnectionUtil;

import java.sql.*;

public class UserService {
    private static UserService instance;
    private Connection connection;


    private UserService() {
        connection = ConnectionUtil.conDB().conn;
    }
    public static UserService getInstace() {
        if(instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public void createUpdateUser(User user){
        String request ="INSERT INTO user VALUES ()";
        Statement st;
        try {
            st = connection.createStatement();
            st.executeQuery(request);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User readUserBy(int id){
        User user;
        try {
            PreparedStatement pt = connection.prepareStatement("SELECT * FROM user WHERE id = ?");
            pt.setInt(1,id);
            user = resultSetToUser(pt.executeQuery());
            return user;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public User readUserBy(String credential){
        User user;
        try {
            PreparedStatement pt = connection.prepareStatement("SELECT * FROM user WHERE username = ? OR email = ?");
            pt.setString(1,credential);
            pt.setString(2,credential);
            user = resultSetToUser(pt.executeQuery());
            return user;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public void deleteUser(User user){
        PreparedStatement ps ;
        try {
            ps = connection.prepareStatement("DELETE FROM user WHERE id=?");
            ps.setInt(1,user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public ObservableList<User> listUsers(){
        ObservableList<User> users = FXCollections.observableArrayList();
        try {
            PreparedStatement pt = connection.prepareStatement("SELECT * FROM user");
            ResultSet rs = pt.executeQuery();
            while(rs.next()){
                users.add(resultSetToUser(rs));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean validateLogin(String credential, String password) throws SQLException {
        String sql= "SELECT * FROM user Where username =? OR  email =? ";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, credential);
        preparedStatement.setString(2, credential);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next() && BCrypt.checkpw(password, resultSet.getString("password"))){
            UserSession us =UserSession.getInstace(UserService.getInstace().resultSetToUser(resultSet));
        }
        return UserSession.getInstace()!=null;
    }

    public void register(User u) {
        String req = "insert into user (username,username_canonical,email,email_canonical,enabled,password,nom,prenom,date_naissance,cin,roles) values (?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, u.getUsername());
            preparedStatement.setString(2, u.getUsername());
            preparedStatement.setString(3, u.getEmail());
            preparedStatement.setString(4, u.getEmail());
            preparedStatement.setString(6, u.getPassword());
            preparedStatement.setString(7, u.getProfile().getNom());
            preparedStatement.setString(8, u.getProfile().getPrenom());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    private User resultSetToUser(ResultSet rs) throws SQLException {
        User u = new User();
        if(rs.next()) {
            u.setId(rs.getInt("id"));
            u.setUsername(rs.getString("username"));
            u.setEmail(rs.getString("email"));
            u.setPlainPassword(rs.getString("password_plain"));
            u.setEnabled(rs.getBoolean("enabled"));
            u.setLast_login(rs.getTimestamp("last_login"));
            u.setRoles(Pherialize.unserialize(rs.getString("roles")).toArray());
            u.getProfile().setImage(rs.getString("image"));
            //u.getProfile().setAdresse();
        }
        return u;
    }

    public void disable(User u) {
        String req = "update user set enabled=? where username=?";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(req);
            preparedStatement.setInt(1, 0);
            preparedStatement.setString(2, u.getUsername());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void enable(User u) {
        String req = "update user set enabled=? where username=?";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(req);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, u.getUsername());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


}
