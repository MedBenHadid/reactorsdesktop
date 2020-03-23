package Packages.Chihab.Scenes;

import Packages.Chihab.Models.Category;
import Packages.Chihab.Services.CategoryService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class DomainesController implements Initializable {
    @FXML
    private TableView<Category> catTV;
    @FXML
    private TableColumn<Category, String> nomCol;
    @FXML
    private TableColumn<Category, String> descCol;
    @FXML
    private TableColumn<Category, Number> idCol;
    @FXML
    private TableColumn<Category, Category> deleteOption, updateOption, options;
    @FXML
    private TextField input;
    /**
     * Create Section
     */
    @FXML
    private TextField nom;
    @FXML
    private TextArea description;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fetch();
        TextFields.bindAutoCompletion(input, catTV.getItems().stream().map(Category::getNom).toArray());
        // TODO : Filter catTV list based on the array
    }

    private void fetch() {
        ObservableList list = FXCollections.observableArrayList();
        try {
            list.addAll(CategoryService.getInstace().readAll());
        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Error whilst fetching data");
            a.setHeaderText("Raison : Mana3rach ya frr ");
            a.showAndWait();
        }
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nomCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nomCol.setOnEditCommit(
                categoryStringCellEditEvent -> {
                    Category current = catTV.getItems().get(categoryStringCellEditEvent.getTablePosition().getRow());
                    current.setNom(categoryStringCellEditEvent.getNewValue());
                }
        );
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descCol.setOnEditCommit(
                categoryStringCellEditEvent -> {
                    Category current = catTV.getItems().get(categoryStringCellEditEvent.getTablePosition().getRow());
                    current.setDescription(categoryStringCellEditEvent.getNewValue());
                }
        );
        /**
         * Delete Section
         */
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
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation :");
                    alert.setHeaderText("Vous aller supprimer le domaine " + cat.getNom() + "!");
                    alert.setContentText("Etes vous sure?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        try {
                            CategoryService.getInstace().delete(cat);
                            catTV.getItems().remove(cat);
                            Alert a = new Alert(Alert.AlertType.INFORMATION);
                            a.setHeaderText("Domaine supprimée !!");
                            a.show();
                        } catch (SQLException e) {
                            Alert a = new Alert(Alert.AlertType.ERROR);
                            a.setHeaderText("Domaine ne peut pas étre supprimé!");
                            a.setContentText("Raison : Reference ");
                            a.showAndWait();
                        }
                    }
                });
            }
        });
        /**
         * Update Section
         */
        updateOption.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        updateOption.setCellFactory(param -> new TableCell<>() {
            public final Button update = new Button("Modifier");

            @Override
            protected void updateItem(Category cat, boolean empty) {
                super.updateItem(cat, empty);
                if (cat == null) {
                    setGraphic(null);
                    return;
                }
                // TODO : Set it disabled at first, and enabled when value changes
                update.setDisable(true);
                setGraphic(update);
                update.setOnAction(event -> {
                    try {
                        CategoryService.getInstace().update(cat);
                        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                        a.setHeaderText("Domaine modifé avec succées");
                        a.show();
                        fetch();
                    } catch (SQLException e) {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setHeaderText("Domaine ne peut pas étre modifier!");
                        a.setContentText("Raison : JCP L7a9 ");
                        a.showAndWait();
                    }
                });
            }
        });
        catTV.setItems(list);
    }

    public void createCategory() {
        if (nom.getText().isBlank()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Veuillez saisir le nom du domain");
            a.show();
        } else if (description.getText().isBlank()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Veuillez saisir la description du domain");
            a.show();
        } else {
            Category c = new Category();
            c.setNom(nom.getText());
            c.setDescription(description.getText());
            description.clear();
            nom.clear();
            try {
                c.setId(CategoryService.getInstace().create(c));
                catTV.getItems().add(c);
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setContentText("Domaine ajouté avec succées");
                a.show();
            } catch (SQLException e) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText("Domaine ne peut pas étre créer!");
                a.setContentText("Raison : IDK");
                a.show();
            }
        }
    }


}
