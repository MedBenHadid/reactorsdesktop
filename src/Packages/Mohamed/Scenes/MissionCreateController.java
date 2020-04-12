package Packages.Mohamed.Scenes;

import Main.Entities.User;
import Main.Services.UserService;
import Packages.Chihab.Custom.AutoCompleteBox;
import Packages.Chihab.Custom.ComboBoxAutoComplete;
import Packages.Mohamed.Entities.Mission;
import Packages.Mohamed.Services.MissionService;
import Packages.Mohamed.util.ComboBoxItemWrap;
import com.jfoenix.controls.JFXButton;
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
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MissionCreateController implements Initializable {
    @FXML
    private DatePicker datedeb, datefin;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField TitleInput, adresseInput,objectifInput;
    @FXML
    private TextArea descriptionInput;
    @FXML
    private JFXButton  createButton;
    @FXML
    private Button photoButton;
    @FXML
    private WebView gmapWebView;
    @FXML
    private ComboBox<ComboBoxItemWrap<User>> managerComboBox;

    private Mission mission;
    private Connection connection;
    public List<ComboBoxItemWrap<User>> checkedList = new ArrayList<>();

    public MissionCreateController() {
        this.mission = new Mission();
    }

    // TODO : implement the maps mcThingy
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BooleanBinding TitleFieldValid = Bindings.createBooleanBinding(() -> !checkValidStringInput(TitleInput.getText(), false, 6, 30), TitleInput.textProperty());
        BooleanBinding descriptionFieldValid = Bindings.createBooleanBinding(() -> !checkValidStringInput(descriptionInput.getText(), true, 6, 255), descriptionInput.textProperty());
        BooleanBinding datedebFieldValid = Bindings.createBooleanBinding(() -> !datedeb.valueProperty().isNotNull().get(), datedeb.valueProperty());
        BooleanBinding datefinFieldValid = Bindings.createBooleanBinding(() -> !datefin.valueProperty().isNotNull().get(), datefin.valueProperty());
        BooleanBinding adresseFieldValid = Bindings.createBooleanBinding(() -> rueValidCheck(adresseInput.getText()), adresseInput.textProperty());
        new AutoCompleteBox<String>(managerComboBox);
        try {
            ObservableList<ComboBoxItemWrap<User>> options = FXCollections.observableArrayList();
            ObservableList<User> users = UserService.getInstace().listUsers();
            for (User u: users) {
                options.add(new ComboBoxItemWrap<>(u));
            }

            managerComboBox.setCellFactory( c -> {
                ListCell<ComboBoxItemWrap<User>> cell = new ListCell<>(){
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
                    managerComboBox.getItems().filtered( f-> f!=null).filtered( f-> f.getCheck()).forEach( p -> {
                        sb.append("; ").append(p.getItem());
                    });
                    final String string = sb.toString();
                    managerComboBox.setPromptText(string.substring(Integer.min(2, string.length())));

                    System.out.println(managerComboBox.getSelectionModel().getSelectedItem());
                    if(managerComboBox.getSelectionModel().getSelectedItem().getCheck()) {
                        System.out.println("chekeed");
                        checkedList.add(managerComboBox.getSelectionModel().getSelectedItem()) ;
                    }else{
                        checkedList.remove(managerComboBox.getSelectionModel().getSelectedItem()) ;

                    }

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

        // Piece justificative
        FileChooser fileChooser = new FileChooser();

        photoButton.setTooltip(new Tooltip("Veuillez choisir la photo de l'mission"));
        photoButton.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
            if (file != null) {
                String[] names = ImageIO.getReaderFormatNames();
                //ImageIO.read(file);
                for (String exst : names) {
                    if (file.getName().endsWith(exst)) {
                        // TODO : upload to server

                        mission.setPicture(mission.getTitleMission() + file.getName());
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

                        window.setMember("mission", new Mission());
                        String script = "initMap()";

                        gmapWebView.getEngine().executeScript(script);
                    }
                }
        );
        createButton.setOnAction(e -> {
       //     System.out.println(window.getMember("lat"));
           mission.setLat((double) window.getMember("lat"));
            mission.setLon((double) window.getMember("lon"));
     System.out.println(mission.getLon());
     System.out.println(mission.getLat());
/**try {
                mission.setTitleMission(TitleInput.getText());
                mission.setDescription(descriptionInput.getText());
                mission.setLocation(adresseInput.getText());
                mission.setObjectif(Double.parseDouble(objectifInput.getText()));
                mission.setTitleMission(TitleInput.getText());
                mission.setDateCreation(java.sql.Date.valueOf(datedeb.getValue().toString()));
                mission.setDateFin(java.sql.Date.valueOf(datefin.getValue().toString()));


                MissionService.getInstace().create(mission,checkedList);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }*/

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
