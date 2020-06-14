package Packages.Mohamed.Scenes;

import Main.Entities.User;
import Main.Entities.UserSession;
import Packages.Chihab.Models.Entities.Association;
import Packages.Mohamed.Entities.Mission;
import Packages.Mohamed.Entities.Notification;
import Packages.Mohamed.Services.MissionService;
import Packages.Mohamed.Services.NotificationService;
import SharedResources.URLScenes;
import SharedResources.URLServer;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.apache.commons.net.ftp.FTP;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MissionController implements Initializable {
    @FXML
    public StackPane rootStackPane;
    @FXML
    private TableView<Mission> missionTableView;
    @FXML
    private TableColumn<Mission, String> TitleCol, descCol, locationCol, domaineCol;
    @FXML
    private TableColumn<Mission, Double> SumCollectedCol, objectifCol;
    @FXML
    private TableColumn<Mission, Number> upsCol;
    @FXML
    private TableColumn<User, String> managerCol;
    @FXML
    private TableColumn<Mission, Date> DateCreationCol, DateFinCol;
    @FXML
    private TextField inputName, inputCity;
    @FXML
    private Label size;
    @FXML
    Button addButton,notifbtn;
    @FXML
    private JFXListView<Notification> notifListView;
    private final JFXDialogLayout layout = new JFXDialogLayout();
    private final JFXButton close = new JFXButton("Fermer");
    private static JFXDialog dialog;
    private int k =-1;

    public static JFXDialog getDialog() {
        return dialog;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dialog = new JFXDialog(rootStackPane, layout, JFXDialog.DialogTransition.CENTER);
        //addButton.setVisible(UserSession.getInstace().getUser().isAssociationAdmin());
        missionTableView.itemsProperty().bind(Bindings.createObjectBinding(() -> MissionService.getInstace().getDatabase().values().stream().filter(mission -> {
            boolean test = true;
            if( inputName.isFocused() ){
                test = mission.getTitleMission().contains(inputName.getText());
            }
            if( inputCity.isFocused()){
                test = test && mission.getLocation().contains(inputCity.getText());
            }
            return test;
        }).collect(Collectors.toCollection(FXCollections::observableArrayList)),MissionService.getInstace().getDatabase(),inputName.textProperty(),inputCity.textProperty()));
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

        TitleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descCol.setCellFactory(TextFieldTableCell.forTableColumn());
        // SumCollectedCol.setCellFactory(TextFieldTableCell.forTableColumn());
        objectifCol.setCellFactory(this::objectiveSliderCallBack);
        //DateCreationCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //DateFinCol.setCellFactory(DateTableCell.forTableColumn());

        // TODO - Pre-Finishing steps: JFXToggleButton this bitch ChoiceBoxTableCell.forTableColumn(ScheduleEntry.ALLOWED_TYPES.toArray(new String[0])));

        // Update section
        // Update name section
      /*  TitleCol.setOnEditCommit(
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
*/
        // Profile Section
        missionTableView.setRowFactory(tv -> {
            TableRow<Mission> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(UserSession.getInstace().getUser().isAdmin()){
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Mission rowData = row.getItem();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(URLScenes.missionProfile));
                    MissionProfileController controller = new MissionProfileController(rowData);
                    loader.setController(controller);
                    try {
                        StackPane createMission = loader.load();
                        dialog(createMission,"Mission :" + rowData.getTitleMission()).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                }else{
                 try {
                        dialog(new MissionProfileUpdateController(row.getItem()),"Profile :"+ row.getItem().getTitleMission()).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
        addButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(URLScenes.missionCreate));
                AnchorPane createMission = loader.load();
                dialog(createMission,"Creation d'une nouvelle mission :").show();
            } catch (IOException ex) {
                Logger.getLogger(MissionController.class.getName()).log(Level.WARNING, null, e);
            }
        });
        notifListView.itemsProperty().bind(Bindings.createObjectBinding(() ->NotificationService.getInstance().getRecords().values().stream().collect(Collectors.toCollection(FXCollections::observableArrayList)),NotificationService.getInstance().getRecords()));
        notifListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Notification> call(ListView<Notification> param) {
                final HBox actionsContainer = new HBox();
                final VBox infoContainer = new VBox();
                final ImageView assProfile = new ImageView();
                final JFXButton deny = new JFXButton("Deny");
                final JFXButton accept = new JFXButton("Accepter");
                final Label missionName = new Label();
                final Label missionDescription = new Label();
                return new JFXListCell<>() {
                    @Override
                    public void updateItem(Notification item, boolean empty) {

                        //TODO:sync notif and test
                        super.updateItem(item, empty);
                        assProfile.setFitHeight(20);
                        assProfile.setFitWidth(20);
                        infoContainer.setAlignment(Pos.CENTER);
                        actionsContainer.setAlignment(Pos.CENTER);
                        accept.setStyle("-fx-background-color: green");
                        deny.setStyle("-fx-background-color: red");
                        if (item != null && !empty) {
                            setOnMouseClicked(event -> {
                                rootStackPane.getChildren().get(0).setViewOrder(0);
                                k=-1;
                                try {
                                    assProfile.setImage(new Image(Objects.requireNonNull(FTPInterface.getInstance().downloadFile(URLServer.associationImageDir + item.getId_association().getPhotoAgence(), FTP.BINARY_FILE_TYPE)).toURI().toString()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                missionName.setText("Titre :"+item.getTitle());
                                missionDescription.setText("Description de l'evenement :"+item.getDescription());
                                accept.setOnMouseClicked(event1 -> {
                                    try {
                                        if ( NotificationService.getInstance().AcceptMissionNotification(item)){
                                            dialog.close();
                                            showDialog(Alert.AlertType.INFORMATION, "Mission accépter", "", "\"Merci pour votre acceptation ! \"");

                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }


                                    dialog.close();
                                });
                                deny.setOnMouseClicked(event1 -> {

                                    try {
                                        if (NotificationService.getInstance().RefuserMissionNotification(item)){
                                            dialog.close();
                                            showDialog(Alert.AlertType.INFORMATION, "Mission réfuser", "", "\"Merci pour votre réponse ! \"");
                                        }

                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }

                                });
                                actionsContainer.getChildren().addAll(accept,deny);
                                infoContainer.getChildren().addAll(assProfile,missionName,missionDescription,actionsContainer);
                                dialog(infoContainer,"Responding to :" + item.getId_mission().getTitleMission()).show();
                            });
                            setGraphic(new NotificationItemController(item));
                            setText("");
                        }
                    }
                };
            }
        });
        notifbtn.textProperty().bind(Bindings.createStringBinding(()-> notifListView.getItems().size()+" notifications",notifListView.itemsProperty()));
        notifbtn.setOnAction(event -> {
            rootStackPane.getChildren().get(0).setViewOrder(k);
            k = k==-1 ? 0 : -1 ;
        });
        TextFields.bindAutoCompletion(inputName, missionTableView.getItems().stream().map(Mission::getTitleMission).toArray());
        TextFields.bindAutoCompletion(inputCity, missionTableView.getItems().stream().map(Mission::getLocation).toArray());
        size.textProperty().bind(Bindings.size((missionTableView.getItems())).asString("Missions : %d"));
    }

    private TableCell<Mission, Double> objectiveSliderCallBack(TableColumn<Mission, Double> missionFloatTableColumn){
        return new TableCell<>() {
            JFXSlider progressSlider = new JFXSlider();
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null && empty) {
                    setText("");
                    setGraphic(null);
                } else {
                    progressSlider.setDisable(true);
                    progressSlider.setMax(getTableRow().getItem().getObjectif());
                    progressSlider.setValue(getTableRow().getItem().getSumCollected());
                    setText("Somme collecté");
                    setGraphic(progressSlider);
                }
            }
        };
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
    private JFXDialog dialog(Node body, String heading){
        close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> dialog.close());
        layout.setActions(close);
        layout.setBody(body);
        layout.setHeading(new Text(heading));
        return dialog;

    }

}
