package Packages.Chihab.Services;

import Main.Entities.User;
import Main.Services.UserService;
import Packages.Chihab.Models.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private static CategoryService instance;
    private Connection connection;

    // TODO : Add readById so you can show domain in association profile and so the boys can use it too
    public void create(Category category){
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

    public List<Category> readAll(){
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

    public Category readById(int id){
        Category c = new Category();

        return c;
    }

    public void update(Category c) {
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

    public void delete(Category c){
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

    public List<Category> search(String name){
        ArrayList<Category> categories = new ArrayList<>();
        String req = "SELECT * FROM category WHERE name LIKE '%"+name+"%' ";
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

    public Category resultSetToCategory(ResultSet rs) throws SQLException {
        Category cat=new Category();
        cat.setId(rs.getInt("id"));
        cat.setNom(rs.getString("name"));
        cat.setDescription("description");
        return cat;
    }
    private CategoryService() { connection = utils.Utils.Connector.ConnectionUtil.conDB().conn; }
    public static CategoryService getInstace() {
        if(instance == null) { instance = new CategoryService(); }
        return instance;
    }
}
