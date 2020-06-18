package Packages.Issam.Controller;


import Main.Entities.UserSession;
import Packages.Issam.Models.Demande;
import Packages.Issam.Models.Don;
import Packages.Issam.Services.DemandeService;
import Packages.Issam.Services.DonService;
import SharedResources.URLScenes;
import SharedResources.URLServer;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSpinner;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

import javax.naming.Binding;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MainUserController implements Initializable {
    @FXML
    public StackPane rootStackPane;
    @FXML
    private TableView<Don> donTV;

    @FXML
    private TableView<Demande> demandeTV ;

    @FXML
    private TableColumn<Don , String> titleCol , descCol , addrCol ,phoneCol ,categoryCol,imageCol      ;

    @FXML
    private TableColumn<Don, Number> likeCol;

    @FXML
    private TableColumn<Demande, String> imageDemCol,titleDemCol,descDemCol,addrDemCol , phoneDemCol , ribDemCol;
    @FXML
    private TableColumn<Don, Number> idCol;
    @FXML
    private  TableColumn<Demande , Number> idDemCol ;

    @FXML
    private TextField inputDemTitle, inputDonTitle;

    @FXML
    private Button addDonButton ;

    @FXML
    private  Button addDemandeButton ;


//    final Image dislike = new Image(getClass().getResourceAsStream(("..\\..\\..\\SharedResources\\Images\\dislike.png")));
 //   final Image like = new Image(getClass().getResourceAsStream(("..\\..\\..\\SharedResources\\Images\\dislike.png")));
    private final JFXDialogLayout layout = new JFXDialogLayout();
    private final JFXButton close = new JFXButton("Fermer");

    public static JFXDialog getDialog() {
        return dialog;
    }

    public static JFXDialog dialog;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    // Don TableVIeW
        close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> dialog.close());
        donTV.itemsProperty().bind(Bindings.createObjectBinding(() ->
                DonService.getInstace().getDb().values().stream().filter(don ->{
                        if(inputDonTitle.getText().isEmpty()) return true;
                    return don.getTitle().toLowerCase().contains(inputDonTitle.getText().toLowerCase());
                        }


                ).collect(Collectors.toCollection(FXCollections::observableArrayList))
                ,DonService.getInstace().getDb(),inputDonTitle.textProperty(),inputDonTitle.focusedProperty()));


        this.dialog = new JFXDialog(rootStackPane,layout, JFXDialog.DialogTransition.CENTER);

        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        addrCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("domaineName"));
        imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));
        likeCol.setCellFactory(this::likeCall);

        idCol.setVisible(false);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descCol.setCellFactory(TextFieldTableCell.forTableColumn());
        addrCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneCol.setCellFactory(TextFieldTableCell.forTableColumn());
        categoryCol.setCellFactory(TextFieldTableCell.forTableColumn());
        imageCol.setCellFactory(this::photoCall);



     donTV.setRowFactory(this::rowFactoryCall);
     demandeTV.setRowFactory(this::rowDemFactoryCall);

       //Demande Table VIEW
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


        titleDemCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descDemCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        addrDemCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneDemCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        ribDemCol.setCellValueFactory(new PropertyValueFactory<>("rib"));
        imageDemCol.setCellValueFactory(new PropertyValueFactory<>("image"));



        idDemCol.setVisible(false);
        idDemCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleDemCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descDemCol.setCellFactory(TextFieldTableCell.forTableColumn());
        addrDemCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneDemCol.setCellFactory(TextFieldTableCell.forTableColumn());
        ribDemCol.setCellFactory(TextFieldTableCell.forTableColumn());
        imageDemCol.setCellFactory(this::imgDemCall);

















        addDonButton.setOnAction(e -> {
            try {
                //AnchorPane createMission = FXMLLoader.load(getClass().getResource("/Packages/Mohamed/Scenes/MissionCreate.fxml"));
                JFXDialogLayout layout = new JFXDialogLayout();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(URLScenes.donCreate));
                AnchorPane donCreate = loader.load();
                JFXDialog dialog = new JFXDialog(rootStackPane, layout, JFXDialog.DialogTransition.CENTER);
                JFXButton close = new JFXButton("Cancel");
                layout.setBody(donCreate);
                layout.setHeading(new Text("Ajout d'une nouvelle mission"));

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



        addDemandeButton.setOnAction(e -> {
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
        inputDemTitle.setOnKeyReleased(e -> {
            inputDemTitle.textProperty().addListener((observableValue, s, t1) -> {
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



    }




    private TableCell<Demande, String> imgDemCall(TableColumn<Demande, String> observableAssociationValueUserTableColumn) {
        return new TableCell<>() {
            private final ImageView imageView = new ImageView();
            private Image userImage;

            @Override
            protected void updateItem(String demande, boolean empty) {
                super.updateItem(demande, empty);

                System.out.println(demande);
                if (demande == null) {
                    setText("");
                    setGraphic(null);
                } else {

                        try {
                            userImage = new Image(Objects.requireNonNull(FTPInterface.getInstance().downloadFile(URLServer.donImageDir + demande, FTP.BINARY_FILE_TYPE)).toURI().toString());
                        } catch (IOException e) {
                            setGraphic(null);
                        }
                    imageView.setImage(userImage);
                    imageView.setFitHeight(100.0);
                    imageView.setFitWidth(150.0);
                    setGraphic(imageView);
                }
            }
        };


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
        };


    }
    private TableCell<Don, Number> likeCall(TableColumn<Don, Number> missionNumberTableColumn)  {
        return new TableCell<>() {
            JFXButton btnUps = new JFXButton();
            @Override
            protected void updateItem(Number item, boolean empty) {


                super.updateItem(item, empty);


                if (item == null && empty) {
                    setText("");
                    setGraphic(null);
                } else {
                    try {
                       // IconView.setImage(dislike);
                        //setting icon to button
                        // System.out.println("??"+getTableRow().getItem());;
                        if(DonService.getInstace().testLike(getTableRow().getItem())) {
                            btnUps.setStyle("-fx-background-color: red");
                        }else{
                            btnUps.setStyle("-fx-background-color: green");
                        }
                        int nbrLike=DonService.getInstace().likeCount(getTableRow().getItem());

                        btnUps.setText(nbrLike+" likes");

                       btnUps.setText(DonService.getInstace().likeCount(getTableRow().getItem())+" Like");

                        btnUps.setOnMouseClicked(
                                event -> {

                                    try {
                                        //System.out.println(getTableRow().getItem());
                                        boolean rep=DonService.getInstace().addLike(getTableRow().getItem());
                                        if (rep){
                                            int nbrLikes=DonService.getInstace().likeCount(getTableRow().getItem());

                                            btnUps.setText(nbrLikes+" likes");
                                            btnUps.setStyle("-fx-background-color: red");

                                        }else{
                                            int nbrLikse=DonService.getInstace().likeCount(getTableRow().getItem());

                                            btnUps.setText(nbrLikse+" likes");
                                            btnUps.setStyle("-fx-background-color: green");

                                         //   btnUps.setGraphic(IconView);

                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                        );
                        setGraphic(btnUps);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }




    void showDialog(Alert.AlertType t, String title, String header, String context) {
        Alert a = new Alert(t);
        a.setTitle(title);
        a.setHeaderText(header);
        a.setContentText(context);
        a.showAndWait();
    }


    private JFXDialog dialog(Node body, String heading){
        layout.setActions(close);
        layout.setBody(body);
        layout.setHeading(new Text(heading));
        return dialog;
    }

    private TableRow<Don> rowFactoryCall(TableView<Don> row) {
        return new TableRow<>() {
            boolean isViewing;
            @Override
            public void updateItem(Don don, boolean empty) {
                super.updateItem(don, empty);
                if(don==null) {
                    setGraphic(null);
                    setText("");
                } else {
                    setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && (!isEmpty()))
                             if (UserSession.getInstace().getUser().getId()==don.getUserId()){

                                    dialog(new DonUpdateController(don),"update").show();
                            }else {
                            dialog(new DonProfileShowController(don), "Profile").show();
                        }
                    });
                }
            }
        };
    }








    private TableRow<Demande> rowDemFactoryCall(TableView<Demande> row) {
        return new TableRow<>() {
            boolean isViewing;
            @Override
            public void updateItem(Demande demande, boolean empty) {
                super.updateItem(demande, empty);
                if(demande==null) {
                    setGraphic(null);
                    setText("");
                } else {
                    setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && (!isEmpty()))
                            dialog(new DemandeProfileShow(demande), "Profile demande").show();


                            //if (UserSession.getInstace().getUser().getId()==demande.getUser()){

                              //  dialog(new DonUpdateController(demande),"update").show();
                            //}else {

                    });
                }
            }
        };
    }

}
