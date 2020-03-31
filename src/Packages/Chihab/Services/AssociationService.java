package Packages.Chihab.Services;

import Main.Services.UserService;
import Packages.Chihab.Models.Association;
import SharedResources.Utils.Connector.ConnectionUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class AssociationService {
    private static AssociationService instance;
    private Connection connection;

    public int create(Association a, int categoryId, int managerId) throws SQLException {
        PreparedStatement st = connection.prepareStatement("INSERT INTO association " +
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
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                , Statement.RETURN_GENERATED_KEYS
        );
        st.setInt(1, a.getId());
        st.setInt(2, categoryId);
        st.setInt(3, managerId);
        st.setString(4, a.getNom());
        st.setInt(5, a.getTelephone());
        st.setString(6, a.getHoraireTravail());
        st.setString(7, a.getPhotoAgence());
        st.setString(8, a.getPieceJustificatif());
        st.setString(9, a.getRue());
        st.setInt(10, a.getCodePostal());
        st.setString(11, a.getVille());
        st.setDouble(12, a.getLat());
        st.setDouble(13, a.getLon());
        st.setBoolean(14, a.isApprouved());
        st.setString(15, a.getDescription());
        st.executeUpdate();
        ResultSet rs = st.getGeneratedKeys();
        if (rs.next())
            return rs.getInt(1);
        return -1;
    }

    public ObservableList<Association> readAll() throws SQLException {
        ObservableList<Association> ms = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM association");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Association a = resultSetToAssociation(resultSet);
            a.setDomaine(CategoryService.getInstace().readById(resultSet.getInt("domaine_id")));
            a.setManager(UserService.getInstace().readUserBy(resultSet.getInt("id_manager")));
            ms.add(a);
        }
        return ms;
    }

    public void update(Association m) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE association SET " +
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
                "WHERE id=?"
        );
        preparedStatement.setString(1, m.getNom());
        preparedStatement.setInt(2, m.getTelephone());
        preparedStatement.setString(3, m.getHoraireTravail());
        preparedStatement.setString(4, m.getPhotoAgence());
        preparedStatement.setString(5, m.getPieceJustificatif());
        preparedStatement.setString(6, m.getRue());
        preparedStatement.setInt(7, m.getCodePostal());
        preparedStatement.setString(8, m.getVille());
        preparedStatement.setDouble(9, m.getLat());
        preparedStatement.setDouble(10, m.getLon());
        preparedStatement.setBoolean(11, m.isApprouved());
        preparedStatement.setString(12, m.getDescription());
        preparedStatement.setInt(13, m.getId());
        preparedStatement.executeUpdate();
    }

    public void delete(Association m) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM association WHERE id=?");
        preparedStatement.setInt(1, m.getId());
        preparedStatement.executeUpdate();
    }

    public Association searchByAssociationId(int id) throws SQLException {
        Association a = new Association();
        String req = "SELECT * FROM association WHERE id=?";
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);
        a = resultSetToAssociation(preparedStatement.executeQuery());
        return a;
    }

    public Association searchByManagerId(int managerId) throws SQLException {
        Association a = new Association();
        String req = "SELECT * FROM association WHERE id_manager=?";
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, managerId);
        a = resultSetToAssociation(preparedStatement.executeQuery());
        return a;
    }

    public Association searchByDomaine(int idDomaine) throws SQLException {
        Association a = new Association();
        String req = "SELECT * FROM association WHERE domaine_id=?";
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, idDomaine);
        a = resultSetToAssociation(preparedStatement.executeQuery());
        return a;
    }

    private Association resultSetToAssociation(ResultSet r) throws SQLException {
        Association a = new Association();
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
        return a;
    }
    private AssociationService() {
        connection = ConnectionUtil.conDB().conn;
    }
    public static AssociationService getInstace() {
        if(instance == null) { instance = new AssociationService(); }
        return instance;
    }

    public Association readAssociationBy(int id) {
        try {
            PreparedStatement pt = connection.prepareStatement("SELECT * FROM association WHERE id = ?");
            pt.setInt(1, id);
            ResultSet rs = pt.executeQuery();
            if (rs.next()) {
                return resultSetToAssociation(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
