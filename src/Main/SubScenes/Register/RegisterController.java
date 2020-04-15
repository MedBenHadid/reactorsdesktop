/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.SubScenes.Register;

import Main.Entities.User;
import Packages.Chihab.Controllers.AssociationCreateController;
import SharedResources.URLScenes;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author proxc
 */
public class RegisterController implements Initializable {
    private final User user;
    @FXML
    private JFXTextField firstName, lastName, username, email, phoneNumber, cin, adress;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXDatePicker dateOfBirth;
    @FXML
    private JFXButton fileUploadButton, signupUser, signupAssociation;
    @FXML
    private AnchorPane anchorPane;

    public RegisterController() {
        this.user = new User();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // TODO : name the buttons and input ids
        // TODO : Validate the inputs
        // TODO : Persist the user
        // Done : Create association
        signupAssociation.setOnMouseClicked(mouseEvent -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(URLScenes.associationCreate));
            AssociationCreateController controller = new AssociationCreateController(user);
            loader.setController(controller);
            AnchorPane a = null;
            try {
                a = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            anchorPane.getChildren().setAll(a);
        });
    }    
    
}
