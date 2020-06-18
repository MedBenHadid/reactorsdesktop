package Packages.Issam.Controller;

import Packages.Chihab.Models.Entities.Category;
import Packages.Chihab.Services.CategoryService;
import Packages.Issam.Models.Demande;
import Packages.Issam.Services.DemandeService;
import SharedResources.URLServer;
import SharedResources.Utils.AutoCompleteBox;
import SharedResources.Utils.BinaryValidator.RegexValidator;
import SharedResources.Utils.ComboBoxAutoComplete;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DemandeCreateController implements Initializable {


    private static DemandeService instance ;
    @FXML
    private AnchorPane rootPane;
    private final Demande demande ;
    public List<Category> checkedList = new ArrayList<>();
    @FXML
    private JFXTextField TitleInput, addressInput, phoneInput , ribInput;
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
    private Connection connection;
    private File file;
    private final FileChooser photoChooser = new FileChooser();
    @FXML
    private ImageView imageView ;

    private final SimpleBooleanProperty photoValid = new SimpleBooleanProperty(true);

    public DemandeCreateController() {
        this.demande = new Demande();

            FTPInterface.getInstance();
    }

    public Demande getDemande() {
        return demande;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator("Champ manquant");


        TitleInput.getValidators().addAll(new RegexValidator("Veuillez saisir un titre dont la taille est comprise entre 3 et 30", "^[\\w\\s]{3,10}$"));
        TitleInput.focusedProperty().addListener((o, a, t) -> {
            if (!t) TitleInput.validate();
            if (TitleInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                demande.setTitle(TitleInput.getText());
        });
        TitleInput.setOnKeyTyped(keyEvent -> TitleInput.validate());

        descriptionInput.getValidators().addAll(requiredFieldValidator,new RegexValidator("Veuillez saisir une description valide", "^[\\w\\s]{8,30}$"));
        descriptionInput.focusedProperty().addListener((o, a, t) -> {
            if (!t) descriptionInput.validate();
            if (descriptionInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                demande.setDescription(descriptionInput.getText());
        });
        descriptionInput.setOnKeyTyped(keyEvent -> descriptionInput.validate());

        addressInput.getValidators().addAll(requiredFieldValidator,new RegexValidator("Veuillez saisir une adresse valide", "^[\\w\\s]{3,30}$"));
        addressInput.focusedProperty().addListener((o, a, t) -> {
            if (!t) addressInput.validate();
            if (addressInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                demande.setAddress(addressInput.getText());
        });
        addressInput.setOnKeyTyped(keyEvent -> addressInput.validate());



        phoneInput.getValidators().addAll(new RegexValidator("Veuillez saisir u numero de telephone valide", "^[\\d]{8}$"));
        phoneInput.focusedProperty().addListener((o, a, t) -> {
            if (!t) phoneInput.validate();
            if (phoneInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                demande.setAddress(phoneInput.getText());
        });
        phoneInput.setOnKeyTyped(keyEvent -> phoneInput.validate());



        ribInput.getValidators().addAll(new RegexValidator("Veuillez saisir u numero de telephone valide", "^[\\d]{8}$"));
        ribInput.focusedProperty().addListener((o, a, t) -> {
            if (!t) ribInput.validate();
            if (ribInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                demande.setAddress(ribInput.getText());
        });
        ribInput.setOnKeyTyped(keyEvent -> ribInput.validate());


        new AutoCompleteBox<String>(categoryComboBox);
        try {

            categoryComboBox.setItems(CategoryService.getInstace().readAll());
            categoryComboBox.setOnHiding(event -> {
                demande.setCategory(categoryComboBox.getSelectionModel().getSelectedItem());
                System.out.println(demande.getCategory().getId());
            });

            new ComboBoxAutoComplete<>(categoryComboBox);
        } catch (Exception e) {
            Logger.getLogger(
                    DonCreateController.class.getName()).log(
                    Level.SEVERE, null, e
            );
            e.printStackTrace();
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
            file = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
            if (file == null) {
                photoValid.setValue(false);
                imageView.setImage(null);
            }
            else {
                demande.setImage(file.getName());
                photoValid.setValue(true);
                imageView.setImage(new Image(file.toURI().toString()) );
            }
        });
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
            else if (!ribInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                showDialog(Alert.AlertType.ERROR, "Creation échoué ", "Raison : Reference", "Déscription non valide non valdie");
          else {
                try {

                    demande.setTitle(TitleInput.getText());
                    demande.setDescription(descriptionInput.getText());
                    demande.setAddress(addressInput.getText());
                    demande.setPhone(phoneInput.getText());
                    demande.setRib(ribInput.getText());
                    FTPInterface.getInstance().send(file, URLServer.donImageDir);
                    DemandeService.getInstance().create(demande);
                } catch (Exception ex) {
                    ex.printStackTrace();
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
