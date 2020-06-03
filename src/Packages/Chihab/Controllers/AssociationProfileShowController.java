package Packages.Chihab.Controllers;

import Packages.Chihab.Models.Entities.Association;
import Packages.Chihab.Models.Entities.Membership;
import Packages.Chihab.Services.MembershipService;
import SharedResources.URLScenes;
import SharedResources.URLServer;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.JFXSpinner;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AssociationProfileShowController extends AnchorPane implements Initializable {
    @FXML private Label nameLabel, descriptionLabel, numeroLabel;
    @FXML private ImageView imageImageView;
    @FXML private WebView mapWebView;
    @FXML private JFXSpinner spinner;
    @FXML private VBox membersVbox;
    private final Association association;
    private FXMLLoader loader = null;

    private final FXMLLoader thisLoader = new FXMLLoader( getClass().getResource(URLScenes.associationProfile) );
    /**
     * Accepts an Association type and stores it to specific instance variables
     * in order to show its profile ( Preferably in a new TabPane )
     *
     * @param association
     */
    public AssociationProfileShowController(Association association) {
        this.association = association;
        thisLoader.setRoot( this );
        thisLoader.setController( this );
        try {
            thisLoader.load();
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameLabel.textProperty().bind(association.nomProperty());
        descriptionLabel.textProperty().bind(association.descriptionProperty());
        numeroLabel.textProperty().bind(association.telephoneProperty().asString());
        //  + " " + a.getVille() + " " + a.getRue() + " " + a.getCodePostal() + " " + a.getHoraireTravail()
        mapWebView.setVisible(false);
        mapWebView.getEngine().load(this.getClass().getResource("/Packages/Chihab/Scenes/WebView/showAssociationLocation.html").toString());
        mapWebView.getEngine().getLoadWorker().stateProperty().addListener(
                (ChangeListener<? super Worker.State>) (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        spinner.setVisible(false);
                        mapWebView.setVisible(true);
                        mapWebView.getEngine().executeScript("initMap(" + association.getLat() + "," + association.getLon() + ")");
                        Document document = mapWebView.getEngine().getDocument();
                    }
                }
        );

        try {
            File imageAss = FTPInterface.getInstance().downloadFile(URLServer.associationImageDir + association.getPhotoAgence(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            imageImageView.setImage(new Image(imageAss.toURI().toString()));
        } catch (IOException e) {
            Logger.getLogger(AssociationProfileShowController.class.getName()).log(Level.SEVERE, "Error whilst fetching image", e);
            Alert photoMissing = new Alert(Alert.AlertType.WARNING);
            photoMissing.setContentText("Error whilst fetching photo");
            photoMissing.show();
        }/**
        afficherPieceButton.setOnAction(event -> {
            try {
                File file = FTPInterface.getInstance().downloadFile(URLServer.associationPieceDir + association.getPieceJustificatif(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
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
                connAlert.setContentText("Error whilst fetching " + association.get().getPieceJustificatif() + " from FTP server");
                connAlert.show();
            }
        });*/
        // TODO : Show by association you dumbass
        try {
            for (Membership m : MembershipService.getInstace().readAll()) {
                if(m.getStatus().equals(Membership.ACCEPTED)){
                    loader = new FXMLLoader(getClass().getResource(URLScenes.memberShipItem));
                    MemberItemController controller = new MemberItemController(m);
                    loader.setController(controller);
                    membersVbox.getChildren().add(loader.load());
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

}
