package Packages.Issam.Controller;

import Packages.Chihab.Models.Entities.Category;
import Packages.Chihab.Services.CategoryService;
import Packages.Issam.Models.Don;
import Packages.Issam.Services.DonService;
import SharedResources.URLScenes;
import SharedResources.URLServer;
import SharedResources.Utils.BinaryValidator.RegexValidator;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.stage.FileChooser;
import netscape.javascript.JSObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DonUpdateController extends StackPane implements Initializable {
    @FXML
    private Label donNameLabel;
    @FXML private ImageView imageImageView;
    @FXML private JFXTextArea descInput ;
    @FXML private JFXTextField phoneInput , addrInput , titleInput;
    @FXML private WebView mapWebViews;
    @FXML private JFXButton UpdateButton;
    private final Don don;
    @FXML
    private JFXComboBox<Category> categoryComboBox;
    private final FileChooser photoChooser = new FileChooser();
    @FXML
    private WebView gmapWebView;
    @FXML
    private JFXButton photoButton;
    private File file;
    private final SimpleBooleanProperty photoValid = new SimpleBooleanProperty(true);


    private final FXMLLoader thisLoader = new FXMLLoader( getClass().getResource(URLScenes.donUpdate) );

    public DonUpdateController(Don don) {
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
    public void initialize(URL url, ResourceBundle resourceBundle) {


        gmapWebView.getEngine().load(this.getClass().getResource("/Packages/Issam/Scenes/WebView/getDonLocation.html").toString());



        donNameLabel.setText(don.getTitle());
        titleInput.setText(don.getTitle());
        addrInput.setText(don.getAddress());
        phoneInput.setText(don.getPhone());
        categoryComboBox.setItems(CategoryService.getInstace().readAll());
        categoryComboBox.getSelectionModel().select(don.getCategory());
        descInput.setText(don.getDescription());




        titleInput.getValidators().addAll(new RegexValidator("Veuillez saisir un titre dont la taille est comprise entre 3 et 30", "^[\\w\\s]{3,10}$"));
        titleInput.focusedProperty().addListener((o, a, t) -> {
            if (!t) titleInput.validate();
            if (titleInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                don.setTitle(titleInput.getText());
        });
        titleInput.setOnKeyTyped(keyEvent -> titleInput.validate());


        descInput.getValidators().addAll(new RegexValidator("Veuillez saisir une Description dont la taille est comprise entre 3 et 30", "^[\\w\\s]{8,30}$"));
        descInput.focusedProperty().addListener((o, a, t) -> {
            if (!t) descInput.validate();
            if (descInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
            don.setDescription(descInput.getText());
        });
        descInput.setOnKeyTyped(keyEvent -> descInput.validate());

        addrInput.getValidators().addAll(new RegexValidator("Veuillez saisir une adresse valide", "^[\\w\\s]{3,30}$"));
        addrInput.focusedProperty().addListener((o, a, t) -> {
            if (!t) addrInput.validate();
            if (addrInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                don.setAddress(addrInput.getText());
        });
        addrInput.setOnKeyTyped(keyEvent -> addrInput.validate());


        phoneInput.getValidators().addAll(new RegexValidator("Veuillez saisir u numero de telephone valide", "^[\\d]{8}$"));
        phoneInput.focusedProperty().addListener((o, a, t) -> {
            if (!t) phoneInput.validate();
            if (phoneInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                don.setPhone(phoneInput.getText());
        });
        phoneInput.setOnKeyTyped(keyEvent -> phoneInput.validate());





        try {
            file = FTPInterface.getInstance().downloadFile(URLServer.donImageDir + don.getImage(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            imageImageView.setImage(new Image(file.toURI().toString()));
        } catch (IOException e) {
            Logger.getLogger(DonProfileShowController.class.getName()).log(Level.SEVERE, "Error whilst fetching image", e);
        }

        FileChooser fileChooser = new FileChooser();
        photoButton.setTooltip(new Tooltip("Veuillez choisir la photo de l'mission"));
        photoChooser.setTitle("Veuillez choisir la photo de l'association");
        photoChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        photoButton.setOnMouseClicked(e -> {
            photoValid.setValue(true);
            file = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (file == null) {
                photoValid.setValue(false);
                imageImageView.setImage(null);

            }
            else {
                don.setImage(file.getName());
                System.out.println(don.getImage());
                photoValid.setValue(true);
                imageImageView.setImage(new Image(file.toURI().toString()) );
            }
        });




        JSObject window = (JSObject) gmapWebView.getEngine().executeScript("window");
        gmapWebView.getEngine().load(this.getClass().getResource("/Packages/Issam/Scenes/WebView/getDonLocation.html").toString());
        gmapWebView.getEngine().getLoadWorker().stateProperty().addListener(
                (ChangeListener<? super Worker.State>) (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        String script = "initMap()";

                        gmapWebView.getEngine().executeScript(script);
                        window.setMember("don", new Don());

                    }
                }
        );



        UpdateButton.setOnAction(actionEvent -> {
            if (photoValid.get()==false)
                showDialog(Alert.AlertType.ERROR, "Creation échoué ", "Raison : Reference", "vous devez choisir une image");
            else if(!titleInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                showDialog(Alert.AlertType.ERROR, "Creation échoué ", "Raison : Reference", "vous devez tape un titre");
            else if (!addrInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                showDialog(Alert.AlertType.ERROR, "Creation échoué ", "Raison : Reference", "Adresse non valdie");
            else if (!phoneInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                showDialog(Alert.AlertType.ERROR, "Creation échoué ", "Raison : Reference", "numero telephone non valdie");
            else if (!descInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                showDialog(Alert.AlertType.ERROR, "Creation échoué ", "Raison : Reference", "Déscription non valide non valdie");
            else {
                don.setTitle(titleInput.getText());
                don.setAddress(addrInput.getText());
                don.setPhone(phoneInput.getText());
                don.setDescription(descInput.getText());
                don.setCategory(categoryComboBox.getSelectionModel().getSelectedItem());
                FTPInterface.getInstance().send(file, URLServer.donImageDir);
                try {
                    DonService.getInstace().update(don);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                MainUserController.getDialog().close();
            }
        });
}




    void showDialog(Alert.AlertType t, String title, String header, String context) {
        Alert a = new Alert(t);
        a.setTitle(title);
        a.setHeaderText(header);
        a.setContentText(context);
        a.showAndWait();
    }


}
