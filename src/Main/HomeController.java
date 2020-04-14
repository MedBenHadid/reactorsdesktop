package Main;

import Main.Entities.UserSession;
import SharedResources.URLScenes;
import SharedResources.URLServer;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeController implements Initializable {
    private final UserSession userSession;
    private final Tab associationsTab;
    private final Tab domainesTab;
    private final Tab missionsTab;
    @FXML
    private JFXTabPane tabPane;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;
    private FTPInterface ftp;
    private HamburgerBackArrowBasicTransition transition;
    private Tab acceuilTab;

    public HomeController() {
        this.userSession = UserSession.getInstace();
        try {
            this.ftp = FTPInterface.getInstance(URLServer.ftpServerLink, URLServer.ftpSocketPort, URLServer.ftpUser, URLServer.ftpPassword);
        } catch (IOException e) {
            Logger.getLogger(
                    HomeController.class.getName()).log(
                    Level.WARNING, "Exception loading FTP Interface", e
            );
        }


        this.associationsTab = new Tab();
        this.associationsTab.setText("Associations");
        try {
            this.associationsTab.setContent(FXMLLoader.load(getClass().getResource(URLScenes.associationSuperAdminDashboard)));
        } catch (IOException e) {
            Logger.getLogger(
                    HomeController.class.getName()).log(
                    Level.SEVERE, "Exception loading AssociationDashboard FXML", e
            );
        }
        this.domainesTab = new Tab();
        this.domainesTab.setText("Domaine's d'activité");
        try {
            this.domainesTab.setContent(FXMLLoader.load(getClass().getResource(URLScenes.domaines)));
        } catch (IOException e) {
            Logger.getLogger(
                    HomeController.class.getName()).log(
                    Level.SEVERE, "Exception loading create FXML", e
            );
        }
        this.missionsTab = new Tab();
        this.missionsTab.setText("Missions de bénévolat");
        try {
            this.missionsTab.setContent(FXMLLoader.load(getClass().getResource(URLScenes.missionDashbord)));
        } catch (IOException e) {
            Logger.getLogger(
                    HomeController.class.getName()).log(
                    Level.SEVERE, "Exception loading create FXML", e
            );
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(-1);

        VBox box = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(URLScenes.sidePanel));
            box = loader.load();
            // Side panel header
            Label username = (Label) loader.getNamespace().get("username");
            Label fullname = (Label) loader.getNamespace().get("fullname");
            username.setText(this.userSession.getUser().getUsername());
            fullname.setText((this.userSession.getUser().getProfile().getNom() + " " + this.userSession.getUser().getProfile().getPrenom()).toUpperCase());
            Circle circle = (Circle) loader.getNamespace().get("circle");
            File image = this.ftp.downloadFile(URLServer.userImageDir + this.userSession.getUser().getProfile().getImage(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            circle.setFill(new ImagePattern(new Image(image.toURI().toString())));
            JFXRippler rippler = (JFXRippler) loader.getNamespace().get("rippler");
            // Side panel header
            // Buttons
            JFXButton logoutButton = (JFXButton) loader.getNamespace().get("logout");
            logoutButton.setOnMouseClicked(mouseEvent -> {
                // TODO : this
            });
            JFXButton profileButton = (JFXButton) loader.getNamespace().get("profile");
            profileButton.setOnMouseClicked(mouseEvent -> {
                // TODO : this
            });
            // !!!!!!!!!!!!!! ------------------------ Add your buttons here ------------------------ !!!!!!!!!!!!!! \\
            JFXButton associations = (JFXButton) loader.getNamespace().get("chihabAssociation");
            associations.setOnMouseClicked(mouseEvent -> {
                changeTab(associationsTab);
            });
            JFXButton domaines = (JFXButton) loader.getNamespace().get("chihabDomaines");
            domaines.setOnMouseClicked(mouseEvent -> {
                changeTab(domainesTab);
            });
            JFXButton missions = (JFXButton) loader.getNamespace().get("mohamedMissions");
            missions.setOnMouseClicked(mouseEvent -> {
                changeTab(missionsTab);
            });

            // !!!!!!!!!!!!!! ------------------------ Add your buttons here ------------------------ !!!!!!!!!!!!!! \\
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            drawer.setContent(box);
            drawer.setSidePane(box);
        }
        transition.rateProperty().addListener((observableValue, number, t1) -> {
            if (t1.intValue() == 1)
                drawer.open();
            else
                drawer.close();
            transition.play();
        });
        tabPane.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            transition.setRate(-1);
        });
    }

    @FXML
    void onHamburgerClicked(MouseEvent event) {
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            transition.setRate(transition.getRate() * -1);
        });
    }

    void changeTab(Tab tab) {
        if (!tabPane.getTabs().contains(tab))
            tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        transition.setRate(-1);
    }

}
