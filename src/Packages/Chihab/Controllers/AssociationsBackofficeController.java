package Packages.Chihab.Controllers;

import Main.Entities.User;
import Main.Entities.UserSession;
import Packages.Chihab.Models.Association;
import Packages.Chihab.Services.AssociationService;
import SharedResources.URLScenes;
import SharedResources.URLServer;
import SharedResources.Utils.BinaryValidator.RegexValidator;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.apache.commons.net.ftp.FTP;
import org.controlsfx.control.textfield.TextFields;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AssociationsBackofficeController implements Initializable {
    @FXML
    private TableView<Association> associationTableView;
    @FXML
    private TableColumn<Association, Association> deleteOption, pieceCol;
    @FXML
    public StackPane rootStackPane;
    @FXML
    private TableColumn<Association, JFXComboBox<String>> villeCol;
    @FXML
    private TableColumn<User, String> managerCol;
    @FXML
    private JFXTextField inputName, inputCity;
    @FXML
    private Label size;
    @FXML
    private JFXButton addButton;
    @FXML
    private TableColumn<Association, String> domaineCol;
    @FXML
    private TableColumn<Association, Association> statusCol, nomCol;
    private final boolean isAdmin;
    private FTPInterface ftpInterface;
    private final ObservableList<Association> associationList;
    @FXML
    private TableColumn actions;

    public AssociationsBackofficeController() {
        this.isAdmin = UserSession.getInstace().getUser().isAdmin();
        try {
            this.ftpInterface = FTPInterface.getInstance(URLServer.ftpServerLink, URLServer.ftpSocketPort, URLServer.ftpUser, URLServer.ftpPassword);
        } catch (IOException e) {
            Logger.getLogger(AssociationsBackofficeController.class.getName()).log(Level.INFO, null, e);
        }
        associationList = FXCollections.observableArrayList();
        try {
            associationList.addAll(AssociationService.getInstace().readAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 1: Based on role, disable { addButton, deleteOption, managerCol, statusCol}
        addButton.setVisible(this.isAdmin);
        actions.setVisible(this.isAdmin);

        nomCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        nomCol.setCellFactory(param -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Association a, boolean empty) {
                super.updateItem(a, empty);
                if (a == null) {
                    setGraphic(null);
                    return;
                } else {
                    File imageAss = null;
                    try {
                        imageAss = ftpInterface.downloadFile(URLServer.associationImageDir + a.getPhotoAgence(), FTP.BINARY_FILE_TYPE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assert imageAss != null;
                    imageView.setImage(new Image(imageAss.toURI().toString()));
                    imageView.setFitHeight(100.0);
                    imageView.setFitWidth(100.0);
                    setText(a.getNom());
                    setGraphic(imageView);
                }
            }
        });
        villeCol.setCellValueFactory(new PropertyValueFactory<>("ville"));
        managerCol.setCellValueFactory(new PropertyValueFactory<>("managerUserName"));
        domaineCol.setCellValueFactory(new PropertyValueFactory<>("domaineNom"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("approuved"));
        associationTableView.setItems(associationList);
        statusCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        statusCol.setCellFactory(param -> new TableCell<>() {
            private final JFXToggleButton sliderButton = new JFXToggleButton();

            @Override
            protected void updateItem(Association a, boolean empty) {
                super.updateItem(a, empty);
                if (a == null) {
                    setGraphic(null);
                    return;
                }
                sliderButton.getStyleClass().add("fx-toggle-button");
                sliderButton.selectedProperty().setValue(a.isApprouved());
                sliderButton.textProperty().bind(Bindings.createStringBinding(() -> sliderButton.selectedProperty().getValue() ? "Approuvé" : "Non approuvé", sliderButton.selectedProperty()));
                setGraphic(sliderButton);
                sliderButton.setOnAction(event -> {
                    if (!sliderButton.selectedProperty().getValue()) {
                        JFXDialogLayout layout = new JFXDialogLayout();
                        layout.setHeading(new Text("Dissaproving association " + a.getNom()));
                        JFXTextArea tr = new JFXTextArea();
                        tr.setPromptText("Veuillez saisir la raison");
                        tr.getValidators().add(new RegexValidator("La raison doit etre comprise entre 5 et 255", "^[\\d\\w]{5,255}"));
                        tr.setOnKeyTyped(e -> tr.validate());
                        layout.setBody(tr);
                        JFXDialog dialog = new JFXDialog(rootStackPane, layout, JFXDialog.DialogTransition.CENTER);
                        JFXButton verify = new JFXButton("Désapprouver");
                        JFXButton close = new JFXButton("Fermer");
                        close.getStyleClass().addAll("jfx-button-error");
                        close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> dialog.close());
                        verify.disableProperty().bind(Bindings.createBooleanBinding(() -> !tr.textProperty().get().matches("^[\\d\\w]{5,255}"), tr.textProperty()));
                        verify.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                            a.setApprouved(false);
                            try {
                                AssociationService.getInstace().update(a);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            } finally {
                                dialog.close();
                                JFXDialogLayout l = new JFXDialogLayout();
                                l.setHeading(new Text(a.getNom() + " : Approval"));
                                l.setBody(new Label("Modification enregistré avec success"));
                                JFXDialog d = new JFXDialog(rootStackPane, l, JFXDialog.DialogTransition.CENTER);
                                JFXButton fermer = new JFXButton("Fermer");
                                fermer.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> d.close());
                                l.setActions(fermer);
                                d.show();
                                // TODO : Email
                            }
                        });
                        layout.setActions(close, verify);
                        dialog.show();
                    } else {
                        // TODO : Approve
                        a.setApprouved(true);
                        try {
                            AssociationService.getInstace().update(a);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        } finally {
                            JFXDialogLayout l = new JFXDialogLayout();
                            l.setHeading(new Text("Modification de ville de " + a.getNom()));
                            l.setBody(new Label("Modification avec success"));
                            JFXDialog d = new JFXDialog(rootStackPane, l, JFXDialog.DialogTransition.CENTER);
                            JFXButton fermer = new JFXButton("Fermer");
                            fermer.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> d.close());
                            l.setActions(fermer);
                            d.show();
                        }
                    }
                });
            }
        });
        villeCol.setCellValueFactory(associationJFXComboBoxCellDataFeatures -> {
            Association a = associationJFXComboBoxCellDataFeatures.getValue();
            JFXComboBox<String> comboBox = new JFXComboBox<>();
            comboBox.setItems(FXCollections.observableArrayList("Ariana", "Béja", "Ben Arous", "Bizerte", "Gabes", "Gafsa", "Jendouba", "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine", "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan"));
            comboBox.getSelectionModel().select(a.getVille());
            comboBox.valueProperty().addListener((observableValue, s, t1) -> {
                if (!s.equals(t1)) {
                    a.setVille(t1);
                    try {
                        AssociationService.getInstace().update(a);
                    } catch (SQLException e) {
                        Logger.getLogger(AssociationsBackofficeController.class.getName()).log(Level.INFO, "Error editing ville", e);
                    } finally {
                        JFXDialogLayout layout = new JFXDialogLayout();
                        layout.setBody(new Label("Modifié avec success"));
                        layout.setHeading(new Text("Modification de ville de " + a.getNom()));
                        JFXDialog dialog = new JFXDialog(rootStackPane, layout, JFXDialog.DialogTransition.CENTER);
                        JFXButton close = new JFXButton("Fermer");
                        close.getStyleClass().addAll("jfx-button-secondary-variant");
                        close.setButtonType(JFXButton.ButtonType.RAISED);
                        close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> dialog.close());
                        layout.setActions(close);
                        dialog.show();
                    }
                }
            });
            comboBox.selectionModelProperty().addListener((observableValue, stringSingleSelectionModel, t1) -> {
                // TODO : Update
            });
            return new SimpleObjectProperty<>(comboBox);
        });
        // Update section
        // Update description event handler
        // Delete section
        deleteOption.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteOption.setCellFactory(param -> new TableCell<>() {
            private final JFXButton deleteButton = new JFXButton("Delete");

            @Override
            protected void updateItem(Association a, boolean empty) {
                deleteButton.getStyleClass().addAll("jfx-button-secondary");
                super.updateItem(a, empty);
                if (a == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    // TODO : JFXDialog
                    JFXDialogLayout layout = new JFXDialogLayout();
                    layout.setHeading(new Label("Are you sure that you want to delete " + a.getNom() + "?"));
                    JFXButton confirm = new JFXButton();
                    confirm.setText("I know what i'm doing, delete!");
                    confirm.setOnMouseClicked(mouseEvent -> {
                        try {
                            AssociationService.getInstace().delete(a);
                            associationTableView.getItems().remove(a);
                        } catch (SQLException e) {
                            Logger.getLogger(
                                    AssociationsBackofficeController.class.getName()).log(
                                    Level.INFO, null, e
                            );
                        } finally {
                            JFXDialogLayout l = new JFXDialogLayout();
                            layout.setBody(new Text("Association " + a.getNom() + " succesfully deleted"));
                            layout.setHeading(new Label("Delete:"));
                            JFXDialog dialog = new JFXDialog(rootStackPane, l, JFXDialog.DialogTransition.CENTER);
                            JFXButton close = new JFXButton("Exit");
                            close.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> dialog.close());
                            layout.setActions(close);
                            dialog.show();
                        }
                    });
                    layout.setBody(confirm);
                    JFXDialog d = new JFXDialog(rootStackPane, layout, JFXDialog.DialogTransition.CENTER);
                    JFXButton close = new JFXButton("Cancel");
                    close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> d.close());
                    layout.setActions(close);
                    d.show();
                });
            }
        });
        // Delete Section
        // Piece justificative section
        pieceCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        pieceCol.setCellFactory(param -> new TableCell<>() {
            private final JFXButton pieceButton = new JFXButton("Afficher");

            @Override
            protected void updateItem(Association a, boolean empty) {
                pieceButton.getStyleClass().addAll("jfx-button-secondary-variant");
                super.updateItem(a, empty);
                if (a == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(pieceButton);
                pieceButton.setOnAction(event -> {
                    try {
                        File file = ftpInterface.downloadFile(URLServer.associationPieceDir + a.getPieceJustificatif(), FTP.BINARY_FILE_TYPE);
                        if (!Desktop.isDesktopSupported())
                            return;
                        Desktop desktop = Desktop.getDesktop();
                        if (file.exists()) {
                            desktop.open(file);
                            file.deleteOnExit();
                        }
                    } catch (Exception e) {
                        Logger.getLogger(
                                AssociationsBackofficeController.class.getName()).log(
                                Level.SEVERE, null, e
                        );
                        JFXDialogLayout layout = new JFXDialogLayout();
                        layout.setBody(new Label("Error fetching file"));
                        layout.setHeading(new Text("FTP Related error"));
                        JFXDialog dialog = new JFXDialog(rootStackPane, layout, JFXDialog.DialogTransition.CENTER);
                        JFXButton close = new JFXButton("Annuler");
                        close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> dialog.close());
                        layout.setActions(close);
                        dialog.show();
                    }

                });
            }
        });
        // Piece justificative section
        // Profile Section
        associationTableView.setRowFactory(tv -> {
            TableRow<Association> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Association rowData = row.getItem();
                    FXMLLoader loader;
                    if (1 == 1)
                        loader = new FXMLLoader(getClass().getResource(URLScenes.associationUpdateProfile));
                    else
                        loader = new FXMLLoader(getClass().getResource(URLScenes.associationProfile));
                    AssociationProfileUpdateController controller = new AssociationProfileUpdateController(rowData);
                    loader.setController(controller);
                    StackPane createAssociation = null;
                    try {
                        createAssociation = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    JFXDialogLayout layout = new JFXDialogLayout();
                    layout.setBody(createAssociation);
                    layout.setHeading(new Text("Profile :" + rowData.getNom()));
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
        // Add association section
        addButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(URLScenes.associationCreate));
                AnchorPane createAssociation = loader.load();
                JFXButton foo = (JFXButton) loader.getNamespace().get("validateButton");
                JFXDialogLayout layout = new JFXDialogLayout();
                layout.setBody(createAssociation);
                layout.setHeading(new Text("Ajout d'une nouvelle association"));
                JFXDialog dialog = new JFXDialog(rootStackPane, layout, JFXDialog.DialogTransition.CENTER);
                foo.setOnMouseClicked(mouseEvent -> {
                    associationList.add(((AssociationCreateController) loader.getController()).getAssociation());
                    dialog.close();
                });
                JFXButton close = new JFXButton("Cancel");
                close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> dialog.close());
                layout.setActions(close);
                dialog.show();
            } catch (IOException ex) {
                Logger.getLogger(AssociationsBackofficeController.class.getName()).log(Level.WARNING, "Exception loading create FXML", e);
            }
        });
        // Add association section
        // Search by name bindings
        TextFields.bindAutoCompletion(inputName, associationTableView.getItems().stream().map(Association::getNom).toArray());
        FilteredList<Association> filteredName = new FilteredList<>(associationTableView.getItems(), e -> true);
        inputName.setOnKeyReleased(e -> {
            inputName.textProperty().addListener((observableValue, s, t1) -> filteredName.setPredicate(association -> {
                if (t1 == null || t1.isEmpty())
                    return true;
                return association.getNom().toLowerCase().startsWith(t1.toLowerCase());
            }));
            SortedList<Association> sortedListName = new SortedList<>(filteredName);
            sortedListName.comparatorProperty().bind(associationTableView.comparatorProperty());
            associationTableView.setItems(sortedListName);
        });
        // Search by city bindings
        TextFields.bindAutoCompletion(inputCity, associationTableView.getItems().stream().map(Association::getVille).toArray());
        FilteredList<Association> filteredCity = new FilteredList<>(associationTableView.getItems(), e -> true);
        inputCity.setOnKeyReleased(e -> {
            inputCity.textProperty().addListener((observableValue, s, t1) -> filteredCity.setPredicate(association -> {
                if (t1 == null || t1.isEmpty())
                    return true;
                return association.getVille().toLowerCase().startsWith(t1.toLowerCase());
            }));
            SortedList<Association> sortedCityList = new SortedList<>(filteredCity);
            sortedCityList.comparatorProperty().bind(associationTableView.comparatorProperty());
            associationTableView.setItems(sortedCityList);
        });
        size.textProperty().bind(Bindings.size((associationList)).asString("%d associations inscrits"));

    }





}
