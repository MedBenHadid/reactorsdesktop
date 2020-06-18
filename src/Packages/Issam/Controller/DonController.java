package Packages.Issam.Controller;

import Packages.Chihab.Controllers.DomainesController;
import Packages.Issam.Models.Don;
import Packages.Issam.Services.DonService;
import Packages.Mohamed.Scenes.MissionController;
import Packages.Mohamed.Scenes.MissionCreateController;
import SharedResources.URLScenes;
import SharedResources.URLServer;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.apache.commons.net.ftp.FTP;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

// 1 : --module-path "C:\javafx-sdk-11.0.2\lib" --add-modules javafx.controls,javafx.fxml --add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED
// 2 : --add-exports=javafx.graphics/com.sun.javafx.util=ALL-UNNAMED
public class DonController implements Initializable {
    @FXML
    public StackPane rootStackPane;
    @FXML
    private TableView<Don> donTV;
    @FXML
    private TableColumn<Don ,Don> deleteOption;
    @FXML
    private  TableColumn<Don , String>  titleCol , descCol , addrCol ,phoneCol ,imageCol ;
    @FXML
    private TableColumn<Don, Number> idCol;
    @FXML
    private TextField title , description, address , phone , creationDate, inputTitle, inputAddress;


    @FXML
    private Button addButton;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        donTV.itemsProperty().bind(Bindings.createObjectBinding(() -> DonService.getInstace().getDb().values().stream().filter(don -> don.getTitle().toLowerCase().contains(inputTitle.getText().toLowerCase())).collect(Collectors.toCollection(FXCollections::observableArrayList)),DonService.getInstace().getDb(),inputTitle.textProperty(),inputTitle.focusedProperty()));

        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        addrCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));

        deleteOption.setVisible(true);
        idCol.setVisible(true);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descCol.setCellFactory(TextFieldTableCell.forTableColumn());
        addrCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneCol.setCellFactory(TextFieldTableCell.forTableColumn());
        imageCol.setCellFactory(this::photoCall);


        //Update title
        titleCol.setOnEditCommit(
                donStringCellEditEvent ->  {
                    Don current = donTV.getItems().get(donStringCellEditEvent.getTablePosition().getRow());
                    if (checkValidUpdate(donStringCellEditEvent.getOldValue(), donStringCellEditEvent.getNewValue(), false, 5, 30, "nom"))
                        try {
                            current.setTitle(donStringCellEditEvent.getNewValue());
                            DonService.getInstace().update(current);
                        } catch (SQLException e) {
                            Logger.getLogger(
                                    DonController.class.getName()).log(
                                    Level.INFO, null, e
                            );
                            showDialog(Alert.AlertType.ERROR, "Erreur de modification", e.getMessage(), "Modification échoué");
                        }
                    else
                        donTV.getItems().set(donStringCellEditEvent.getTablePosition().getRow(), current);
                }
        );



        //Update descripotion
        descCol.setOnEditCommit(
                donStringCellEditEvent ->  {
                    Don current = donTV.getItems().get(donStringCellEditEvent.getTablePosition().getRow());
                    if (checkValidUpdate(donStringCellEditEvent.getOldValue(), donStringCellEditEvent.getNewValue(), false, 8, 50, "nom"))
                        try {
                            current.setDescription(donStringCellEditEvent.getNewValue());
                            DonService.getInstace().update(current);
                        } catch (SQLException e) {
                            Logger.getLogger(
                                    DomainesController.class.getName()).log(
                                    Level.INFO, null, e
                            );
                            showDialog(Alert.AlertType.ERROR, "Erreur de modification", e.getMessage(), "Modification échoué");
                        }
                    else
                        donTV.getItems().set(donStringCellEditEvent.getTablePosition().getRow(), current);
                }
        );


        //Update descripotion
        addrCol.setOnEditCommit(
                donStringCellEditEvent ->  {
                    Don current = donTV.getItems().get(donStringCellEditEvent.getTablePosition().getRow());
                    if (checkValidUpdate(donStringCellEditEvent.getOldValue(), donStringCellEditEvent.getNewValue(), false, 5, 30, "nom"))
                        try {
                            current.setAddress(donStringCellEditEvent.getNewValue());
                            DonService.getInstace().update(current);
                        } catch (SQLException e) {
                            Logger.getLogger(
                                    DomainesController.class.getName()).log(
                                    Level.INFO, null, e
                            );
                            showDialog(Alert.AlertType.ERROR, "Erreur de modification", e.getMessage(), "Modification échoué");
                        }
                    else
                        donTV.getItems().set(donStringCellEditEvent.getTablePosition().getRow(), current);
                }
        );


        //Update descripotion
        phoneCol.setOnEditCommit(
                donStringCellEditEvent ->  {
                    Don current = donTV.getItems().get(donStringCellEditEvent.getTablePosition().getRow());
                    if (checkValidUpdate(donStringCellEditEvent.getOldValue(), donStringCellEditEvent.getNewValue(), true ,  5, 30, "nom"))
                        try {
                            current.setPhone(donStringCellEditEvent.getNewValue());
                            DonService.getInstace().update(current);
                        } catch (SQLException e) {
                            Logger.getLogger(
                                    DomainesController.class.getName()).log(
                                    Level.INFO, null, e
                            );
                            showDialog(Alert.AlertType.ERROR, "Erreur de modification", e.getMessage(), "Modification échoué");
                        }
                    else
                        donTV.getItems().set(donStringCellEditEvent.getTablePosition().getRow(), current);
                }
        );

        deleteOption.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteOption.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");
            @Override
            protected void updateItem(Don don, boolean empty) {
                super.updateItem(don, empty);
                if (don == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationDialog.setTitle("Suppression");
                    confirmationDialog.setHeaderText("Vous aller supprimer le domaine " + don.getTitle() + "!");
                    confirmationDialog.setContentText("Etes vous sure?");
                    Optional<ButtonType> confirmationResult = confirmationDialog.showAndWait();
                    if (confirmationResult.isPresent())
                        if (confirmationResult.get() == ButtonType.OK)
                                if(DonService.getInstace().delete(don))
                                showDialog(Alert.AlertType.CONFIRMATION, "", "", "Don supprimée !!");
                                else
                                showDialog(Alert.AlertType.ERROR, "Suppression échoué", "Raison : Reference", "cette Don ne peut pas étre supprimé!");

                });
            }
        });
        addButton.setOnAction(e -> {
            try {
                //AnchorPane createMission = FXMLLoader.load(getClass().getResource("/Packages/Mohamed/Scenes/MissionCreate.fxml"));
                JFXDialogLayout layout = new JFXDialogLayout();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(URLScenes.donCreate));
                AnchorPane donCreate = loader.load();
                JFXDialog dialog = new JFXDialog(rootStackPane, layout, JFXDialog.DialogTransition.CENTER);
                JFXButton close = new JFXButton("Cancel");
                JFXButton foo = (JFXButton) loader.getNamespace().get("createButton");
                foo.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    dialog.close();
                });
                layout.setBody(donCreate);
                layout.setHeading(new Text("Ajout d'un nouveaux don"));

                close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> dialog.close());
                layout.setActions(close);
                dialog.show();


            } catch (IOException ex) {

                Logger.getLogger(
                        DonController.class.getName()).log(
                        Level.WARNING, null, e
                );
                ex.printStackTrace();
            }
        });

        // Search by name bindings
       // TextFields.bindAutoCompletion(inputTitle, donTV.getItems().stream().map(Don::getTitle).toArray());
        FilteredList<Don> filteredName = new FilteredList<>(donTV.getItems(), e -> true);
        inputTitle.setOnKeyReleased(e -> {
            inputTitle.textProperty().addListener((observableValue, s, t1) -> {
                filteredName.setPredicate(don -> {
                    if (t1 == null || t1.isEmpty())
                        return true;
                    return don.getTitle().toLowerCase().startsWith(t1.toLowerCase());
                });
            });
            SortedList<Don> sortedListName = new SortedList<>(filteredName);
            sortedListName.comparatorProperty().bind(donTV.comparatorProperty());
            donTV.setItems(sortedListName);
        });
        //TextFields.bindAutoCompletion(inputCity, missionTableView.getItems().stream().map(Mission::getLocation).toArray());
        FilteredList<Don> filteredCity = new FilteredList<>(donTV.getItems(), e -> true);
        inputAddress.setOnKeyReleased(e -> {
            inputAddress.textProperty().addListener((observableValue, s, t1) -> {
                filteredCity.setPredicate(don -> {
                    if (t1 == null || t1.isEmpty())
                        return true;
                    return don.getAddress().toLowerCase().startsWith(t1.toLowerCase());
                });
            });
            SortedList<Don> sortedCityList = new SortedList<>(filteredCity);
            sortedCityList.comparatorProperty().bind(donTV.comparatorProperty());
            donTV.setItems(sortedCityList);
        });










    }





/**
     * Create Section
  **/

    public void createDon() {
        if (checkValidStringInput(title.getText(), false, 6, 30, "Title") && checkValidStringInput(description.getText(), false, 6, 255, "Description")) {
            Don don = new Don();
            don.setTitle(title.getText());
            don.setDescription(description.getText());
            don.setAddress(address.getText());
            don.setPhone(phone.getText());
            don.setCreationDate(creationDate.getText());
            description.clear();
            title.clear();
            address.clear();
            phone.clear();
            creationDate.clear();
            if(DonService.getInstace().create(don)){
                showDialog(Alert.AlertType.CONFIRMATION, "Success", "", "Domaine ajouté avec succées");
            }else {
                showDialog(Alert.AlertType.ERROR, "Création échoué", "", "Création du don échoué!!");

            }
        }
    }


    private TableCell<Don, String> photoCall(TableColumn<Don, String> observableAssociationValueUserTableColumn) {
        return new TableCell<>() {
            private final ImageView imageView = new ImageView();
            private Image userImage;

            @Override
            protected void updateItem(String don, boolean empty) {
                super.updateItem(don, empty);

                System.out.println(don);
                if (don == null) {
                    setText("");
                    setGraphic(null);
                } else {
                    if (userImage == null)
                        try {
                            userImage = new Image(Objects.requireNonNull(FTPInterface.getInstance().downloadFile(URLServer.donImageDir + don, FTP.BINARY_FILE_TYPE)).toURI().toString());
                        } catch (IOException e) {
                            setGraphic(null);
                        }
                    imageView.setImage(userImage);
                    imageView.setFitHeight(100.0);
                    imageView.setFitWidth(150.0);
                    setGraphic(imageView);
                }
            }
        };}

    boolean checkValidStringInput(String input, Boolean isAlphaNumerical, int minLength, int maxLength, String string) {
        if (input.isEmpty() || input.isBlank()) {
            showDialog(Alert.AlertType.ERROR, "Invalid input size!", "", "Veuillez remplire ce champ par un " + string + " adéquat non vide");
            return false;
        } else if (input.length() > maxLength || input.length() < minLength) {
            showDialog(Alert.AlertType.ERROR, "Invalid input size!", "", string + "doit etre comprise entre " + minLength + " et " + maxLength + "!");
            return false;
        } else if (!isAlphaNumerical) {
            if (input.matches(".*\\d.*")) {
                showDialog(Alert.AlertType.INFORMATION, "Invalid input constraint!", "", string + " ne peut pas contenir des chiffres");
                return false;
            }
        }
        return true;
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






    void showDialog(Alert.AlertType t, String title, String header, String context) {
        Alert a = new Alert(t);
        a.setTitle(title);
        a.setHeaderText(header);
        a.setContentText(context);
        a.showAndWait();
    }
}
