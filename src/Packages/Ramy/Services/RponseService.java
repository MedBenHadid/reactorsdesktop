package Packages.Ramy.Services;

import Packages.Ramy.Models.Rponse;
import SharedResources.Utils.Connector.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class RponseService implements IService<Rponse> {
    Connection connection= ConnectionUtil.getInstance().getConn();

    @Override
    public void add(Rponse rponse) {
        try {
            String query = "INSERT INTO rponse (user_id,requete_id,Sujet, Rep, Date, Rating ) VALUES ('" + rponse.getUser() + "','" + rponse.getRequete() + "','" +
                    rponse.getSujet() + "','" + rponse.getRep() + "','" + rponse.getDate() + "','" + rponse.getRating() + "')";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Rponse added !");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }

    @Override
    public void delete(Rponse rponse) {
        try {
            String query = "DELETE FROM rponse WHERE id=" + rponse.getId();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Rponse deleted !");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }

    @Override
    public void update(Rponse rponse) {
        try {
            String query = "UPDATE rponse SET Sujet = '" + rponse.getSujet()
                    + "', Rep = '" + rponse.getRep() + "' WHERE id=" + rponse.getId();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Requete updated !");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }


    }

    public int getidrep(Rponse rponse) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM rponse  WHERE requete_id=" + rponse.getRequete());
            // ResultSet resultSet = statement.executeQuery(query);
            // int rep = resultSet.getInt(1);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return -1;
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return 0;
    }

    @Override
    public List<Rponse> display() {
        List<Rponse> list = new ArrayList<>();

        try {
            String query = "SELECT * FROM rponse";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                list.add(new Rponse(resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getDate(6),
                        resultSet.getInt(7)));
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return list;
    }
}
