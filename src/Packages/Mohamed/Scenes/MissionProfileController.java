package Packages.Mohamed.Scenes;

import Packages.Chihab.Models.Association;
import Packages.Chihab.Models.Membership;
import Packages.Chihab.Scenes.AssociationsController;
import Packages.Chihab.Services.MembershipService;
import SharedResources.URLServer;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MissionProfileController implements Initializable {
    @FXML
    Label nameLabel, descriptionLabel, numeroLabel;
    @FXML
    ImageView imageImageView;
    @FXML
    JFXToggleButton statusToggleButton;
    @FXML
    TreeView<Membership> membresTreeView;
    @FXML
    Button afficherPieceButton;
    @FXML
    WebView mapWebView;
    @FXML
    JFXSpinner spinner;
    //private Desktop desktop = Desktop.getDesktop();
    FTPInterface ftpInterface;
    private MembershipService membershipService = MembershipService.getInstace();
    private Association a;

    public MissionProfileController() {
        try {
            this.ftpInterface = FTPInterface.getInstance(URLServer.ftpServerLink, URLServer.ftpSocketPort, URLServer.ftpUser, URLServer.ftpPassword);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    /**
     * Accepts an Association type and stores it to specific instance variables
     * in order to show its profile ( Preferably in a new TabPane )
     *
     * @param association
     */
    public MissionProfileController(Association association) {
        this.a = association;
        try {
            this.ftpInterface = FTPInterface.getInstance(URLServer.ftpServerLink, URLServer.ftpSocketPort, URLServer.ftpUser, URLServer.ftpPassword);
        } catch (IOException e) {
            Alert ftpAlert = new Alert(Alert.AlertType.WARNING);
            ftpAlert.setContentText("Error connecting to FTP server");
            ftpAlert.show();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameLabel.setText(a.getNom());
        descriptionLabel.setText(a.getDescription());
        numeroLabel.setText(String.valueOf(a.getTelephone()));
        statusToggleButton.selectedProperty().bindBidirectional(new SimpleBooleanProperty(a.isApprouved()));

        statusToggleButton.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                Dialog confDialog = new TextInputDialog("");
                confDialog.setContentText("Veuillez indiquer la raison :");
                Optional<String> result = confDialog.showAndWait();
                if (result.isPresent()) {
                    String entered = result.get();
                    System.out.println(entered);
                    // TODO : Disable
                    // TODO : Send email with reason {entered}
                } else {
                    statusToggleButton.selectedProperty().setValue(true);

                }
            }
        });
        mapWebView.setVisible(false);
        mapWebView.getEngine().load(this.getClass().getResource("/Packages/Chihab/Scenes/WebView/showAssociationLocation.html").toString());
        mapWebView.getEngine().getLoadWorker().stateProperty().addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                        if (newValue == Worker.State.SUCCEEDED) {
                            spinner.setVisible(false);
                            mapWebView.setVisible(true);
                            mapWebView.getEngine().executeScript("initMap(" + a.getLat() + "," + a.getLon() + ")");
                            Document document = mapWebView.getEngine().getDocument();
                            System.out.println(document.getElementById("lat").getTextContent());
                        }
                    }
                }
        );

        try {
            TreeItem<Membership> root = new TreeItem<>(membershipService.readAll().get(0));
            membresTreeView.setRoot(root);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            File imageAss = ftpInterface.downloadFile(URLServer.associationImageDir + a.getPhotoAgence(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            imageImageView.setImage(new Image(imageAss.toURI().toString()));
        } catch (IOException e) {
            Logger.getLogger(
                    MissionProfileController.class.getName()).log(
                    Level.SEVERE, "Error whilst fetching image", e
            );
            Alert photoMissing = new Alert(Alert.AlertType.WARNING);
            photoMissing.setContentText("Error whilst fetching photo");
            photoMissing.show();
        }
        afficherPieceButton.setOnAction(event -> {
            try {
                File file = ftpInterface.downloadFile(URLServer.associationPieceDir + a.getPieceJustificatif(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                if (!Desktop.isDesktopSupported())
                    return;
                Desktop desktop = Desktop.getDesktop();
                if (file.exists()) {
                    desktop.open(file);
                    file.deleteOnExit();
                }
            } catch (Exception e) {
                Logger.getLogger(
                        AssociationsController.class.getName()).log(
                        Level.SEVERE, null, e
                );
                Alert connAlert = new Alert(Alert.AlertType.WARNING);
                connAlert.setContentText("Error whilst fetching " + a.getPieceJustificatif() + " from FTP server");
                connAlert.show();
            }
        });
    }

}
