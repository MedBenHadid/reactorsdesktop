package Packages.Chihab.Controllers;

import Main.Entities.User;
import Main.Entities.UserSession;
import Main.Services.UserService;
import Packages.Chihab.Models.Entities.Association;
import Packages.Chihab.Models.Entities.Category;
import Packages.Chihab.Models.Entities.ObservableAssociationValue;
import Packages.Chihab.Services.CategoryService;
import SharedResources.URLScenes;
import SharedResources.URLServer;
import SharedResources.Utils.AutoCompleteBox;
import SharedResources.Utils.BinaryValidator.PhoneNumberValidator;
import SharedResources.Utils.BinaryValidator.RegexValidator;
import SharedResources.Utils.BinaryValidator.StringLengthValidator;
import SharedResources.Utils.ComboBoxAutoComplete;
import SharedResources.Utils.Connector.ConnectionUtil;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import netscape.javascript.JSObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class AssociationCreateController extends StackPane implements Initializable {
    @FXML private JFXTimePicker de, vers;
    @FXML private StackPane rootPane;
    @FXML private JFXTextField nomInput, rueInput, zipInput, phoneNumberInput;
    @FXML private JFXTextArea descriptionInput;
    @FXML JFXButton validateButton;
    @FXML JFXButton photoButton, pieceButton;
    @FXML JFXComboBox<String> villeComboBox;
    @FXML JFXComboBox<Category> domaineComboBox;
    @FXML JFXComboBox<User> managerComboBox;
    @FXML WebView gmapWebView;
    @FXML Label photoError, pieceError;
    @FXML SplitPane splitPane;
    @FXML JFXProgressBar progress;
    private final ObservableList<Category> domaines = FXCollections.observableArrayList();
    private final RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator("Champ manquant");
    private final PhoneNumberValidator d = new PhoneNumberValidator("");
    private final SimpleBooleanProperty photoValid = new SimpleBooleanProperty(true);
    private final SimpleBooleanProperty pieceValid = new SimpleBooleanProperty(true);
    private final FileChooser fileChooser = new FileChooser();
    private final FileChooser photoChooser = new FileChooser();
    private final ObservableAssociationValue association = new ObservableAssociationValue(new Association());
    private final JFXDialogLayout layout = new JFXDialogLayout();
    private File photo, piece;
    private final FXMLLoader loader = new FXMLLoader( getClass().getResource(URLScenes.associationCreate) );
    private final JFXButton close = new JFXButton("Close");

    public AssociationCreateController() {
        this.association.get().setApprouved(UserSession.getInstace().isLoggedIn());
        loader.setRoot( this );
        loader.setController( this );
        try {
            loader.load();
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gmapWebView.getEngine().load(this.getClass().getResource("/Packages/Chihab/Scenes/WebView/getAssociationLocation.html").toString());
        if(UserSession.getInstace().isLoggedIn()){
            if(UserSession.getInstace().getUser().isAdmin()){
                initManagers();
            }
        } else {
            managerComboBox.setVisible(false);
        }
        if(ConnectionUtil.isIsConnected()){
            initDomaineBox();
        }else {
            ConnectionUtil.isConnectedProperty().addListener((observableValue, aBoolean, t1) -> {
                if(t1){
                    initDomaineBox();
                }
            });
        }
        // Nom input validation
        nomInput.getValidators().addAll(requiredFieldValidator, new RegexValidator("Veuillez saisir un nom dont la taille est comprise entre 3 et 30", "^[\\w\\s]{3,30}$"));
        nomInput.focusedProperty().addListener((o, a, t) -> {
            if (!t) nomInput.validate();
            if (nomInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                this.association.get().setNom(nomInput.getText());

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
                    association.get().setDescription(descriptionInput.getText());
            }
        });
        descriptionInput.setOnKeyTyped(keyEvent -> {
            descriptionInput.resetValidation();
            descriptionInput.validate();
        });
        // Phone validation
        phoneNumberInput.getValidators().addAll(requiredFieldValidator, d);
        phoneNumberInput.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                phoneNumberInput.validate();
                if (phoneNumberInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                    association.get().setTelephone(Integer.parseInt(phoneNumberInput.getText()));
            }
        });
        phoneNumberInput.setOnKeyReleased(keyEvent -> phoneNumberInput.validate());
        // TODO : Add regex
        rueInput.getValidators().addAll(requiredFieldValidator, new StringLengthValidator(8, -1, "La taille doit etre supérieure a "));
        rueInput.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                rueInput.validate();
                if (phoneNumberInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                    association.get().setRue(rueInput.getText());
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
                    association.get().setCodePostal(Integer.parseInt(zipInput.getText()));
            }
        });
        zipInput.setOnKeyTyped(keyEvent -> zipInput.validate());
        villeComboBox.setItems(FXCollections.observableArrayList("Ariana", "Béja", "Ben Arous", "Bizerte", "Gabes", "Gafsa", "Jendouba", "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine", "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan"));
        villeComboBox.setTooltip(new Tooltip("Sélectionner la ville de votre association"));
        AutoCompleteBox<String> villeAutoCompleteBoc = new AutoCompleteBox<>(villeComboBox);
        villeComboBox.getValidators().add(requiredFieldValidator);
        villeComboBox.setOnHiding(event -> {
            villeComboBox.validate();
            if (villeComboBox.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                association.get().setVille(villeComboBox.getValue());
        });
        photoChooser.setTitle("Veuillez choisir la photo de l'association");
        photoChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        photoButton.setOnMouseClicked(e -> {
            photoValid.setValue(true);
            photoError.setVisible(false);
            photo = photoChooser.showOpenDialog(this.getScene().getWindow());
            if (photo == null) photoError.setVisible(true);
            else {
                photoValid.setValue(false);
                photoError.setVisible(false);
                association.get().setPhotoAgence(photo.getName());
            }
        });
        // Piece justificative
        fileChooser.setTitle("Veuillez choisir la photo de l'association");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("DOCX", "*.docx,*.doc")
        );


        pieceButton.setOnMouseClicked(e -> {
            pieceValid.set(true);
            pieceError.setVisible(false);
            piece = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
            if (piece == null) pieceError.setVisible(true);
            else {
                pieceValid.setValue(false);
                association.get().setPieceJustificatif(piece.getName());
                System.out.println(association);
            }
        });

        //gmapWebView.getEngine().load(this.getClass().getResource("/Packages/Chihab/Scenes/WebView/getAssociationLocation.html").toString());
        JSObject window = (JSObject) gmapWebView.getEngine().executeScript("window");
        window.setMember("association", association.getValue());

        gmapWebView.getEngine().getLoadWorker().stateProperty().addListener(
                (ChangeListener<? super Worker.State>) (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        gmapWebView.getEngine().executeScript("initMap()");
                    }
                }
        );
        association.get().latProperty().addListener((observableValue, number, t1) -> {
            if(!number.equals(t1))
            System.out.println("changed to :"+ t1);
        });


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
                dialog(new Label("Date is inferior to start date, reverting to start + 8h, you can change if u want"),"Invalid date");
                vers.setValue(de.getValue().plusHours(8));
            } else {
                association.get().setHoraireTravail("De " + de.getValue().getHour()+" : " +de.getValue().getMinute() + " vers : " + vers.getValue()+" : " +vers.getValue().getMinute() );
            }
        });
        validateButton.setOnAction(actionEvent -> {
                if(FTPInterface.getInstance().send(photo, URLServer.associationImageDir)&&FTPInterface.getInstance().send(piece, URLServer.associationImageDir)) {
                    if(1==1){
                        dialog(new Text(association.toString()),"FTP Error");
                    } else {
                        dialog(new Text("Couldnt connect to database!"),"DB Error");
                    }
                } else{
                    dialog(new Text("Couldnt upload files!"),"FTP Error");
                }

        });
        BooleanBinding managerSelected;
        if (UserSession.getInstace().isLoggedIn())
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

    private void initManagers() {
        try {
            managerComboBox.setItems(FXCollections.observableList(UserService.getInstace().readAll().stream().filter(user -> !user.isAdmin()&&!user.isAssociationAdmin()&&user.getApprouved()).collect(Collectors.toCollection(FXCollections::observableArrayList))));
            if(managerComboBox.getItems().size()<1){
                dialog(new Text("No users found that are not already association admins or are not yet approuved !! create some ya admin!!"),"No users found");
                // TODO : Add refresh
            } else {
                managerComboBox.setVisibleRowCount(6);
                managerComboBox.setTooltip(new Tooltip("Sélectionner le manager de l'association"));
                new ComboBoxAutoComplete<>(managerComboBox);
                managerComboBox.setOnHiding(e -> {
                    managerComboBox.validate();
                    if (managerComboBox.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                        association.get().setManager(managerComboBox.getValue());
                });
            }
            managerComboBox.getValidators().add(requiredFieldValidator);
        } catch (SQLException throwables) {
            dialog(new Text("Couldnt retrieve data !!"),"Error whilst fetching users !");
            // TODO : ADd refresh
        }
    }

    private void initDomaineBox(){
        domaines.addAll(CategoryService.getInstace().readAll());
        managerComboBox.setVisibleRowCount(6);
        domaineComboBox.setTooltip(new Tooltip("Sélectionner le domaine de votre association"));
        domaineComboBox.getItems().addAll(domaines);
        domaineComboBox.getValidators().add(requiredFieldValidator);
        domaineComboBox.setOnHiding(event -> {
            domaineComboBox.validate();
            if (domaineComboBox.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                association.get().setDomaine(domaineComboBox.getSelectionModel().getSelectedItem());
        });
        new ComboBoxAutoComplete<>(domaineComboBox);

    }
    private void dialog(Node body, String heading){
        final JFXDialog dialog = new JFXDialog(this,layout, JFXDialog.DialogTransition.CENTER);
        close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> dialog.close());
        layout.setActions(close);
        layout.setBody(body);
        layout.setHeading(new Text(heading));
        dialog.show();
    }
}
