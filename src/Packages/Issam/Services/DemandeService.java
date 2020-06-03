package Packages.Issam.Services;

import Packages.Issam.Models.Demande;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DemandeService {

    private static DemandeService instance;
    private Connection connection;

    public void ajouter(Demande d) {
        String sql = "INSERT INTO association " +
                "(`id`, " +
                "`domaine_id`, " +
                "`user_id`, " +
                "`title`, " +
                "`description`, " +
                "`address`, " +
                "`phone`, " +
                "`ups`, " +
                "`creationDate`, " +
                "`rib`, " +
                "`latitude`, " +
                "`longitude`, " +
                "`image`, " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement st;
        try {
            st = connection.prepareStatement(sql);
            st.setInt(1, d.getId());
            st.setInt(2, d.getCategory());
            st.setInt(3, d.getUser());
            st.setString(4, d.getTitle());
            st.setString(5, d.getDescription());
            st.setString(6, d.getAddress());
            st.setString(7, d.getPhone());
            st.setInt(8, d.getUps());
            st.setString(9, d.getCreationDate());
            st.setString(10, d.getRib());
            st.setDouble(11, d.getLat());
            st.setDouble(12, d.getLon());
            st.setString(15, d.getRib());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Demande> afficher() {
        List<Demande> list = new ArrayList<>();

        try {
            String requete = "SELECT * FROM personne";
            PreparedStatement pst = connection.prepareStatement(requete);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Demande(rs.getInt(1), rs.getInt(2), rs.getString(3)
                        , rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7),
                        rs.getInt(8), rs.getDouble(9),
                        rs.getDouble(10), rs.getString(11),
                        rs.getInt(12), rs.getString(13)));
                System.out.println();
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return list;
    }
}