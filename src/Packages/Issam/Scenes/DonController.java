package Packages.Issam.Scenes;

import Packages.Chihab.Controllers.DomainesController;
import Packages.Issam.Models.Don;
import Packages.Issam.Services.DonService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

// 1 : --module-path "C:\javafx-sdk-11.0.2\lib" --add-modules javafx.controls,javafx.fxml --add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED
// 2 : --add-exports=javafx.graphics/com.sun.javafx.util=ALL-UNNAMED
public class DonController implements Initializable {
    @FXML
    private TableView<Don> donTV;
    @FXML
    private TableColumn<Don, Don> deleteOption;
    @FXML
    private TableColumn<Don, String> titleCol, descCol, addrCol, phoneCol;
    @FXML
    private TableColumn<Don, Number> idCol;
    @FXML
    private TextField title, description, address, phone, creationDate;
    @FXML
    private Tab newTab;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList donList = FXCollections.observableArrayList();
        try {
            donTV.getItems().addAll(DonService.getInstace().readAll());
        } catch (SQLException e) {
            Logger.getLogger(
                    DonController.class.getName()).log(
                    Level.INFO, null, e
            );
            System.out.println("ok");
            showDialog(Alert.AlertType.ERROR, "", "Connexion au serveur échoué:", "Veuillez assurer la bonne connexion");
        }


        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        addrCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        newTab.setDisable(false);
        deleteOption.setVisible(true);
        idCol.setVisible(true);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descCol.setCellFactory(TextFieldTableCell.forTableColumn());
        addrCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneCol.setCellFactory(TextFieldTableCell.forTableColumn());


        //Update title
        titleCol.setOnEditCommit(
                donStringCellEditEvent -> {
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
                donStringCellEditEvent -> {
                    Don current = donTV.getItems().get(donStringCellEditEvent.getTablePosition().getRow());
                    if (checkValidUpdate(donStringCellEditEvent.getOldValue(), donStringCellEditEvent.getNewValue(), false, 5, 30, "nom"))
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
                donStringCellEditEvent -> {
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
                donStringCellEditEvent -> {
                    Don current = donTV.getItems().get(donStringCellEditEvent.getTablePosition().getRow());
                    if (checkValidUpdate(donStringCellEditEvent.getOldValue(), donStringCellEditEvent.getNewValue(), false, 5, 30, "nom"))
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
                            try {
                                DonService.getInstace().delete(don);
                                donTV.getItems().remove(don);
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

    }


    /**
     * Create Section
     **/

    public void createDon() {
        if (checkValidStringInput(title.getText(), false, 6, 30, "Tiitle") && checkValidStringInput(description.getText(), false, 6, 255, "Description")) {
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
            try {
                don.setId(DonService.getInstace().create(don));
                donTV.getItems().add(don);
                showDialog(Alert.AlertType.CONFIRMATION, "Success", "", "Domaine ajouté avec succées");
            } catch (SQLException e) {
                Logger.getLogger(
                        DonController.class.getName()).log(
                        Level.INFO, null, e
                );
                showDialog(Alert.AlertType.ERROR, "Création échoué", "Raison " + e.getErrorCode(), "Création du domaine échoué!!");
            }
        }
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
