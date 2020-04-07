package Packages.Ramy.views;

import Packages.Chihab.Models.Category;
import Packages.Ramy.Models.Requete;
import Packages.Ramy.Services.RequeteService;
import SharedResources.Utils.Connector.ConnectionUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class RequeteController implements Initializable {

    @FXML
    private TableView<Requete> catTV;
    @FXML
    private TableColumn<Requete, Category> deleteOption;
    @FXML
    private TableColumn<Requete, String> nomCol, descCol;
    @FXML
    private TableColumn<Requete, Number> idCol, statutCol, typeCol;
    @FXML
    private TableColumn<Requete, Date> dateCol;
    @FXML
    private javafx.scene.control.TextArea description;
    @FXML
    private javafx.scene.control.TextField input, nom;
    @FXML
    private TabPane tabPane;
    @FXML
    private Label size;
    @FXML
    private Tab newTab;
    @FXML
    private ComboBox<String> combobox;

    ObservableList<String> listt = FXCollections.observableArrayList("Question générale sur Reactors","J'ai un problem concernant cette application","Autre");



    ObservableList<Requete> oblist = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = ConnectionUtil.conDB().getConnection();
        combobox.setItems(listt);

        try {
            ResultSet rs = connection.createStatement().executeQuery("select * from requete");
            while (rs.next()){
                oblist.add(new Requete(rs.getInt("id"),rs.getString("Sujet"),rs.getString("Description"),rs.getDate("DernierMAJ"),rs.getInt("Statut"),rs.getInt("Type")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //System.out.println("Hello world");


        nomCol.setCellValueFactory(new PropertyValueFactory<>("sujet"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("derniermaj"));

        catTV.setItems(oblist);
    }
    public void ajoutButton(){
        Requete req = new Requete();
        req.setUser(72);
        req.setDescription(description.getText());
        req.setSujet(nom.getText());
        if(combobox.getValue() == "Question générale sur League of Legends"){

        req.setType(1);}
        if(combobox.getValue() == "J'ai un problem concernant cette application"){

            req.setType(2);}
        if(combobox.getValue() == "Autre"){

            req.setType(3);}
        java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        req.setDerniermaj( sqlDate);
        req.setStatut(0);
        RequeteService servicereq = new RequeteService();
        servicereq.add(req);




    }



}
