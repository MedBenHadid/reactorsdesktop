package Packages.Issam.Controller;

import Packages.Issam.Models.Don;
import SharedResources.URLScenes;
import SharedResources.URLServer;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DonProfileShowController  extends StackPane implements Initializable  {

    @FXML private Label donNameLabel;
    @FXML private ImageView imageImageView;
    @FXML private TextArea descInput ;
    @FXML private TextField phoneInput , addrInput;
    @FXML private Button SmsButton ;
    @FXML private WebView mapWebViews;

    private final Don don;

    private final FXMLLoader thisLoader = new FXMLLoader( getClass().getResource(URLScenes.donProfile) );
    /**
     *
     * @param don
     */
    public DonProfileShowController(Don don) {
        this.don = don;
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
        donNameLabel.setText(don.getTitle());
        descInput.setText(don.getDescription());
        addrInput.setText(don.getPhone());
        phoneInput.setText(don.getAddress());


        mapWebViews.setVisible(false);
        mapWebViews.getEngine().load(this.getClass().getResource("/Packages/Issam/Scenes/WebView/showDonLocation.html").toString());
        mapWebViews.getEngine().getLoadWorker().stateProperty().addListener(
                (ChangeListener<? super Worker.State>) (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        mapWebViews.setVisible(true);
                        mapWebViews.getEngine().executeScript("initMap(" + don.getLat() + "," + don.getLon() + ")");
                        Document document = mapWebViews.getEngine().getDocument();
                    }
                }
        );

        try {
            File imageAss = FTPInterface.getInstance().downloadFile(URLServer.donImageDir + don.getImage(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            imageImageView.setImage(new Image(imageAss.toURI().toString()));
        } catch (IOException e) {
            Logger.getLogger(DonProfileShowController.class.getName()).log(Level.SEVERE, "Error whilst fetching image", e);
            Alert photoMissing = new Alert(Alert.AlertType.WARNING);
            photoMissing.setContentText("Error whilst fetching photo");
            photoMissing.show();
        }

        SmsButton.setOnAction(e -> {

            String ACCOUNT_SID = "ACaceab88f5e2e1f1c06a9e3cc785c59f2";
            String AUTH_TOKEN = "37c61524e9caf746710eeb6374437118";


            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            Message message = Message.creator(new PhoneNumber("+21652663728"),
                    new PhoneNumber("+12054967545"),
                    "intrested about your post its cal mee on  " +don.getPhone()+"am in" + don.getAddress()).create();
            System.out.println(message.getSid());


        });

    }}
