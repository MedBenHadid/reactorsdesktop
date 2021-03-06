package Packages.Mohamed.Services;

import Main.Entities.UserSession;
import Main.Services.UserService;
import Packages.Chihab.Models.Entities.Association;
import Packages.Chihab.Services.AssociationService;
import Packages.Mohamed.Entities.Mission;
import Packages.Mohamed.Entities.Notification;
import Packages.Mohamed.Entities.enums.EtatEnum;
import SharedResources.Utils.Connector.ConnectionUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationService {
    private final ObservableMap<Integer, Notification> records = FXCollections.observableHashMap();
    private final Connection connection = ConnectionUtil.getInstance().getConn();
    private final Logger logger = Logger.getLogger(NotificationService.class.getName());
    private static NotificationService instance;
    public ObservableMap<Integer, Notification> getDatabase() {
        return database;
    }

    private final ObservableMap<Integer,Notification> database = FXCollections.observableHashMap();

    //TODO : Sync dataBase
    private void init() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM notification WHERE id_user = ? AND seen = 0");
        preparedStatement.setInt(1, UserSession.getInstace().getUser().getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Notification notif = new Notification(resultSet);
            int ida,idm,idu;
            ida = resultSet.getInt("id_association");
            idm = resultSet.getInt("id_mission");
            idu = resultSet.getInt("id_user");
            notif.setId_association(AssociationService.getInstance().getAssociationById(ida));
            notif.setId_mission(MissionService.getInstace().searchByMissionId(idm));
            notif.setId_user(UserService.getInstace().readUserBy(idu));
            records.put(resultSet.getInt("id"),notif);
        }
    }

    public Runnable updateCheckRunnable(){
        return () -> {
            ArrayList<Integer> toRemoveList = new ArrayList<>();
            HashMap<Integer,Notification> toUpdate = new HashMap<>();
            Runnable r;
            try {
                System.out.println("Running");
                PreparedStatement readAllPreparedStatement = connection.prepareStatement("SELECT * FROM notification");
                ResultSet fetchedRes = readAllPreparedStatement.executeQuery();
                while (fetchedRes.next()){
                    int fetchedId = fetchedRes.getInt("id");
                    if(!records.containsKey(fetchedId)){
                        records.put(fetchedId,new Notification(fetchedRes) );
                        System.out.println("Added");
                    }
                }
                for(int i: records.keySet()){
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM notification WHERE id=?");
                    preparedStatement.setInt(1,i);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if(!resultSet.next()){
                        toRemoveList.add(i);
                        //records.remove(i);
                    }else {
                        Notification toTest = new Notification(resultSet);
                        if(!records.get(i).equals(toTest)){
                            toUpdate.put(i,toTest);
                            //records.put(i,toTest);
                        }
                    }
                }
            } catch (SQLException throwables) {
                logger.log(Level.SEVERE,"Error while checking for updates", throwables);
            }
            for(int i : toUpdate.keySet()){
                records.remove(i);
                records.put(toUpdate.get(i).getId(),toUpdate.get(i));
            }
            for(int i : toRemoveList){
                System.out.println("Removed");
                records.remove(i);
            }
        };
    }

    public boolean AcceptMissionNotification(Notification n) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE invitation SET etat=? WHERE id_mission=? AND id_user=? AND id_notification=?");
        preparedStatement.setString(1, String.valueOf(EtatEnum.accepter));
        preparedStatement.setInt(2, n.getId_mission().getId());
        preparedStatement.setInt(3, UserSession.getInstace().getUser().getId());
        preparedStatement.setInt(4, n.getId());
        PreparedStatement ps = connection.prepareStatement("UPDATE notification SET seen=1 WHERE id_mission=? AND id_user=?");
        ps.setInt(1, n.getId_mission().getId());
        ps.setInt(2, UserSession.getInstace().getUser().getId());
        ps.executeUpdate();
        return preparedStatement.executeUpdate() > 0;
    }
    public boolean RefuserMissionNotification(Notification n) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE invitation SET etat=? WHERE id_mission=? AND id_user=? AND id_notification=?");
        preparedStatement.setString(1, String.valueOf(EtatEnum.r??fuser));
        preparedStatement.setInt(2, n.getId_mission().getId());
        preparedStatement.setInt(3, UserSession.getInstace().getUser().getId());
        preparedStatement.setInt(4, n.getId());
        PreparedStatement ps = connection.prepareStatement("UPDATE notification SET seen=1 WHERE id_mission=? AND id_user=?");
        ps.setInt(1, n.getId_mission().getId());
        ps.setInt(2, UserSession.getInstace().getUser().getId());
        ps.executeUpdate();
        return preparedStatement.executeUpdate() > 0;
    }

    public ObservableMap<Integer, Notification> getRecords() {
        return records;
    }

    private NotificationService() {
        try {
            init();
        } catch (SQLException throwables) {
            logger.log(Level.SEVERE,"Error while inniting", throwables);
        }
    }

    public static NotificationService getInstance() {
        if(instance == null) { instance = new NotificationService(); }
        return instance;
    }

}
