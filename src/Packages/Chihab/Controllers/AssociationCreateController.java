package Packages.Chihab.Controllers;

import Main.Entities.User;
import Main.Services.UserService;
import Packages.Chihab.Custom.AutoCompleteBox;
import Packages.Chihab.Custom.ComboBoxAutoComplete;
import Packages.Chihab.Models.Association;
import Packages.Chihab.Models.Category;
import Packages.Chihab.Services.AssociationService;
import Packages.Chihab.Services.CategoryService;
import SharedResources.URLServer;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
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
    private TextField nomInput, rueInput, zipInput, phoneNumberInput;
    @FXML
    private TextArea descriptionInput;
    @FXML
    private JFXButton validateButton;
    @FXML
    private Button photoButton, pieceButton;
    @FXML
    private ComboBox<String> villeComboBox;
    @FXML
    private ComboBox<User> managerComboBox;
    @FXML
    private ComboBox<Category> domaineComboBox;
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
        // TODO : Used when registering
        this.association = new Association();
        this.association.setManager(manager);
        this.isRegisterPage = true;
        try {
            this.ftpInterface = FTPInterface.getInstance(URLServer.ftpServerLink, URLServer.ftpSocketPort, URLServer.ftpUser, URLServer.ftpPassword);
        } catch (IOException e) {
            Alert ftpAlert = new Alert(Alert.AlertType.WARNING);
            ftpAlert.setContentText("Error connecting to FTP server");
            ftpAlert.show();
        }
    }

    // TODO : implement the maps mcThingy
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BooleanBinding nomFieldValid = Bindings.createBooleanBinding(() -> !checkValidStringInput(nomInput.getText(), false, 6, 30), nomInput.textProperty());
        BooleanBinding descriptionFieldValid = Bindings.createBooleanBinding(() -> !checkValidStringInput(descriptionInput.getText(), true, 6, 255), descriptionInput.textProperty());
        BooleanBinding zipFieldValid = Bindings.createBooleanBinding(() -> !zipInput.getText().matches(".*\\d.*") || zipInput.getText().length() != 4, zipInput.textProperty());
        BooleanBinding phoneNumberFieldValid = Bindings.createBooleanBinding(() -> !phoneNumberInput.getText().matches(".*\\d.*") || phoneNumberInput.getText().length() != 8, phoneNumberInput.textProperty());
        BooleanBinding versTimeFieldValid = Bindings.createBooleanBinding(() -> !vers.valueProperty().isNotNull().get(), vers.valueProperty());
        BooleanBinding rueFieldValid = Bindings.createBooleanBinding(() -> rueValidCheck(rueInput.getText()), rueInput.textProperty());
        BooleanBinding villeComboSelected = Bindings.createBooleanBinding(() -> villeComboBox.getSelectionModel().getSelectedIndex() == -1, villeComboBox.valueProperty());
        villeComboBox.setItems(FXCollections.observableArrayList("Ariana", "Béja", "Ben Arous", "Bizerte", "Gabes", "Gafsa", "Jendouba", "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine", "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan"));
        villeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> association.setVille(newValue));
        villeComboBox.setTooltip(new Tooltip());
        new AutoCompleteBox<String>(villeComboBox);
        System.out.println();
        try {
            domaineComboBox.getItems().addAll(CategoryService.getInstace().readAll());
            domaineComboBox.setVisibleRowCount(6);
            domaineComboBox.setTooltip(new Tooltip());
            domaineComboBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(Category c) {
                    return c.getNom();
                }

                @Override
                public Category fromString(String s) {
                    return domaineComboBox.getItems().stream().filter(c -> c.getNom().equals(s)).findFirst().get();
                }
            });
            new ComboBoxAutoComplete<>(managerComboBox);
            // TODO : Set its visibility based on logged in user's role
            if (isRegisterPage)
                managerComboBox.setVisible(false);
            else {
                managerComboBox.getItems().addAll(UserService.getInstace().readAll().stream().filter(u -> !u.isAdmin()).collect(Collectors.toCollection(ArrayList::new)));
                managerComboBox.setVisibleRowCount(6);
                managerComboBox.setTooltip(new Tooltip());
                managerComboBox.setConverter(new StringConverter<>() {
                    @Override
                    public String toString(User user) {
                        return user.getEmail();
                    }

                    @Override
                    public User fromString(String s) {
                        return managerComboBox.getItems().stream().filter(user -> user.getEmail().equals(s)).findFirst().get();
                    }
                });
                new ComboBoxAutoComplete<>(managerComboBox);
            }

        } catch (Exception e) {
            Logger.getLogger(
                    AssociationCreateController.class.getName()).log(
                    Level.SEVERE, null, e
            );
            e.printStackTrace();
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
        de.valueProperty().addListener(a -> {
            vers.setValue(de.getValue().plusHours(1));
        });
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

        validateButton.disableProperty().bind(nomFieldValid.or(descriptionFieldValid.or(zipFieldValid.or(rueFieldValid.or(phoneNumberFieldValid.or(versTimeFieldValid.or(pieceValid.not().or(photoValid.not().or(villeComboSelected.or(managerSelected))))))))));

        validateButton.setOnAction(actionEvent -> {
            association.setLat(Double.valueOf(((Association) window.getMember("association")).getLat()));
            association.setLon(Double.valueOf(((Association) window.getMember("association")).getLon()));
            if (!isRegisterPage) {
                association.setManager(managerComboBox.getValue());
                association.setApprouved(true);
            } else {
                association.setApprouved(false);
            }
            association.setRue(rueInput.getText());
            association.setVille(villeComboBox.getValue());
            association.setCodePostal(Integer.parseInt(zipInput.getText()));
            association.setNom(nomInput.getText());
            association.setDescription(descriptionInput.getText());
            association.setDomaine(domaineComboBox.getValue());
            association.setTelephone(Integer.parseInt(phoneNumberInput.getText()));
            association.setPhotoAgence(photo.getName());
            association.setPieceJustificatif(piece.getName());
            try {
                ftpInterface.fileUpload(piece, URLServer.associationPieceDir);
                ftpInterface.fileUpload(photo, URLServer.associationImageDir);
                AssociationService.getInstace().create(association);
            } catch (IOException | SQLException ex) {
                ex.printStackTrace();
            }

        });
    }

    boolean rueValidCheck(String rue) {
        if (!rue.isBlank() && rue.length() > 8) {
            if (rue.contains(" ")) {
                return !rue.substring(0, rue.indexOf(' ')).matches(".*\\d.*");
            }
        }
        return true;
    }

    boolean checkValidStringInput(String input, Boolean isAlphaNumerical, int minLength, int maxLength) {
        if (input.isEmpty() || input.isBlank()) {
            return false;
        } else if (input.length() > maxLength || input.length() < minLength) {
            return false;
        } else if (!isAlphaNumerical) {
            return !input.matches(".*\\d.*");
        }
        return true;
    }
}
