package Main;

import Main.Entities.UserSession;
import Packages.Chihab.Controllers.AssociationsBackofficeController;
import SharedResources.URLScenes;
import SharedResources.URLServer;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeController implements Initializable {
    @FXML private JFXTabPane tabPane;
    @FXML private JFXHamburger hamburger;
    @FXML private JFXDrawer drawer;
    private final Tab associationsTab = new Tab(), domainesTab = new Tab(), missionsTab = new Tab(), refugeesTab = new Tab(), helpdeskTab = new Tab();
    private final Tab donTab = new Tab();
    private final FXMLLoader loader = new FXMLLoader(getClass().getResource(URLScenes.sidePanel));
    private HamburgerBackArrowBasicTransition transition;
    private final AssociationsBackofficeController associationsMain = new AssociationsBackofficeController();
    private final VBox box;
    private final Label username;
    private final Label fullname;
    private final Circle circle;
    private final JFXButton associations;
    private final JFXButton logoutButton;
    private final JFXButton profileButton;
    private final JFXButton domaines;
    private final JFXButton missions;
    private final JFXButton refugees;
    private final JFXButton refugeesUser;
    private final JFXButton helpdesk;
    private final JFXButton don;


    public HomeController() throws IOException {
        box = loader.load();
        // Side panel header
        username = (Label) loader.getNamespace().get("username");
        fullname = (Label) loader.getNamespace().get("fullname");
        circle = (Circle) loader.getNamespace().get("circle");
        logoutButton = (JFXButton) loader.getNamespace().get("logout");
        profileButton = (JFXButton) loader.getNamespace().get("profile");

        this.associations = (JFXButton) loader.getNamespace().get("chihabAssociation");
        this.associationsTab.setText("Associations");
        this.associationsTab.setContent(associationsMain);


        this.don=(JFXButton) loader.getNamespace().get("donButton");



        domaines = (JFXButton) loader.getNamespace().get("chihabDomaines");
        missions = (JFXButton) loader.getNamespace().get("mohamedMissions");
        refugees = (JFXButton) loader.getNamespace().get("nasriRefugees");
        refugeesUser = (JFXButton) loader.getNamespace().get("nasriRefugeesUser");
        helpdesk = (JFXButton) loader.getNamespace().get("ramyHelpesk");

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        transition = new HamburgerBackArrowBasicTransition(hamburger);
        this.transition.setRate(-1);
        try {
            username.setText(UserSession.getInstace().getUser().getUsername());
            fullname.setText((UserSession.getInstace().getUser().getProfile().getNom() + " " + UserSession.getInstace().getUser().getProfile().getPrenom()).toUpperCase());
            File image = FTPInterface.getInstance().downloadFile(URLServer.userImageDir + UserSession.getInstace().getUser().getProfile().getImage(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            circle.setFill(new ImagePattern(new Image(image.toURI().toString())));
            // Side panel header
            // Buttons
            logoutButton.setOnMouseClicked(mouseEvent -> {
                System.exit(0);
            });
            profileButton.setOnMouseClicked(mouseEvent -> {
                // TODO : this
            });
            // !!!!!!!!!!!!!! ------------------------ Add your buttons here ------------------------ !!!!!!!!!!!!!! \\
            /** Chihab */
            associations.setOnMouseClicked(mouseEvent -> {
                changeTab(associationsTab);
            });

            domaines.setOnMouseClicked(mouseEvent -> {
                this.domainesTab.setText("Domaine's d'activité");
                try {
                    this.domainesTab.setContent(FXMLLoader.load(getClass().getResource(URLScenes.domaines)));
                } catch (IOException e) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, "Exception loading create FXML", e);
                }
                changeTab(domainesTab);
            });
            /** Mohamed */

            missions.setOnMouseClicked(mouseEvent -> {
                this.missionsTab.setText("Missions de bénévolat");
                try {
                    this.missionsTab.setContent(FXMLLoader.load(getClass().getResource(URLScenes.missionDashbord)));
                } catch (IOException e) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, "Exception loading create FXML", e);
                }
                changeTab(missionsTab);
            });
            /** Nasri */

            refugees.setVisible(UserSession.getInstace().getUser().isAdmin());
            refugees.setOnMouseClicked(mouseEvent -> {
                this.refugeesTab.setText("Refugee");
                try {
                    URL scene = getClass().getResource(URLScenes.refugeesDashboardMainScene);
                    Stage stage = new Stage();
                    Parent root = FXMLLoader.load(scene);
                    Scene refugeeScene = new Scene(root);
                    stage.setScene(refugeeScene);
                    stage.centerOnScreen();
                    stage.setResizable(false);
                    stage.show();
                } catch (IOException e) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, "Exception loading create FXML", e);
                }
                changeTab(refugeesTab);
            });

            refugeesUser.setVisible(!UserSession.getInstace().getUser().isAdmin());
            refugeesUser.setOnMouseClicked(mouseEvent -> {
                this.refugeesTab.setText("Refugee");
                try {
                    //this.refugeesTab.setContent(FXMLLoader.load(getClass().getResource(URLScenes.refugeesDashboardMainScene)));
                    URL scene = getClass().getResource(URLScenes.refugeesUserMainScene);
                    Stage stage = new Stage();
                    Parent root = FXMLLoader.load(scene);
                    Scene refugeeScene = new Scene(root);
                    stage.setScene(refugeeScene);
                    stage.centerOnScreen();
                    stage.setResizable(false);
                    stage.show();
                } catch (IOException e) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, "Exception loading create FXML", e);
                }
                changeTab(refugeesTab);
            });
            /** Ramy */
            helpdesk.setVisible(UserSession.getInstace().getUser().isAdmin());
            helpdesk.setOnMouseClicked(mouseEvent -> {
                this.helpdeskTab.setText("Helpdesk");
                try {
                    this.helpdeskTab.setContent(FXMLLoader.load(getClass().getResource(URLScenes.Requetefxml)));
                } catch (IOException e) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, "Exception loading create FXML", e);
                }
                changeTab(helpdeskTab);
            });
            /** Issam */




            don.setOnMouseClicked(mouseEvent -> {



                        this.donTab.setText("donnation");
                        try {
                            if (UserSession.getInstace().getUser().isAdmin())
                                this.donTab.setContent(FXMLLoader.load(getClass().getResource(URLScenes.donDash)));

                            else

                            this.donTab.setContent(FXMLLoader.load(getClass().getResource(URLScenes.donMain)));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        changeTab(donTab);

                    }

                    );




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
