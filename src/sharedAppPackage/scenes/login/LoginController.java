package sharedAppPackage.scenes.login;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import sharedAppPackage.models.User;
import sharedAppPackage.models.UserSession;
import sharedAppPackage.services.UserService;
import sharedAppPackage.utils.BinaryValidator.EmailValidator;
import sharedAppPackage.utils.connector.ConnectionUtil;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Set;


public class LoginController implements Initializable {

    @FXML
    private Label lblErrors;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnSignin;

    @FXML
    private Label btnForgot;

    @FXML
    private Button btnFB;

    @FXML
    private Button btnSignup;

    private UserService userService = UserService.getInstace();
    private Connection con ;

    private Alert a = new Alert(AlertType.ERROR);
    @FXML
    public void handleButtonAction(MouseEvent event) {

        if (event.getSource() == btnSignin) {
            //login here
            String status = logIn();
            switch (status) {
                case "Success":
                    try {
                        //add you loading or delays - ;-)
                        Node node = (Node) event.getSource();
                        Stage stage = (Stage) node.getScene().getWindow();
                        //stage.setMaximized(true);
                        stage.close();
                        UserSession us = UserSession.getInstace();
                        Scene scene = null;
                        if (us.getUser().isAdmin())
                            scene = new Scene(FXMLLoader.load(getClass().getResource("./../adminDashboard/Home.fxml")));
                        else if (us.getUser().isAssociationAdmin())
                            scene = new Scene(FXMLLoader.load(getClass().getResource("./../OnBoard/fxml/OnBoard.fxml")));
                        else if (us.getUser().isMember())
                            System.out.println("Member");
                        else if (us.getUser().isClient())
                            System.out.println("User");

                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        a = new Alert(AlertType.ERROR);
                        a.setContentText(ex.getMessage());
                        a.show();
                    }
                    break;
                case "Error":
                    a.setTitle("Authentification error:");
                    a.setHeaderText("Please check your (Username/Email) - Password combinations");
                    a.setContentText("ReactorsFX");
                    a.show();
                    break;
                case "Error1":
                    a.setTitle("Missing informations:");
                    a.setHeaderText("Please provide your login credentials");
                    a.setContentText("ReactorsFX");
                    a.show();
                    break;
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (con == null) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Server Error : Check");
        } else {
            lblErrors.setTextFill(Color.GREEN);
            lblErrors.setText("Server is up : Good to go");
        }
    }

    public LoginController() {
        con = ConnectionUtil.conDB().conn;
    }

    //we gonna use string to check for status
    private String logIn() {
        String status = "Success";
        String credential = txtUsername.getText();
        String password = txtPassword.getText();
        if(credential.isEmpty() || password.isEmpty()) {
            setLblError(Color.TOMATO, "Empty credentials");
            status = "Error1";
        } else {
            try {
                if (!userService.validateLogin(credential,password)) {
                    setLblError(Color.TOMATO, "Enter Correct (Email/Username)-/Password");
                    status = "Error";
                } else {
                    setLblError(Color.GREEN, "Login Successful..Redirecting..");
                }
            } catch (SQLException ex) {
                status = "Exception";
            }
        }
        return status;
    }

    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
    }

}




