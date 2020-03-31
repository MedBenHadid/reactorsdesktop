package Packages.Nasri.services;

import Packages.Nasri.enums.CivilStatus;
import Packages.Nasri.enums.HebergementStatus;
import Packages.Nasri.models.HebergementOffer;
import Packages.Nasri.models.HebergementRequest;
import Packages.Nasri.utils.Helpers;
import SharedResources.Utils.Connector.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceHebergementRequest implements IService<HebergementRequest> {
    Connection connection = ConnectionUtil.conDB().getConnection();

    @Override
    public void add(HebergementRequest hebergementRequest) {
        try {
            String query = "INSERT INTO hebergement_request " +
                    " (user_id, description, region, state, native_country, arrival_date, " +
                    " passport_number, civil_status, children_number, name, telephone, " +
                    " creation_date, is_anonymous) " +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, hebergementRequest.getUserId());
            preparedStatement.setString(2, hebergementRequest.getDescription());
            preparedStatement.setString(3, hebergementRequest.getRegion());
            preparedStatement.setInt(4, hebergementRequest.getState().ordinal());
            preparedStatement.setString(5, hebergementRequest.getNativeCountry());
            preparedStatement.setDate(6, Helpers.convertLocalDateTimeToDate(hebergementRequest.getArrivalDate()));
            preparedStatement.setString(7, hebergementRequest.getPassportNumber());
            preparedStatement.setInt(8, hebergementRequest.getCivilStatus().ordinal());
            preparedStatement.setInt(9, hebergementRequest.getChildrenNumber());
            preparedStatement.setString(10, hebergementRequest.getName());
            preparedStatement.setString(11, hebergementRequest.getTelephone());
            preparedStatement.setDate(12, Helpers.convertLocalDateTimeToDate(hebergementRequest.getCreationDate()));
            preparedStatement.setBoolean(13, hebergementRequest.isAnonymous());
            preparedStatement.executeUpdate();
            System.out.println("Hebergement request added !");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        try {
            String query = "DELETE FROM hebergement_request WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Hebergement request deleted !");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void update(HebergementRequest hebergementRequest) {
        try {
            String query = "UPDATE hebergement_request SET description = ?, region = ?, state = ?, native_country = ?, "
                    + " arrival_date = ?, passport_number = ?, civil_status = ?, children_number = ? "
                    + ", name = ?, telephone = ?, is_anonymous = ?"
                    + " WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, hebergementRequest.getDescription());
            preparedStatement.setString(2, hebergementRequest.getRegion());
            preparedStatement.setInt(3, hebergementRequest.getState().ordinal());
            preparedStatement.setString(4, hebergementRequest.getNativeCountry());
            preparedStatement.setDate(5, Helpers.convertLocalDateTimeToDate(hebergementRequest.getArrivalDate()));
            preparedStatement.setString(6, hebergementRequest.getPassportNumber());
            preparedStatement.setInt(7, hebergementRequest.getCivilStatus().ordinal());
            preparedStatement.setInt(8, hebergementRequest.getChildrenNumber());
            preparedStatement.setString(9, hebergementRequest.getName());
            preparedStatement.setString(10, hebergementRequest.getTelephone());
            preparedStatement.setBoolean(11, hebergementRequest.isAnonymous());
            preparedStatement.setInt(12, hebergementRequest.getId());
            preparedStatement.executeUpdate();
            System.out.println("Hebergement request updated !");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public List<HebergementRequest> get() {
        List<HebergementRequest> list = new ArrayList<>();

        try {
            String query = "SELECT * FROM hebergement_request";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
//                list.add(new HebergementRequest(
//                        resultSet.getInt(1),
//                        resultSet.getInt(2),
//                        resultSet.getString(3),
//                        resultSet.getString(4),
//                        CivilStatus.values()[resultSet.getInt(5)],
//                        resultSet.getString(6),
//                        Helpers.convertDateToLocalDateTime(resultSet.getDate(7),
//                        resultSet.getString(8),
//                        CivilStatus.values()[resultSet.getInt(9)],
//                        resultSet.getInt(10),
//                        resultSet.getString(11),
//                        resultSet.getString(12),
//                        Helpers.convertDateToLocalDateTime(resultSet.getDate(13)) ,
//                        resultSet.getBoolean(14)
//                ));
                list.add(new HebergementRequest(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        HebergementStatus.values()[resultSet.getInt(5)],
                        resultSet.getString(6),
                        Helpers.convertDateToLocalDateTime(resultSet.getDate(7)),
                        resultSet.getString(8),
                        CivilStatus.values()[resultSet.getInt(9)],
                        resultSet.getInt(10),
                        resultSet.getString(11),
                        resultSet.getString(12),
                        Helpers.convertDateToLocalDateTime(resultSet.getDate(13)),
                        resultSet.getBoolean(14)
                ));
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return list;
    }
}
