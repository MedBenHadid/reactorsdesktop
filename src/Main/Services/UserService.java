package Main.Services;

import Main.Entities.User;
import Packages.Chihab.Models.enums.RoleEnum;
import SharedResources.Utils.BCrypt.BCrypt;
import SharedResources.Utils.Connector.ConnectionUtil;
import de.ailis.pherialize.Pherialize;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.sql.*;
import java.util.Optional;

public class UserService {
    private static UserService instance;
    // TODO : Update profile
    // TODO : Update
    private final Connection connection = ConnectionUtil.getInstance().getConn();

    public UserService() throws SQLException {
    }

    public static UserService getInstace() throws SQLException {
        if(instance == null) {
            instance = new UserService();
        }
        return instance;
    }


    public User update(User u) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user SET roles=? WHERE id=?");
        preparedStatement.setString(1, Pherialize.serialize(u.getRoles()));
        preparedStatement.setInt(2, u.getId());
        preparedStatement.executeUpdate();
        return u;
    }

    public ObservableMap<Integer,User> readAllHashed() throws SQLException {
        ObservableMap<Integer,User> users = FXCollections.observableHashMap();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            User user = resultSetToUser(resultSet);
            users.put(user.getId(),user);
        }
        return users;
    }

    public Optional<User> validateLogin(String credential, String password) throws SQLException {
        String sql = "SELECT * FROM user Where username =? OR  email =? ";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, credential);
        preparedStatement.setString(2, credential);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next() && BCrypt.checkpw(password, resultSet.getString("password"))) {
            return Optional.of(resultSetToUser(resultSet));
        }
        return Optional.empty();
    }


    private User resultSetToUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setEnabled(rs.getInt("enabled") == 1);
        u.setApprouved(rs.getInt("approuved") == 1);
        u.setLast_login(rs.getTimestamp("last_login"));
        u.setRoles(Pherialize.unserialize(rs.getString("roles")).toArray());
        u.getRoles().put(u.getRoles().size(), RoleEnum.ROLE_CLIENT);
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
