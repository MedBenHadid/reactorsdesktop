package Packages.Issam.Services;

import Packages.Issam.Models.Don;
import SharedResources.Utils.Connector.ConnectionUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DonService {


    private static DonService instance;
    private final Connection connection = ConnectionUtil.getInstance().getConn();

    public static DonService getInstace() {
        if (instance == null) {
            instance = new DonService();
        }
        return instance;
    }
    public ObservableList<Don> readAll() throws SQLException {
        ObservableList<Don> donItems = FXCollections.observableArrayList();
        String req = "SELECT * FROM don";
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(req);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            donItems.add(resultSetToDon(resultSet));
        }
        return donItems;
    }

    public int create(Don don) throws SQLException {
        PreparedStatement st = connection.prepareStatement("INSERT INTO don (Title,Description , address , phone , ups , creationDate , latitude , longitude) VALUES (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        st.setString(1, don.getTitle());
        st.setString(2, don.getDescription());
        st.setString(3,don.getAddress());
        st.setString(4,don.getPhone());
        st.setInt(5 , don.getUps());
        st.setString(6, don.getCreationDate());
        st.setString(7,don.getLat());
        st.setString(8 , don.getLon());
        st.executeUpdate();
        ResultSet rs = st.getGeneratedKeys();
        if (rs.next())
            return rs.getInt(1);
        return 0;
    }





    public void update(Don don) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE don SET Title=?,Description=?,address=?,phone=?,creationDate=?  WHERE id=?");
        preparedStatement.setString(1, don.getTitle());
        preparedStatement.setString(2, don.getDescription());
        preparedStatement.setString(3, don.getAddress());
        preparedStatement.setString(4, don.getPhone());
        preparedStatement.setString(5, don.getCreationDate());
        preparedStatement.setInt(6, don.getId());

        preparedStatement.executeUpdate();
    }



    public void delete(Don don) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM don WHERE id=?");
        preparedStatement.setInt(1, don.getId());
        preparedStatement.executeUpdate();
    }




    Don resultSetToDon(ResultSet rs) throws SQLException {
        Don don = new Don();
        if(rs.next()) {
            don.setId(rs.getInt("id"));
            don.setTitle(rs.getString("Title"));
            don.setDescription(rs.getString("Description"));
            don.setAddress(rs.getString("address"));
            don.setPhone(rs.getString("phone"));
            don.setUps(rs.getInt("ups"));
            don.setCreationDate(rs.getString("creationDate"));
            don.setLat(rs.getString("latitude"));
            don.setLon(rs.getString("longitude"));
            //don.setImage(rs.getString("image"));

        }
        return don;
    }

}
