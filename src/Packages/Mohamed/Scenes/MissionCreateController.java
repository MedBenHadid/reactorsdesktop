package Packages.Mohamed.Scenes;

import Main.Entities.User;
import Main.Services.UserService;
import Packages.Mohamed.Entities.Mission;
import Packages.Mohamed.Services.MissionService;
import Packages.Mohamed.util.ComboBoxItemWrap;
import SharedResources.URLServer;
import SharedResources.Utils.AutoCompleteBox;
import SharedResources.Utils.BinaryValidator.RegexValidator;
import SharedResources.Utils.ComboBoxAutoComplete;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import netscape.javascript.JSObject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MissionCreateController implements Initializable {
    private static MissionService instance;
    @FXML
    private AnchorPane rootPane;
    private final Mission mission;
    public List<User> checkedList = new ArrayList<>();
    @FXML
    private JFXDatePicker datedeb, datefin;
    @FXML
    private JFXTextField TitleInput, adresseInput, objectifInput;
    @FXML
    private JFXTextArea descriptionInput;
    @FXML
    private JFXButton createButton;
    @FXML
    private JFXButton photoButton;
    @FXML
    private WebView gmapWebView;
    @FXML
    private ComboBox<ComboBoxItemWrap<User>> managerComboBox;
    private FTPInterface ftpInterface;
    private Connection connection;
    private File file;

    public MissionCreateController() {
        this.mission = new Mission();

        try {
            this.ftpInterface = FTPInterface.getInstance(URLServer.ftpServerLink, URLServer.ftpSocketPort, URLServer.ftpUser, URLServer.ftpPassword);
        } catch (IOException e) {
            Alert ftpAlert = new Alert(Alert.AlertType.WARNING);
            ftpAlert.setContentText("Error connecting to FTP server");
            ftpAlert.show();
        }
    }

    public Mission getMission() {
        return mission;
    }

    // TODO : implement the maps mcThingy
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator("Champ manquant");

        TitleInput.getValidators().addAll(new RegexValidator("Veuillez saisir un Titre dont la taille est comprise entre 3 et 30", "^[\\w\\s]{3,30}$"));
        TitleInput.focusedProperty().addListener((o, a, t) -> {
            if (!t) TitleInput.validate();
            if (TitleInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                mission.setTitleMission(TitleInput.getText());
        });
        TitleInput.setOnKeyTyped(keyEvent -> TitleInput.validate());


        descriptionInput.setOnKeyTyped(keyEvent -> {
            if (!descriptionInput.getText().isEmpty())
                descriptionInput.validate();
            if (!descriptionInput.getText().isEmpty())
                if (descriptionInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                    mission.setDescription(descriptionInput.getText());
        });

        adresseInput.getValidators().addAll(new RegexValidator("Veuillez saisir une adresse valide", "^[\\w\\s]{3,30}$"));
        adresseInput.focusedProperty().addListener((o, a, t) -> {
            if (!t) adresseInput.validate();
            if (adresseInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                mission.setDescription(adresseInput.getText());
        });
        adresseInput.setOnKeyTyped(keyEvent -> adresseInput.validate());

        //  objectifInput.getValidators().addAll(new NumberValidator("Veuillez saisir un objectif valide numérique"), new StringLengthValidator(8, 0, "Veuillez saisir un numéro dont la taille est "), requiredFieldValidator);

        objectifInput.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                objectifInput.validate();
                if (!objectifInput.getText().isEmpty())
                    if (objectifInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                        mission.setObjectif((double) Integer.parseInt(objectifInput.getText()));
            }
        });
        objectifInput.setOnKeyTyped(keyEvent -> {
            if (!objectifInput.getText().isEmpty())
                objectifInput.validate();
        });

        BooleanBinding TitleFieldValid = Bindings.createBooleanBinding(() -> !checkValidStringInput(TitleInput.getText(), false, 6, 30), TitleInput.textProperty());
        BooleanBinding descriptionFieldValid = Bindings.createBooleanBinding(() -> !checkValidStringInput(descriptionInput.getText(), true, 6, 255), descriptionInput.textProperty());
        BooleanBinding datedebFieldValid = Bindings.createBooleanBinding(() -> !datedeb.valueProperty().isNotNull().get(), datedeb.valueProperty());
        BooleanBinding datefinFieldValid = Bindings.createBooleanBinding(() -> !datefin.valueProperty().isNotNull().get(), datefin.valueProperty());
        BooleanBinding adresseFieldValid = Bindings.createBooleanBinding(() -> rueValidCheck(adresseInput.getText()), adresseInput.textProperty());
        new AutoCompleteBox<String>(managerComboBox);
        try {
            ObservableList<ComboBoxItemWrap<User>> options = FXCollections.observableArrayList();
            ObservableList<User> users = UserService.getInstace().readAll();
            for (User u : users) {
                options.add(new ComboBoxItemWrap<>(u));
            }

            managerComboBox.setCellFactory(c -> {
                ListCell<ComboBoxItemWrap<User>> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(ComboBoxItemWrap<User> item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            final CheckBox cb = new CheckBox(item.toString());
                            cb.selectedProperty().bind(item.checkProperty());
                            setGraphic(cb);
                        }
                    }
                };

                cell.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
                    cell.getItem().checkProperty().set(!cell.getItem().checkProperty().get());
                    StringBuilder sb = new StringBuilder();
                    managerComboBox.getItems().filtered(Objects::nonNull).filtered(ComboBoxItemWrap::getCheck).forEach(p -> sb.append("; ").append(p.getItem()));
                    final String string = sb.toString();
                    managerComboBox.setPromptText(string.substring(Integer.min(2, string.length())));

                    if (managerComboBox.getSelectionModel().getSelectedItem().getCheck()) {
                        System.out.println("chekeed");
                        System.out.println("=+" + managerComboBox.getSelectionModel().getSelectedItem());
                        checkedList.add(managerComboBox.getSelectionModel().getSelectedItem().getItem());
                    } else {
                        checkedList.remove(managerComboBox.getSelectionModel().getSelectedItem().getItem());
                        System.out.println("=-" + managerComboBox.getSelectionModel().getSelectedItem().getItem());


                    }
                    System.out.println(checkedList);

                });


                return cell;
            });
            managerComboBox.setItems(options);


            new ComboBoxAutoComplete<>(managerComboBox);
        } catch (Exception e) {
            Logger.getLogger(
                    MissionCreateController.class.getName()).log(
                    Level.SEVERE, null, e
            );
            e.printStackTrace();
        }

        FileChooser fileChooser = new FileChooser();

        photoButton.setTooltip(new Tooltip("Veuillez choisir la photo de l'mission"));
        photoButton.setOnAction(e -> {
            file = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
            if (file != null) {
                String[] names = ImageIO.getReaderFormatNames();
                //ImageIO.read(file);
                for (String exst : names) {
                    if (file.getName().endsWith(exst)) {
                        // TODO : upload to server

                        mission.setPicture(mission.getTitleMission() + file.getName());
                        try {
                            ftpInterface.fileUpload(file, URLServer.missionImageDir + mission.getPicture());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    }
                }
            } else {
                Alert pieceAlert = new Alert(Alert.AlertType.WARNING);
                pieceAlert.setContentText("Veuillez choisir un document avec un format supporté");
                pieceAlert.showAndWait();
            }
        });
        JSObject window = (JSObject) gmapWebView.getEngine().executeScript("window");
        gmapWebView.getEngine().load(this.getClass().getResource("/Packages/Mohamed/Scenes/WebView/getMissionLocation.html").toString());
        gmapWebView.getEngine().getLoadWorker().stateProperty().addListener(
                (ChangeListener<? super Worker.State>) (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        String script = "initMap()";

                        gmapWebView.getEngine().executeScript(script);
                        window.setMember("mission", new Mission());

                    }
                }
        );


        createButton.setOnAction(e -> {


            try {
                mission.setLat((double) window.getMember("lat"));
                mission.setLon((double) window.getMember("lon"));
                System.out.println(mission.getLon());
                System.out.println(mission.getLat());
                mission.setTitleMission(TitleInput.getText());
                mission.setDescription(descriptionInput.getText());
                mission.setLocation(adresseInput.getText());
                mission.setObjectif(Double.parseDouble(objectifInput.getText()));
                mission.setTitleMission(TitleInput.getText());
                mission.setDateCreation(java.sql.Date.valueOf(datedeb.getValue().toString()));
                mission.setDateFin(java.sql.Date.valueOf(datefin.getValue().toString()));


                MissionService.getInstace().create(mission, checkedList);
            } catch (Exception ex) {
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
