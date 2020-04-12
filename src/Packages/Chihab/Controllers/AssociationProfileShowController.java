package Packages.Chihab.Controllers;

import Packages.Chihab.Models.Association;
import Packages.Chihab.Models.Membership;
import Packages.Chihab.Services.MembershipService;
import SharedResources.URLScenes;
import SharedResources.URLServer;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
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

public class AssociationProfileShowController implements Initializable {
    @FXML
    Label nameLabel, descriptionLabel, numeroLabel;
    @FXML
    ImageView imageImageView;
    @FXML
    JFXToggleButton statusToggleButton;
    @FXML
    Button afficherPieceButton;
    @FXML
    WebView mapWebView;
    @FXML
    JFXSpinner spinner;
    @FXML
    VBox membersVbox;

    FTPInterface ftpInterface;
    private MembershipService membershipService = MembershipService.getInstace();
    private Association a;

    public AssociationProfileShowController() {
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
    public AssociationProfileShowController(Association association) {
        this.a = association;
        try {
            this.ftpInterface = FTPInterface.getInstance(URLServer.ftpServerLink, URLServer.ftpSocketPort, URLServer.ftpUser, URLServer.ftpPassword);
        } catch (IOException e) {
            // TODO : Log
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameLabel.setText(a.getNom());
        descriptionLabel.setText(a.getDescription());
        numeroLabel.setText(a.getTelephone() + " " + a.getVille() + " " + a.getRue() + " " + a.getCodePostal() + " " + a.getHoraireTravail());
        statusToggleButton.selectedProperty().bindBidirectional(new SimpleBooleanProperty(a.isApprouved()));

        statusToggleButton.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean) {
                Dialog<String> confDialog = new TextInputDialog("");
                confDialog.setContentText("Veuillez indiquer la raison :");
                Optional<String> result = confDialog.showAndWait();
                if (result.isPresent()) {
                    String entered = result.get();
                    System.out.println(a.isApprouved());
                    // TODO : Disable
                    // TODO : Send email with reason {entered}
                } else {
                    statusToggleButton.selectedProperty().setValue(true);
                    // TODO : Send confirmation email
                }
            }
        });
        mapWebView.setVisible(false);
        mapWebView.getEngine().load(this.getClass().getResource("/Packages/Chihab/Scenes/WebView/showAssociationLocation.html").toString());
        mapWebView.getEngine().getLoadWorker().stateProperty().addListener(
                (ChangeListener<? super Worker.State>) (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        spinner.setVisible(false);
                        mapWebView.setVisible(true);
                        mapWebView.getEngine().executeScript("initMap(" + a.getLat() + "," + a.getLon() + ")");
                        Document document = mapWebView.getEngine().getDocument();
                        System.out.println(document.getElementById("lat_association").getTextContent());
                    }
                }
        );

        try {
            File imageAss = ftpInterface.downloadFile(URLServer.associationImageDir + a.getPhotoAgence(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            imageImageView.setImage(new Image(imageAss.toURI().toString()));
        } catch (IOException e) {
            Logger.getLogger(
                    AssociationProfileShowController.class.getName()).log(
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
                        AssociationsBackofficeController.class.getName()).log(
                        Level.SEVERE, null, e
                );
                Alert connAlert = new Alert(Alert.AlertType.WARNING);
                connAlert.setContentText("Error whilst fetching " + a.getPieceJustificatif() + " from FTP server");
                connAlert.show();
            }
        });
        FXMLLoader loader = null;
        try {
            for (Membership m : membershipService.readAll()) {
                loader = new FXMLLoader(getClass().getResource(URLScenes.memberShipItem));
                MemberItemController controller = new MemberItemController(m);
                loader.setController(controller);
                membersVbox.getChildren().add(loader.load());
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

}
