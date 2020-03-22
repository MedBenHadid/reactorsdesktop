package Packages.Chihab.Services;

import Packages.Chihab.Models.Category;
import SharedResources.Utils.Connector.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private static CategoryService instance;
    private Connection connection;

    private CategoryService() {
        connection = ConnectionUtil.conDB().conn;
    }

    static CategoryService getInstace() {
        if (instance == null) {
            instance = new CategoryService();
        }
        return instance;
    }

    // DONE
    void create(Category category) {
        String sql ="INSERT INTO category (name,description) VALUES (?,?)";
        PreparedStatement st;
        try {
            st = connection.prepareStatement(sql);
            st.setString(1,category.getNom());
            st.setString(2,category.getDescription());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    List<Category> readAll() {
        ArrayList<Category> categories = new ArrayList<>();
        String req = "SELECT * FROM category";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(req);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                categories.add(resultSetToCategory(resultSet));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return categories;
    }

    Category readById(int id) {
        Category c = new Category();
        try {
            PreparedStatement pt = connection.prepareStatement("SELECT * FROM category WHERE id = ?");
            pt.setInt(1,id);
            c =  resultSetToCategory(pt.executeQuery());
        } catch (SQLException e){
            e.printStackTrace();
        }
        return c;
    }

    public List<Category> search(String name) {
        ArrayList<Category> categories = new ArrayList<>();
        String req = "SELECT * FROM category WHERE name LIKE '%" + name + "%' ";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(req);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                categories.add(resultSetToCategory(resultSet));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return categories;
    }

    void update(Category c) {
        String req = "UPDATE category SET name=?,description=?WHERE id=?";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, c.getNom());
            preparedStatement.setString(2, c.getDescription());
            preparedStatement.setInt(3, c.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void delete(Category c) {
        String req = "DELETE FROM category WHERE id=?";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(req);
            preparedStatement.setInt(1, c.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    Category resultSetToCategory(ResultSet rs) throws SQLException {
        Category cat=new Category();
        if(rs.next()) {
            cat.setId(rs.getInt("id"));
            cat.setNom(rs.getString("name"));
            cat.setDescription("description");
        }
        return cat;
    }
}
