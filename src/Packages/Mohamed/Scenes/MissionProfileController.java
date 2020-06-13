package Packages.Mohamed.Scenes;

import Main.Entities.UserSession;
import Packages.Mohamed.Entities.Invitation;
import Packages.Mohamed.Entities.Mission;
import Packages.Mohamed.Services.MissionService;
import Packages.Mohamed.util.sendMail;
import SharedResources.URLScenes;
import SharedResources.URLServer;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.*;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MissionProfileController implements Initializable {
    @FXML
    Label t, descriptionLabel, DateDebLabel, DateFinLabel, ObjectifLabel, SumColLabel, pourcantageLabel;
    @FXML
    ImageView imageImageView;
    @FXML
    WebView mapWebView;
    @FXML
    VBox membersVbox;
    @FXML
    JFXProgressBar progressBar;
    @FXML
    StackPane profileStack;

    private Mission m;

    public MissionProfileController() {
    }

    /**
     * Accepts an Association type and stores it to specific instance variables
     * in order to show its profile ( Preferably in a new TabPane )
     *
     * @param mission
     */
    public MissionProfileController(Mission mission) {
        this.m = mission;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(m.getTitleMission());
        t.textProperty().setValue(m.getTitleMission());
        descriptionLabel.setText(m.getDescription());
        DateDebLabel.setText(m.getDateCreation().toString());
        DateFinLabel.setText(m.getDateFin().toString());
        ObjectifLabel.setText(String.valueOf(m.getObjectif()));
        SumColLabel.setText(String.valueOf(m.getSumCollected()));
        pourcantageLabel.setText((100 * m.getSumCollected()) / m.getObjectif() + "%");
        mapWebView.setVisible(false);
        mapWebView.getEngine().load(this.getClass().getResource("/Packages/Mohamed/Scenes/WebView/showMissionLocation.html").toString());
        mapWebView.getEngine().getLoadWorker().stateProperty().addListener(
                (ChangeListener<? super Worker.State>) (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        mapWebView.setVisible(true);
                        mapWebView.getEngine().executeScript("initMap(" + m.getLat() + "," + m.getLon() + ")");
                        Document document = mapWebView.getEngine().getDocument();
                        System.out.println(document.getElementById("lat_mission").getTextContent());
                    }
                }
        );
        progressBar.progressProperty().setValue(((100 * m.getSumCollected()) / m.getObjectif()) / 100);
   try {
         File imageAss = FTPInterface.getInstance().downloadFile(URLServer.missionImageDir + m.getPicture(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
         imageImageView.setImage(new Image(imageAss.toURI().toString()));
         } catch (IOException e) {
         Logger.getLogger(
         MissionProfileController.class.getName()).log(
         Level.SEVERE, "Error whilst fetching image", e
         );
         }


        FXMLLoader loader = null;
        try {
            ArrayList<Invitation> invits = MissionService.getInstace().getInvitationsByMission(m);
            for (Invitation i : invits) {
                loader = new FXMLLoader(getClass().getResource(URLScenes.UserItem));
                UserItemController controller = new UserItemController(i.getId_user());
                loader.setController(controller);
                HBox h = loader.load();
                h.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2) {
                        JFXDialogLayout layout = new JFXDialogLayout();
                        layout.setHeading(new Text("Emailing " + i.getId_user().getProfile().getNom()));
                        JFXTextArea tr = new JFXTextArea();
                        tr.setPromptText("Veuillez saisir la raison");
                        //tr.getValidators().add(new RegexValidator("La raison doit etre comprise entre 5 et 255", "^[\\d\\w]{5,255}"));
                        //tr.setOnKeyTyped(e -> tr.validate());
                        layout.setBody(tr);
                        JFXDialog dialog = new JFXDialog(profileStack, layout, JFXDialog.DialogTransition.CENTER);
                        JFXButton verify = new JFXButton("Désapprouver");
                        JFXButton close = new JFXButton("Fermer");
                        close.getStyleClass().addAll("jfx-button-error");
                        close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> dialog.close());
                        verify.disableProperty().bind(Bindings.createBooleanBinding(() -> !tr.textProperty().get().matches("^[\\d\\w]{5,255}"), tr.textProperty()));
                        verify.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                            dialog.close();
                            JFXDialogLayout l = new JFXDialogLayout();
                            l.setHeading(new Text("Contact "));
                            l.setBody(new Label("Modification enregistré avec success"));
                            JFXDialog d = new JFXDialog(profileStack, l, JFXDialog.DialogTransition.CENTER);
                            JFXButton fermer = new JFXButton("Fermer");
                            fermer.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> d.close());
                            l.setActions(fermer);
                            d.show();
                            System.out.println(tr.getText());

                            try {
                               // assert UserSession.getInstace() != null;
                                sendMail.sendMailToAdmin(UserSession.getInstace().getUser().getEmail(), i.getId_user().getEmail(), tr.getText());
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println(UserSession.getInstace().getUser().getEmail());
                            }


                            // TODO : Email

                        });
                        layout.setActions(close, verify);
                        dialog.show();
                        System.out.println("___________");
                    }
                });
                membersVbox.getChildren().add(h);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }


    }

}
