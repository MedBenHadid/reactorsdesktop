package Packages.Nasri.ui.controllers.admin;

import Packages.Nasri.services.ServiceHebergementOffer;
import Packages.Nasri.services.ServiceHebergementRequest;
import Packages.Nasri.ui.models.HebergementOfferTableModel;
import Packages.Nasri.ui.models.HebergementRequestTableModel;
import SharedResources.URLScenes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private TextField searchInputHebergementOffer;
    @FXML
    private TextField searchInputHebergementRequest;
    @FXML
    private TableView hebergementOffersTable;
    @FXML
    private TableView hebergementRequestsTable;

    // Offers table collumns
    @FXML
    private TableColumn<HebergementOfferTableModel, String> userNameOffersTableCol
            = new TableColumn<>("Nom d'utilisateur");
    @FXML
    private TableColumn<HebergementOfferTableModel, String> descriptionOffersTableCol
            = new TableColumn<>("Descirption");
    @FXML
    private TableColumn<HebergementOfferTableModel, String> governoratOffersTableCol
            = new TableColumn<>("Governorat");
    @FXML
    private TableColumn<HebergementOfferTableModel, String> numberRoomsOffersTableCol
            = new TableColumn<>("Nombre de chambres");
    @FXML
    private TableColumn<HebergementOfferTableModel, String> durationOffersTableCol
            = new TableColumn<>("Durée (mois)");
    @FXML
    private TableColumn<HebergementOfferTableModel, String> stateOffersTableCol
            = new TableColumn<>("Etat");
    @FXML
    private TableColumn<HebergementOfferTableModel, String> telephoneOffersTableCol
            = new TableColumn<>("Telephone");
    @FXML
    private TableColumn<HebergementOfferTableModel, String> imageOffersTableCol
            = new TableColumn<>("Image");



    // Requests Table columns
    @FXML
    private TableColumn<HebergementRequestTableModel, String> userNameRequestTableCol
            = new TableColumn<>("Nom d'utilisateur");
    @FXML
    private TableColumn<HebergementRequestTableModel, String> demanderNameRequestTableCol
            = new TableColumn<>("Nom du demandeur");
    @FXML
    private TableColumn<HebergementRequestTableModel, String> descriptionRequestTableCol
            = new TableColumn<>("Description");
    @FXML
    private TableColumn<HebergementRequestTableModel, String> regionRequestTableCol
            = new TableColumn<>("Region");
    @FXML
    private TableColumn<HebergementRequestTableModel, String> nativeCountryRequestTableCol
            = new TableColumn<>("Pays d'origine");
    @FXML
    private TableColumn<HebergementRequestTableModel, String> arrivalDateRequestTableCol
            = new TableColumn<>("Date d'arrivée");
    @FXML
    private TableColumn<HebergementRequestTableModel, String> stateRequestTableCol
            = new TableColumn<>("Etat");
    @FXML
    private TableColumn<HebergementRequestTableModel, String> passportNumberRequestTableCol
            = new TableColumn<>("Numero de passport");
    @FXML
    private TableColumn<HebergementRequestTableModel, String> civilStateRequestTableCol
            = new TableColumn<>("Etat civil");
    @FXML
    private TableColumn<HebergementRequestTableModel, String> telephoneRequestTableCol
            = new TableColumn<>("Telephone");

    private ObservableList<HebergementOfferTableModel> hebergementOffersObservables;

    private ObservableList<HebergementRequestTableModel> hebergementRequestObservables;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //1. get data for hebergementRequests and hebergementOffers
        ArrayList<HebergementOfferTableModel> hebergementOffers
                = HebergementOfferTableModel.get(new ServiceHebergementOffer().get());
        ArrayList<HebergementRequestTableModel> hebergementRequests
                = HebergementRequestTableModel.get(new ServiceHebergementRequest().get());

        hebergementOffersObservables = FXCollections.observableArrayList();

        hebergementRequestObservables = FXCollections.observableArrayList();

        // setting up the OffersTable columns
        userNameOffersTableCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        descriptionOffersTableCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        governoratOffersTableCol.setCellValueFactory(new PropertyValueFactory<>("governorat"));
        numberRoomsOffersTableCol.setCellValueFactory(new PropertyValueFactory<>("numberRooms"));
        durationOffersTableCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        stateOffersTableCol.setCellValueFactory(new PropertyValueFactory<>("state"));
        telephoneOffersTableCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));

//        imageOffersTableCol.setCellValueFactory(new PropertyValueFactory<>("image"));
        imageOffersTableCol.setCellValueFactory(new PropertyValueFactory<>("image"));
        imageOffersTableCol.setCellFactory(column -> {
            TableCell<HebergementOfferTableModel, String> cell = new TableCell<HebergementOfferTableModel, String>() {
                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(String filePath, boolean empty) {
                    if (filePath != null) {
                        try {
                            Image image = new Image(new FileInputStream(filePath));
                            ImageView imageView = new ImageView(image);

                            imageView.setFitHeight(300);
                            imageView.setFitWidth(200);
                            imageView.setPreserveRatio(true);

                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(imageView);
                            }
                        } catch (FileNotFoundException e) {
                            System.out.println(e.getMessage() + ": " + filePath);
                        }
                    }
                }
            };

            return cell;
        });

        hebergementOffersTable.getColumns()
                .addAll(userNameOffersTableCol, descriptionOffersTableCol, governoratOffersTableCol,
                        numberRoomsOffersTableCol, durationOffersTableCol, stateOffersTableCol,
                        telephoneOffersTableCol, imageOffersTableCol);


        // setting up the RequestsTable Columns
        userNameRequestTableCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        demanderNameRequestTableCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionRequestTableCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        regionRequestTableCol.setCellValueFactory(new PropertyValueFactory<>("region"));
        nativeCountryRequestTableCol.setCellValueFactory(new PropertyValueFactory<>("nativeCountry"));

        arrivalDateRequestTableCol.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));

        stateRequestTableCol.setCellValueFactory(new PropertyValueFactory<>("state"));
        passportNumberRequestTableCol.setCellValueFactory(new PropertyValueFactory<>("passportNumber"));
        civilStateRequestTableCol.setCellValueFactory(new PropertyValueFactory<>("civilState"));
        telephoneRequestTableCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        hebergementRequestsTable.getColumns()
                .addAll(userNameRequestTableCol, demanderNameRequestTableCol, descriptionRequestTableCol,
                        regionRequestTableCol, nativeCountryRequestTableCol, arrivalDateRequestTableCol, stateRequestTableCol,
                        passportNumberRequestTableCol, civilStateRequestTableCol, telephoneRequestTableCol
                        );


        //2. add it to the tables
        loadOffersTable();
        loadRequestsTable();
        System.out.println("Opened");
    }

    private void loadRequestsTable() {
        ArrayList<HebergementRequestTableModel> hebergementRequests
                = HebergementRequestTableModel.get(new ServiceHebergementRequest().get());
        this.hebergementRequestsTable.getItems().addAll(hebergementRequests);
        this.addButtonToRequestTable();
    }

    private void loadOffersTable() {
        ArrayList<HebergementOfferTableModel> hebergementOffers
                = HebergementOfferTableModel.get(new ServiceHebergementOffer().get());
        this.hebergementOffersTable.getItems().addAll(hebergementOffers);
        this.addButtonToOffersTable();
    }

    @FXML
    protected void handleSearchHebergementOffersButtonAction() {
        System.out.println("handleSearchHebergementOffersButtonAction clicked");
    }

    @FXML
    protected void handleAddHebergementOfferButtonAction() throws IOException {
        URL url = getClass().getResource(URLScenes.refugeesAdminAddHebergementOfferScene);
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        Stage addHebergementOfferStage = new Stage();
        addHebergementOfferStage.setScene(scene);
        addHebergementOfferStage.show();
        System.out.println("handleAddHebergementAddButtonAction clicked");
    }

    @FXML
    protected void handleRefreshHebergementOffersButtonAction() {
        this.hebergementOffersTable.setItems(FXCollections.observableArrayList());
        this.loadOffersTable();
    }

    @FXML
    protected void handleSearchHebergementRequestsButtonAction() {
        System.out.println("handleSearchHebergementRequestsButtonAction clicked");
    }

    @FXML
    protected void handleAddHebergementRequestButtonAction() throws IOException {
        URL url = getClass().getResource(URLScenes.refugeesAdminAddHebergementRequestScene);
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        Stage addHebergementRequestStage = new Stage();
        addHebergementRequestStage.setScene(scene);
        addHebergementRequestStage.show();
    }

    @FXML
    protected void handleRefreshHebergementRequestsButtonAction() {
        this.hebergementRequestsTable.setItems(FXCollections.observableArrayList());
        this.loadRequestsTable();
    }

    // code from: https://riptutorial.com/javafx/example/27946/add-button-to-tableview
    private void addButtonToOffersTable() {
        // I can't fix why this method keeps adding everytime i hit refresh it keep adding a new column of Action buttons, even though i try to empty the table
        // this is a hacky fix -- test on the number of column that shoudln't be > 8
        if (hebergementOffersTable.getColumns().size() <= 8) {
            TableColumn<HebergementOfferTableModel, Void> colBtn = new TableColumn("Actions");

            Callback<TableColumn<HebergementOfferTableModel, Void>, TableCell<HebergementOfferTableModel, Void>> cellFactory
                    = new Callback<TableColumn<HebergementOfferTableModel, Void>, TableCell<HebergementOfferTableModel, Void>>() {
                @Override
                public TableCell<HebergementOfferTableModel, Void> call(final TableColumn<HebergementOfferTableModel, Void> param) {
                    final TableCell<HebergementOfferTableModel, Void> cell = new TableCell<HebergementOfferTableModel, Void>() {


                        private final Button deleteButton = new Button("Supprimer");

                        {
                            deleteButton.getStyleClass().add("deleteButton");
                            deleteButton.setOnAction((ActionEvent event) -> {
                                HebergementOfferTableModel data = getTableView().getItems().get(getIndex());
                                System.out.println("selectedData: " + data.getId());
                                //open deleteOffer stage;
                                Alert alert =
                                        new Alert(Alert.AlertType.WARNING, "Vous êtes sure ?", ButtonType.YES, ButtonType.CANCEL);
                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.get() == ButtonType.YES) {
                                    ServiceHebergementOffer serviceHebergementOffer = new ServiceHebergementOffer();
                                    serviceHebergementOffer.delete(data.getId());
                                    handleRefreshHebergementOffersButtonAction();
                                }
                            });
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                HBox buttonsContainer = new HBox();
                                buttonsContainer.getStyleClass().add("actionsButtonsContainer");
                                buttonsContainer.getChildren().add(deleteButton);
                                setGraphic(buttonsContainer);
                            }
                        }
                    };
                    return cell;
                }
            };

            colBtn.setCellFactory(cellFactory);

            hebergementOffersTable.getColumns().add(colBtn);
        }
    }
    private void addButtonToRequestTable() {
        // I can't fix why this method keeps adding everytime i hit refresh it keep adding a new column of Action buttons, even though i try to empty the table
        // this is a hacky fix -- test on the number of column that shoudln't be > 8
        if (hebergementRequestsTable.getColumns().size() <= 10) {
            TableColumn<HebergementRequestTableModel, Void> colBtn = new TableColumn("Actions");

            Callback<TableColumn<HebergementRequestTableModel, Void>, TableCell<HebergementRequestTableModel, Void>> cellFactory
                    = new Callback<TableColumn<HebergementRequestTableModel, Void>, TableCell<HebergementRequestTableModel, Void>>() {
                @Override
                public TableCell<HebergementRequestTableModel, Void> call(final TableColumn<HebergementRequestTableModel, Void> param) {
                    final TableCell<HebergementRequestTableModel, Void> cell = new TableCell<HebergementRequestTableModel, Void>() {
                        private final Button deleteButton = new Button("Supprimer");
                        {
                            deleteButton.getStyleClass().add("deleteButton");
                            deleteButton.setOnAction((ActionEvent event) -> {
                                HebergementRequestTableModel data = getTableView().getItems().get(getIndex());
                                System.out.println("selectedData: " + data.getId());
                                Alert alert =
                                        new Alert(Alert.AlertType.WARNING, "Vous êtes sure ?", ButtonType.YES, ButtonType.CANCEL);
                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.get() == ButtonType.YES) {
                                    ServiceHebergementRequest serviceHebergementRequest = new ServiceHebergementRequest();
                                    serviceHebergementRequest.delete(data.getId());
                                    handleRefreshHebergementRequestsButtonAction();
                                }
                            });
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                HBox buttonsContainer = new HBox();
                                buttonsContainer.getStyleClass().add("actionsButtonsContainer");
                                buttonsContainer.getChildren().add(deleteButton);
                                setGraphic(buttonsContainer);
                            }
                        }
                    };
                    return cell;
                }
            };

            colBtn.setCellFactory(cellFactory);

            hebergementRequestsTable.getColumns().add(colBtn);
        }
    }
}
