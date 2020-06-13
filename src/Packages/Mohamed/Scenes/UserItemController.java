package Packages.Mohamed.Scenes;

import Main.Entities.User;
import SharedResources.URLScenes;
import SharedResources.URLServer;
import SharedResources.Utils.FTPInterface.FTPInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

    private final User u;

    public UserItemController(User u) {
        this.u = u;
        FXMLLoader thisLoader = new FXMLLoader(getClass().getResource(URLScenes.NotifItem));
        thisLoader.setRoot( this );
        thisLoader.setController( this );
        try {
            thisLoader.load();
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameLabel.textProperty().setValue(u.getUsername());
        emailLabel.setText(u.getEmail());
        telLabel.setText(u.getProfile().getTelephone());

        try {
            File imageAss = FTPInterface.getInstance().downloadFile(URLServer.userImageDir + u.getProfile().getImage(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            userImageView.setImage(new Image(imageAss.toURI().toString()));
        } catch (IOException e) {
            Logger.getLogger(
                    UserItemController.class.getName()).log(
                    Level.SEVERE, "Error whilst fetching image", e
            );
        }
    }


}
