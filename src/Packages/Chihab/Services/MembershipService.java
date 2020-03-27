package Packages.Chihab.Services;

import Main.Entities.User;
import Main.Services.UserService;
import Packages.Chihab.Models.Membership;
import Packages.Chihab.Models.enums.AccessType;
import SharedResources.Utils.Connector.ConnectionUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class MembershipService {
    //TODO : Search by associatio ID, Search by user ID
    public int create(Membership m, int userId, int associationId) throws SQLException {
        PreparedStatement st = connection.prepareStatement("INSERT INTO adherance " +
                        "(`id`, " +
                        "`user_id`, " +
                        "`association_id`, " +
                        "`joined`, " +
                        "`fonction`, " +
                        "`description`, " +
                        "`role`) " +
                        "VALUES (?,?,?,?,?,?,?)"
                , Statement.RETURN_GENERATED_KEYS
        );
        st.setInt(1, m.getId());
        st.setInt(2, userId);
        st.setInt(3, associationId);
        st.setDate(4, m.getJoinDate());
        st.setString(5, m.getFonction());
        st.setString(6, m.getDescription());
        st.setInt(7, AccessType.getRole(m.getAccess()));
        st.executeUpdate();
        ResultSet rs = st.getGeneratedKeys();
        if (rs.next())
            return rs.getInt(1);
        return 0;
    }

    public ObservableList<Membership> readAll() throws SQLException {
        ObservableList<Membership> ms = FXCollections.observableArrayList();
        ResultSet rs = connection.prepareStatement("SELECT * FROM adherance").executeQuery();
        while (rs.next()) {
            ms.add(rsToMembership(rs));
        }
        return ms;
    }

    public void update(Membership m) throws SQLException {
        String req = "UPDATE adherance SET user_id=?,association_id=?,joined=?,fonction=?,description=?,role=? WHERE id=?";
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, m.getMember().getId());
        preparedStatement.setInt(2, m.getAssociation().getId());
        preparedStatement.setDate(3, m.getJoinDate());
        preparedStatement.setString(4, m.getFonction());
        preparedStatement.setString(5, m.getDescription());
        preparedStatement.setInt(6, AccessType.getRole(m.getAccess()));
        preparedStatement.setInt(7, m.getId());
        preparedStatement.executeUpdate();
    }

    public void delete(Membership m) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM adherance WHERE id=?");
        preparedStatement.setInt(1, m.getId());
        preparedStatement.executeUpdate();
    }

    public ObservableList<Membership> searchByAssociationId(int id) throws SQLException {
        ObservableList<Membership> ms = FXCollections.observableArrayList();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM adherance WHERE association_id=?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ms.add(rsToMembership(rs));
        }
        return ms;
    }

    public Membership searchByMember(User member) {
        Membership m = new Membership();
        return m;
    }

    public Membership rsToMembership(ResultSet rs) throws SQLException {
        Membership m = new Membership();
        m.setId(rs.getInt("id"));
        m.setFonction(rs.getString("fonction"));
        m.setDescription(rs.getString("description"));
        m.setJoinDate(rs.getDate("joined"));
        m.setAccess(rs.getInt("role"));
        m.setMember(UserService.getInstace().readUserBy(rs.getInt("user_id")));
        m.setAssociation(AssociationService.getInstace().readAssociationBy(rs.getInt("association_id")));
        return m;
    }

    private static MembershipService instance;
    private Connection connection;

    private MembershipService() {
        connection = ConnectionUtil.conDB().conn;
    }
    public static MembershipService getInstace() {
        if(instance == null) { instance = new MembershipService(); }
        return instance;
    }
}
