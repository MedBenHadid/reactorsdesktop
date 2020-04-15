package Packages.Mohamed.Scenes;

import Main.Entities.User;
import Packages.Mohamed.Entities.Mission;
import Packages.Mohamed.Services.MissionService;
import SharedResources.URLScenes;
import SharedResources.URLServer;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXProgressBar;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MissionController implements Initializable {
    @FXML
    public StackPane rootStackPane;
    FTPInterface ftpInterface;
    @FXML
    private TableView<Mission> missionTableView;
    @FXML
    private TableColumn<Mission, Mission> deleteOption;
    @FXML
    private TableColumn<Mission, String> TitleCol, descCol, locationCol, domaineCol;
    @FXML
    private TableColumn<Mission, Float> SumCollectedCol, objectifCol;
    @FXML
    private TableColumn<Mission, Number> idCol, upsCol;
    @FXML
    private TableColumn<User, String> managerCol;
    @FXML
    private TableColumn<Mission, Date> DateCreationCol, DateFinCol;
    @FXML
    private TextField inputName, inputCity;
    @FXML
    private Label size;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab missionListTab;
    @FXML
    private Button addButton;
    @FXML
    private JFXProgressBar progressBar;

    public MissionController() {
        try {
            this.ftpInterface = FTPInterface.getInstance(URLServer.ftpServerLink, URLServer.ftpSocketPort, URLServer.ftpUser, URLServer.ftpPassword);
        } catch (IOException e) {
            Alert ftpAlert = new Alert(Alert.AlertType.WARNING);
            ftpAlert.setContentText("Error connecting to FTP server");
            ftpAlert.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 1: Based on role, disable { addButton, idCol's view, deleteOption, managerCol, statusCol}
        missionListTab.setClosable(false);
        //addButton.setVisible(false);
        ObservableList<Mission> MissionList = FXCollections.observableArrayList();
        try {
            MissionList.addAll(MissionService.getInstace().readAll());
        } catch (SQLException e) {
            showDialog(Alert.AlertType.ERROR, "", "Connexion au serveur échoué", "Veuillez assurer la bonne connexion");
            e.printStackTrace();
        }
        TitleCol.setCellValueFactory(new PropertyValueFactory<>("TitleMission"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        managerCol.setCellValueFactory(new PropertyValueFactory<>("CretedBy"));
        domaineCol.setCellValueFactory(new PropertyValueFactory<>("domaine"));
        SumCollectedCol.setCellValueFactory(new PropertyValueFactory<>("sumCollected"));
        objectifCol.setCellValueFactory(new PropertyValueFactory<>("objectif"));
        DateCreationCol.setCellValueFactory(new PropertyValueFactory<>("DateCreation"));
        DateFinCol.setCellValueFactory(new PropertyValueFactory<>("DateFin"));
        upsCol.setCellValueFactory(new PropertyValueFactory<>("ups"));
        missionTableView.setItems(MissionList);
        /**
         * Start of Super Admin section
         */
        deleteOption.setVisible(true);
        idCol.setVisible(true);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TitleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //  SumCollectedCol.setCellFactory(TextFieldTableCell.forTableColumn());
        // objectifCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //DateCreationCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //DateFinCol.setCellFactory(DateTableCell.forTableColumn());

        // TODO - Pre-Finishing steps: JFXToggleButton this bitch ChoiceBoxTableCell.forTableColumn(ScheduleEntry.ALLOWED_TYPES.toArray(new String[0])));

        // Update section
        // Update name section
        TitleCol.setOnEditCommit(
                categoryStringCellEditEvent -> {
                    Mission current = missionTableView.getItems().get(categoryStringCellEditEvent.getTablePosition().getRow());
                    if (checkValidUpdate(categoryStringCellEditEvent.getOldValue(), categoryStringCellEditEvent.getNewValue(), false, 5, 30, "nom"))
                        try {
                            current.setTitleMission(categoryStringCellEditEvent.getNewValue());
                            MissionService.getInstace().update(current);
                        } catch (SQLException e) {
                            Logger.getLogger(
                                    MissionController.class.getName()).log(
                                    Level.INFO, null, e
                            );
                            showDialog(Alert.AlertType.ERROR, "Erreur de modification", e.getMessage(), "Modification échoué");
                        }
                    else
                        missionTableView.getItems().set(categoryStringCellEditEvent.getTablePosition().getRow(), current);
                }
        );
        // Update description event handler
        descCol.setOnEditCommit(
                categoryStringCellEditEvent -> {
                    Mission current = missionTableView.getItems().get(categoryStringCellEditEvent.getTablePosition().getRow());
                    if (checkValidUpdate(categoryStringCellEditEvent.getOldValue(), categoryStringCellEditEvent.getNewValue(), false, 5, 255, "Description"))
                        try {
                            current.setDescription(categoryStringCellEditEvent.getNewValue());
                            MissionService.getInstace().update(current);
                        } catch (SQLException e) {
                            Logger.getLogger(
                                    MissionController.class.getName()).log(
                                    Level.INFO, null, e
                            );
                            showDialog(Alert.AlertType.ERROR, "Erreur de modification", e.getMessage(), "Modification échoué");
                        }
                    else
                        missionTableView.getItems().set(categoryStringCellEditEvent.getTablePosition().getRow(), current);
                }
        );


        // Update section
        // Delete section
        deleteOption.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteOption.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            @Override
            protected void updateItem(Mission a, boolean empty) {
                super.updateItem(a, empty);
                if (a == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationDialog.setTitle("Suppression");
                    confirmationDialog.setHeaderText("Vous aller supprimer la mission " + a.getTitleMission() + "!");
                    confirmationDialog.setContentText("Etes vous sure?");
                    Optional<ButtonType> confirmationResult = confirmationDialog.showAndWait();
                    if (confirmationResult.isPresent())
                        if (confirmationResult.get() == ButtonType.OK)
                            try {
                                MissionService.getInstace().delete(a);
                                missionTableView.getItems().remove(a);
                                showDialog(Alert.AlertType.CONFIRMATION, "", "", "Mission supprimée !!");
                            } catch (SQLException e) {
                                Logger.getLogger(
                                        MissionController.class.getName()).log(
                                        Level.INFO, null, e
                                );
                                showDialog(Alert.AlertType.ERROR, "Suppression échoué", "Raison : Reference", "Domaine ne peut pas étre supprimé!");
                            }
                });
            }
        });


        // Profile Section
        missionTableView.setRowFactory(tv -> {
            TableRow<Mission> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Mission rowData = row.getItem();
                    FXMLLoader loader;
                    if (1 == 1)
                        loader = new FXMLLoader(getClass().getResource(URLScenes.missionProfile));
                    else
                        loader = new FXMLLoader(getClass().getResource(URLScenes.associationProfile));
                    MissionProfileController controller = new MissionProfileController(rowData);
                    loader.setController(controller);

                    StackPane createMission = null;
                    try {
                        createMission = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    JFXDialogLayout layout = new JFXDialogLayout();
                    layout.setBody(createMission);
                    layout.setHeading(new Text("Mission :" + rowData.getTitleMission()));
                    JFXDialog dialog = new JFXDialog(rootStackPane, layout, JFXDialog.DialogTransition.CENTER);
                    JFXButton close = new JFXButton("Annuler");
                    close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> dialog.close());
                    layout.setActions(close);
                    dialog.show();


                }
            });
            return row;
        });
        // Profile Section
        // Add Mission section
        addButton.setOnAction(e -> {
            try {
                //AnchorPane createMission = FXMLLoader.load(getClass().getResource("/Packages/Mohamed/Scenes/MissionCreate.fxml"));
                JFXDialogLayout layout = new JFXDialogLayout();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(URLScenes.missionCreate));
                AnchorPane createMission = loader.load();
                JFXDialog dialog = new JFXDialog(rootStackPane, layout, JFXDialog.DialogTransition.CENTER);
                JFXButton close = new JFXButton("Cancel");
                JFXButton foo = (JFXButton) loader.getNamespace().get("createButton");
                foo.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    dialog.close();
                    MissionList.add(((MissionCreateController) loader.getController()).getMission());
                });
                layout.setBody(createMission);
                layout.setHeading(new Text("Ajout d'une nouvelle mission"));

                close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> dialog.close());
                layout.setActions(close);
                dialog.show();


            } catch (IOException ex) {

                Logger.getLogger(
                        MissionController.class.getName()).log(
                        Level.WARNING, null, e
                );
                ex.printStackTrace();
            }
        });
        // Add Mission section
        /**
         * End of Super Admin section
         */
        // Search by name bindings
        TextFields.bindAutoCompletion(inputName, missionTableView.getItems().stream().map(Mission::getTitleMission).toArray());
        FilteredList<Mission> filteredName = new FilteredList<>(missionTableView.getItems(), e -> true);
        inputName.setOnKeyReleased(e -> {
            inputName.textProperty().addListener((observableValue, s, t1) -> {
                filteredName.setPredicate(mission -> {
                    if (t1 == null || t1.isEmpty())
                        return true;
                    return mission.getTitleMission().toLowerCase().startsWith(t1.toLowerCase());
                });
            });
            SortedList<Mission> sortedListName = new SortedList<>(filteredName);
            sortedListName.comparatorProperty().bind(missionTableView.comparatorProperty());
            missionTableView.setItems(sortedListName);
        });
        // Search by city bindings
        TextFields.bindAutoCompletion(inputCity, missionTableView.getItems().stream().map(Mission::getLocation).toArray());
        FilteredList<Mission> filteredCity = new FilteredList<>(missionTableView.getItems(), e -> true);
        inputCity.setOnKeyReleased(e -> {
            inputCity.textProperty().addListener((observableValue, s, t1) -> {
                filteredCity.setPredicate(mission -> {
                    if (t1 == null || t1.isEmpty())
                        return true;
                    return mission.getLocation().toLowerCase().startsWith(t1.toLowerCase());
                });
            });
            SortedList<Mission> sortedCityList = new SortedList<>(filteredCity);
            sortedCityList.comparatorProperty().bind(missionTableView.comparatorProperty());
            missionTableView.setItems(sortedCityList);
        });
        // TODO : Search by manager
        // TODO : Client view of Mission list, render items inside HBox whilst passing Mission instance to that item, i hope you fucker know what i'm talking about when you wake up
        // Label for displaying number of current Mission bindings
        size.textProperty().bind(Bindings.size((MissionList)).asString("Missions : %d"));

    }


    boolean checkValidUpdate(String oldValue, String newValue, Boolean isAlphaNumerical, int minLength, int maxLength, String string) {
        if (!checkValidStringInput(newValue, isAlphaNumerical, minLength, maxLength, string)) {
            return false;
        }
        if (oldValue.toLowerCase().equals(newValue.toLowerCase())) {
            showDialog(Alert.AlertType.INFORMATION, "No changes detected!", "", "No changes detected, reverting.");
            return false;
        }
        return true;
    }

    boolean checkValidStringInput(String input, Boolean isAlphaNumerical, int minLength, int maxLength, String string) {
        if (input.isEmpty() || input.isBlank()) {
            showDialog(Alert.AlertType.ERROR, "Invalid input size!", "", "Veuillez remplire ce champ par un " + string + " adéquat non vide");
            return false;
        } else if (input.length() > maxLength || input.length() < minLength) {
            showDialog(Alert.AlertType.ERROR, "Invalid input size!", "", string + "de domaine doit etre comprise entre " + minLength + " et " + maxLength + "!");
            return false;
        } else if (!isAlphaNumerical) {
            if (input.matches(".*\\d.*")) {
                showDialog(Alert.AlertType.INFORMATION, "Invalid input constraint!", "", string + " ne peut pas contenir des chiffres");
                return false;
            }
        }
        return true;
    }

    void showDialog(Alert.AlertType t, String title, String header, String context) {
        Alert a = new Alert(t);
        a.setTitle(title);
        a.setHeaderText(header);
        a.setContentText(context);
        a.showAndWait();
    }

}
