package Packages.Mohamed.Services;

import Main.Entities.User;
import Main.Services.UserService;
import Packages.Chihab.Services.CategoryService;
import Packages.Mohamed.Entities.Mission;
import Packages.Mohamed.util.ComboBoxItemWrap;
import SharedResources.Utils.Connector.ConnectionUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.List;

public class MissionService {
    private static MissionService instance;
    private Connection connection;

    public void create(Mission m, List<ComboBoxItemWrap<User>> checkedList) throws SQLException {
        PreparedStatement st = connection.prepareStatement("INSERT INTO mission " +
                "(`domaine_id`, " +
                "`TitleMission`, " +
                "`picture`, " +
                "`description`, " +
                "`location`, " +
                "`sumCollected`, " +
                "`objectif`, " +
                "`DateCreation`, " +
                "`DateFin`, " +
                "`ups`, " +
                "`CreatedBy`) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?)"
                , Statement.RETURN_GENERATED_KEYS
        );
       // System.out.println(m.getDateCreation());
        st.setInt(1, 76);
        st.setString(2, m.getTitleMission());
        st.setString(3, m.getPicture());
        st.setString(4, m.getDescription());
        st.setString(5, m.getLocation());
        st.setDouble(6, 0);
        st.setDouble(7, m.getObjectif());
        st.setDate(8, m.getDateCreation());
        st.setDate(9,m.getDateFin());
        st.setInt(10, 0);
        System.out.println(checkedList);

     //   st.setDouble(12, m.getLat());
     //   st.setDouble(13, m.getLon());
        st.setInt(11, 75);

        st.executeUpdate();
        ResultSet rs = st.getGeneratedKeys();
        if (rs.next()) {
            rs.getInt(1);
        }
    }

    public ObservableList<Mission> readAll() throws SQLException {
        ObservableList<Mission> ms = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM mission");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Mission m = resultSetToMission(resultSet);
            m.setDomaine(CategoryService.getInstace().readById(resultSet.getInt("domaine_id")));
            m.setCretedBy(UserService.getInstace().readUserBy(resultSet.getInt("CreatedBy")));
            ms.add(m);
        }
        return ms;
    }

    public void update(Mission m) throws SQLException {
        PreparedStatement st = connection.prepareStatement("UPDATE mission SET " +
                "TitleMission=?" +
                ",picture=?" +
                ",description=?" +
                ",location=?" +
                ",sumCollected=?" +
                ",objectif=?" +
                ",DateCreation=?" +
                ",DateFin=?" +
                ",ups=?" +
                ",latitude=?" +
                ",longitude=?" +
                ",CreatedBy=?" +
                ",domaine_id=? " +
                "WHERE id=?"
        );
        System.out.println(m.getId()+"--"+m.getTitleMission()+"--"+m.getPicture()+"--"+m.getDescription()+"--"+m.getLocation()+"--"+m.getSumCollected()+"--"+m.getObjectif()+"--"+m.getDateCreation()+"--"+m.getDateFin()+"--");
        st.setString(1, m.getTitleMission());
        st.setString(2, m.getPicture());
        st.setString(3, m.getDescription());
        st.setString(4, m.getLocation());
        st.setDouble(5, m.getSumCollected());
        st.setDouble(6, m.getObjectif());
        st.setDate(7, (Date) m.getDateCreation());
        st.setDate(8, (Date) m.getDateFin());
        st.setInt(9, m.getUps());
        st.setDouble(10, m.getLat());
        st.setDouble(11, m.getLon());
        //st.setInt(12, m.getCretedBy().getId());
        st.setInt(13, m.getDomaine().getId());
        st.setInt(14, m.getId());
        System.out.println(st);
        st.executeUpdate();
    }

    public void delete(Mission m) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Mission WHERE id=?");
        preparedStatement.setInt(1, m.getId());
        preparedStatement.executeUpdate();
    }

    public Mission searchByMissionId(int id) throws SQLException {
        Mission m = new Mission();
        String req = "SELECT * FROM mission WHERE id=?";
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);
        m = resultSetToMission(preparedStatement.executeQuery());
        return m;
    }

    public Mission searchByManagerId(int managerId) throws SQLException {
        Mission m = new Mission();
        String req = "SELECT * FROM mission WHERE CreatedBy=?";
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, managerId);
        m = resultSetToMission(preparedStatement.executeQuery());
        return m;
    }
    public int searchByManagerName(String manager) throws SQLException {
        User u = new User();
        String req = "SELECT id FROM user WHERE username=?";
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, manager);
        u = (User) preparedStatement.executeQuery();
        return u.getId();
    }

    public Mission searchByDomaine(int idDomaine) throws SQLException {
        Mission m = new Mission();
        String req = "SELECT * FROM mission WHERE domaine_id=?";
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, idDomaine);
        m = resultSetToMission(preparedStatement.executeQuery());
        return m;
    }

    private Mission resultSetToMission(ResultSet r) throws SQLException {
        Mission m = new Mission();
        m.setId(r.getInt("id"));
        m.setTitleMission(r.getString("TitleMission"));
        m.setPicture(r.getString("picture"));
        m.setDescription(r.getString("description"));
        m.setLocation(r.getString("location"));
       m.setSumCollected(r.getDouble("sumCollected"));
       m.setObjectif(r.getDouble("objectif"));
        m.setDateCreation(r.getDate("DateCreation"));
        m.setDateFin(r.getDate("DateFin"));
        m.setUps(r.getInt("ups"));
        m.setLat(r.getDouble("latitude"));
        m.setLon(r.getDouble("longitude"));
        return m;
    }
    private MissionService() {
        connection = ConnectionUtil.conDB().conn;
    }

    public static MissionService getInstace() {
        if(instance == null) { instance = new MissionService(); }
        return instance;
    }

    public Mission readMissionBy(int id) {
        try {
            PreparedStatement pt = connection.prepareStatement("SELECT * FROM mission WHERE id = ?");
            pt.setInt(1, id);
            ResultSet rs = pt.executeQuery();
            if (rs.next()) {
                return resultSetToMission(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
