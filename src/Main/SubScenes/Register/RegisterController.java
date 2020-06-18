/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.SubScenes.Register;

import Main.Entities.User;
import Main.Services.UserService;
import Packages.Chihab.Controllers.AssociationCreateController;
import SharedResources.URLServer;
import SharedResources.Utils.BCrypt.BCrypt;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Stack;

/**
 * @author proxc
 */
public class RegisterController implements Initializable {
    private final User user = new User();

    @FXML
    private JFXTextField myEmail = new JFXTextField();
    @FXML
    private JFXButton photoChooserButton;
    @FXML
    private JFXTextField myFirstName = new JFXTextField();
    @FXML
    private JFXTextField myLastName = new JFXTextField();;
    @FXML
    private JFXTextField myUsername = new JFXTextField();
    @FXML
    private JFXTextField phoneNumber = new JFXTextField();
    @FXML
    private JFXPasswordField myPassword = new JFXPasswordField();
    @FXML
    private JFXDatePicker myDateOfBirth = new JFXDatePicker();
    @FXML
    private JFXButton signupAssociation;
    @FXML
    private StackPane anchorPane;

    @FXML
    private JFXButton handleAddBtn;
    @FXML
    private ImageView profilePicturePicture;
    private final SimpleBooleanProperty pieceValid = new SimpleBooleanProperty(true);
    private final FileChooser fileChooser = new FileChooser();

    private File profileImage;
    public RegisterController() {
    }    

    @FXML
    protected void handleAddBtn() {
        if (!pieceValid.get()) {
            // TODO : Show error that file is missing
        }else {
            String firstNameValue = myFirstName.getText();

            String lastNameValue = myLastName.getText();
            String phoneNumberValue = phoneNumber.getText();


            LocalDate dateOfBirthValue = myDateOfBirth.getValue();
            System.out.println("lol");

            User newUser = new User(myEmail.getText(),myUsername.getText(), BCrypt.hashpw(myPassword.getText(),BCrypt.gensalt(13)),profileImage.getName());
            UserService.getInstace().create(newUser);
            FTPInterface.getInstance().send(profileImage, URLServer.userImageDir);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        photoChooserButton.setOnMouseClicked(mouseEvent -> {
            profileImage = fileChooser.showOpenDialog(anchorPane.getScene().getWindow());
            pieceValid.setValue(profileImage!=null);
            profilePicturePicture.setImage(new Image(profileImage.toURI().toString()));
        });
        signupAssociation.setOnMouseClicked(mouseEvent -> {
            anchorPane.getChildren().setAll(new AssociationCreateController());
        });
    }
}
