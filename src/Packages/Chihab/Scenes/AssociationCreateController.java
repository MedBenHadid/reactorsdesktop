package Packages.Chihab.Scenes;

import Main.Entities.User;
import Main.Services.UserService;
import Packages.Chihab.Models.Association;
import Packages.Chihab.Scenes.Custom.ComboBoxAutoComplete;
import SharedResources.URLServer;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.apache.commons.net.ftp.FTPClient;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.UUID;
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
    private JFXButton registerButton, createButton;
    @FXML
    private Button photoButton, pieceButton;
    @FXML
    private ComboBox<String> villeComboBox;
    @FXML
    private ComboBox<User> managerComboBox;

    private Association association;

    public AssociationCreateController() {
        this.association = new Association();
    }

    private static void uploadFile(File file, String directory) throws IOException {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(URLServer.ftpServerLink, URLServer.ftpSocketPort);
            ftpClient.login(URLServer.ftpUser, URLServer.ftpPassword);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            // TODO : set file name as file.getName() + UUID.randomUUID
            String firstRemoteFile = directory + "/" + file.getName() + UUID.randomUUID();
            InputStream inputStream = new FileInputStream(file);
            System.out.println("Start uploading first file");
            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("The first file is uploaded successfully.");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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
        villeComboBox.setItems(FXCollections.observableArrayList("Ariana", "Béja", "Ben Arous", "Bizerte", "Gabes", "Gafsa", "Jendouba", "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine", "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan"));
        villeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> association.setVille(newValue));
        villeComboBox.setTooltip(new Tooltip());
        new ComboBoxAutoComplete<>(villeComboBox);


        try {
            managerComboBox.setItems(FXCollections.observableArrayList((Collection<? extends User>) UserService.getInstace().listUsers().stream().filter(u -> !u.isAdmin()).collect(Collectors.toCollection(ArrayList::new))));
            managerComboBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(User u) {
                    return u.getUsername();
                }

                @Override
                public User fromString(String string) {
                    return managerComboBox.getItems().stream().filter(ap -> ap.getUsername().equals(string)).findFirst().orElse(null);
                }
            });
            managerComboBox.setVisibleRowCount(6);
        } catch (SQLException e) {
            Logger.getLogger(
                    AssociationCreateController.class.getName()).log(
                    Level.SEVERE, null, e
            );
            e.printStackTrace();
        }

        // Piece justificative
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Veuillez choisir la photo de l'association");
        pieceButton.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
            if (file != null) {
                String[] names = ImageIO.getReaderFormatNames();
                //ImageIO.read(file);
                boolean isValidExt = false;
                for (String exst : names) {
                    if (file.getName().endsWith(exst) || file.getName().endsWith(".pdf") || file.getName().endsWith(".doc") || file.getName().endsWith(".docx")) {

                        association.setPieceJustificatif(file.getName());
                        isValidExt = true;
                        break;
                    }
                }
                if (!isValidExt) {
                    Alert pieceAlert = new Alert(Alert.AlertType.WARNING);
                    pieceAlert.setContentText("Veuillez choisir un document avec un format supporté");
                    pieceAlert.showAndWait();
                }
                //openFile(file);
            }
        });
        // Piece justificative
        // Photo validation
        fileChooser.setTitle("Veuillez choisir la piece justificative de l'association");
        photoButton.setTooltip(new Tooltip("Veuillez choisir la photo de l'association"));
        photoButton.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
            if (file != null) {
                String[] names = ImageIO.getReaderFormatNames();
                //ImageIO.read(file);
                for (String exst : names) {
                    if (file.getName().endsWith(exst)) {
                        // TODO : upload to server

                        association.setPhotoAgence(association.getNom() + file.getName());
                        break;
                    }
                }
                //openFile(file);
            } else {

            }
        });
        // Photo validation
        // Date validation
        de.valueProperty().setValue(LocalTime.now());
        de.valueProperty().addListener(a -> {
            vers.setValue(de.getValue().plusHours(1));
        });
        vers.valueProperty().addListener((observableValue, localTime, t1) -> {
            if (t1.getHour() == de.getValue().getHour())
                de.setValue(de.getValue().minusHours(1));
        });
        vers.onHiddenProperty().setValue(event -> {
            int compValue = de.getValue().compareTo(vers.getValue());
            if (compValue == 0 || compValue == 1) {
                Alert dateTimeAlert = new Alert(Alert.AlertType.WARNING);
                dateTimeAlert.setContentText("Veuillez choisir une date supérieure a la date d'ouverture");
                dateTimeAlert.showAndWait();
                vers.setValue(de.getValue().plusHours(1));
            } else {
                association.setHoraireTravail("De " + de.getValue() + " vers : " + vers.getValue());
            }
        });
        // Date validation
        // TODO : Check Ville is not null on Register/Create
        createButton.disableProperty().bind(versTimeFieldValid);
        registerButton.disableProperty().bind(nomFieldValid.or(descriptionFieldValid.or(zipFieldValid.or(rueFieldValid.or(phoneNumberFieldValid.or(versTimeFieldValid))))));
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
