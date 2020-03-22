package Packages.Chihab.Services;

import Main.Services.UserService;
import Packages.Chihab.Models.Association;
import SharedResources.Utils.Connector.ConnectionUtil;

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
            st.setInt(5,a.getTelephone());
            st.setString(6,a.getHoraireTravail());
            st.setString(7,a.getPhotoAgence());
            st.setString(8,a.getPieceJustificatif());
            st.setString(9,a.getRue());
            st.setInt(10,a.getCodePostal());
            st.setString(11,a.getVille());
            st.setDouble(12,a.getLat());
            st.setDouble(13,a.getLon());
            st.setBoolean(14,a.isApprouved());
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
        if(r.next()) {
            a.setDomaine(CategoryService.getInstace().readById(r.getInt("domaine_id")));
            a.setManager(UserService.getInstace().readUserBy(r.getInt("id_manager")));
            a.setId(r.getInt("id"));
            a.setNom(r.getString("nom"));
            a.setTelephone(r.getInt("telephone"));
            a.setHoraireTravail(r.getString("horaire_travail"));
            a.setPhotoAgence(r.getString("photo_agence"));
            a.setPieceJustificatif(r.getString("piece_justificatif"));
            a.setRue(r.getString("rue"));
            a.setCodePostal(r.getInt("code_postal"));
            a.setVille(r.getString("ville"));
            a.setLat(r.getDouble("latitude"));
            a.setLon(r.getDouble("longitude"));
            a.setApprouved(r.getBoolean("approuved"));
            a.setDescription(r.getString("description"));
        }
        return a;
    }

    public void update(Association m){
        String req =
                "UPDATE association SET " +
                        "nom=?" +
                        ",telephone=?" +
                        ",horaire_travail=?" +
                        ",photo_agence=?" +
                        ",piece_justificatif=?" +
                        ",rue=?" +
                        ",code_postal=?" +
                        ",ville=?" +
                        ",latitude=?" +
                        ",longitude=?" +
                        ",approuved=?" +
                        ",description=?" +
                        "WHERE id=?";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, m.getNom());
            preparedStatement.setInt(2,m.getTelephone());
            preparedStatement.setString(3, m.getHoraireTravail());
            preparedStatement.setString(4, m.getPhotoAgence());
            preparedStatement.setString(5, m.getPieceJustificatif());
            preparedStatement.setString(6, m.getRue());
            preparedStatement.setInt(7, m.getCodePostal());
            preparedStatement.setString(8, m.getVille());
            preparedStatement.setDouble(9,m.getLat());
            preparedStatement.setDouble(10,m.getLon());
            preparedStatement.setBoolean(11,m.isApprouved());
            preparedStatement.setString(12, m.getDescription());
            preparedStatement.setInt(13, m.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void delete(Association m){
        String req = "DELETE FROM association WHERE id=?";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(req);
            preparedStatement.setInt(1, m.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Association searchByAssociationId(int id){
        Association a = new Association();
        String req = "SELECT * FROM association WHERE id=?";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(req);
            preparedStatement.setInt(1, id);
            a = resultSetToAssociation(preparedStatement.executeQuery());
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return a;
    }

    public Association searchByManagerId(int managerId){
        Association a = new Association();
        String req = "SELECT * FROM association WHERE id_manager=?";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(req);
            preparedStatement.setInt(1, managerId);
            a = resultSetToAssociation(preparedStatement.executeQuery());
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return a;
    }

    public Association searchByDomaine(int idDomaine){
        Association a = new Association();
        String req = "SELECT * FROM association WHERE domaine_id=?";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(req);
            preparedStatement.setInt(1, idDomaine);
            a = resultSetToAssociation(preparedStatement.executeQuery());
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return a;
    }

    public ArrayList<Association> searchByNom(String nom){
        ArrayList<Association> associations = new ArrayList<>();
        String req = "SELECT * FROM category WHERE name LIKE '%"+nom+"%' ";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(req);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                associations.add(resultSetToAssociation(resultSet));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return associations;
    }

    public Association searchStatus(){
        Association m = new Association();
        return m;
    }


    // TODO
    public Association readById(){
        Association m = new Association();
        return m;
    }

    private AssociationService() {
        connection = ConnectionUtil.conDB().conn;
    }
    public static AssociationService getInstace() {
        if(instance == null) { instance = new AssociationService(); }
        return instance;
    }
}
