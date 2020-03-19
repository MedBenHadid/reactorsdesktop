/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Packages.UserManagementPackage.Scenes.LoggedInStage.onBoard.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import Packages.UserManagementPackage.Models.User;
import Packages.UserManagementPackage.Services.UserService;
import utils.Utils.Connector.ConnectionUtil;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private TextField txtFirstname;
    @FXML
    private TextField txtLastname;
    @FXML
    private TextField txtEmail;
    @FXML
    private DatePicker txtDOB;
    @FXML
    private Button btnSave;
    @FXML
    private ComboBox<String> txtGender;
    @FXML
    private
    Label lblStatus;

    @FXML
    private
    TableView tblData;

    private List<User> users;

    private Connection connection;

    public HomeController() {
        connection = ConnectionUtil.conDB().conn;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        txtGender.getItems().addAll("Male", "Female", "Other");
        txtGender.getSelectionModel().select("Male");
        users = UserService.getInstace().listUsers();
        fetRowList();

    }

    @FXML
    private void HandleEvents(MouseEvent event) {
        //check if not empty
        if (txtEmail.getText().isEmpty() || txtFirstname.getText().isEmpty() || txtLastname.getText().isEmpty() || txtDOB.getValue() == null) {
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("Enter all details");
        } else {
            saveData();
        }

    }

    private void clearFields() {
        txtFirstname.clear();
        txtLastname.clear();
        txtEmail.clear();
    }

    private String saveData() {

        try {
            String st = "INSERT INTO user ( prenom, nom, email, adresse, date_naissance) VALUES (?,?,?,?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(st);
            preparedStatement.setString(1, txtFirstname.getText());
            preparedStatement.setString(2, txtLastname.getText());
            preparedStatement.setString(3, txtEmail.getText());
            preparedStatement.setString(4, txtGender.getValue());
            preparedStatement.setString(5, txtDOB.getValue().toString());

            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Added Successfully");

            fetRowList();
            //clear fields
            clearFields();
            return "Success";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }


    //fetches rows and data from the list
    private void fetRowList() {
        ObservableList<User> usersObs = FXCollections.observableArrayList(users);
        for (User u : users) {
            System.out.println(u);
        }

    }



}
