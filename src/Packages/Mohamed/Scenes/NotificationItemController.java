package Packages.Mohamed.Scenes;

import Main.Entities.User;
import Packages.Mohamed.Entities.Notification;
import SharedResources.URLScenes;
import SharedResources.URLServer;
import SharedResources.Utils.FTPInterface.FTPInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationItemController extends HBox implements Initializable {
    @FXML
    private ImageView ImgAssNotif;
    @FXML
    private Label titleNotif;
    @FXML
    private Label dateNotif;


    private final Notification n;

    public NotificationItemController(Notification n) {
        this.n = n;

        FXMLLoader thisLoader = new FXMLLoader(getClass().getResource(URLScenes.NotifItem));
        thisLoader.setRoot( this );
        thisLoader.setController( this );
        try {
            thisLoader.load();
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(n.getId()+" : "+n.getSeen());
        if(!n.getSeen()){
            Image notSeenImage = new Image(new File("/SharedResources/Images/notificationnotseen.png").toURI().toString());
            //  Image image = new Image(new FileInputStream("../Images/notificationnotseen.png"));
      //      isSeenImageView.setImage(notSeenImage);

        }
        titleNotif.textProperty().setValue(n.getTitle());
        Date date = (Date) n.getNotification_date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        dateNotif.setText(dateFormat.format(date));

    try {
            File imageAss = FTPInterface.getInstance().downloadFile(URLServer.associationImageDir + n.getId_association().getPhotoAgence(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
        assert imageAss != null;
        ImgAssNotif.setImage(new Image(imageAss.toURI().toString()));
        } catch (IOException e) {
            Logger.getLogger(
                    NotificationItemController.class.getName()).log(
                    Level.SEVERE, "Error whilst fetching image", e
            );
        }
    }


}
