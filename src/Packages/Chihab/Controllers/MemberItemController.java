package Packages.Chihab.Controllers;

import Packages.Chihab.Models.Membership;
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

public class MemberItemController implements Initializable {
    FTPInterface ftpInterface;
    @FXML
    private ImageView userImageView;
    @FXML
    private Label nameLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Label fonctionLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label descriptionLabel;
    private Membership m;

    public MemberItemController(Membership m) {
        try {
            this.m = m;
            this.ftpInterface = FTPInterface.getInstance(URLServer.ftpServerLink, URLServer.ftpSocketPort, URLServer.ftpUser, URLServer.ftpPassword);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameLabel.textProperty().setValue(m.getMember().getProfile().getNom() + " " + m.getMember().getProfile().getPrenom());
        descriptionLabel.setText(m.getDescription());
        dateLabel.setText(m.getJoinDate().toString());
        fonctionLabel.setText(m.getFonction());

        try {
            File imageAss = ftpInterface.downloadFile(URLServer.userImageDir + m.getMember().getProfile().getImage(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            userImageView.setImage(new Image(imageAss.toURI().toString()));
        } catch (IOException e) {
            Logger.getLogger(
                    MemberItemController.class.getName()).log(
                    Level.SEVERE, "Error whilst fetching image", e
            );
        }
    }


}
