package Packages.Chihab.Services;

import Packages.Chihab.Models.Entities.Association;
import SharedResources.Utils.Connector.ConnectionUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AssociationService {
    private final ObservableMap<Integer,Association> records = FXCollections.observableHashMap();
    private final Connection connection = ConnectionUtil.getInstance().getConn();
    private final Logger logger = Logger.getLogger(AssociationService.class.getName());
    private static AssociationService instance;

    private void init() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM association");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            records.put(resultSet.getInt("id"),new Association(resultSet));
        }
    }

    public boolean add(Association a) throws SQLException {
        PreparedStatement st = connection.prepareStatement("INSERT INTO association (`id`,`domaine_id`,`id_manager`,`nom`,`telephone`,`horaire_travail`,`photo_agence`,`piece_justificatif`,`rue`,`code_postal`,`ville`,`latitude`,`longitude`,`approuved`,`description`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        st.setNull(1, 1);
        st.setInt(2, a.getDomaine().getId());
        st.setInt(3, a.getManager().getId());
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
        if (rs.next()){
            a.setId(rs.getInt(1));
            records.put(a.getId(),a);
            return true;
        }
        return false;
    }
    public boolean update(Association m) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE association SET nom=?,telephone=?,horaire_travail=?,photo_agence=?,piece_justificatif=?,rue=?,code_postal=?,ville=?,latitude=?,longitude=?,approuved=?,description=? WHERE id=?");
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
        if(preparedStatement.executeUpdate() > 0) {
            records.remove(m.getId());
            records.put(m.getId(),m);
            return true;
        }else {
            return false;
        }
    }
    public Task<Void> updateWizard(){
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                System.out.println("Running");
                updateTitle("Running");
                updateMessage("Initializing");
                updateProgress(0.0,1.0);
                ArrayList<Integer> toRemoveList = new ArrayList<>();
                HashMap<Integer,Association> toUpdate = new HashMap<>();
                updateMessage("Querying database");
                updateProgress(0.2,1.0);
                PreparedStatement readAllPreparedStatement = connection.prepareStatement("SELECT * FROM association");
                ResultSet fetchedRes = readAllPreparedStatement.executeQuery();
                updateMessage("Checking for new records");
                updateProgress(0.4,1.0);
                while (fetchedRes.next()){
                    int fetchedId = fetchedRes.getInt("id");
                    if(!records.containsKey(fetchedId)){
                        records.put(fetchedId,new Association(fetchedRes) );
                        System.out.println("Added");
                    }
                }
                updateMessage("Checking for removed records");
                updateProgress(0.6,1.0);
                for(int i: records.keySet()){
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM association WHERE id=?");
                    preparedStatement.setInt(1,i);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if(!resultSet.next()){
                        toRemoveList.add(i);
                    }else {
                        Association toTest = new Association(resultSet);
                        if(!records.get(i).getNom().equals(toTest.getNom())){
                            System.out.println("new name :"+toTest.getNom());
                            toUpdate.put(i,toTest);
                            //records.put(i,toTest);
                        }
                    }
                }
                updateProgress(0.8,1.0);
                for(int i : toUpdate.keySet()){
                    updateMessage("removing");
                    records.remove(i);
                    records.put(toUpdate.get(i).getId(),toUpdate.get(i));
                }
                updateProgress(0.9,1.0);
                for(int i : toRemoveList){
                    updateMessage("updating");
                    System.out.println("Removed");
                    records.remove(i);
                }
                updateProgress(1.0,1.0);
                updateMessage("Synchronized with database");
                done();
                return null;
            }
        };
    }

    public Runnable updateCheckRunnable(){
        return () -> {
            ArrayList<Integer> toRemoveList = new ArrayList<>();
            HashMap<Integer,Association> toUpdate = new HashMap<>();
            Runnable r;
            try {
                System.out.println("Running");
                PreparedStatement readAllPreparedStatement = connection.prepareStatement("SELECT * FROM association");
                ResultSet fetchedRes = readAllPreparedStatement.executeQuery();
                while (fetchedRes.next()){
                    int fetchedId = fetchedRes.getInt("id");
                    if(!records.containsKey(fetchedId)){
                        records.put(fetchedId,new Association(fetchedRes) );
                        System.out.println("Added");
                    }
                }
                for(int i: records.keySet()){
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM association WHERE id=?");
                    preparedStatement.setInt(1,i);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if(!resultSet.next()){
                        toRemoveList.add(i);
                        //records.remove(i);
                    }else {
                        Association toTest = new Association(resultSet);
                        if(!records.get(i).getNom().equals(toTest.getNom())){
                            System.out.println("new name :"+toTest.getNom());
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

    public boolean delete(Association toBeDeleted){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM association WHERE id=?");
            preparedStatement.setInt(1, toBeDeleted.getId());
            if(preparedStatement.executeUpdate() > 0){
                records.remove(toBeDeleted.getId());
                return true;
            } else {
                return false;
            }
        } catch (SQLException throwables) {
            logger.log(Level.SEVERE, "Error whilst deleting", throwables.getLocalizedMessage());
            return false;
        }
    }

    public ObservableMap<Integer, Association> getRecords() {
        return records;
    }

    private AssociationService() {
        try {
            init();
        } catch (SQLException throwables) {
            logger.log(Level.SEVERE,"Error while inniting", throwables);
        }
    }

    public static AssociationService getInstance() {
        if(instance == null) { instance = new AssociationService(); }
        return instance;
    }

}
