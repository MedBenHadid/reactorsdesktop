package Packages.Chihab.Services;

import Packages.Chihab.Models.Category;
import SharedResources.Utils.Connector.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    // DONE
    public int create(Category category) throws SQLException {
        String sql ="INSERT INTO category (name,description) VALUES (?,?)";
        PreparedStatement st;
        st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        st.setString(1, category.getNom());
        st.setString(2, category.getDescription());
        int id = st.executeUpdate();
        ResultSet rs = st.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public List<Category> readAll() throws SQLException {
        ArrayList<Category> categoriesItems = new ArrayList<>();
        String req = "SELECT * FROM category";
        PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(req);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                categoriesItems.add(resultSetToCategory(resultSet));
            }
        return categoriesItems;
    }


    Category readById(int id) throws SQLException {
        Category c = new Category();
            PreparedStatement pt = connection.prepareStatement("SELECT * FROM category WHERE id = ?");
            pt.setInt(1,id);
            c =  resultSetToCategory(pt.executeQuery());
        return c;
    }

    public List<Category> search(String name) throws SQLException {
        ArrayList<Category> categories = new ArrayList<>();
        String req = "SELECT * FROM category WHERE name LIKE '%" + name + "%' ";
        PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(req);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                categories.add(resultSetToCategory(resultSet));
            }
        return categories;
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
        if(rs.next()) {
            cat.setId(rs.getInt("id"));
            cat.setNom(rs.getString("name"));
            cat.setDescription(rs.getString("description"));
        }
        return cat;
    }
}
