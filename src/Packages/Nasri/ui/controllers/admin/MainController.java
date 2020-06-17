/*
package Packages.Nasri.ui.controllers.admin;

import Packages.Nasri.entities.HebergementOffer;
import Packages.Nasri.entities.HebergementRequest;
import Packages.Nasri.enums.CivilStatus;
import Packages.Nasri.enums.HebergementStatus;
import Packages.Nasri.services.ServiceHebergementOffer;
import Packages.Nasri.services.ServiceHebergementRequest;
import Packages.Nasri.ui.models.HebergementOfferTableModel;
import Packages.Nasri.ui.models.HebergementRequestTableModel;
import Packages.Nasri.utils.Helpers;
import SharedResources.URLScenes;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
import org.apache.commons.net.ntp.TimeStamp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    // Offers table collumns
    @FXML
    private final TableColumn<HebergementOfferTableModel, String> userNameOffersTableCol
            = new TableColumn<>("Nom d'utilisateur");
    @FXML
    private final TableColumn<HebergementOfferTableModel, String> descriptionOffersTableCol
            = new TableColumn<>("Description");
    @FXML
    private final TableColumn<HebergementOfferTableModel, String> governoratOffersTableCol
            = new TableColumn<>("Governorat");
    @FXML
    private final TableColumn<HebergementOfferTableModel, Integer> numberRoomsOffersTableCol
            = new TableColumn<>("Nombre de chambres");
    @FXML
    private final TableColumn<HebergementOfferTableModel, Integer> durationOffersTableCol
            = new TableColumn<>("Durée (mois)");
    @FXML
    private final TableColumn<HebergementOfferTableModel, String> stateOffersTableCol
            = new TableColumn<>("Etat");
    @FXML
    private final TableColumn<HebergementOfferTableModel, String> telephoneOffersTableCol
            = new TableColumn<>("Telephone");
    @FXML
    private final TableColumn<HebergementOfferTableModel, String> imageOffersTableCol
            = new TableColumn<>("Image");
    // Requests Table columns
    @FXML
    private final TableColumn<HebergementRequestTableModel, String> userNameRequestTableCol
            = new TableColumn<>("Nom d'utilisateur");
    @FXML
    private final TableColumn<HebergementRequestTableModel, String> demanderNameRequestTableCol
            = new TableColumn<>("Nom du demandeur");
    @FXML
    private final TableColumn<HebergementRequestTableModel, String> descriptionRequestTableCol
            = new TableColumn<>("Description");
    @FXML
    private final TableColumn<HebergementRequestTableModel, String> regionRequestTableCol
            = new TableColumn<>("Region");
    @FXML
    private final TableColumn<HebergementRequestTableModel, String> nativeCountryRequestTableCol
            = new TableColumn<>("Pays d'origine");
    @FXML
    private final TableColumn<HebergementRequestTableModel, String> arrivalDateRequestTableCol
            = new TableColumn<>("Date d'arrivée");
    @FXML
    private final TableColumn<HebergementRequestTableModel, String> stateRequestTableCol
            = new TableColumn<>("Etat");
    @FXML
    private final TableColumn<HebergementRequestTableModel, String> passportNumberRequestTableCol
            = new TableColumn<>("Numero de passport");
    @FXML
    private final TableColumn<HebergementRequestTableModel, String> civilStateRequestTableCol
            = new TableColumn<>("Etat civil");
    @FXML
    private final TableColumn<HebergementRequestTableModel, String> telephoneRequestTableCol
            = new TableColumn<>("Telephone");
    @FXML
    private TextField searchInputHebergementOffer;
    @FXML
    private TextField searchInputHebergementRequest;
    @FXML
    private TableView hebergementOffersTable;
    @FXML
    private TableView hebergementRequestsTable;
    private ObservableList<HebergementOfferTableModel> hebergementOffersObservables;

    private ObservableList<HebergementRequestTableModel> hebergementRequestObservables;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //1. get data for hebergementRequests and hebergementOffers
        try {
            ArrayList<HebergementOfferTableModel> hebergementOffers
                    = HebergementOfferTableModel.get(new ServiceHebergementOffer().get());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
                    super.updateItem(filePath, empty);
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

        // making the offersTable editable
        hebergementOffersTable.setEditable(true);
        descriptionOffersTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        governoratOffersTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        numberRoomsOffersTableCol.setCellFactory(column -> {
            TableCell<HebergementOfferTableModel, Integer> cell = new TableCell<HebergementOfferTableModel, Integer>() {
                private final TextField numberRoomsTextField = new TextField();

                @Override
                protected void updateItem(Integer numberRooms, boolean empty) {
                    super.updateItem(numberRooms, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        numberRoomsTextField.setText(numberRooms.toString());
                        setGraphic(numberRoomsTextField);
                    }
                }
            };

            return cell;
        });
        durationOffersTableCol.setCellFactory(column -> {
            TableCell<HebergementOfferTableModel, Integer> cell = new TableCell<HebergementOfferTableModel, Integer>() {
                private final TextField durationInputText = new TextField();

                @Override
                protected void updateItem(Integer duration, boolean empty) {
                    super.updateItem(duration, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        durationInputText.setText(duration.toString());
                        setGraphic(durationInputText);
                    }
                }
            };

            return cell;
        });
        stateOffersTableCol.setCellFactory(column -> {
            TableCell<HebergementOfferTableModel, String> cell = new TableCell<HebergementOfferTableModel, String>() {
                private final ComboBox stateComboBox = new ComboBox();

                @Override
                protected void updateItem(String state, boolean empty) {
                    super.updateItem(state, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        stateComboBox.getItems().add("En cours");
                        stateComboBox.getItems().add("Terminé");
                        int index = state.equals("En cours") ? 0 : 1;
                        stateComboBox.getSelectionModel().select(index);
                        setGraphic(stateComboBox);
                    }
                }
            };

            return cell;
        });
        telephoneOffersTableCol.setCellFactory(TextFieldTableCell.forTableColumn());

        descriptionOffersTableCol.setOnEditCommit(
                descriptionCellEditEvent -> {
                    HebergementOfferTableModel model =
                            (HebergementOfferTableModel) hebergementOffersTable.getSelectionModel().getSelectedItem();
                    model.setDescription(descriptionCellEditEvent.getNewValue());
                    handleUpdateOffer(model);
                }
        );
        governoratOffersTableCol.setOnEditCommit(
                governoratCellEditEvent -> {
                    HebergementOfferTableModel model =
                            (HebergementOfferTableModel) hebergementOffersTable.getSelectionModel().getSelectedItem();
                    model.setGovernorat(governoratCellEditEvent.getNewValue());
                    handleUpdateOffer(model);
                }
        );
        numberRoomsOffersTableCol.setOnEditCommit(
                numberRoomsCellEditEvent -> {
                    HebergementOfferTableModel model =
                            (HebergementOfferTableModel) hebergementOffersTable.getSelectionModel().getSelectedItem();
                    model.setNumberRooms(numberRoomsCellEditEvent.getNewValue());
                    handleUpdateOffer(model);
                }
        );
        durationOffersTableCol.setOnEditCommit(
                durationCellEditEvent -> {
                    HebergementOfferTableModel model =
                            (HebergementOfferTableModel) hebergementOffersTable.getSelectionModel().getSelectedItem();
                    model.setDuration(durationCellEditEvent.getNewValue());
                    handleUpdateOffer(model);
                }
        );
        stateOffersTableCol.setOnEditCommit(
                stateCellEditEvent -> {
                    HebergementOfferTableModel model =
                            (HebergementOfferTableModel) hebergementOffersTable.getSelectionModel().getSelectedItem();
                    model.setState(stateCellEditEvent.getNewValue());
                    handleUpdateOffer(model);
                }
        );
        telephoneOffersTableCol.setOnEditCommit(
                telephoneCellEditEvent -> {
                    HebergementOfferTableModel model =
                            (HebergementOfferTableModel) hebergementOffersTable.getSelectionModel().getSelectedItem();
                    model.setTelephone(telephoneCellEditEvent.getNewValue());
                    handleUpdateOffer(model);
                }
        );


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

        //making the requetsTable editable
        hebergementRequestsTable.setEditable(true);
        demanderNameRequestTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionRequestTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        regionRequestTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nativeCountryRequestTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        arrivalDateRequestTableCol.setCellFactory(column -> {
            TableCell<HebergementRequestTableModel, String> cell = new TableCell<HebergementRequestTableModel, String>() {
                private DatePicker datePicker;

                @Override
                protected void updateItem(String date, boolean empty) {
                    super.updateItem(date, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        LocalDate localDate = Helpers.convertStringToLocalDate(date, "yyyy-MM-dd");
                        datePicker = new DatePicker(localDate);
                        setGraphic(datePicker);
                    }
                }
            };

            return cell;
        });
        stateRequestTableCol.setCellFactory(column -> {
            TableCell<HebergementRequestTableModel, String> cell = new TableCell<HebergementRequestTableModel, String>() {
                private final ComboBox stateComboBox = new ComboBox();

                @Override
                protected void updateItem(String state, boolean empty) {
                    super.updateItem(state, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        stateComboBox.getItems().add("En cours");
                        stateComboBox.getItems().add("Terminé");
                        int index = state.equals("En cours") ? 0 : 1;
                        stateComboBox.getSelectionModel().select(index);
                        setGraphic(stateComboBox);
                    }
                }
            };

            return cell;
        });
        passportNumberRequestTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        telephoneRequestTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        civilStateRequestTableCol.setCellFactory(column -> {
            TableCell<HebergementRequestTableModel, String> cell = new TableCell<HebergementRequestTableModel, String>() {
                private final ComboBox civilStatusComboBox = new ComboBox();

                @Override
                protected void updateItem(String state, boolean empty) {
                    super.updateItem(state, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        civilStatusComboBox.getItems().add("Marié(e)");
                        civilStatusComboBox.getItems().add("Celibataire");
                        int index = state.equals("Marié(e)") ? 0 : 1;
                        civilStatusComboBox.getSelectionModel().select(index);
                        setGraphic(civilStatusComboBox);
                    }
                }
            };

            return cell;
        });


        descriptionRequestTableCol.setOnEditCommit(
                descriptionCellEditEvent -> {
                    HebergementRequestTableModel model =
                            (HebergementRequestTableModel) hebergementRequestsTable.getSelectionModel().getSelectedItem();
                    model.setDescription(descriptionCellEditEvent.getNewValue());
                    handleUpdateRequest(model);
                }
        );
        demanderNameRequestTableCol.setOnEditCommit(
                demanderNameCellEditEvent -> {
                    HebergementRequestTableModel model =
                            (HebergementRequestTableModel) hebergementRequestsTable.getSelectionModel().getSelectedItem();
                    model.setName(demanderNameCellEditEvent.getNewValue());
                    handleUpdateRequest(model);
                }
        );
        regionRequestTableCol.setOnEditCommit(
                regionCellEditEvent -> {
                    HebergementRequestTableModel model =
                            (HebergementRequestTableModel) hebergementRequestsTable.getSelectionModel().getSelectedItem();
                    model.setRegion(regionCellEditEvent.getNewValue());
                    handleUpdateRequest(model);
                }
        );
        nativeCountryRequestTableCol.setOnEditCommit(
                nativeCellEditEvent -> {
                    HebergementRequestTableModel model =
                            (HebergementRequestTableModel) hebergementRequestsTable.getSelectionModel().getSelectedItem();
                    model.setNativeCountry(nativeCellEditEvent.getNewValue());
                    handleUpdateRequest(model);
                }
        );
        arrivalDateRequestTableCol.setOnEditCommit(
                arrivalDateCellEditEvent -> {
                    HebergementRequestTableModel model =
                            (HebergementRequestTableModel) hebergementRequestsTable.getSelectionModel().getSelectedItem();
                    model.setArrivalDate(arrivalDateCellEditEvent.getNewValue());
                    handleUpdateRequest(model);
                }
        );
        stateRequestTableCol.setOnEditCommit(
                stateCellEditEvent -> {
                    HebergementRequestTableModel model =
                            (HebergementRequestTableModel) hebergementRequestsTable.getSelectionModel().getSelectedItem();
                    model.setState(stateCellEditEvent.getNewValue());
                    handleUpdateRequest(model);
                }
        );
        passportNumberRequestTableCol.setOnEditCommit(
                passportNumberCellEditEvent -> {
                    HebergementRequestTableModel model =
                            (HebergementRequestTableModel) hebergementRequestsTable.getSelectionModel().getSelectedItem();
                    model.setPassportNumber(passportNumberCellEditEvent.getNewValue());
                    handleUpdateRequest(model);
                }
        );
        telephoneRequestTableCol.setOnEditCommit(
                telephoneCellEditEvent -> {
                    HebergementRequestTableModel model =
                            (HebergementRequestTableModel) hebergementRequestsTable.getSelectionModel().getSelectedItem();
                    model.setTelephone(telephoneCellEditEvent.getNewValue());
                    handleUpdateRequest(model);
                }
        );
        civilStateRequestTableCol.setOnEditCommit(
                civilStateCellEditEvent -> {
                    HebergementRequestTableModel model =
                            (HebergementRequestTableModel) hebergementRequestsTable.getSelectionModel().getSelectedItem();
                    model.setCivilState(civilStateCellEditEvent.getNewValue());
                    handleUpdateRequest(model);
                }
        );


        //2. add it to the tables
        loadOffersTable();
        loadRequestsTable();
    }

    private void handleUpdateOffer(HebergementOfferTableModel model) {
        HebergementOffer hebergementOffer = new HebergementOffer(
                model.getId(),
                Helpers.TMP_USER_ID,
                model.getDescription(),
                model.getGovernorat(),
                model.getNumberRooms(),
                model.getDuration(),
                model.getState().equals("En cours") ? HebergementStatus.inProcess : HebergementStatus.done,
                model.getTelephone(), model.getImage()
        );
        try {
            new ServiceHebergementOffer().update(hebergementOffer);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void handleUpdateRequest(HebergementRequestTableModel model) {
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
                model.getChildrenNumber(),
                model.getName(),
                model.getTelephone(),
                model.isAnonymous()
        );

        new ServiceHebergementRequest().update(hebergementRequest);
    }

    private void loadRequestsTable() {
        ArrayList<HebergementRequestTableModel> hebergementRequests
                = HebergementRequestTableModel.get(new ServiceHebergementRequest().get());
        hebergementRequestsTable.getItems().clear();
        hebergementRequestsTable.getItems().addAll(hebergementRequests);
        addButtonToRequestTable();
    }

    private void loadOffersTable() {
        ArrayList<HebergementOfferTableModel> hebergementOffers
                = null;
        try {
            hebergementOffers = HebergementOfferTableModel.get(new ServiceHebergementOffer().get());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        hebergementOffersTable.getItems().clear();
        hebergementOffersTable.getItems().addAll(hebergementOffers);
        addButtonToOffersTable();
    }

    private void loadOffersTable(ArrayList<HebergementOfferTableModel> models) {
        hebergementOffersTable.getItems().clear();
        hebergementOffersTable.getItems().addAll(models);
        addButtonToOffersTable();
    }

    private void loadRequestsTable(ArrayList<HebergementRequestTableModel> models) {
        hebergementRequestsTable.getItems().clear();
        hebergementRequestsTable.getItems().addAll(models);
        addButtonToRequestTable();
    }

    @FXML
    protected void handleSearchHebergementOffersButtonAction() {
        System.out.println("handleSearchHebergementOffersButtonAction clicked");
    }

    @FXML
    protected void handleAddHebergementOfferButtonAction() throws IOException {
        URL url = getClass().getResource(URLScenes.refugeesAddHebergementOfferScene);
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
        URL url = getClass().getResource(URLScenes.refugeesAddHebergementRequestScene);
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

    @FXML
    protected void exportOffersToPDF() {
        System.out.println("exportOffersToPDF");
        try {
            Document document = new Document();
            String FILE = "c:/temp/admin/" + "Export-Offers-" + TimeStamp.getCurrentTime().toString() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            document.addTitle("Export-Offers-" + TimeStamp.getCurrentTime().toString());
            //document content
            PdfPTable table = new PdfPTable(7);
            ArrayList<HebergementOffer> offers = new ServiceHebergementOffer().get();
            ArrayList<HebergementOfferTableModel> data = HebergementOfferTableModel.get(offers);
            //adding headers
            table.addCell("Nom d'utilisateur");
            table.addCell("Description");
            table.addCell("Governorat");
            table.addCell("Nombre de chambres");
            table.addCell("Durée(mois)");
            table.addCell("Etat");
            table.addCell("Telephone");
            table.completeRow();
            for (HebergementOfferTableModel offer : data) {
                table.addCell(offer.getUserName());
                table.addCell(offer.getDescription());
                table.addCell(offer.getGovernorat());
                table.addCell(Integer.toString(offer.getNumberRooms()));
                table.addCell(Integer.toString(offer.getDuration()));
                table.addCell(offer.getState());
                table.addCell(offer.getTelephone());
                table.completeRow();
            }
            document.add(table);
            document.close();
        } catch (IOException | DocumentException | SQLException e) {
            System.out.println("xyz");
        }
    }

    @FXML
    protected void exportRequestsToPDF() {
        System.out.println("exportOffersToPDF");
        try {
            Document document = new Document();
            String FILE = "c:/temp/admin/" + "Export-Requests-" + TimeStamp.getCurrentTime().toString() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            document.addTitle("Export-Offers-" + TimeStamp.getCurrentTime().toString());
            //document content
            PdfPTable table = new PdfPTable(10);
            ArrayList<HebergementRequest> offers = new ServiceHebergementRequest().get();
            ArrayList<HebergementRequestTableModel> data = HebergementRequestTableModel.get(offers);
            //adding headers
            table.addCell("Nom d'utilisateur");
            table.addCell("Description");
            table.addCell("Region");
            table.addCell("Pays d'origine");
            table.addCell("Date d'arrivée");
            table.addCell("Etat");
            table.addCell("Passport");
            table.addCell("Telephone");
            table.addCell("Etat Civil");
            table.addCell("Nombre d'enfants");
            table.completeRow();
            for (HebergementRequestTableModel request : data) {
                table.addCell(request.getUserName());
                table.addCell(request.getDescription());
                table.addCell(request.getRegion());
                table.addCell(request.getNativeCountry());
                table.addCell(request.getArrivalDate());
                table.addCell(request.getState());
                table.addCell(request.getPassportNumber());
                table.addCell(request.getTelephone());
                table.addCell(request.getCivilState());
                table.addCell(Integer.toString(request.getChildrenNumber()));
                table.completeRow();
            }
            document.add(table);
            document.close();
        } catch (IOException | DocumentException e) {
            System.out.println("xyz");
        }
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
                                    ServiceHebergementOffer serviceHebergementOffer = null;
                                    try {
                                        serviceHebergementOffer = new ServiceHebergementOffer();
                                    } catch (SQLException throwables) {
                                        throwables.printStackTrace();
                                    }
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

    @FXML
    protected void handleSearchInputKeyReleasedOffersAction() {

        if (searchInputHebergementOffer.getText().equals("")) {
            loadOffersTable();
            return;
        }

        //2. query + get
        ArrayList<HebergementOffer> entities =
                null;
        try {
            entities = new ServiceHebergementOffer().get(searchInputHebergementOffer.getText());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ArrayList<HebergementOfferTableModel> models =
                HebergementOfferTableModel.get(entities);
        //3. display
        loadOffersTable(models);
    }

    @FXML
    protected void handleSearchInputKeyReleasedRequestsAction() {
        if (searchInputHebergementRequest.getText().equals("")) {
            loadRequestsTable();
            return;
        }

        ArrayList<HebergementRequest> entities =
                new ServiceHebergementRequest().get(searchInputHebergementRequest.getText());
        ArrayList<HebergementRequestTableModel> models =
                HebergementRequestTableModel.get(entities);
        loadRequestsTable(models);
    }
}
*/
