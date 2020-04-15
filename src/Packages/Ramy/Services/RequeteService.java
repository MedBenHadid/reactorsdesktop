package Packages.Ramy.Services;


import Packages.Ramy.Models.Requete;
import SharedResources.Utils.Connector.ConnectionUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RequeteService implements IService<Requete> {
    Connection connection = ConnectionUtil.conDB().conn;


    @Override
    public void add(Requete requete) {
        try {
            String query = "INSERT INTO requete (user_id,Sujet, Description, DernierMAJ, Statut, Type ) " +
                    " VALUES ('" + requete.getUser() + "','" + requete.getSujet() + "','" + requete.getDescription() + "','"
                    + requete.getDerniermaj() + "','" + requete.getStatut() + "','" + requete.getType() + "')";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Requete added !");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }

    @Override
    public void delete(Requete requete) {
        try {
            String query = "DELETE FROM requete WHERE id=" + requete.getId();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Requete deleted !");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }

    @Override
    public void update(Requete requete) {
        try {
            String query = "UPDATE requete SET Sujet = '" + requete.getSujet()
                    + "', Description = '" + requete.getDescription() + "' WHERE id=" + requete.getId();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Requete updated !");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }

    public void updaterponse(Requete requete) {
        try {
            String query = "UPDATE requete SET rponse_id = '" + requete.getRponse() + "', Statut = '" + 1
                    + "' WHERE id=" + requete.getId();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Requete updated !");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }

    @Override
    public List<Requete> display() {
        List<Requete> list = new ArrayList<>();

        try {
            String query = "SELECT * FROM requete";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                list.add(new Requete(resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getDate(5),
                        resultSet.getInt(6),
                        resultSet.getInt(7),
                        resultSet.getString(8)));
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return list;

    }
}
