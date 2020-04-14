package Packages.Chihab.Controllers;

import Packages.Chihab.Models.Membership;
import Packages.Chihab.Services.MembershipService;
import SharedResources.URLServer;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MemberUpdateItemController implements Initializable {
    FTPInterface ftpInterface;
    @FXML
    private ImageView userImageView;
    @FXML
    private Label dateLabel;
    @FXML
    private JFXTextArea descriptionLabel;
    @FXML
    private JFXComboBox roleComboBox;
    @FXML
    private JFXTextField fonctionTextField;
    @FXML
    private Label nameLabel;
    private Membership m;

    public MemberUpdateItemController(Membership m) {
        try {
            this.m = m;
            this.ftpInterface = FTPInterface.getInstance(URLServer.ftpServerLink, URLServer.ftpSocketPort, URLServer.ftpUser, URLServer.ftpPassword);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //roleComboBox.textProperty().setValue(m.getMember().getProfile().getNom() + " " + m.getMember().getProfile().getPrenom());
        nameLabel.textProperty().setValue(m.getMember().getProfile().getNom() + " " + m.getMember().getProfile().getPrenom());
        descriptionLabel.setText(m.getDescription());
        dateLabel.setText(m.getJoinDate().toString());
        fonctionTextField.setText(m.getFonction());
        roleComboBox.setItems(FXCollections.observableArrayList("Livreur", "Rédacteur", "Adhérant simple"));
        roleComboBox.getSelectionModel().select(2);
        roleComboBox.valueProperty().addListener((ChangeListener<String>) (ov, t, t1) -> {
            if (!t.equals(t1)) {
                if (t1.equals("Livreur"))
                    m.setAccess(2);
                else if (t1.equals("Rédacteur"))
                    m.setAccess(1);
                else
                    m.setAccess(3);
                try {
                    MembershipService.getInstace().update(m);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        fonctionTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                if (fonctionTextField.getText().length() > 6) {
                    m.setFonction(fonctionTextField.getText());
                    try {
                        MembershipService.getInstace().update(m);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    // TODO : Success alert
                }
            }
        });
        descriptionLabel.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                if (descriptionLabel.getText().length() > 6) {
                    m.setDescription(descriptionLabel.getText());
                    try {
                        MembershipService.getInstace().update(m);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    // TODO : Success alert
                }
            }
        });
        try {
            File imageAss = ftpInterface.downloadFile(URLServer.userImageDir + m.getMember().getProfile().getImage(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            userImageView.setImage(new Image(imageAss.toURI().toString()));
        } catch (IOException e) {
            Logger.getLogger(
                    MemberUpdateItemController.class.getName()).log(
                    Level.SEVERE, "Error whilst fetching image", e
            );
        }

    }


}
