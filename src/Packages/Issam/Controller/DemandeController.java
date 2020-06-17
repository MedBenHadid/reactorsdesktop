package Packages.Issam.Controller;

import Packages.Chihab.Controllers.DomainesController;
import Packages.Issam.Models.Demande;
import Packages.Issam.Services.DemandeService;
import SharedResources.URLScenes;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DemandeController implements Initializable {
    @FXML
    public StackPane rootStackPane;
    @FXML
    private TableView<Demande> demandeTV;

    @FXML
    private TableColumn<Demande,Demande> deleteOption;
    @FXML
    private  TableColumn<Demande, String>  titleCol , descCol , addrCol ,phoneCol, ribCol ;

    @FXML
    private TableColumn<Demande, Number> idCol;

    @FXML
    private TextField inputTitle, inputAddress;
    @FXML
    private Button addButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList demandeList = FXCollections.observableArrayList();
        try {
            demandeTV.getItems().addAll(DemandeService.getInstance().readAll());
        }
        catch (SQLException e) {
            Logger.getLogger(
                    DemandeController.class.getName()).log(
                    Level.INFO, null, e
            );
            System.out.println("ok");
            showDialog(Alert.AlertType.ERROR, "", "Connexion au serveur échoué:", "Veuillez assurer la bonne connexion");
        }


        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        addrCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        ribCol.setCellValueFactory(new PropertyValueFactory<>("rib"));



        deleteOption.setVisible(true);
        idCol.setVisible(true);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descCol.setCellFactory(TextFieldTableCell.forTableColumn());
        addrCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneCol.setCellFactory(TextFieldTableCell.forTableColumn());
        ribCol.setCellFactory(TextFieldTableCell.forTableColumn());

        //Update title
        titleCol.setOnEditCommit(
                donStringCellEditEvent ->  {
                    Demande current = demandeTV.getItems().get(donStringCellEditEvent.getTablePosition().getRow());
                    if (checkValidUpdate(donStringCellEditEvent.getOldValue(), donStringCellEditEvent.getNewValue(), false, 5, 30, "nom"))
                        try {
                            current.setTitle(donStringCellEditEvent.getNewValue());
                            DemandeService.getInstance().update(current);
                        } catch (SQLException e) {
                            Logger.getLogger(
                                    DemandeController.class.getName()).log(
                                    Level.INFO, null, e
                            );
                            showDialog(Alert.AlertType.ERROR, "Erreur de modification", e.getMessage(), "Modification échoué");
                        }
                    else
                        demandeTV.getItems().set(donStringCellEditEvent.getTablePosition().getRow(), current);
                }
        );



        //Update descripotion
        descCol.setOnEditCommit(
                donStringCellEditEvent ->  {
                    Demande current = demandeTV.getItems().get(donStringCellEditEvent.getTablePosition().getRow());
                    if (checkValidUpdate(donStringCellEditEvent.getOldValue(), donStringCellEditEvent.getNewValue(), false, 5, 30, "nom"))
                        try {
                            current.setDescription(donStringCellEditEvent.getNewValue());
                            DemandeService.getInstance().update(current);
                        } catch (SQLException e) {
                            Logger.getLogger(
                                    DomainesController.class.getName()).log(
                                    Level.INFO, null, e
                            );
                            showDialog(Alert.AlertType.ERROR, "Erreur de modification", e.getMessage(), "Modification échoué");
                        }
                    else
                        demandeTV.getItems().set(donStringCellEditEvent.getTablePosition().getRow(), current);
                }
        );


        //Update descripotion
        addrCol.setOnEditCommit(
                donStringCellEditEvent ->  {
                    Demande current = demandeTV.getItems().get(donStringCellEditEvent.getTablePosition().getRow());
                    if (checkValidUpdate(donStringCellEditEvent.getOldValue(), donStringCellEditEvent.getNewValue(), false, 5, 30, "nom"))
                        try {
                            current.setAddress(donStringCellEditEvent.getNewValue());
                            DemandeService.getInstance().update(current);
                        } catch (SQLException e) {
                            Logger.getLogger(
                                    DomainesController.class.getName()).log(
                                    Level.INFO, null, e
                            );
                            showDialog(Alert.AlertType.ERROR, "Erreur de modification", e.getMessage(), "Modification échoué");
                        }
                    else
                        demandeTV.getItems().set(donStringCellEditEvent.getTablePosition().getRow(), current);
                }
        );


        //Update descripotion
        phoneCol.setOnEditCommit(
                donStringCellEditEvent ->  {
                    Demande current = demandeTV.getItems().get(donStringCellEditEvent.getTablePosition().getRow());
                    if (checkValidUpdate(donStringCellEditEvent.getOldValue(), donStringCellEditEvent.getNewValue(), false, 5, 30, "nom"))
                        try {
                            current.setPhone(donStringCellEditEvent.getNewValue());
                            DemandeService.getInstance().update(current);
                        } catch (SQLException e) {
                            Logger.getLogger(
                                    DomainesController.class.getName()).log(
                                    Level.INFO, null, e
                            );
                            showDialog(Alert.AlertType.ERROR, "Erreur de modification", e.getMessage(), "Modification échoué");
                        }
                    else
                        demandeTV.getItems().set(donStringCellEditEvent.getTablePosition().getRow(), current);
                }
        );


        //Update RIb
        ribCol.setOnEditCommit(
                donStringCellEditEvent ->  {
                    Demande current = demandeTV.getItems().get(donStringCellEditEvent.getTablePosition().getRow());
                    if (checkValidUpdate(donStringCellEditEvent.getOldValue(), donStringCellEditEvent.getNewValue(), false, 5, 30, "nom"))
                        try {
                            current.setRib(donStringCellEditEvent.getNewValue());
                            DemandeService.getInstance().update(current);
                        } catch (SQLException e) {
                            Logger.getLogger(
                                    DomainesController.class.getName()).log(
                                    Level.INFO, null, e
                            );
                            showDialog(Alert.AlertType.ERROR, "Erreur de modification", e.getMessage(), "Modification échoué");
                        }
                    else
                        demandeTV.getItems().set(donStringCellEditEvent.getTablePosition().getRow(), current);
                }
        );

        deleteOption.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteOption.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");
            @Override
            protected void updateItem(Demande demande, boolean empty) {
                super.updateItem(demande, empty);
                if (demande == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationDialog.setTitle("Suppression");
                    confirmationDialog.setHeaderText("Vous aller supprimer le domaine " + demande.getTitle() + "!");
                    confirmationDialog.setContentText("Etes vous sure?");
                    Optional<ButtonType> confirmationResult = confirmationDialog.showAndWait();
                    if (confirmationResult.isPresent())
                        if (confirmationResult.get() == ButtonType.OK)
                            try {
                                DemandeService.getInstance().delete(demande);
                                demandeTV.getItems().remove(demande);
                                showDialog(Alert.AlertType.CONFIRMATION, "", "", "Domaine supprimée !!");
                            } catch (SQLException e) {
                                Logger.getLogger(
                                        DomainesController.class.getName()).log(
                                        Level.INFO, null, e
                                );
                                showDialog(Alert.AlertType.ERROR, "Suppression échoué", "Raison : Reference", "Domaine ne peut pas étre supprimé!");
                            }
                });
            }
        });
        addButton.setOnAction(e -> {
            try {
                //AnchorPane createMission = FXMLLoader.load(getClass().getResource("/Packages/Mohamed/Scenes/MissionCreate.fxml"));
                JFXDialogLayout layout = new JFXDialogLayout();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(URLScenes.demandeCreate));
                AnchorPane demandeCreate = loader.load();
                JFXDialog dialog = new JFXDialog(rootStackPane, layout, JFXDialog.DialogTransition.CENTER);
                JFXButton close = new JFXButton("Cancel");
                JFXButton foo = (JFXButton) loader.getNamespace().get("createButton");
                foo.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    dialog.close();
                    demandeList.add(((DemandeCreateController) loader.getController()).getDemande());
                });
                layout.setBody(demandeCreate);
                layout.setHeading(new Text("Ajout d'une nouvelle mission"));

                close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> dialog.close());
                layout.setActions(close);
                dialog.show();


            } catch (IOException ex) {

                Logger.getLogger(
                        DemandeController.class.getName()).log(
                        Level.WARNING, null, e
                );
                ex.printStackTrace();
            }
        });

        // Search by name bindings
        // TextFields.bindAutoCompletion(inputTitle, donTV.getItems().stream().map(Don::getTitle).toArray());
        FilteredList<Demande> filteredName = new FilteredList<>(demandeTV.getItems(), e -> true);
        inputTitle.setOnKeyReleased(e -> {
            inputTitle.textProperty().addListener((observableValue, s, t1) -> {
                filteredName.setPredicate(demande -> {
                    if (t1 == null || t1.isEmpty())
                        return true;
                    return demande.getTitle().toLowerCase().startsWith(t1.toLowerCase());
                });
            });
            SortedList<Demande> sortedListName = new SortedList<>(filteredName);
            sortedListName.comparatorProperty().bind(demandeTV.comparatorProperty());
            demandeTV.setItems(sortedListName);
        });
        //TextFields.bindAutoCompletion(inputCity, missionTableView.getItems().stream().map(Mission::getLocation).toArray());
        FilteredList<Demande> filteredCity = new FilteredList<>(demandeTV.getItems(), e -> true);
        inputAddress.setOnKeyReleased(e -> {
            inputAddress.textProperty().addListener((observableValue, s, t1) -> {
                filteredCity.setPredicate(demande -> {
                    if (t1 == null || t1.isEmpty())
                        return true;
                    return demande.getAddress().toLowerCase().startsWith(t1.toLowerCase());
                });
            });
            SortedList<Demande> sortedCityList = new SortedList<>(filteredCity);
            sortedCityList.comparatorProperty().bind(demandeTV.comparatorProperty());
            demandeTV.setItems(sortedCityList);
        });
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
