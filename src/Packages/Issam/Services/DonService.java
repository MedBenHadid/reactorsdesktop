package Packages.Issam.Services;

import Main.Entities.UserSession;
import Packages.Chihab.Services.CategoryService;
import Packages.Issam.Models.Don;
import SharedResources.Utils.Connector.ConnectionUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.sql.*;

public class DonService {
    private static DonService instance;
    private final Connection connection = ConnectionUtil.getInstance().getConn();

    public ObservableMap<Integer, Don> getDb() {
        return db;
    }

    private final ObservableMap<Integer,Don> db = FXCollections.observableHashMap();


    public static DonService getInstace() {
        if (instance == null) {
            instance = new DonService();
            instance.readAll();
        }
        return instance;
    }
    public void readAll() {
        try{
            String req = "SELECT * FROM don";
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(req);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Don don = resultSetToDon(resultSet);
                db.put(don.getId(),don);
            }
        } catch (SQLException e){
            System.out.println(e);
        }

    }

    public boolean create(Don don ) {
        try{
            PreparedStatement st = connection.prepareStatement("INSERT INTO don (domaine_id,Title,Description,address,phone,creationDate , latitude , longitude , image) VALUES (?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, don.getCategory().getId());
            st.setString(2, don.getTitle());
            st.setString(3, don.getDescription());
            st.setString(4,don.getAddress());
            st.setString(5,don.getPhone());
            st.setString(6, don.getCreationDate());
            st.setDouble(7,don.getLat());
            st.setDouble(8 , don.getLon());
            st.setString(9 , don.getImage());
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()){
                don.setId(rs.getInt(1));
                db.put(don.getId(),don);
                return true;
            }
        } catch (SQLException e){
            return false;
        }
        return false;
    }





    public void update(Don don) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE don SET Title=?,Description=?,address=?,phone=?,creationDate=?,image=?,domaine_id=?,latitude=?,longitude=?  WHERE id=?");
        preparedStatement.setString(1, don.getTitle());
        preparedStatement.setString(2, don.getDescription());
        preparedStatement.setString(3, don.getAddress());
        preparedStatement.setString(4, don.getPhone());
        preparedStatement.setString(5, don.getCreationDate());
        preparedStatement.setInt(10, don.getId());
        preparedStatement.setString(6 , don.getImage());
        preparedStatement.setInt(7, don.getCategory().getId());
        preparedStatement.setDouble(8,don.getLat());
        preparedStatement.setDouble(9 , don.getLon());
        if(preparedStatement.executeUpdate() > 0){
            db.remove(don.getId());
            db.put(don.getId(),don);
        }
    }



    public boolean delete(Don don) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM don WHERE id=?");
            preparedStatement.setInt(1, don.getId());
            if(preparedStatement.executeUpdate()>0){
                db.remove(don.getId());
                return true;
            }
        } catch (SQLException e){

        }
        return false;
    }




    Don resultSetToDon(ResultSet rs) throws SQLException {
        Don don = new Don();
            don.setId(rs.getInt("id"));
            don.setTitle(rs.getString("Title"));
            don.setDescription(rs.getString("Description"));
            don.setAddress(rs.getString("address"));
            don.setPhone(rs.getString("phone"));
            don.setCreationDate(rs.getString("creationDate"));
            don.setLat(rs.getDouble("latitude"));
            don.setLon(rs.getDouble("longitude"));
            don.setImage(rs.getString("image"));
            don.setCategory(CategoryService.getInstace().readById(rs.getInt("domaine_id")));
            don.setUserId(rs.getInt("user_id"));
        return don;
    }
    public boolean testLike(Don don) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM post_like WHERE don_id=? AND user_id=?");
        preparedStatement.setInt(1, don.getId());
        preparedStatement.setInt(2, UserSession.getInstace().getUser().getId());
        ResultSet rs= preparedStatement.executeQuery();
        return rs.next();


    }

    public boolean addLike(Don d) throws SQLException{
        if (this.testLike(d)){
            PreparedStatement psn = connection.prepareStatement("DELETE FROM post_like WHERE user_id=? AND don_id=? ");
            psn.setInt(1, UserSession.getInstace().getUser().getId());
            psn.setInt(2, d.getId());
            psn.executeUpdate() ;

            this.update(d);
            return false;

        }else{
            PreparedStatement stn = connection.prepareStatement("INSERT INTO post_like " +
                            "(`user_id`, " +
                            "`don_id`)" +
                            "VALUES (?,?)"
                    , Statement.RETURN_GENERATED_KEYS
            );
            stn.setInt(1, UserSession.getInstace().getUser().getId());
            stn.setInt(2,d.getId());
            stn.executeUpdate();
            this.update(d);
            return true;
        }
    }



    public int likeCount(Don don) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*)  FROM post_like WHERE don_id=?");
        preparedStatement.setInt(1, don.getId());
        ResultSet r = preparedStatement.executeQuery();
        r.next();
        System.out.println(r.getInt(1));

        return r.getInt(1);

    }

}
