package Packages.Chihab.Controllers;

import Main.Entities.User;
import Main.Services.UserService;
import Packages.Chihab.Models.Association;
import Packages.Chihab.Models.Category;
import Packages.Chihab.Services.AssociationService;
import Packages.Chihab.Services.CategoryService;
import SharedResources.URLServer;
import SharedResources.Utils.AutoCompleteBox;
import SharedResources.Utils.BinaryValidator.PhoneNumberValidator;
import SharedResources.Utils.BinaryValidator.RegexValidator;
import SharedResources.Utils.BinaryValidator.StringLengthValidator;
import SharedResources.Utils.ComboBoxAutoComplete;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.*;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import netscape.javascript.JSObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
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
    @FXML
    private Label photoError, pieceError;
    @FXML
    private SplitPane splitPane;
    @FXML
    private JFXProgressBar progress;
    private File photo, piece;
    private Association association;
    private FTPInterface ftpInterface;
    private boolean isRegisterPage = false;
    private User manager;

    public AssociationCreateController() {
        try {
            this.ftpInterface = FTPInterface.getInstance(URLServer.ftpServerLink, URLServer.ftpSocketPort, URLServer.ftpUser, URLServer.ftpPassword);
        } catch (IOException e) {
            Logger.getLogger(
                    AssociationCreateController.class.getName()).log(
                    Level.SEVERE, "Error connecting to FTP Server :", e.getCause()
            );
        }
    }

    public AssociationCreateController(User manager) {
        this.isRegisterPage = true;
        this.manager = manager;
        new AssociationCreateController();
    }

    public Association getAssociation() {
        return association;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Category> domaines = FXCollections.observableArrayList();
        ObservableList<User> managers = FXCollections.observableArrayList();
        this.association = new Association();
        association.setApprouved(!isRegisterPage);
        try {
            if (!isRegisterPage)
                managers = UserService.getInstace().readAll().stream().filter(u -> !u.isAdmin()).collect(Collectors.toCollection(FXCollections::observableArrayList));
            else {
                this.association.setManager(this.manager);
                managerComboBox.setVisible(false);
            }
            domaines = CategoryService.getInstace().readAll();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator("Champ manquant");
        // Nom input validation
        nomInput.getValidators().addAll(requiredFieldValidator, new RegexValidator("Veuillez saisir un nom dont la taille est comprise entre 3 et 30", "^[\\w\\s]{3,30}$"));
        nomInput.focusedProperty().addListener((o, a, t) -> {
            if (!t) nomInput.validate();
            if (nomInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                association.setNom(nomInput.getText());

        });
        nomInput.setOnKeyTyped(keyEvent -> {
            nomInput.resetValidation();
            nomInput.validate();
        });

        // Description validation
        descriptionInput.getValidators().addAll(requiredFieldValidator, new RegexValidator("Veuillez saisir une description valide 3-255", "^[\\d\\w\\s]{3,255}$"));
        descriptionInput.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                descriptionInput.validate();
                if (descriptionInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                    association.setDescription(descriptionInput.getText());
            }
        });
        descriptionInput.setOnKeyTyped(keyEvent -> {
            descriptionInput.resetValidation();
            descriptionInput.validate();
        });
        // Phone validation
        PhoneNumberValidator d = new PhoneNumberValidator("");
        phoneNumberInput.getValidators().addAll(requiredFieldValidator, d);
        phoneNumberInput.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                phoneNumberInput.validate();
                if (phoneNumberInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                    association.setTelephone(Integer.parseInt(phoneNumberInput.getText()));
            }
        });
        phoneNumberInput.setOnKeyReleased(keyEvent -> phoneNumberInput.validate());
        // TODO : Add regex
        rueInput.getValidators().addAll(requiredFieldValidator, new StringLengthValidator(8, -1, "La taille doit etre supérieure a "));
        rueInput.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                rueInput.validate();
                if (phoneNumberInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                    association.setRue(rueInput.getText());
            }
        });
        rueInput.setOnKeyPressed(keyEvent -> {
            rueInput.resetValidation();
            rueInput.validate();
        });
        //Zip validation
        zipInput.getValidators().addAll(new NumberValidator("Veuillez saisir un code postale numérique"), requiredFieldValidator, new StringLengthValidator(4, 0, "La taille du code postale est "));
        zipInput.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                zipInput.validate();
                if (zipInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                    association.setCodePostal(Integer.parseInt(zipInput.getText()));
            }
        });
        zipInput.setOnKeyTyped(keyEvent -> zipInput.validate());
        villeComboBox.setItems(FXCollections.observableArrayList("Ariana", "Béja", "Ben Arous", "Bizerte", "Gabes", "Gafsa", "Jendouba", "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine", "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan"));
        villeComboBox.setTooltip(new Tooltip("Sélectionner la ville de votre association"));
        new AutoCompleteBox<String>(villeComboBox);
        villeComboBox.getValidators().add(requiredFieldValidator);
        villeComboBox.setOnHiding(event -> {
            villeComboBox.validate();
            if (villeComboBox.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                association.setVille(villeComboBox.getValue());
        });

        // TODO : Add regex
        ObservableList<Category> finalDomaines = domaines;
        System.out.println();
        domaineComboBox.getItems().addAll(finalDomaines.stream().map(Category::getNom).collect(Collectors.toCollection(FXCollections::observableArrayList)));
        domaineComboBox.getValidators().add(requiredFieldValidator);
        domaineComboBox.setOnHiding(event -> {
            domaineComboBox.validate();
            if (domaineComboBox.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                association.setDomaine(finalDomaines.stream().filter(s -> s.getNom().equals(domaineComboBox.getValue())).findFirst().get());
        });
        domaineComboBox.setVisibleRowCount(6);
        domaineComboBox.setTooltip(new Tooltip("Sélectionner le domaine d'activité de votre association"));
        new ComboBoxAutoComplete<>(domaineComboBox);
        if (!isRegisterPage) {
            ObservableList<User> finalManagers = managers;
            managerComboBox.getItems().addAll(finalManagers.stream().map(User::getEmail).collect(Collectors.toCollection(FXCollections::observableArrayList)));
            managerComboBox.setVisibleRowCount(6);
            domaineComboBox.setTooltip(new Tooltip("Sélectionner le manager de l'association"));
            new ComboBoxAutoComplete<>(managerComboBox);
            managerComboBox.getValidators().add(requiredFieldValidator);
            managerComboBox.setOnHiding(e -> {
                managerComboBox.validate();
                if (managerComboBox.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                    association.setManager(finalManagers.stream().filter(user -> user.getEmail().equals(managerComboBox.getSelectionModel().getSelectedItem())).findFirst().get());
            });
        }

        FileChooser photoChooser = new FileChooser();
        photoChooser.setTitle("Veuillez choisir la photo de l'association");
        photoChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        SimpleBooleanProperty photoValid = new SimpleBooleanProperty(true);
        photoButton.setOnMouseClicked(e -> {
            photoValid.setValue(true);
            photoError.setVisible(false);
            photo = photoChooser.showOpenDialog(rootPane.getScene().getWindow());
            if (photo == null) photoError.setVisible(true);
            else {
                photoValid.setValue(false);
                photoError.setVisible(false);
                association.setPhotoAgence(photo.getName());
            }
        });
        // Piece justificative
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Veuillez choisir la photo de l'association");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"), new FileChooser.ExtensionFilter("PDF", "*.pdf"), new FileChooser.ExtensionFilter("DOCX", "*.docx,*.doc"));
        SimpleBooleanProperty pieceValid = new SimpleBooleanProperty(true);
        pieceButton.setOnMouseClicked(e -> {
            pieceValid.set(true);
            pieceError.setVisible(false);
            piece = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
            if (piece == null) pieceError.setVisible(true);
            else {
                pieceValid.setValue(false);
                association.setPieceJustificatif(piece.getName());
                System.out.println(association);
            }
        });
        JSObject window = (JSObject) gmapWebView.getEngine().executeScript("window");
        gmapWebView.getEngine().load(this.getClass().getResource("/Packages/Chihab/Scenes/WebView/getAssociationLocation.html").toString());
        gmapWebView.getEngine().getLoadWorker().stateProperty().addListener(
                (ChangeListener<? super Worker.State>) (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        window.setMember("association", association);
                        gmapWebView.getEngine().executeScript("initMap()");
                    }
                }
        );


        vers.getValidators().add(requiredFieldValidator);
        vers.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) vers.validate();
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
        validateButton.setOnAction(actionEvent -> {
            try {
                ftpInterface.fileUpload(piece, URLServer.associationPieceDir);
                ftpInterface.fileUpload(photo, URLServer.associationImageDir);
            } catch (IOException ex) {
                Logger.getLogger(
                        AssociationCreateController.class.getName()).log(
                        Level.SEVERE, "Error in uploaing file :", ex.getCause()
                );
            } finally {
                try {
                    AssociationService.getInstace().create(association);
                } catch (SQLException throwables) {
                    Logger.getLogger(
                            AssociationCreateController.class.getName()).log(
                            Level.SEVERE, "Error persisting to database :", throwables.getCause()
                            // TODO : Delete uploaded files
                    );
                } finally {
                    // TODO : Close current window
                }
            }

        });
        BooleanBinding managerSelected;
        if (isRegisterPage)
            managerSelected = Bindings.createBooleanBinding(() -> false);
        else
            managerSelected = Bindings.createBooleanBinding(() -> managerComboBox.getSelectionModel().getSelectedIndex() == -1, managerComboBox.valueProperty());
        BooleanBinding villeComboSelected = Bindings.createBooleanBinding(() -> villeComboBox.getSelectionModel().getSelectedIndex() == -1, villeComboBox.valueProperty());
        BooleanBinding domaineComboSelected = Bindings.createBooleanBinding(() -> domaineComboBox.getSelectionModel().getSelectedIndex() == -1, domaineComboBox.valueProperty());
        BooleanBinding versTimeFieldValid = Bindings.createBooleanBinding(() -> !vers.valueProperty().isNotNull().get(), vers.valueProperty());
        BooleanBinding nomFieldValid = Bindings.createBooleanBinding(() -> nomInput.getValidators().stream().anyMatch(ValidatorBase::getHasErrors) || nomInput.textProperty().isEmpty().getValue(), nomInput.getValidators(), nomInput.textProperty());
        BooleanBinding descriptionFieldValid = Bindings.createBooleanBinding(() -> descriptionInput.getValidators().stream().anyMatch(ValidatorBase::getHasErrors) || descriptionInput.textProperty().isEmpty().getValue(), descriptionInput.getValidators(), descriptionInput.textProperty());
        BooleanBinding zipFieldValid = Bindings.createBooleanBinding(() -> (zipInput.getValidators().stream().anyMatch(ValidatorBase::getHasErrors) || zipInput.textProperty().isEmpty().getValue()), zipInput.getValidators(), zipInput.textProperty());
        BooleanBinding phoneNumberFieldValid = Bindings.createBooleanBinding(() -> phoneNumberInput.getValidators().stream().anyMatch(ValidatorBase::getHasErrors) || phoneNumberInput.textProperty().isEmpty().getValue(), phoneNumberInput.getValidators(), phoneNumberInput.textProperty());
        BooleanBinding rueFieldValid = Bindings.createBooleanBinding(() -> rueInput.getValidators().stream().anyMatch(ValidatorBase::getHasErrors) || rueInput.textProperty().isEmpty().getValue(), rueInput.getValidators(), rueInput.textProperty());

        DoubleBinding dd = Bindings.createDoubleBinding(() ->
                        (!managerSelected.get() ? 0.09090909090909090909090909090909 : 0.0) +
                                (!villeComboSelected.get() ? 0.09090909090909090909090909090909 : 0) +
                                (!domaineComboSelected.get() ? 0.09090909090909090909090909090909 : 0) +
                                (!nomFieldValid.get() ? 0.09090909090909090909090909090909 : 0) +
                                (!descriptionFieldValid.get() ? 0.09090909090909090909090909090909 : 0) +
                                (phoneNumberInput.textProperty().get().matches("^[\\d]{8}$") ? 0.09090909090909090909090909090909 : 0) +
                                (!rueFieldValid.get() ? 0.09090909090909090909090909090909 : 0) +
                                (!versTimeFieldValid.get() ? 0.09090909090909090909090909090909 : 0) +
                                (zipInput.textProperty().get().matches("^[\\d]{4}$") ? 0.09090909090909090909090909090909 : 0) +
                                (!photoValid.get() ? 0.09090909090909090909090909090909 : 0) +
                                (!pieceValid.get() ? 0.09090909090909090909090909090909 : 0)
                , phoneNumberInput.textProperty(), zipInput.textProperty(), managerSelected, villeComboSelected, domaineComboSelected, nomFieldValid, descriptionFieldValid, rueFieldValid, versTimeFieldValid, zipFieldValid, photoValid, pieceValid);
        progress.progressProperty().bind(dd);
        //managerSelected.or(villeComboSelected.or(domaineComboSelected.or(nomFieldValid.or(descriptionFieldValid.or(phoneNumberFieldValid.or(zipFieldValid.or(rueFieldValid.or(versTimeFieldValid.or(pieceValid.or(photoValid))))))))))
        validateButton.disableProperty().bind(Bindings.createBooleanBinding(() -> dd.get() < 1.0, dd));
        splitPane.setOnMouseEntered(mouseEvent -> splitPane.setTooltip(new Tooltip(
                (nomFieldValid.get() ? "Le nom saisie est non valide \r\n" : "") +
                        (descriptionFieldValid.get() ? "La description saisie est non valide \r\n" : "") +
                        (phoneNumberFieldValid.get() ? "Le numéro n'est pas valide \r\n" : "") +
                        (zipFieldValid.get() ? "Le code postale n'est pas valide \r\n" : "") +
                        (managerSelected.get() ? " Le manager n'est pas choisi\r\n" : "") +
                        (domaineComboSelected.get() ? "Le domaine n'est pas choisi\r\n" : "") +
                        (villeComboSelected.get() ? "La ville n'est pas choisi\r\n" : "") +
                        (pieceValid.get() ? "La piece justificative n'est pas fournis\r\n" : "") +
                        (photoValid.get() ? "La photo n'est pas fournis\r\n" : "") +
                        (versTimeFieldValid.get() ? "Horaire non introduit\r\n" : "")
        )));
    }
}
