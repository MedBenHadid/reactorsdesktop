package Main.SubScenes.Login;

import Main.Services.UserService;
import SharedResources.URLScenes;
import SharedResources.Utils.Connector.ConnectionUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    @FXML
    private Label lblErrors, btnForgot;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnSignin, btnSignup;

    private final UserService userService = UserService.getInstace();
    private final Connection con;
    private final Alert a = new Alert(AlertType.ERROR);

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
                        stage.setMaximized(true);
                        stage.close();
                        Scene scene = new Scene(FXMLLoader.load(getClass().getResource(URLScenes.mainScene)));
                        stage.setScene(scene);
                        stage.setFullScreen(true);
                        stage.setResizable(true);
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
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
        if (event.getSource() == btnSignup){
            try {
                AnchorPane a = FXMLLoader.load(getClass().getResource(URLScenes.register));
                Group group = (Group) ((Node)event.getSource()).getParent().getParent().getParent().getParent();
                group.getChildren().clear();
                group.getChildren().add(a);
            } catch (IOException ex){
                ex.printStackTrace();
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
                    setLblError(Color.GREEN, "MainPackage.AuthScenes.SharedScenes.Login Successful..Redirecting..");
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




