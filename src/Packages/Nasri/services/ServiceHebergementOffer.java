package Packages.Nasri.services;

import Packages.Nasri.enums.HebergementStatus;
import Packages.Nasri.entities.HebergementOffer;
import Packages.Nasri.utils.Helpers;
import SharedResources.Utils.Connector.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceHebergementOffer implements IService<HebergementOffer> {
    Connection connection = ConnectionUtil.conDB().getConnection();

    @Override
    public void add(HebergementOffer hebergementOffer) {
        try {
            String query = "INSERT INTO hebergement " +
                    " (user_id, description, governorat, nbr_rooms, duration, creation_date, state, telephone, image) " +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, hebergementOffer.getUserId());
            preparedStatement.setString(2, hebergementOffer.getDescription());
            preparedStatement.setString(3, hebergementOffer.getGovernorat());
            preparedStatement.setInt(4, hebergementOffer.getNumberRooms());
            preparedStatement.setInt(5, hebergementOffer.getDuration());
            preparedStatement.setDate(6, Helpers.convertLocalDateTimeToDate(hebergementOffer.getCreationDate()));
            preparedStatement.setInt(7, hebergementOffer.getState().ordinal());
            preparedStatement.setString(8, hebergementOffer.getTelephone());
            preparedStatement.setString(9, hebergementOffer.getImage());
            preparedStatement.executeUpdate();
            System.out.println("Hebergement offer added !");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        try {
            String query = "DELETE FROM hebergement WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Hebergement offer deleted !");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void update(HebergementOffer hebergementOffer) {
        try {
            String query = "UPDATE hebergement SET description = ?, governorat = ?, nbr_rooms = ?, duration = ?, "
                    + " creation_date = ?, state = ?, telephone = ?, image = ? "
                    + " WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, hebergementOffer.getDescription());
            preparedStatement.setString(2, hebergementOffer.getGovernorat());
            preparedStatement.setInt(3, hebergementOffer.getNumberRooms());
            preparedStatement.setInt(4, hebergementOffer.getDuration());
            preparedStatement.setDate(5, Helpers.convertLocalDateTimeToDate(hebergementOffer.getCreationDate()));
            preparedStatement.setInt(6, hebergementOffer.getState().ordinal());
            preparedStatement.setString(7, hebergementOffer.getTelephone());
            preparedStatement.setString(8, hebergementOffer.getImage());
            preparedStatement.setInt(9, hebergementOffer.getId());
            preparedStatement.executeUpdate();
            System.out.println("Hebergement Offer updated !");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public ArrayList<HebergementOffer> get() {
        ArrayList<HebergementOffer> list = new ArrayList<>();

        try {
            String query = "SELECT * FROM hebergement";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new HebergementOffer(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getInt(5),
                        resultSet.getInt(6),
                        Helpers.convertDateToLocalDateTime(resultSet.getDate(7)),
                        HebergementStatus.values()[resultSet.getInt(8)],
                        resultSet.getString(9),
                        resultSet.getString(10)
                ));
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return list;
    }

    public String getUserName(int userId) {
        String userName = null;

        try {
            String query = "SELECT user.username FROM hebergement, user WHERE hebergement.user_id = ? " +
                    " and hebergement.user_id = user.id";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if (resultSet != null) {
                userName = resultSet.getString(1);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return userName;
    }
}
