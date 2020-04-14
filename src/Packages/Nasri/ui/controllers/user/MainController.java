package Packages.Nasri.ui.controllers.user;

import Packages.Nasri.entities.HebergementOffer;
import Packages.Nasri.entities.HebergementRequest;
import Packages.Nasri.enums.CivilStatus;
import Packages.Nasri.enums.HebergementStatus;
import Packages.Nasri.services.ServiceHebergementOffer;
import Packages.Nasri.services.ServiceHebergementRequest;
import Packages.Nasri.ui.models.HebergementOfferTableModel;
import Packages.Nasri.ui.models.HebergementRequestTableModel;
import Packages.Nasri.ui.models.UserHebergementOfferTableModel;
import Packages.Nasri.ui.models.UserHebergementRequestTableModel;
import Packages.Nasri.utils.Helpers;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
            = new TableColumn<>("Description");
    @FXML
    private TableColumn<HebergementOfferTableModel, String> governoratOffersTableCol
            = new TableColumn<>("Governorat");
    @FXML
    private TableColumn<HebergementOfferTableModel, String> telephoneOffersTableCol
            = new TableColumn<>("Telephone");


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
    private TableColumn<HebergementRequestTableModel, String> telephoneRequestTableCol
            = new TableColumn<>("Telephone");

    private ObservableList<UserHebergementOfferTableModel> hebergementOffersObservables;

    private ObservableList<UserHebergementRequestTableModel> hebergementRequestObservables;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<UserHebergementOfferTableModel> hebergementOffers
                = UserHebergementOfferTableModel.get(new ServiceHebergementOffer().get());
        ArrayList<UserHebergementRequestTableModel> hebergementRequests
                = UserHebergementRequestTableModel.get(new ServiceHebergementRequest().get());

        hebergementOffersObservables = FXCollections.observableArrayList();

        hebergementRequestObservables = FXCollections.observableArrayList();

        // setting up the OffersTable columns
        userNameOffersTableCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        descriptionOffersTableCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        governoratOffersTableCol.setCellValueFactory(new PropertyValueFactory<>("governorat"));
        telephoneOffersTableCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        hebergementOffersTable.getColumns()
                .addAll(userNameOffersTableCol, descriptionOffersTableCol,
                        governoratOffersTableCol, telephoneOffersTableCol);

        // making the offersTable editable
        hebergementOffersTable.setEditable(true);
        descriptionOffersTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        governoratOffersTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        telephoneOffersTableCol.setCellFactory(TextFieldTableCell.forTableColumn());

        descriptionOffersTableCol.setOnEditCommit(
                descriptionCellEditEvent -> {
                    UserHebergementOfferTableModel model =
                            (UserHebergementOfferTableModel)hebergementOffersTable.getSelectionModel().getSelectedItem();
                    model.setDescription(descriptionCellEditEvent.getNewValue());
                    handleUpdateOffer(model);
                }
        );
        governoratOffersTableCol.setOnEditCommit(
                governoratCellEditEvent -> {
                    UserHebergementOfferTableModel model =
                            (UserHebergementOfferTableModel)hebergementOffersTable.getSelectionModel().getSelectedItem();
                    model.setGovernorat(governoratCellEditEvent.getNewValue());
                    handleUpdateOffer(model);
                }
        );
        telephoneOffersTableCol.setOnEditCommit(
                telephoneCellEditEvent -> {
                    UserHebergementOfferTableModel model =
                            (UserHebergementOfferTableModel)hebergementOffersTable.getSelectionModel().getSelectedItem();
                    model.setTelephone(telephoneCellEditEvent.getNewValue());
                    handleUpdateOffer(model);
                }
        );

        // setting up the RequestsTable Columns
        userNameRequestTableCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        demanderNameRequestTableCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionRequestTableCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        regionRequestTableCol.setCellValueFactory(new PropertyValueFactory<>("region"));
        telephoneRequestTableCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        hebergementRequestsTable.getColumns()
                .addAll(userNameRequestTableCol, demanderNameRequestTableCol, descriptionRequestTableCol,
                        regionRequestTableCol, telephoneRequestTableCol);

        //making the requetsTable editable
        hebergementRequestsTable.setEditable(true);
        demanderNameRequestTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionRequestTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        regionRequestTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        telephoneRequestTableCol.setCellFactory(TextFieldTableCell.forTableColumn());

        descriptionRequestTableCol.setOnEditCommit(
                descriptionCellEditEvent -> {
                    UserHebergementRequestTableModel model =
                            (UserHebergementRequestTableModel)hebergementRequestsTable.getSelectionModel().getSelectedItem();
                    model.setDescription(descriptionCellEditEvent.getNewValue());
                    handleUpdateRequest(model);
                }
        );
        demanderNameRequestTableCol.setOnEditCommit(
                demanderNameCellEditEvent -> {
                    UserHebergementRequestTableModel model =
                            (UserHebergementRequestTableModel)hebergementRequestsTable.getSelectionModel().getSelectedItem();
                    model.setName(demanderNameCellEditEvent.getNewValue());
                    handleUpdateRequest(model);
                }
        );
        regionRequestTableCol.setOnEditCommit(
                regionCellEditEvent -> {
                    UserHebergementRequestTableModel model =
                            (UserHebergementRequestTableModel)hebergementRequestsTable.getSelectionModel().getSelectedItem();
                    model.setRegion(regionCellEditEvent.getNewValue());
                    handleUpdateRequest(model);
                }
        );
        telephoneRequestTableCol.setOnEditCommit(
                telephoneCellEditEvent -> {
                    UserHebergementRequestTableModel model =
                            (UserHebergementRequestTableModel)hebergementRequestsTable.getSelectionModel().getSelectedItem();
                    model.setTelephone(telephoneCellEditEvent.getNewValue());
                    handleUpdateRequest(model);
                }
        );

        //2. add it to the tables
        loadOffersTable();
        loadRequestsTable();
    }




    @FXML
    protected void handleAddHebergementOfferButtonAction() throws IOException {
        URL url = getClass().getResource(URLScenes.refugeesAddHebergementOfferScene);
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        Stage addHebergementOfferStage = new Stage();
        addHebergementOfferStage.setScene(scene);
        addHebergementOfferStage.show();
    }

    @FXML
    protected void handleAddHebergementRequestButtonAction() throws IOException {
        URL url = getClass().getResource(URLScenes.refugeesAddHebergementRequestScene);
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        Stage addHebergementRequestStage = new Stage();
        addHebergementRequestStage.setScene(scene);
        addHebergementRequestStage.show();
    }

    @FXML
    protected void handleSearchHebergementOffersButtonAction() {

    }

    @FXML
    protected void handleSearchHebergementRequestsButtonAction() {

    }

    @FXML
    protected void handleSearchInputKeyReleasedRequestsAction() {
        if (searchInputHebergementRequest.getText().equals("")) {
            loadRequestsTable();
            return;
        }

        ArrayList<HebergementRequest> entities =
                new ServiceHebergementRequest().get(searchInputHebergementRequest.getText());
        ArrayList<UserHebergementRequestTableModel> models =
                UserHebergementRequestTableModel.get(entities);
        loadRequestsTable(models);
    }

    @FXML
    protected void handleSearchInputKeyReleasedOffersAction() {
        if (searchInputHebergementOffer.getText().equals("")) {
            loadOffersTable();
            return;
        }

        //2. query + get
        ArrayList<HebergementOffer> entities =
                new ServiceHebergementOffer().get(searchInputHebergementOffer.getText());
        ArrayList<UserHebergementOfferTableModel> models =
                UserHebergementOfferTableModel.get(entities);
        //3. display
        loadOffersTable(models);
    }



    @FXML
    protected void handleRefreshHebergementOffersButtonAction() {
        this.hebergementOffersTable.setItems(FXCollections.observableArrayList());
        this.loadOffersTable();
    }

    @FXML
    protected void handleRefreshHebergementRequestsButtonAction() {
        this.hebergementRequestsTable.setItems(FXCollections.observableArrayList());
        this.loadRequestsTable();
    }

    private void loadOffersTable() {
        ArrayList<UserHebergementOfferTableModel> hebergementOffers
                = UserHebergementOfferTableModel.get(new ServiceHebergementOffer().get());
        hebergementOffersTable.getItems().clear();
        hebergementOffersTable.getItems().addAll(hebergementOffers);
        addButtonToOffersTable();
    }

    private void loadRequestsTable() {
        ArrayList<UserHebergementRequestTableModel> hebergementRequests
                = UserHebergementRequestTableModel.get(new ServiceHebergementRequest().get());
        hebergementRequestsTable.getItems().clear();
        hebergementRequestsTable.getItems().addAll(hebergementRequests);
        addButtonToRequestTable();
    }

    private void loadOffersTable(ArrayList<UserHebergementOfferTableModel> models) {
        hebergementOffersTable.getItems().clear();
        hebergementOffersTable.getItems().addAll(models);
        addButtonToOffersTable();
    }

    private void loadRequestsTable(ArrayList<UserHebergementRequestTableModel> models) {
        hebergementRequestsTable.getItems().clear();
        hebergementRequestsTable.getItems().addAll(models);
        addButtonToRequestTable();
    }

    private void handleUpdateOffer(UserHebergementOfferTableModel model) {
        HebergementOffer hebergementOffer = new HebergementOffer(
                model.getId(),
                Helpers.TMP_USER_ID,
                model.getDescription(),
                model.getGovernorat(),
                Integer.parseInt(model.getNumberRooms()),
                Integer.parseInt(model.getDuration()),
                model.getState().equals("En cours") ? HebergementStatus.inProcess : HebergementStatus.done,
                model.getTelephone(),
                model.getImage()
        );
        new ServiceHebergementOffer().update(hebergementOffer);
    }

    private void handleUpdateRequest(UserHebergementRequestTableModel model) {
        HebergementRequest hebergementRequest = new HebergementRequest(
                model.getId(),
                Helpers.TMP_USER_ID,
                model.getDescription(),
                model.getRegion(),
                model.getState().equals("En cours") ? HebergementStatus.inProcess : HebergementStatus.done,
                model.getNativeCountry(),
                Helpers.convertStringToLocalDate(model.getArrivalDate(), "yyyy-MM-dd").atStartOfDay(),
                model.getPassportNumber(),
                model.getCivilState().equals("Marié(e)") ? CivilStatus.Married : CivilStatus.Single,
                Integer.parseInt(model.getChildrenNumber()),
                model.getName(),
                model.getTelephone(),
                model.isAnonymous()
        );

        new ServiceHebergementRequest().update(hebergementRequest);
    }

    // code from: https://riptutorial.com/javafx/example/27946/add-button-to-tableview
    private void addButtonToOffersTable() {
        // I can't fix why this method keeps adding everytime i hit refresh it keep adding a new column of Action buttons, even though i try to empty the table
        // this is a hacky fix -- test on the number of column that shoudln't be > 4
        if (hebergementOffersTable.getColumns().size() <= 4) {
            TableColumn<UserHebergementOfferTableModel, Void> colBtn = new TableColumn("Actions");

            Callback<TableColumn<UserHebergementOfferTableModel, Void>, TableCell<UserHebergementOfferTableModel, Void>> cellFactory
                    = new Callback<TableColumn<UserHebergementOfferTableModel, Void>, TableCell<UserHebergementOfferTableModel, Void>>() {
                @Override
                public TableCell<UserHebergementOfferTableModel, Void> call(final TableColumn<UserHebergementOfferTableModel, Void> param) {
                    final TableCell<UserHebergementOfferTableModel, Void> cell
                            = new TableCell<UserHebergementOfferTableModel, Void>() {

                        private final Button deleteButton = new Button("Supprimer");
                        private boolean sameUser = true;

                        {
                            deleteButton.getStyleClass().add("deleteButton");
                            deleteButton.setOnAction((ActionEvent event) -> {
                                UserHebergementOfferTableModel data = getTableView().getItems().get(getIndex());
                                sameUser = data.getUserId() == Helpers.TMP_USER_ID;
                                if (sameUser) {
                                    Alert alert =
                                            new Alert(Alert.AlertType.WARNING, "Vous êtes sure ?", ButtonType.YES, ButtonType.CANCEL);
                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.YES) {
                                        ServiceHebergementOffer serviceHebergementOffer = new ServiceHebergementOffer();
                                        serviceHebergementOffer.delete(data.getId());
                                        handleRefreshHebergementOffersButtonAction();
                                    }
                                }
                            });
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || !sameUser) {
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
        // this is a hacky fix -- test on the number of column that shoudln't be > 5
        if (hebergementRequestsTable.getColumns().size() <= 5) {
            TableColumn<UserHebergementRequestTableModel, Void> colBtn = new TableColumn("Actions");

            Callback<TableColumn<UserHebergementRequestTableModel, Void>, TableCell<UserHebergementRequestTableModel, Void>> cellFactory
                    = new Callback<TableColumn<UserHebergementRequestTableModel, Void>, TableCell<UserHebergementRequestTableModel, Void>>() {
                @Override
                public TableCell<UserHebergementRequestTableModel, Void> call(final TableColumn<UserHebergementRequestTableModel, Void> param) {
                    final TableCell<UserHebergementRequestTableModel, Void> cell = new TableCell<UserHebergementRequestTableModel, Void>() {
                        private final Button deleteButton = new Button("Supprimer");
                        private boolean sameUser = true;

                        {
                            deleteButton.getStyleClass().add("deleteButton");
                            deleteButton.setOnAction((ActionEvent event) -> {
                                UserHebergementRequestTableModel data = getTableView().getItems().get(getIndex());
                                sameUser = data.getUserId() == Helpers.TMP_USER_ID;
                                if (sameUser) {
                                    Alert alert =
                                            new Alert(Alert.AlertType.WARNING, "Vous êtes sure ?", ButtonType.YES, ButtonType.CANCEL);
                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.YES) {
                                        ServiceHebergementRequest serviceHebergementRequest = new ServiceHebergementRequest();
                                        serviceHebergementRequest.delete(data.getId());
                                        handleRefreshHebergementRequestsButtonAction();
                                    }
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
