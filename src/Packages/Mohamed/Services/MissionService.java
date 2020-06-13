package Packages.Mohamed.Services;

import Main.Entities.User;
import Main.Entities.UserSession;
import Main.Services.UserService;
import Packages.Chihab.Models.Entities.Association;
import Packages.Chihab.Services.CategoryService;
import Packages.Mohamed.Entities.Invitation;
import Packages.Mohamed.Entities.Mission;
import Packages.Mohamed.Entities.enums.EtatEnum;
import Packages.Mohamed.util.sendMail;
import SharedResources.Utils.Connector.ConnectionUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MissionService {
    private static MissionService instance;
    private final Connection connection = ConnectionUtil.getInstance().getConn();

    public ObservableMap<Integer, Mission> getDatabase() {
        return database;
    }

    private final ObservableMap<Integer,Mission> database = FXCollections.observableHashMap();
    private MissionService() {
        try {
            readAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static MissionService getInstace() {
        if (instance == null) {
            instance = new MissionService();
        }
        return instance;
    }

    public void create(Mission m, List<User> checkedList) throws Exception {
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
                        "`latitude`, " +
                        "`longitude`, " +
                        "`ups`, " +
                        "`CreatedBy`) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"
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
        st.setDate(9, m.getDateFin());
        st.setDouble(10, m.getLat());
        st.setDouble(11, m.getLon());
        st.setInt(12, 0);
        st.setInt(13, UserSession.getInstace().getUser().getId());
        st.executeUpdate();
        ResultSet rs = st.getGeneratedKeys();
        if (rs.next()) {
            m.setId(rs.getInt(1));
            database.put(m.getId(),m);
        }
        //sendMailToMembers
        for (int i = 0; i < checkedList.size(); i++) {
            sendMail.sendMail(checkedList.get(i).getEmail(), m);
            PreparedStatement sti = connection.prepareStatement("INSERT INTO invitation " +
                            "(`id_mission`, " +
                            "`id_user`)" +
                            "VALUES (?,?)"
                    , Statement.RETURN_GENERATED_KEYS
            );
            System.out.println("------------------");
            Invitation invitation = new Invitation();
            String inviter = "inviter";
            invitation.setEtat(EtatEnum.inviter);
            invitation.setId_mission(m);
            // invitation.setId_notification(1);
            invitation.setId_user(checkedList.get(i));
            sti.setInt(1, rs.getInt(1));
            sti.setInt(2, invitation.getId_user().getId());
            // sti.setString(4,invitation.getEtat().toString());
            sti.executeUpdate();
            // System.out.println(sti);

        }
    }

    public void readAll() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM mission");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Mission m = resultSetToMission(resultSet);
            m.setDomaine(CategoryService.getInstace().readById(resultSet.getInt("domaine_id")));
            m.setCretedBy(UserService.getInstace().readUserBy(resultSet.getInt("CreatedBy")));
            database.put(m.getId(),m);
        }
    }

    public ObservableList<User> readMissionUsers(Mission m) throws SQLException {
        ObservableList<User> ms = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM invitation WHERE id_mission=?");
        preparedStatement.setInt(1, m.getId());
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {

            Mission u = resultSetToMission(resultSet);

            System.out.println(resultSet.getInt("id_user"));

       //     ms.add(u);
        }
        return ms;
    }

    public ArrayList<Invitation> getInvitationsByMission(Mission m) throws SQLException {
        ArrayList<Invitation> is = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM invitation WHERE id_mission=? ");
        preparedStatement.setInt(1, m.getId());
        ResultSet r = preparedStatement.executeQuery();
        while (r.next()) {
            Invitation i = resultSetToInvitation(r);
            i.setId_mission(m);
            is.add(i);
        }
        return is;
    }


    public ArrayList<Invitation> memberInMission(Mission m) throws SQLException {
        ArrayList<Invitation> is = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM invitation WHERE id_mission=? ");
        preparedStatement.setInt(1, m.getId());
        ResultSet r = preparedStatement.executeQuery();
        while (r.next()) {
            Invitation i = resultSetToInvitation(r);
            i.setId_mission(m);
            is.add(i);
        }
        return is;
    }
    private Invitation resultSetToInvitation(ResultSet r) throws SQLException {
        Invitation i = new Invitation();
        i.setId(r.getInt("id"));
        i.setId_user(UserService.getInstace().readUserBy(r.getInt("id_user")));
        i.setId_notification(r.getInt("id_notification"));
        return i;
    }

    public boolean update(Mission m) throws SQLException {
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
        System.out.println("__" + m.getLat());
        st.setString(1, m.getTitleMission());
        st.setString(2, m.getPicture());
        st.setString(3, m.getDescription());
        st.setString(4, m.getLocation());
        st.setDouble(5, m.getSumCollected());
        st.setDouble(6, m.getObjectif());
        st.setDate(7, m.getDateCreation());
        st.setDate(8, m.getDateFin());
        st.setInt(9, m.getUps());
        st.setDouble(10, m.getLat());
        st.setDouble(11, m.getLon());
        st.setInt(12, m.getCretedBy().getId());
        st.setInt(13, m.getDomaine().getId());
        st.setInt(14, m.getId());

        if(st.executeUpdate() > 0) {
            database.remove(m.getId());
            database.put(m.getId(),m);
            return true;
        }else {
            return false;
        }
    }

    public boolean delete(Mission m) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Mission WHERE id=?");
        preparedStatement.setInt(1, m.getId());
        if(preparedStatement.executeUpdate() > 0){
            database.remove(m.getId());
            return true;
        } else {
            return false;
        }
    }

    public Mission searchByMissionId(int id){
        return database.get(id);
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

    public int serchAssociationByid_manager(int m_id) throws SQLException {
        Association a = new Association();
        String req = "SELECT * FROM association WHERE id_manager=?";
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, m_id);

        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()){
            return new Association(rs).getId();
        }
        return a.getId();
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
