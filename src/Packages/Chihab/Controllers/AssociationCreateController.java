package Packages.Chihab.Controllers;

import Main.Entities.User;
import Main.Services.UserService;
import Packages.Chihab.Controllers.Custom.RegexValidator;
import Packages.Chihab.Controllers.Custom.StringLengthValidator;
import Packages.Chihab.Custom.AutoCompleteBox;
import Packages.Chihab.Custom.ComboBoxAutoComplete;
import Packages.Chihab.Models.Association;
import Packages.Chihab.Models.Category;
import Packages.Chihab.Services.AssociationService;
import Packages.Chihab.Services.CategoryService;
import SharedResources.URLServer;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.*;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import netscape.javascript.JSObject;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AssociationCreateController implements Initializable {
    @FXML
    private JFXTimePicker de, vers;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXTextField nomInput, rueInput, zipInput, phoneNumberInput;
    @FXML
    private JFXTextArea descriptionInput;
    @FXML
    private JFXButton validateButton;
    @FXML
    private JFXButton photoButton, pieceButton;
    @FXML
    private JFXComboBox<String> villeComboBox, managerComboBox, domaineComboBox;
    @FXML
    private WebView gmapWebView;

    private File photo, piece;
    private Association association;
    private FTPInterface ftpInterface;
    private boolean isRegisterPage = false;

    public AssociationCreateController() {
        this.association = new Association();
        try {
            this.ftpInterface = FTPInterface.getInstance(URLServer.ftpServerLink, URLServer.ftpSocketPort, URLServer.ftpUser, URLServer.ftpPassword);
        } catch (IOException e) {
            Alert ftpAlert = new Alert(Alert.AlertType.WARNING);
            ftpAlert.setContentText("Error connecting to FTP server");
            ftpAlert.show();
        }
    }

    public AssociationCreateController(User manager) {
        // INFO : Used when registering
        new AssociationCreateController();
        this.association.setManager(manager);
        managerComboBox.setVisible(false);
        this.isRegisterPage = true;
    }

    // TODO : implement the maps mcThingy
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator("Champ manquant");
        System.out.println();
        // Nom input validation
        vers.getValidators().add(requiredFieldValidator);
        vers.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) vers.validate();
        });
        nomInput.getValidators().addAll(new RegexValidator("Veuillez saisir un nom dont la taille est comprise entre 3 et 30", "^[\\w\\s]{3,30}$"));
        nomInput.focusedProperty().addListener((o, a, t) -> {
            if (!t) nomInput.validate();
            if (nomInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                association.setNom(nomInput.getText());
        });
        nomInput.setOnKeyTyped(keyEvent -> nomInput.validate());

        // Description validation
        descriptionInput.getValidators().addAll(new RegexValidator("Veuillez saisir une description valide 3-255", "^[\\w\\s]{3,255}$"), requiredFieldValidator);
        descriptionInput.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                descriptionInput.validate();
            }
        });
        descriptionInput.setOnKeyTyped(keyEvent -> {
            if (!descriptionInput.getText().isEmpty())
                descriptionInput.validate();
            if (!descriptionInput.getText().isEmpty())
                if (descriptionInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                    association.setDescription(descriptionInput.getText());
        });
        // Phone validation
        phoneNumberInput.getValidators().addAll(new NumberValidator("Veuillez saisir un numéro valide numérique"), new StringLengthValidator(8, 0, "Veuillez saisir un numéro dont la taille est "), requiredFieldValidator);
        phoneNumberInput.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                phoneNumberInput.validate();
                if (!phoneNumberInput.getText().isEmpty())
                    if (phoneNumberInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                        association.setTelephone(Integer.parseInt(phoneNumberInput.getText()));
            }
        });
        phoneNumberInput.setOnKeyTyped(keyEvent -> {
            if (!phoneNumberInput.getText().isEmpty())
                phoneNumberInput.validate();
        });
        // You get the gist of this dummy, gonna stop with the comments i'm tired
        rueInput.getValidators().addAll(new StringLengthValidator(8, -1, "La taille doit etre supérieure a"), requiredFieldValidator);
        rueInput.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                rueInput.validate();
            }
        });
        //Zip validation
        zipInput.getValidators().addAll(new RegexValidator("Veuillez saisir un code postale valide", "^[0-9]{4}?$"), requiredFieldValidator);
        zipInput.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                zipInput.validate();
            }
        });

        BooleanBinding nomFieldValid = Bindings.createBooleanBinding(() -> nomInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors), nomInput.getValidators());
        BooleanBinding descriptionFieldValid = Bindings.createBooleanBinding(() -> descriptionInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors), descriptionInput.getValidators());
        BooleanBinding zipFieldValid = Bindings.createBooleanBinding(() -> zipInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors), zipInput.getValidators());
        BooleanBinding phoneNumberFieldValid = Bindings.createBooleanBinding(() -> phoneNumberInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors), phoneNumberInput.getValidators());
        BooleanBinding versTimeFieldValid = Bindings.createBooleanBinding(() -> vers.getValidators().stream().noneMatch(ValidatorBase::getHasErrors), vers.getValidators());
        BooleanBinding rueFieldValid = Bindings.createBooleanBinding(() -> rueInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors), rueInput.getValidators());
        BooleanBinding villeComboSelected = Bindings.createBooleanBinding(() -> villeComboBox.getValidators().stream().noneMatch(ValidatorBase::getHasErrors), villeComboBox.getValidators());


        villeComboBox.setItems(FXCollections.observableArrayList("Ariana", "Béja", "Ben Arous", "Bizerte", "Gabes", "Gafsa", "Jendouba", "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine", "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan"));
        villeComboBox.setTooltip(new Tooltip("Sélectionner la ville de votre association"));
        new AutoCompleteBox<String>(villeComboBox);
        villeComboBox.getValidators().add(requiredFieldValidator);
        villeComboBox.setOnAction(actionEvent -> {
            villeComboBox.validate();
            if (villeComboBox.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                association.setVille(villeComboBox.getValue());
        });


        ObservableList<User> managers = null;
        ObservableList<Category> domaines = null;
        try {
            if (!isRegisterPage)
                managers = UserService.getInstace().readAll().stream().filter(u -> !u.isAdmin()).collect(Collectors.toCollection(FXCollections::observableArrayList));
            domaines = CategoryService.getInstace().readAll();
        } catch (Exception e) {
            Logger.getLogger(
                    AssociationCreateController.class.getName()).log(
                    Level.SEVERE, null, e.getCause()
            );
            e.printStackTrace();
        } finally {
            // TODO : Add regex
            assert domaines != null;
            domaineComboBox.getItems().addAll(domaines.stream().map(Category::getNom).collect(Collectors.toCollection(ArrayList::new)));
            domaineComboBox.getValidators().add(requiredFieldValidator);
            ObservableList<User> finalManagers = managers;
            ObservableList<Category> finalDomaines = domaines;
            domaineComboBox.setOnHiding(event -> {
                domaineComboBox.validate();
                if (domaineComboBox.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                    association.setDomaine(finalDomaines.stream().filter(s -> s.getNom().equals(domaineComboBox.getValue())).findFirst().get());
            });

            domaineComboBox.setVisibleRowCount(6);
            domaineComboBox.setTooltip(new Tooltip("Sélectionner le domaine d'activité de votre association"));
            new ComboBoxAutoComplete<>(domaineComboBox);
            if (isRegisterPage)
                managerComboBox.setVisible(false);
            else {
                managerComboBox.getItems().addAll(managers.stream().map(User::getEmail).collect(Collectors.toCollection(ArrayList::new)));
                managerComboBox.setVisibleRowCount(6);
                domaineComboBox.setTooltip(new Tooltip("Sélectionner le manager de l'association"));
                new ComboBoxAutoComplete<>(managerComboBox);
                managerComboBox.getValidators().add(requiredFieldValidator);
                managerComboBox.setOnMouseClicked(mouseEvent -> managerComboBox.validate());
            }
        }

        // Piece justificative
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Veuillez choisir la photo de l'association");
        SimpleBooleanProperty pieceValid = new SimpleBooleanProperty(false);
        pieceButton.setOnAction(e -> {
            pieceValid.set(false);
            piece = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
            if (piece != null) {
                String[] names = ImageIO.getReaderFormatNames();
                //ImageIO.read(file);
                for (String exst : names) {
                    if (piece.getName().endsWith(exst) || piece.getName().endsWith(".pdf") || piece.getName().endsWith(".doc") || piece.getName().endsWith(".docx")) {
                        association.setPieceJustificatif(piece.getName());
                        pieceValid.set(true);
                        break;
                    }
                }
                if (!pieceValid.get()) {
                    Alert pieceAlert = new Alert(Alert.AlertType.WARNING);
                    pieceAlert.setContentText("Veuillez choisir un document avec un format supporté");
                    pieceAlert.showAndWait();
                }
            }
        });
        JSObject window = (JSObject) gmapWebView.getEngine().executeScript("window");
        gmapWebView.getEngine().load(this.getClass().getResource("/Packages/Chihab/Scenes/WebView/getAssociationLocation.html").toString());
        gmapWebView.getEngine().getLoadWorker().stateProperty().addListener(
                (ChangeListener<? super Worker.State>) (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        gmapWebView.getEngine().executeScript("initMap()");
                        Document document = gmapWebView.getEngine().getDocument();
                        window.setMember("association", association);
                    }
                }
        );


        fileChooser.setTitle("Veuillez choisir la photo de l'association");
        photoButton.setTooltip(new Tooltip("Veuillez choisir la photo de l'association"));
        SimpleBooleanProperty photoValid = new SimpleBooleanProperty(false);
        photoButton.setOnAction(e -> {
            photoValid.set(false);
            photo = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
            if (photo != null) {
                String[] names = ImageIO.getReaderFormatNames();
                //ImageIO.read(file);
                for (String exst : names) {
                    if (photo.getName().endsWith(exst)) {
                        photoValid.set(true);
                        break;
                    }
                }
            }
            if (!photoValid.get()) {
                Alert pieceAlert = new Alert(Alert.AlertType.WARNING);
                pieceAlert.setContentText("Veuillez choisir une photo avec un format supporté");
                pieceAlert.showAndWait();
            }
        });

        de.valueProperty().setValue(LocalTime.now());
        de.valueProperty().addListener(a -> vers.setValue(de.getValue().plusHours(1)));
        vers.valueProperty().addListener((observableValue, localTime, t1) -> {
            if (t1.getHour() == de.getValue().getHour())
                de.setValue(de.getValue().minusHours(1));
        });
        vers.onHiddenProperty().setValue(event -> {
            if (de.getValue().compareTo(vers.getValue()) != -1) {
                Alert dateTimeAlert = new Alert(Alert.AlertType.WARNING);
                dateTimeAlert.setContentText("Veuillez choisir une date supérieure a la date d'ouverture");
                dateTimeAlert.showAndWait();
                vers.setValue(de.getValue().plusHours(1));
            } else {
                association.setHoraireTravail("De " + de.getValue() + " vers : " + vers.getValue());
            }
        });
        // TODO : Check manager is not null on Register/Create
        BooleanBinding managerSelected;
        if (isRegisterPage)
            managerSelected = Bindings.createBooleanBinding(() -> false);
        else
            managerSelected = Bindings.createBooleanBinding(() -> managerComboBox.getSelectionModel().getSelectedIndex() == -1, managerComboBox.valueProperty());

        validateButton.disableProperty().bind(
                nomFieldValid.or(
                        descriptionFieldValid.or(
                                zipFieldValid.or(
                                        rueFieldValid.or(
                                                phoneNumberFieldValid.or(
                                                        versTimeFieldValid
                                                                .or(pieceValid.not()
                                                                        .or(photoValid.not()
                                                                                .or(villeComboSelected
                                                                                        .or(managerSelected)
                                                                                )
                                                                        )
                                                                )
                                                )
                                        )
                                )
                        )
                )
        );
        validateButton.setOnAction(actionEvent -> {
            association.setLat(Double.valueOf(((Association) window.getMember("association")).getLat()));
            association.setLon(Double.valueOf(((Association) window.getMember("association")).getLon()));
            //association.setManager(managerComboBox.getValue());
            association.setApprouved(!isRegisterPage);
            association.setRue(rueInput.getText());
            association.setVille(villeComboBox.getValue());
            association.setCodePostal(Integer.parseInt(zipInput.getText()));
            association.setNom(nomInput.getText());
            association.setDescription(descriptionInput.getText());
            //association.setDomaine(domaineComboBox.getValue());
            association.setTelephone(Integer.parseInt(phoneNumberInput.getText()));
            association.setPhotoAgence(photo.getName());
            association.setPieceJustificatif(piece.getName());
            try {
                ftpInterface.fileUpload(piece, URLServer.associationPieceDir);
                ftpInterface.fileUpload(photo, URLServer.associationImageDir);
                AssociationService.getInstace().create(association);
            } catch (IOException | SQLException ex) {
                ex.printStackTrace();
            } finally {
                System.out.println("close it now bitch");
            }

        });
    }
}
