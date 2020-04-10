package Packages.Mohamed.Scenes;

import Main.Entities.User;
import Packages.Chihab.Controllers.AssociationProfileShowController;
import Packages.Chihab.Models.Association;
import Packages.Chihab.Services.AssociationService;
import SharedResources.URLScenes;
import SharedResources.URLServer;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MissionController implements Initializable {
    @FXML
    private TableView<Association> associationTableView;
    @FXML
    private TableColumn<Association, Association> deleteOption, pieceCol;
    @FXML
    private TableColumn<Association, String> nomCol, descCol, villeCol, domaineCol;
    @FXML
    private TableColumn<Association, Number> idCol;
    @FXML
    private TableColumn<User, String> managerCol;
    @FXML
    private TextField inputName, inputCity;
    @FXML
    private Label size;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab associationListTab;
    @FXML
    private Button addButton;
    @FXML
    private TableColumn<Association, Boolean> statusCol;
    FTPInterface ftpInterface;
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
        associationListTab.setClosable(false);
        //addButton.setVisible(false);
        ObservableList<Association> associationList = FXCollections.observableArrayList();
        try {
            associationList.addAll(AssociationService.getInstace().readAll());
        } catch (SQLException e) {
            showDialog(Alert.AlertType.ERROR, "", "Connexion au serveur échoué", "Veuillez assurer la bonne connexion");
            e.printStackTrace();
        }
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        villeCol.setCellValueFactory(new PropertyValueFactory<>("ville"));
        managerCol.setCellValueFactory(new PropertyValueFactory<>("managerUserName"));
        domaineCol.setCellValueFactory(new PropertyValueFactory<>("domaineNom"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("approuved"));
        associationTableView.setItems(associationList);
        /**
         * Start of Super Admin section
         */
        deleteOption.setVisible(true);
        idCol.setVisible(true);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descCol.setCellFactory(TextFieldTableCell.forTableColumn());
        villeCol.setCellFactory(ComboBoxTableCell.forTableColumn(
                "Ariana", "Béja", "Ben Arous", "Bizerte", "Gabes", "Gafsa", "Jendouba", "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine", "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan"
        ));
        // TODO - Pre-Finishing steps: JFXToggleButton this bitch ChoiceBoxTableCell.forTableColumn(ScheduleEntry.ALLOWED_TYPES.toArray(new String[0])));

        // Update section
        // Update name section
        nomCol.setOnEditCommit(
                categoryStringCellEditEvent -> {
                    Association current = associationTableView.getItems().get(categoryStringCellEditEvent.getTablePosition().getRow());
                    if (checkValidUpdate(categoryStringCellEditEvent.getOldValue(), categoryStringCellEditEvent.getNewValue(), false, 5, 30, "nom"))
                        try {
                            current.setNom(categoryStringCellEditEvent.getNewValue());
                            AssociationService.getInstace().update(current);
                        } catch (SQLException e) {
                            Logger.getLogger(
                                    MissionController.class.getName()).log(
                                    Level.INFO, null, e
                            );
                            showDialog(Alert.AlertType.ERROR, "Erreur de modification", e.getMessage(), "Modification échoué");
                        }
                    else
                        associationTableView.getItems().set(categoryStringCellEditEvent.getTablePosition().getRow(), current);
                }
        );
        // Update description event handler
        descCol.setOnEditCommit(
                categoryStringCellEditEvent -> {
                    Association current = associationTableView.getItems().get(categoryStringCellEditEvent.getTablePosition().getRow());
                    if (checkValidUpdate(categoryStringCellEditEvent.getOldValue(), categoryStringCellEditEvent.getNewValue(), false, 5, 255, "Description"))
                        try {
                            current.setDescription(categoryStringCellEditEvent.getNewValue());
                            AssociationService.getInstace().update(current);
                        } catch (SQLException e) {
                            Logger.getLogger(
                                    MissionController.class.getName()).log(
                                    Level.INFO, null, e
                            );
                            showDialog(Alert.AlertType.ERROR, "Erreur de modification", e.getMessage(), "Modification échoué");
                        }
                    else
                        associationTableView.getItems().set(categoryStringCellEditEvent.getTablePosition().getRow(), current);
                }
        );
        // Update ville section
        villeCol.setOnEditCommit(
                categoryStringCellEditEvent -> {
                    Association current = associationTableView.getItems().get(categoryStringCellEditEvent.getTablePosition().getRow());
                    if (!categoryStringCellEditEvent.getOldValue().equals(categoryStringCellEditEvent.getNewValue()))
                        try {
                            current.setVille(categoryStringCellEditEvent.getNewValue());
                            AssociationService.getInstace().update(current);
                        } catch (SQLException e) {
                            Logger.getLogger(
                                    MissionController.class.getName()).log(
                                    Level.INFO, null, e
                            );
                            showDialog(Alert.AlertType.ERROR, "Erreur de modification", e.getMessage(), "Modification échoué");
                        }
                    else
                        System.out.println("No changes detected, saving you memory, oh look, i printed on the console, nyahahaha fuck you and your memory");
                }
        );
        // Update section
        // Delete section
        deleteOption.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteOption.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            @Override
            protected void updateItem(Association a, boolean empty) {
                super.updateItem(a, empty);
                if (a == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationDialog.setTitle("Suppression");
                    confirmationDialog.setHeaderText("Vous aller supprimer le domaine " + a.getNom() + "!");
                    confirmationDialog.setContentText("Etes vous sure?");
                    Optional<ButtonType> confirmationResult = confirmationDialog.showAndWait();
                    if (confirmationResult.isPresent())
                        if (confirmationResult.get() == ButtonType.OK)
                            try {
                                AssociationService.getInstace().delete(a);
                                associationTableView.getItems().remove(a);
                                showDialog(Alert.AlertType.CONFIRMATION, "", "", "Association supprimée !!");
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
        // Delete Section
        // Piece justificative section
        pieceCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        pieceCol.setCellFactory(param -> new TableCell<>() {
            private final Button pieceButton = new Button("Afficher");

            @Override
            protected void updateItem(Association a, boolean empty) {
                super.updateItem(a, empty);
                if (a == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(pieceButton);
                pieceButton.setOnAction(event -> {
                    try {
                        File file = ftpInterface.downloadFile(URLServer.associationPieceDir + a.getPieceJustificatif(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);

                        if (!Desktop.isDesktopSupported())
                            return;
                        Desktop desktop = Desktop.getDesktop();
                        if (file.exists()) {
                            desktop.open(file);
                            file.deleteOnExit();
                        }
                        // TODO : Implement a print functionality using desktop.print()
                    } catch (Exception e) {
                        Logger.getLogger(
                                MissionController.class.getName()).log(
                                Level.SEVERE, null, e
                        );
                        Alert connAlert = new Alert(Alert.AlertType.WARNING);
                        connAlert.setContentText("Error whilst fetching " + a.getPieceJustificatif() + " from FTP server");
                        connAlert.show();
                    }

                });
            }
        });
        // Piece justificative section
        progressBar.visibleProperty().bind(Bindings.createBooleanBinding(Platform::isNestedLoopRunning));
        // Profile Section
        associationTableView.setRowFactory(tv -> {
            TableRow<Association> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Association rowData = row.getItem();
                    if (tabPane.getTabs().stream().skip(1).map(Tab::getUserData).mapToInt(o -> ((Integer) o)).noneMatch(i -> i == rowData.getId())) {
                        Tab tab = new Tab();
                        tab.setUserData(rowData.getId());
                        tab.setText("Association : " + rowData.getNom());
                        tab.setClosable(true);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource(URLScenes.associationProfile));
                        AssociationProfileShowController controller = new AssociationProfileShowController(rowData);
                        loader.setController(controller);
                        try {
                            ScrollPane scrollPane = loader.load();
                            tab.setContent(scrollPane);
                            tab.setClosable(true);
                            tabPane.getTabs().add(tab);
                        } catch (IOException e) {
                            Logger.getLogger(
                                    MissionController.class.getName()).log(
                                    Level.WARNING, null, e
                            );
                            e.printStackTrace();
                        }
                    }
                }
            });
            return row;
        });
        // Profile Section
        // Add association section
        addButton.setOnAction(e -> {
            try {
                AnchorPane createAssociation = FXMLLoader.load(getClass().getResource(URLScenes.associationCreate));
                Stage stage = new Stage();
                Scene scene = new Scene(createAssociation);
                stage.setScene(scene);
                stage.show();
                stage.setOnCloseRequest(ev -> {
                    // TODO : switch this to child, for readability and to avoid the infamous bug
                    Alert confAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confAlert.setTitle("Confirmation :");
                    confAlert.setContentText("Etes vous sure de vouloir quitter ?");
                    Optional<ButtonType> result = confAlert.showAndWait();
                    if (result.isPresent())
                        if (result.get() == ButtonType.OK)
                            stage.close();
                        else
                            System.out.println("Cancel clicked, but ORACLE Y U DO DIS");
                });
            } catch (IOException ex) {
                Logger.getLogger(
                        MissionController.class.getName()).log(
                        Level.WARNING, null, e
                );
                ex.printStackTrace();
            }
        });
        // Add association section
        /**
         * End of Super Admin section
         */
        // Search by name bindings
        TextFields.bindAutoCompletion(inputName, associationTableView.getItems().stream().map(Association::getNom).toArray());
        FilteredList<Association> filteredName = new FilteredList<>(associationTableView.getItems(), e -> true);
        inputName.setOnKeyReleased(e -> {
            inputName.textProperty().addListener((observableValue, s, t1) -> {
                filteredName.setPredicate(association -> {
                    if (t1 == null || t1.isEmpty())
                        return true;
                    return association.getNom().toLowerCase().startsWith(t1.toLowerCase());
                });
            });
            SortedList<Association> sortedListName = new SortedList<>(filteredName);
            sortedListName.comparatorProperty().bind(associationTableView.comparatorProperty());
            associationTableView.setItems(sortedListName);
        });
        // Search by city bindings
        TextFields.bindAutoCompletion(inputCity, associationTableView.getItems().stream().map(Association::getVille).toArray());
        FilteredList<Association> filteredCity = new FilteredList<>(associationTableView.getItems(), e -> true);
        inputCity.setOnKeyReleased(e -> {
            inputCity.textProperty().addListener((observableValue, s, t1) -> {
                filteredCity.setPredicate(association -> {
                    if (t1 == null || t1.isEmpty())
                        return true;
                    return association.getVille().toLowerCase().startsWith(t1.toLowerCase());
                });
            });
            SortedList<Association> sortedCityList = new SortedList<>(filteredCity);
            sortedCityList.comparatorProperty().bind(associationTableView.comparatorProperty());
            associationTableView.setItems(sortedCityList);
        });
        // TODO : Search by manager
        // TODO : Client view of association list, render items inside HBox whilst passing association instance to that item, i hope you fucker know what i'm talking about when you wake up
        // Label for displaying number of current associations bindings
        size.textProperty().bind(Bindings.size((associationList)).asString("Associations : %d"));
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
