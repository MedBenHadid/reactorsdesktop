package Packages.Chihab.Scenes;

import Main.Entities.User;
import Main.Services.UserService;
import Packages.Chihab.Models.enums.RoleEnum;
import SharedResources.URLServer;
import SharedResources.Utils.BCrypt.BCrypt;
import SharedResources.Utils.BinaryValidator.EmailValidator;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

public class MembershipCreate implements Initializable {
    @FXML
    private JFXTextField emailInput;
    @FXML
    private JFXSpinner imageSpinner;
    @FXML
    private ImageView profilePicture;
    @FXML
    private Label fullNameLabel;
    @FXML
    private AnchorPane profileAnchor;
    @FXML
    private Button inviteButton;

    private FTPInterface ftpInterface;

    public MembershipCreate() {
        try {
            this.ftpInterface = FTPInterface.getInstance(URLServer.ftpServerLink, URLServer.ftpSocketPort, URLServer.ftpUser, URLServer.ftpPassword);
        } catch (IOException e) {
            Alert ftpAlert = new Alert(Alert.AlertType.WARNING);
            ftpAlert.setContentText("Error connecting to FTP server");
            ftpAlert.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<User> users = null;
        try {
            users = UserService.getInstace().readAll().stream().filter(User::isClient).collect(Collectors.toCollection(FXCollections::observableArrayList));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        TextFields.bindAutoCompletion(emailInput, users.stream().map(User::getEmail).toArray());
        FilteredList<User> filteredName = new FilteredList<>(users, e -> true);
        emailInput.setOnKeyReleased(e -> {
            emailInput.textProperty().addListener((observableValue, s, t1) -> {
                filteredName.setPredicate(user -> {
                    if (t1 == null || t1.isEmpty())
                        return true;
                    return user.getEmail().toLowerCase().startsWith(t1.toLowerCase());
                });
            });
        });

        ObservableList<User> finalUsers = users;
        emailInput.setOnKeyPressed(ke -> {
            Optional<User> u = finalUsers.stream().filter(user -> user.getEmail().equals(emailInput.textProperty().get())).findFirst();
            if (emailInput.getText().isEmpty()) {
                imageSpinner.setVisible(false);
                profileAnchor.setVisible(false);
                inviteButton.setVisible(false);
            } else if (EmailValidator.isEmail(emailInput.textProperty().get())) {
                inviteButton.setVisible(true);
                imageSpinner.setVisible(false);
                if (u.isPresent()) {
                    profileAnchor.setVisible(true);
                    fullNameLabel.setText((u.get().getProfile().getNom() + " " + u.get().getProfile().getPrenom()).toUpperCase());
                    File imageAss = null;
                    try {
                        imageAss = ftpInterface.downloadFile(URLServer.userImageDir + u.get().getProfile().getImage(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                        profilePicture.setImage(new Image(imageAss.toURI().toString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                imageSpinner.setVisible(true);
                profileAnchor.setVisible(false);
                inviteButton.setVisible(false);
            }

            if (ke.getCode().equals(KeyCode.ENTER))
                if (EmailValidator.isEmail(emailInput.textProperty().get()))
                    if (u.isPresent()) {

                    } else {

                        // Register user and send him username / password
                        Dialog<String> confDialog = new TextInputDialog("");
                        confDialog.setContentText("Veuillez indiquer un message d'invitation :");
                        Optional<String> result = confDialog.showAndWait();
                        if (result.isPresent()) {
                            String message = result.get();
                            String passwordPlain = UUID.randomUUID().toString().substring(0, 8);
                            User newUser = new User(emailInput.getText(), emailInput.getText().substring(0, emailInput.getText().indexOf("@")), BCrypt.hashpw(passwordPlain, BCrypt.gensalt(13)));
                            newUser.removeRole(RoleEnum.ROLE_CLIENT);
                            System.out.println(newUser.getRoles());
                            //UserService.getInstace().create(newUser);
                            // TODO : Email an invite
                        }
                    }
                else {
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setHeaderText("Email invalide");
                    a.show();
                }

        });
    }
}
