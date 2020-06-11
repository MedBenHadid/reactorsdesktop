package Packages.Chihab.Services;

import Main.Services.UserService;
import Packages.Chihab.Models.Entities.Membership;
import Packages.Chihab.Models.enums.AccessType;
import SharedResources.Utils.Connector.ConnectionUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class MembershipService {
    //TODO : Search by associatio ID, Search by user ID
    public int create(Membership m) throws SQLException {
        PreparedStatement st = connection.prepareStatement("INSERT INTO adherance " + "(`id`,`user_id`,`association_id`,`joined`,`fonction`,`description`,`role`,`status`) " +
                        "VALUES (?,?,?,?,?,?,?,?)"
                , Statement.RETURN_GENERATED_KEYS
        );
        st.setInt(1, m.getId());
        st.setInt(2, m.getMember().getId());
        st.setDate(4, m.getJoinDate());
        st.setString(5, m.getFonction());
        st.setString(6, m.getDescription());
        st.setInt(7, AccessType.getRole(m.getAccess()));
        st.setString(8,m.getStatus());
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

    public void update(Membership m, int idAss) throws SQLException {
        String req = "UPDATE adherance SET user_id=?,association_id=?,joined=?,fonction=?,description=?,role=?,status=? WHERE id=?";
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, m.getMember().getId());
        preparedStatement.setInt(2, idAss);
        preparedStatement.setDate(3, m.getJoinDate());
        preparedStatement.setString(4, m.getFonction());
        preparedStatement.setString(5, m.getDescription());
        preparedStatement.setInt(6, AccessType.getRole(m.getAccess()));
        preparedStatement.setInt(7, m.getId());
        preparedStatement.setString(8, m.getStatus());
        preparedStatement.executeUpdate();
    }

    public void delete(Membership m) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM adherance WHERE id=?");
        preparedStatement.setInt(1, m.getId());
        preparedStatement.executeUpdate();
    }

    public void delete(int mID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM adherance WHERE id=?");
        preparedStatement.setInt(1, mID);
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

    public int[] searchAffiliateIdByMemberId(int memberID) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT association_id FROM adherance WHERE user_id=?");
        ps.setInt(1, memberID);
        ResultSet rs = ps.executeQuery();
        int[] intArray = new int[rs.getFetchSize()];
        int i = 0;
        while (rs.next()) {
            intArray[i] = rs.getInt(1);
            i++;
        }
        return intArray;
    }

    public void deleteAllAssociationMemberShips(int assId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT id FROM adherance WHERE association_id=?");
        ps.setInt(1, assId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            delete(rs.getInt("id"));
        }
    }

    public ObservableList<Membership> searchAffiliateIdByMemberIdReturnArray(int memberID) throws SQLException {
        ObservableList<Membership> ms = FXCollections.observableArrayList();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM adherance WHERE user_id=?");
        ps.setInt(1, memberID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ms.add(rsToMembership(rs));
        }
        return ms;
    }

    public Membership rsToMembership(ResultSet rs) throws SQLException {
        Membership m = new Membership();
        m.setId(rs.getInt("id"));
        m.setFonction(rs.getString("fonction"));
        m.setDescription(rs.getString("description"));
        m.setJoinDate(rs.getDate("joined"));
        m.setAccess(rs.getInt("role"));
        m.setStatus(rs.getString("status"));
        m.setMember(UserService.getInstace().readUserBy(rs.getInt("user_id")));
        return m;
    }

    private static MembershipService instance;
    private final Connection connection = ConnectionUtil.getInstance().getConn();

    public static MembershipService getInstace() {
        if(instance == null) { instance = new MembershipService(); }
        return instance;
    }
}
