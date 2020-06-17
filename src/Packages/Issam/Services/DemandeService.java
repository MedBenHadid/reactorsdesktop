package Packages.Issam.Services;

import Packages.Chihab.Services.CategoryService;
import Packages.Issam.Models.Demande;
import Packages.Issam.Models.Don;
import SharedResources.Utils.Connector.ConnectionUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DemandeService {

    private static DemandeService instance;
    private Connection connection;

    public DemandeService(){
        connection = ConnectionUtil.getInstance().getConn(); }


        public static DemandeService getInstance(){
            if (instance == null) {
                instance = new DemandeService();
            }
            return instance;
        }

    public ObservableList<Demande> readAll() throws SQLException {
        ObservableList<Demande> demandeItems = FXCollections.observableArrayList();
        String req = "SELECT * FROM demande";
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(req);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            demandeItems.add(resultSetToDemande(resultSet));
            System.out.println("dsqdsqdqsd");
            System.out.println(demandeItems);
        }
        return demandeItems;
    }

    public int create(Demande demande ) throws SQLException {
        PreparedStatement st = connection.prepareStatement("INSERT INTO demande (domaine_id,Title,Description,address,phone ,rib, image) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, demande.getCategory().getId());
        st.setString(2, demande.getTitle());
        st.setString(3, demande.getDescription());
        st.setString(4,demande.getAddress());
        st.setString(5,demande.getPhone());
        st.setString(6, demande.getRib());
        st.setString(7 , demande.getImage());
        st.executeUpdate();
        ResultSet rs = st.getGeneratedKeys();
        if (rs.next())
            return rs.getInt(1);
        return 0;
    }


    public void update(Demande demande) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE demande SET Title=?,Description=?,address=?,phone=?,rib=?  WHERE id=?");
        preparedStatement.setString(1, demande.getTitle());
        preparedStatement.setString(2, demande.getDescription());
        preparedStatement.setString(3, demande.getAddress());
        preparedStatement.setString(4, demande.getPhone());
        preparedStatement.setString(5, demande.getRib());
        preparedStatement.setInt(6, demande.getId());

        preparedStatement.executeUpdate();
    }

    public void delete(Demande demande) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM demande WHERE id=?");
        preparedStatement.setInt(1, demande.getId());
        preparedStatement.executeUpdate();
    }

    Demande resultSetToDemande(ResultSet rs) throws SQLException {
        Demande demande = new Demande();

            demande.setId(rs.getInt("id"));
            demande.setTitle(rs.getString("Title"));
            demande.setDescription(rs.getString("Description"));
            demande.setAddress(rs.getString("address"));
            demande.setPhone(rs.getString("phone"));
            demande.setCreationDate(rs.getString("creationDate"));
            demande.setRib(rs.getString("rib"));
            demande.setImage(rs.getString("image"));
            demande.setCategory(CategoryService.getInstace().readById(rs.getInt("domaine_id")));
            demande.setUser(rs.getInt("user_id"));

        return demande;
    }
}