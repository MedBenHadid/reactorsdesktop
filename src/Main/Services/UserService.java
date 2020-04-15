package Main.Services;

import Main.Entities.User;
import Main.Entities.UserSession;
import SharedResources.Utils.BCrypt.BCrypt;
import SharedResources.Utils.Connector.ConnectionUtil;
import de.ailis.pherialize.Pherialize;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class UserService {
    private static UserService instance;
    // TODO : Update profile
    // TODO : Update
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



    public User readUserByCredentials(String credential) {
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



    public boolean validateLogin(String credential, String password) throws SQLException {
        String sql = "SELECT * FROM user Where username =? OR  email =? ";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, credential);
        preparedStatement.setString(2, credential);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next() && BCrypt.checkpw(password, resultSet.getString("password"))) {
            UserSession.getInstace(UserService.getInstace().resultSetToUser(resultSet));
        }
        return UserSession.getInstace() != null;
    }


    private User resultSetToUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setEnabled(rs.getBoolean("enabled"));
        u.setLast_login(rs.getTimestamp("last_login"));
        u.setRoles(Pherialize.unserialize(rs.getString("roles")).toArray());
        u.getProfile().setImage(rs.getString("image"));
        //u.getProfile().setAdresse();
        u.getProfile().setImage(rs.getString("image"));
        u.getProfile().setNom(rs.getString("nom"));
        u.getProfile().setPrenom(rs.getString("prenom"));
        return u;
    }

    public int create(User u) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into user (username,username_canonical,email,email_canonical,enabled,password,roles,banned) values (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, u.getUsername());
        preparedStatement.setString(2, u.getUsername());
        preparedStatement.setString(3, u.getEmail());
        preparedStatement.setString(4, u.getEmail());
        preparedStatement.setBoolean(5, u.getEnabled());
        preparedStatement.setString(6, u.getPassword());
        preparedStatement.setString(7, Pherialize.serialize(u.getRoles()));
        preparedStatement.setBoolean(8, u.getBanned());
        preparedStatement.executeUpdate();

        ResultSet rs = preparedStatement.getGeneratedKeys();
        if (rs.next())
            return rs.getInt(1);
        return -1;
    }

    public User readUserBy(int id) throws SQLException {
        PreparedStatement pt = connection.prepareStatement("SELECT * FROM user WHERE id = ?");
        pt.setInt(1, id);
        ResultSet rs = pt.executeQuery();
        if (rs.next()) {
            return resultSetToUser(rs);
        }
        return null;
    }

    public ObservableList<User> readAll() throws SQLException {
        ObservableList<User> users = FXCollections.observableArrayList();
        PreparedStatement pt = connection.prepareStatement("SELECT * FROM user");
        ResultSet rs = pt.executeQuery();
        while (rs.next()) {
            users.add(resultSetToUser(rs));
        }
        return users;
    }




    public void delete(User user) throws SQLException {
        PreparedStatement ps;
        ps = connection.prepareStatement("DELETE FROM user WHERE id=?");
        ps.setInt(1, user.getId());
        ps.executeUpdate();
    }

}
