package Packages.Mohamed.Scenes;

import Main.Entities.User;
import SharedResources.URLServer;
import SharedResources.Utils.FTPInterface.FTPInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserItemController implements Initializable {
    FTPInterface ftpInterface;
    @FXML
    private ImageView userImageView;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label telLabel;
    @FXML
    private Label adresseLabel;

    private User u;

    public UserItemController(User u) {
        try {
            this.u = u;
            this.ftpInterface = FTPInterface.getInstance(URLServer.ftpServerLink, URLServer.ftpSocketPort, URLServer.ftpUser, URLServer.ftpPassword);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameLabel.textProperty().setValue(u.getUsername());
        emailLabel.setText(u.getEmail());
        telLabel.setText(u.getProfile().getTelephone());

        try {
            File imageAss = ftpInterface.downloadFile(URLServer.userImageDir + u.getProfile().getImage(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            userImageView.setImage(new Image(imageAss.toURI().toString()));
        } catch (IOException e) {
            Logger.getLogger(
                    UserItemController.class.getName()).log(
                    Level.SEVERE, "Error whilst fetching image", e
            );
        }
    }


}
