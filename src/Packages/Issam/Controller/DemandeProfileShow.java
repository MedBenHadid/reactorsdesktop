package Packages.Issam.Controller;

import Packages.Issam.Models.Demande;
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
public class DemandeProfileShow  extends StackPane implements Initializable  {

    @FXML private Label donNameLabel;
    @FXML private ImageView imageImageView;
    @FXML private TextArea descInput ;
    @FXML private TextField phoneInput , addrInput, ribInput;
    @FXML private Button SmsButton ;
    @FXML private WebView mapWebViews;

    private final Demande demande;

    private final FXMLLoader thisLoader = new FXMLLoader( getClass().getResource(URLScenes.demandeProfile) );
    /**
     *
     * @param demande
     */
    public DemandeProfileShow(Demande demande) {
        this.demande = demande;
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
        donNameLabel.setText(demande.getTitle());
        descInput.setText(demande.getDescription());
        addrInput.setText(demande.getPhone());
        phoneInput.setText(demande.getAddress());
        ribInput.setText(demande.getRib());



        try {
            File imageAss = FTPInterface.getInstance().downloadFile(URLServer.donImageDir + demande.getImage(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            imageImageView.setImage(new Image(imageAss.toURI().toString()));
        } catch (IOException e) {
            Logger.getLogger(DemandeProfileShow.class.getName()).log(Level.SEVERE, "Error whilst fetching image", e);
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
                    "intrested about your post its cal mee on  " +demande.getPhone()+"am in" + demande.getAddress()).create();
            System.out.println(message.getSid());


        });


    }}
