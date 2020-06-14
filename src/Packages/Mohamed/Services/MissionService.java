package Packages.Mohamed.Services;

import Main.Entities.User;
import Main.Entities.UserSession;
import Main.Services.UserService;
import Packages.Chihab.Models.Entities.Association;
import Packages.Chihab.Services.AssociationService;
import Packages.Chihab.Services.CategoryService;
import Packages.Mohamed.Entities.Invitation;
import Packages.Mohamed.Entities.Mission;
import Packages.Mohamed.Entities.Notification;
import Packages.Mohamed.Entities.enums.EtatEnum;
import Packages.Mohamed.util.sendMail;
import SharedResources.Utils.Connector.ConnectionUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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

            System.out.println("--------notification----------");


            Notification notification=new Notification();
            notification.setId_user(checkedList.get(i));
            notification.setId_mission(m);
           //notification.getId_association(AssociationService.getInstance().getAssociationById(UserSession.getInstace().getManagedAss().get().)); TODO : Add association id
            notification.setTitle(m.getTitleMission());
            notification.setDescription(m.getDescription());
            notification.setNotification_date(new java.sql.Date(new java.util.Date().getTime()));
            notification.setSeen(false);
            PreparedStatement stn = connection.prepareStatement("INSERT INTO notification " +
                            "(`id_mission`, " +
                            "`id_user`, " +
                            "`id_association`, " +
                            "`title`, " +
                            "`description`, " +
                            "`notification_date`, " +
                            "`seen`)" +
                            "VALUES (?,?,?,?,?,?,?)"
                    , Statement.RETURN_GENERATED_KEYS
            );
            stn.setInt(1, notification.getId_mission().getId());
            stn.setInt(2,notification.getId_user().getId());
            stn.setInt(3, 20); // ass ID
            stn.setString(4, notification.getTitle());
            stn.setString(5, notification.getDescription());
            stn.setDate(6, (java.sql.Date) notification.getNotification_date());
            stn.setBoolean(7, notification.getSeen());

            stn.executeUpdate();
            ResultSet rtn = stn.getGeneratedKeys();
            if (rtn.next()) {
                notification.setId(rtn.getInt(1));
            }

            System.out.println("--------invitation----------");



            Invitation invitation = new Invitation();
            invitation.setEtat(EtatEnum.inviter);
            invitation.setId_mission(m);
             invitation.setId_notification(rtn.getInt(1));
            invitation.setId_user(checkedList.get(i));
            PreparedStatement sti = connection.prepareStatement("INSERT INTO invitation " +
                            "(`id_mission`, " +
                            "`id_user`," +
                            "`etat`," +
                            "`id_notification`)" +
                            "VALUES (?,?,?,?)"
                    , Statement.RETURN_GENERATED_KEYS
            );
            sti.setInt(1,  invitation.getId_mission().getId());
            sti.setInt(2, invitation.getId_user().getId());
            sti.setString(3,invitation.getEtat().toString());
            sti.setInt(4,invitation.getId_notification());
            // sti.setString(4,invitation.getEtat().toString());
            sti.executeUpdate();
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
        if (preparedStatement.executeUpdate() > 0) {
            database.remove(m.getId());
            return true;
        } else {
            return false;
        }
    }




    /**
     * @param newMembersList description : List of members to check with database
     * @param m              description Mission to invite members to
     */
    public void inviteMembers(HashSet<User> newMembersList, Mission m) {
        System.out.println("newMembersList : "+newMembersList);
        ArrayList<Integer> newMembersID = newMembersList.stream().map(User::getId).collect(Collectors.toCollection(ArrayList::new));
        System.out.println("newMembersID : "+newMembersID);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id_user FROM invitation WHERE id_mission=?");
            preparedStatement.setInt(1, m.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Integer> invitedUsers = new ArrayList<>();
            while (resultSet.next()) {
                invitedUsers.add(resultSet.getInt("id_user"));
            }
           // System.out.println("Already invited user : " + invitedUsers);
    //    newMembersID.removeAll(invitedUsers);
      //      System.out.println("ToBeDeleted : "+newMembersID);

          //  System.out.println("Members to email and notify : " + newMembersID);
            for (User member : newMembersList) {
                if (!invitedUsers.contains(member.getId())) {
                    PreparedStatement sti = connection.prepareStatement("INSERT INTO invitation (`id_mission`,`id_user` , `etat`) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
                    sti.setInt(1, m.getId());
                    sti.setInt(2, member.getId());
                    sti.setString(3, EtatEnum.inviter.toString());
                    sti.executeUpdate();
                    sendMail.sendMail(member.getEmail(), m);
                    System.out.println("Emailing " + member.getEmail());

                    //To do : Notif percisT
                }
            }
       //      invitedUsers.removeAll(newMembersID);
           // System.out.println(invitedUsers);
          /*  for (int toRemove : invitedUsers) {

                PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM invitation WHERE id_user=? AND id_mission=?");
                deleteStatement.setInt(1, toRemove);
                deleteStatement.setInt(2, m.getId());
                deleteStatement.executeUpdate();
                System.out.println("removed :" + toRemove);
            }*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Mission searchByMissionId(int id) {
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
