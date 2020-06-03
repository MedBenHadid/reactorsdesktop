package Packages.Chihab.Services;

import Packages.Chihab.Models.Entities.Category;
import SharedResources.Utils.Connector.ConnectionUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CategoryService {
    private static CategoryService instance;

    private CategoryService() {

    }

    public static CategoryService getInstace() {
        if (instance == null) {
            instance = new CategoryService();
        }
        return instance;
    }

    public int create(Category category) throws SQLException {
        PreparedStatement st = ConnectionUtil.getInstance().getConn().prepareStatement("INSERT INTO category (name,description) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
        st.setString(1, category.getNom());
        st.setString(2, category.getDescription());
        st.executeUpdate();
        ResultSet rs = st.getGeneratedKeys();
        if (rs.next())
            return rs.getInt(1);
        return 0;
    }

    public ObservableList<Category> readAll(){
        try {
            ObservableList<Category> categoriesItems = FXCollections.observableArrayList();
            ResultSet resultSet = ConnectionUtil.getInstance().getConn().prepareStatement("SELECT * FROM category").executeQuery();
            while (resultSet.next()) {
                categoriesItems.add(resultSetToCategory(resultSet));
            }
            resultSet.close();
            return categoriesItems;
        } catch (SQLException e){
            System.out.println("Error");
            return FXCollections.observableList(new ArrayList<>());
        }
    }


    public Category readById(int id) throws SQLException {
        PreparedStatement pt = ConnectionUtil.getInstance().getConn().prepareStatement("SELECT * FROM category WHERE id = ?");
        pt.setInt(1, id);
        ResultSet rs = pt.executeQuery();

        if (rs.next()) {
            return resultSetToCategory(rs);
        } else {
            return null;
        }
    }

    public void update(Category c) throws SQLException {
        PreparedStatement preparedStatement = ConnectionUtil.getInstance().getConn().prepareStatement("UPDATE category SET name=?,description=?WHERE id=?");
        preparedStatement.setString(1, c.getNom());
        preparedStatement.setString(2, c.getDescription());
        preparedStatement.setInt(3, c.getId());
        preparedStatement.executeUpdate();
    }

    public void delete(Category c) throws SQLException {
        PreparedStatement preparedStatement = ConnectionUtil.getInstance().getConn().prepareStatement("DELETE FROM category WHERE id=?");
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
