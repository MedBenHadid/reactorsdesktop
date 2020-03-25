package Packages.Chihab.Scenes;

import Main.Entities.UserSession;
import Packages.Chihab.Models.Category;
import Packages.Chihab.Services.CategoryService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DomainesController implements Initializable {
    @FXML
    private TableView<Category> catTV;
    @FXML
    private TableColumn<Category, Category> deleteOption;
    @FXML
    private TableColumn<Category, String> nomCol, descCol;
    @FXML
    private TableColumn<Category, Number> idCol;
    @FXML
    private TextArea description;
    @FXML
    private TextField input, nom;
    @FXML
    private TabPane tabPane;
    @FXML
    private Label size;
    @FXML
    private Tab newTab;

    private UserSession userSession = UserSession.getInstace();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList catList = FXCollections.observableArrayList();
        try {
            catList.addAll(CategoryService.getInstace().readAll());
        } catch (SQLException e) {
            Logger.getLogger(
                    DomainesController.class.getName()).log(
                    Level.INFO, null, e
            );
            showDialog(Alert.AlertType.ERROR, "", "Connexion au serveur échoué:", "Veuillez assurer la bonne connexion");
        }
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        /**
         * Initializing components reserved for Super admin (Create Tab , Update event handlers, Delete button and event handler)
         */

        if (1 == 1) {
            newTab.setDisable(false);
            deleteOption.setVisible(true);
            idCol.setVisible(true);
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            nomCol.setCellFactory(TextFieldTableCell.forTableColumn());
            descCol.setCellFactory(TextFieldTableCell.forTableColumn());
            /**
             * Update section
             */
            // Update name event handler
            nomCol.setOnEditCommit(
                    categoryStringCellEditEvent -> {
                        Category current = catTV.getItems().get(categoryStringCellEditEvent.getTablePosition().getRow());
                        if (checkValidUpdate(categoryStringCellEditEvent.getOldValue(), categoryStringCellEditEvent.getNewValue(), false, 5, 30, "nom"))
                            try {
                                current.setNom(categoryStringCellEditEvent.getNewValue());
                                CategoryService.getInstace().update(current);
                            } catch (SQLException e) {
                                Logger.getLogger(
                                        DomainesController.class.getName()).log(
                                        Level.INFO, null, e
                                );
                                showDialog(Alert.AlertType.ERROR, "Erreur de modification", e.getMessage(), "Modification échoué");
                            }
                        else
                            catTV.getItems().set(categoryStringCellEditEvent.getTablePosition().getRow(), current);
                    }
            );
            // Update description event handler
            descCol.setOnEditCommit(
                    categoryStringCellEditEvent -> {
                        Category current = catTV.getItems().get(categoryStringCellEditEvent.getTablePosition().getRow());
                        if (checkValidUpdate(categoryStringCellEditEvent.getOldValue(), categoryStringCellEditEvent.getNewValue(), false, 5, 255, "Description"))
                            try {
                                current.setDescription(categoryStringCellEditEvent.getNewValue());
                                CategoryService.getInstace().update(current);
                            } catch (SQLException e) {
                                Logger.getLogger(
                                        DomainesController.class.getName()).log(
                                        Level.INFO, null, e
                                );
                                showDialog(Alert.AlertType.ERROR, "Erreur de modification", e.getMessage(), "Modification échoué");
                            }
                        else
                            catTV.getItems().set(categoryStringCellEditEvent.getTablePosition().getRow(), current);
                    }
            );
            // Delete category : Adding button with an event handler
            deleteOption.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
            deleteOption.setCellFactory(param -> new TableCell<>() {
                private final Button deleteButton = new Button("Supprimer");
                @Override
                protected void updateItem(Category cat, boolean empty) {
                    super.updateItem(cat, empty);
                    if (cat == null) {
                        setGraphic(null);
                        return;
                    }
                    setGraphic(deleteButton);
                    deleteButton.setOnAction(event -> {
                        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmationDialog.setTitle("Suppression");
                        confirmationDialog.setHeaderText("Vous aller supprimer le domaine " + cat.getNom() + "!");
                        confirmationDialog.setContentText("Etes vous sure?");
                        Optional<ButtonType> confirmationResult = confirmationDialog.showAndWait();
                        if (confirmationResult.isPresent())
                            if (confirmationResult.get() == ButtonType.OK)
                                try {
                                    CategoryService.getInstace().delete(cat);
                                    catTV.getItems().remove(cat);
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
        } else {
            // Remove Create Tab if user is not an admin
            tabPane.getTabs().remove(1);
        }
        // Set TableView items
        catTV.setItems(catList);
        // Auto complete
        // Binding TextField to Category.getName()
        TextFields.bindAutoCompletion(input, catTV.getItems().stream().map(Category::getNom).toArray());
        FilteredList<Category> filtered = new FilteredList<>(catTV.getItems(), e -> true);
        input.setOnKeyReleased(e -> {
            input.textProperty().addListener((observableValue, s, t1) -> {
                filtered.setPredicate(category -> {
                    if (t1 == null || t1.isEmpty())
                        return true;
                    return category.getNom().toLowerCase().startsWith(t1.toLowerCase());
                });
                });
            SortedList<Category> sortedList = new SortedList<>(filtered);
            sortedList.comparatorProperty().bind(catTV.comparatorProperty());
            catTV.setItems(sortedList);
        });
        size.textProperty().bind(Bindings.size((catTV.getItems())).asString("Domaines : %d"));

    }

    /**
     * Create Section
     */
    public void createCategory() {
        if (checkValidStringInput(nom.getText(), false, 6, 30, "nom") && checkValidStringInput(description.getText(), false, 6, 255, "description")) {
            Category c = new Category();
            c.setNom(nom.getText());
            c.setDescription(description.getText());
            description.clear();
            nom.clear();
            try {
                c.setId(CategoryService.getInstace().create(c));
                catTV.getItems().add(c);
                showDialog(Alert.AlertType.CONFIRMATION, "Success", "", "Domaine ajouté avec succées");
            } catch (SQLException e) {
                Logger.getLogger(
                        DomainesController.class.getName()).log(
                        Level.INFO, null, e
                );
                showDialog(Alert.AlertType.ERROR, "Création échoué", "Raison " + e.getErrorCode(), "Création du domaine échoué!!");
            }
        }
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
