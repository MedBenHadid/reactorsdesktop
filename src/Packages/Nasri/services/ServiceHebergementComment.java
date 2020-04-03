package Packages.Nasri.services;

import Packages.Nasri.entities.HebergementComment;
import Packages.Nasri.utils.Helpers;
import SharedResources.Utils.Connector.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceHebergementComment implements IService<HebergementComment> {
    Connection connection = ConnectionUtil.conDB().getConnection();

    @Override
    public void add(HebergementComment hebergementComment) {
        try {
            String query = "INSERT INTO comment " +
                    " (user_id, hebergement_id, content, creation_date) "
                    + " VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, hebergementComment.getUserId());
            preparedStatement.setInt(2, hebergementComment.getHebergementId());
            preparedStatement.setString(3, hebergementComment.getContent());
            preparedStatement.setDate(4, Helpers.convertLocalDateTimeToDate(hebergementComment.getCreationDate()));
            preparedStatement.executeUpdate();
            System.out.println("Hebergement comment added !");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        try {
            String query = "DELETE FROM comment WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Hebergement comment deleted !");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void update(HebergementComment hebergementComment) {
        try {
            String query = "UPDATE comment SET content = ? "
                    + " WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, hebergementComment.getContent());
            preparedStatement.setInt(2, hebergementComment.getId());
            preparedStatement.executeUpdate();
            System.out.println("Hebergement comment updated !");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public List<HebergementComment> get() {
        List<HebergementComment> list = new ArrayList<>();

        try {
            String query = "SELECT * FROM comment";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new HebergementComment(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        Helpers.convertDateToLocalDateTime(resultSet.getDate(5))
                ));
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return list;
    }
}
