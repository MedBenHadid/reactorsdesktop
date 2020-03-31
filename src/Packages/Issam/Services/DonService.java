package Packages.Issam.Services;

import Packages.Issam.Models.Don;
import SharedResources.Utils.Connector.ConnectionUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DonService {


    private static DonService instance;
    private Connection connection;

    public DonService() {
        connection = ConnectionUtil.conDB().conn;
    }

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
            don.setLat(rs.getDouble("latitude"));
            don.setLon(rs.getDouble("longitude"));
            //don.setImage(rs.getString("image"));

        }
        return don;
    }

}
