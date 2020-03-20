package Packages.Chihab.Services;

import Packages.Chihab.Models.Association;
import Packages.Chihab.Models.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AssociationService {
    private static AssociationService instance;
    private Connection connection;

    public void create(Association a , int categoryId , int managerId){
        String sql ="INSERT INTO association " +
                "(`id`, " +
                "`domaine_id`, " +
                "`id_manager`, " +
                "`nom`, " +
                "`telephone`, " +
                "`horaire_travail`, " +
                "`photo_agence`, " +
                "`piece_justificatif`, " +
                "`rue`, " +
                "`code_postal`, " +
                "`ville`, " +
                "`latitude`, " +
                "`longitude`, " +
                "`approuved`, " +
                "`description`) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement st;
        try {
            st = connection.prepareStatement(sql);
            st.setInt(1,a.getId());
            st.setInt(2,categoryId);
            st.setInt(3,managerId);
            st.setString(4,a.getNom());
            st.setString(5,a.getTelephone());
            //st.setString(6,a.get);
            st.setString(7,a.getPhotoAgence());
            st.setString(8,a.getPieceJustificatif());
            st.setString(9,a.getRue());
            st.setString(10,a.getCodePostal());
            st.setString(11,a.getVille());
            st.setString(12,a.getLat());
            st.setString(13,a.getLon());
            st.setBoolean(14,true);
            st.setString(15,a.getDescription());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Association> read(){
        List<Association> ms = new ArrayList<>();
        String req = "SELECT * FROM association";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(req);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ms.add(resultSetToAssociation(resultSet));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ms;
    }

    private Association resultSetToAssociation(ResultSet r) throws SQLException {
        Association a = new Association();
        a.setId(r.getInt("id"));
        a.setNom(r.getString("nom"));
        a.setTelephone((r.getString("telephone")));
        return a;
    }

    public void update(Association m){ }
    public void delete(Association m){ }
    public Association searchByAssociation(){
        Association ms = new Association();
        return ms;
    }
    public Association searchByUser(){
        Association m = new Association();
        return m;
    }

    public Association rsToEntity(ResultSet rs){
        Association m = new Association();

        return m;
    }



    private AssociationService() { connection = utils.Utils.Connector.ConnectionUtil.conDB().conn; }
    public static AssociationService getInstace() {
        if(instance == null) { instance = new AssociationService(); }
        return instance;
    }
}
