package Packages.Chihab.Services;

import Packages.Chihab.Models.Category;
import SharedResources.Utils.Connector.ConnectionUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CategoryService {
    private static CategoryService instance;
    private Connection connection;

    private CategoryService() {
        connection = ConnectionUtil.conDB().conn;
    }

    public static CategoryService getInstace() {
        if (instance == null) {
            instance = new CategoryService();
        }
        return instance;
    }

    public int create(Category category) throws SQLException {
        String sql ="INSERT INTO category (name,description) VALUES (?,?)";
        PreparedStatement st;
        st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        st.setString(1, category.getNom());
        st.setString(2, category.getDescription());
        st.executeUpdate();
        ResultSet rs = st.getGeneratedKeys();
        if (rs.next())
            return rs.getInt(1);
        return 0;
    }

    public ObservableList<Category> readAll() throws SQLException {
        ObservableList<Category> categoriesItems = FXCollections.observableArrayList();
        String req = "SELECT * FROM category";
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(req);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            categoriesItems.add(resultSetToCategory(resultSet));
        }
        resultSet.close();
        return categoriesItems;
    }


    Category readById(int id) throws SQLException {
        PreparedStatement pt = connection.prepareStatement("SELECT * FROM category WHERE id = ?");
        pt.setInt(1, id);
        ResultSet rs = pt.executeQuery();
        if (rs.next()) {
            return resultSetToCategory(rs);
        } else {
            return null;
        }
    }

    public void update(Category c) throws SQLException {
        String req = "UPDATE category SET name=?,description=?WHERE id=?";
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, c.getNom());
        preparedStatement.setString(2, c.getDescription());
        preparedStatement.setInt(3, c.getId());
        preparedStatement.executeUpdate();
    }

    public void delete(Category c) throws SQLException {
        String req = "DELETE FROM category WHERE id=?";
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, c.getId());
        preparedStatement.executeUpdate();
    }

    Category resultSetToCategory(ResultSet rs) throws SQLException {
        Category cat=new Category();
        cat.setId(rs.getInt("id"));
        cat.setNom(rs.getString("name"));
        cat.setDescription(rs.getString("description"));
        return cat;
    }
}
