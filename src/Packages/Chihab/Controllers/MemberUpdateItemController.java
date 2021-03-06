package Packages.Chihab.Controllers;

import Packages.Chihab.Models.Entities.Membership;
import Packages.Chihab.Services.MembershipService;
import SharedResources.URLServer;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
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
    @FXML
    private ImageView userImageView;
    @FXML
    private JFXTextArea descriptionLabel;
    @FXML
    private JFXComboBox<String> roleComboBox;
    @FXML
    private JFXTextField fonctionTextField;
    @FXML
    private Label nameLabel, dateLabel;

    private final Membership m;
    private final int associationId;

    public MemberUpdateItemController(Membership m, int associationId) {
        this.m = m;
        this.associationId = associationId;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameLabel.textProperty().setValue(m.getMember().getProfile().getNom() + " " + m.getMember().getProfile().getPrenom());
        descriptionLabel.setText(m.getDescription());
        dateLabel.setText(m.getJoinDate().toString());
        fonctionTextField.setText(m.getFonction());
        roleComboBox.setItems(FXCollections.observableArrayList("Livreur", "Rédacteur", "Adhérant simple"));
        roleComboBox.getSelectionModel().select(2);
        roleComboBox.valueProperty().addListener((ov, t, t1) -> {
            if (!t.equals(t1)) {
                if (t1.equals("Livreur"))
                    m.setAccess(2);
                else if (t1.equals("Rédacteur"))
                    m.setAccess(1);
                else
                    m.setAccess(3);
                try {
                    MembershipService.getInstace().update(m, associationId);
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
                        MembershipService.getInstace().update(m, associationId);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        descriptionLabel.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                if (descriptionLabel.getText().length() > 6) {
                    m.setDescription(descriptionLabel.getText());
                    try {
                        MembershipService.getInstace().update(m, associationId);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    // TODO : Success alert
                }
            }
        });
        try {
            File imageAss = FTPInterface.getInstance().downloadFile(URLServer.userImageDir + m.getMember().getProfile().getImage(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            userImageView.setImage(new Image(imageAss.toURI().toString()));
        } catch (IOException e) {
            Logger.getLogger(MemberUpdateItemController.class.getName()).log(Level.SEVERE, "Error whilst fetching image", e);
        }

    }


}
