package Packages.Issam.Controller;


import Packages.Chihab.Models.Entities.Category;
import Packages.Chihab.Services.CategoryService;
import Packages.Issam.Models.Don;
import Packages.Issam.Services.DonService;

import SharedResources.URLServer;
import SharedResources.Utils.AutoCompleteBox;
import SharedResources.Utils.BinaryValidator.RegexValidator;
import SharedResources.Utils.ComboBoxAutoComplete;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import netscape.javascript.JSObject;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DonCreateController implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXTextField TitleInput, addressInput, phoneInput;
    @FXML
    private JFXTextArea descriptionInput;
    @FXML
    private JFXButton createButton;
    @FXML
    private JFXButton photoButton;
    @FXML
    private WebView gmapWebView;
    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private ImageView imageView ;

    private final FileChooser photoChooser = new FileChooser();
    private final FileChooser fileChooser = new FileChooser();
    private final SimpleBooleanProperty photoValid = new SimpleBooleanProperty(false);
    private final Don don ;
    private File file;

    public DonCreateController(){
        this.don = new Don();
    }
     public Don getDon(){return don;}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gmapWebView.getEngine().load(this.getClass().getResource("/Packages/Issam/Scenes/WebView/getDonLocation.html").toString());
        RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator("Champ manquant");

        TitleInput.getValidators().addAll(new RegexValidator("Veuillez saisir un titre dont la taille est comprise entre 3 et 30", "^[\\w\\s]{3,10}$"));
        TitleInput.focusedProperty().addListener((o, a, t) -> {
            if (!t) TitleInput.validate();
            if (TitleInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                don.setTitle(TitleInput.getText());
        });
        TitleInput.setOnKeyTyped(keyEvent -> TitleInput.validate());


        descriptionInput.getValidators().addAll(requiredFieldValidator,new RegexValidator("Veuillez saisir une description valide", "^[\\w\\s]{8,30}$"));
        descriptionInput.focusedProperty().addListener((o, a, t) -> {
            if (!t) descriptionInput.validate();
            if (descriptionInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                don.setDescription(descriptionInput.getText());
        });
        descriptionInput.setOnKeyTyped(keyEvent -> descriptionInput.validate());

        addressInput.getValidators().addAll(requiredFieldValidator,new RegexValidator("Veuillez saisir une adresse valide", "^[\\w\\s]{3,30}$"));
        addressInput.focusedProperty().addListener((o, a, t) -> {
            if (!t) addressInput.validate();
            if (addressInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                don.setAddress(addressInput.getText());
        });
        addressInput.setOnKeyTyped(keyEvent -> addressInput.validate());

        phoneInput.getValidators().addAll(new RegexValidator("Veuillez saisir u numero de telephone valide", "^[\\d]{8}$"));
        phoneInput.focusedProperty().addListener((o, a, t) -> {
            if (!t) phoneInput.validate();
            if (phoneInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                don.setAddress(phoneInput.getText());
        });
        phoneInput.setOnKeyTyped(keyEvent -> phoneInput.validate());



        new AutoCompleteBox<String>(categoryComboBox);
        categoryComboBox.setItems(CategoryService.getInstace().readAll());
        categoryComboBox.setOnHiding(event -> {
            don.setCategory(categoryComboBox.getSelectionModel().getSelectedItem());
        });

        new ComboBoxAutoComplete<>(categoryComboBox);

        photoButton.setTooltip(new Tooltip("Veuillez choisir la photo de l'mission"));
        photoChooser.setTitle("Veuillez choisir la photo de l'association");
        photoChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        photoButton.setOnMouseClicked(e -> {
            photoValid.setValue(true);
            file = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
            if (file == null) {
                photoValid.setValue(false);
                imageView.setImage(null);
            }
            else {
                don.setImage(file.getName());
                photoValid.setValue(true);
                imageView.setImage(new Image(file.toURI().toString()) );
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

        createButton.setOnAction(e -> {
            if (photoValid.get()==false)
                showDialog(Alert.AlertType.ERROR, "Creation échoué ", "Raison : Reference", "vous devez choisir une image");
            else if(!TitleInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                showDialog(Alert.AlertType.ERROR, "Creation échoué ", "Raison : Reference", "vous devez tape un titre");
            else if (!addressInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                showDialog(Alert.AlertType.ERROR, "Creation échoué ", "Raison : Reference", "Adresse non valdie");
            else if (!phoneInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                showDialog(Alert.AlertType.ERROR, "Creation échoué ", "Raison : Reference", "numero telephone non valdie");
            else if (!descriptionInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                showDialog(Alert.AlertType.ERROR, "Creation échoué ", "Raison : Reference", "Déscription non valide non valdie");
            else{
                don.setLat((double) window.getMember("lat"));
                don.setLon((double) window.getMember("lon"));
                don.setTitle(TitleInput.getText());
                don.setDescription(descriptionInput.getText());
                don.setAddress(addressInput.getText());
                don.setPhone(phoneInput.getText());
                if(FTPInterface.getInstance().send(file, URLServer.donImageDir )){
                    if(DonService.getInstace().create(don)){
                        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmationDialog.setTitle("CREATION");
                        confirmationDialog.setHeaderText("Vous aller CREER lA DONNATION " + don.getTitle() + "!");
                        confirmationDialog.setContentText("Etes vous sure?");
                        Optional<ButtonType> confirmationResult = confirmationDialog.showAndWait();
                        MainUserController.getDialog().close();
                    } else {
                        System.out.println("Error db");
                    }
                }else {
                    System.out.println("error photo");
                }
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
