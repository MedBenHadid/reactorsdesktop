package Packages.Chihab.Controllers;

import Main.Entities.User;
import Main.Entities.UserSession;
import Packages.Chihab.Models.Entities.Association;
import Packages.Chihab.Models.Entities.Category;
import Packages.Chihab.Services.AssociationService;
import SharedResources.URLScenes;
import SharedResources.URLServer;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.apache.commons.net.ftp.FTP;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AssociationsBackofficeController extends StackPane implements Initializable {
    @FXML
    private StackPane rootStackPane;
    @FXML
    private TableView<Association> associationTableView;
    @FXML
    private JFXProgressBar fetchProgress;
    @FXML
    private TableColumn<Association, String> villeCol;
    @FXML
    private TableColumn<Association, String> profilePictureCol;
    @FXML
    private TableColumn<Association, String> nameCol;
    @FXML
    private TableColumn<Association, Category> domaineCol;
    @FXML
    private TableColumn<Association, User> managerCol;
    @FXML
    private JFXComboBox<String> vCombo;
    @FXML
    private JFXComboBox<Category> dCombo;
    @FXML
    private JFXTextField inputName;
    @FXML
    private JFXButton addButton, resetButton, espaceMembershipButton;
    @FXML
    private Label size, hits;
    @FXML private JFXSpinner spinner;

    private final JFXDialogLayout layout = new JFXDialogLayout();
    private final AssociationCreateController createPanel = new AssociationCreateController();
    private final User loggedIn = UserSession.getInstace().getUser();
    private final JFXButton close = new JFXButton("Fermer");
    private final FXMLLoader thisLoader = new FXMLLoader( getClass().getResource(URLScenes.associationSuperAdminDashboard) );
    private final JFXDialog dialog;
    public AssociationsBackofficeController(){
        thisLoader.setRoot( this );
        thisLoader.setController( this );
        try {
            thisLoader.load();
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
        this.dialog = new JFXDialog(rootStackPane,layout, JFXDialog.DialogTransition.CENTER);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), ev -> {
            Platform.runLater(()-> AssociationService.getInstance().updateCheckRunnable());
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        associationTableView.itemsProperty().bind(Bindings.createObjectBinding(() -> AssociationService.getInstance().getRecords().values().stream().filter(association -> {
            boolean test = true;
            if(vCombo.getSelectionModel().getSelectedIndex() != -1){
                test = association.getVille().equals(vCombo.getSelectionModel().getSelectedItem());
            }
            if(dCombo.getSelectionModel().getSelectedIndex() != -1){
                test = test && association.getDomaine().equals(dCombo.getSelectionModel().getSelectedItem());
            }
            if( inputName.isFocused() ){
                test = test && association.getNom().contains(inputName.getText());
            }
            return test;
        }).collect(Collectors.toCollection(FXCollections::observableArrayList)),AssociationService.getInstance().getRecords(),inputName.textProperty(),inputName.focusedProperty(),vCombo.armedProperty(),dCombo.armedProperty()));
        AssociationService.getInstance().getRecords().addListener((MapChangeListener<? super Integer, ? super Association>) change -> {
            System.out.println("detected change");
        });
        domaineCol.setCellValueFactory(data -> data.getValue().domaineProperty());
        managerCol.setCellValueFactory(data -> data.getValue().managerProperty());
        villeCol.setCellValueFactory(data -> data.getValue().villeProperty());
        profilePictureCol.setCellValueFactory(data -> data.getValue().photoAgenceProperty());
        nameCol.setCellValueFactory(data -> data.getValue().nomProperty());
        associationTableView.setRowFactory(this::rowFactoryCall);
        profilePictureCol.setCellFactory(this::profilePictureCall);
        domaineCol.setCellFactory(this::domaineCall);
        managerCol.setCellFactory(this::managerCall);
        nameCol.setCellFactory(this::nomCol);
        villeCol.setCellFactory(this::villeCall);
        size.textProperty().bind(Bindings.createStringBinding(() -> AssociationService.getInstance().getRecords().size() +" associations inscrits",AssociationService.getInstance().getRecords()));
        hits.textProperty().bind(Bindings.createStringBinding(() -> associationTableView.getItems().size() +" search hits",associationTableView.itemsProperty()));
        vCombo.itemsProperty().bind(Bindings.createObjectBinding(() -> associationTableView.getItems().stream().map(Association::getVille).distinct().collect(Collectors.toCollection(FXCollections::observableArrayList)), associationTableView.itemsProperty()));
        dCombo.itemsProperty().bind(Bindings.createObjectBinding(() -> associationTableView.getItems().stream().map(Association::getDomaine).distinct().collect(Collectors.toCollection(FXCollections::observableArrayList)), associationTableView.itemsProperty()));
        addButton.setOnMouseClicked(e->{ dialog(createPanel,"Create a new association :").show(); });
        AutoCompletionBinding<String> stringAutoCompletionBinding = TextFields.bindAutoCompletion(inputName, (Collection<String>) associationTableView.getItems().stream().map(Association::getNom).distinct().collect(Collectors.toCollection(FXCollections::observableArrayList)));
        spinner.visibleProperty().bind(stringAutoCompletionBinding.getCompletionTarget().focusedProperty());
        stringAutoCompletionBinding.setVisibleRowCount(6);
        resetButton.setOnMouseClicked(event -> {
            vCombo.getSelectionModel().select(-1);
            dCombo.getSelectionModel().select(-1);
            inputName.deleteText(0,inputName.getText().length());
        });
    }


    private TableCell<Association, String> villeCall(TableColumn<Association, String> associationStringTableColumn) {
        return new TableCell<>(){
            private final WebView wv = new WebView();
            private final WebEngine webengine = wv.getEngine();
            private boolean isLoaded = false;
            @Override
            protected void updateItem(String ville, boolean empty) {
                super.updateItem(ville, empty);
                if (ville == null) {
                    setGraphic(null);
                    setText("");
                } else {
                    wv.getEngine().load(this.getClass().getResource("/Packages/Chihab/Scenes/WebView/domaineAPI.html").toString());
                    webengine.getLoadWorker().stateProperty().addListener(
                            (ChangeListener<? super Worker.State>) (observable, oldValue, newValue) -> {
                                if (newValue == Worker.State.SUCCEEDED) {
                                    if (!isLoaded) {
                                        observable.addListener((observableValue, state, t1) -> {
                                            if (t1 == Worker.State.SUCCEEDED) {
                                               // wv.getEngine().executeScript("initCarousel('" + ville + "')");
                                                isLoaded = true;
                                            }
                                        });
                                    }
                                }
                            }
                    );
                    wv.setPrefSize(200.0, 200.0);
                    setGraphic(wv);
                    setText(ville);
                }
            }
        };
    }

    private TableCell<Association, User> managerCall(TableColumn<Association, User> observableAssociationValueUserTableColumn) {
        return new TableCell<>() {
            private final ImageView imageView = new ImageView();
            private Image userImage;

            @Override
            protected void updateItem(User a, boolean empty) {
                super.updateItem(a, empty);
                if (a == null) {
                    setText("");
                    setGraphic(null);
                } else {
                    if(a.getProfile().getImage()!=null)
                    if(userImage==null)
                    try {
                        userImage = new Image(Objects.requireNonNull(FTPInterface.getInstance().downloadFile(URLServer.userImageDir + a.getProfile().getImage(), FTP.BINARY_FILE_TYPE)).toURI().toString());
                    } catch (IOException e) {
                        setGraphic(null);
                    }
                    imageView.setImage(userImage);
                    imageView.setFitHeight(200.0);
                    imageView.setFitWidth(200.0);
                    setText(a.getUsername());
                    setGraphic(imageView);
                }
            }
        };
    }
    private TableCell<Association, String> nomCol(TableColumn<Association, String> associationStringTableColumn) {
        return new TableCell<>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(item==null){
                    setGraphic(null);
                    setText(null);
                }else{
                    setText(item);
                    setStyle( "-fx-alignment: CENTER-RIGHT;");
                }
            }
        };
    }

    private TableCell<Association, String> profilePictureCall(TableColumn<Association, String> param) {
        return new TableCell<>() {
            private final ImageView imageView = new ImageView();
            private Image profileImage;
            private String lastKnownPath = "";
            @Override
            protected void updateItem(String profilePicture, boolean empty) {
                super.updateItem(profilePicture, empty);
                if (profilePicture==null) {
                    setGraphic(null);
                    setText("");
                } else {
                    if(!lastKnownPath.equals(profilePicture)){
                        try {
                            lastKnownPath=profilePicture;
                            profileImage=new Image(Objects.requireNonNull(FTPInterface.getInstance().downloadFile(URLServer.associationImageDir + profilePicture, FTP.BINARY_FILE_TYPE)).toURI().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if(profileImage==null){
                        lastKnownPath=profilePicture;
                        try {
                            profileImage=new Image(Objects.requireNonNull(FTPInterface.getInstance().downloadFile(URLServer.associationImageDir + profilePicture, FTP.BINARY_FILE_TYPE)).toURI().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    imageView.setImage(profileImage);
                    imageView.setFitHeight(200.0);
                    imageView.setFitWidth(200.0);
                    setGraphic(imageView);
                }
            }
        };
    }

    private TableCell<Association, Category> domaineCall(TableColumn<Association, Category> param) {
        return new TableCell<>() {
            private final WebView wv = new WebView();
            private final WebEngine webengine = wv.getEngine();
            private boolean isLoaded = false;
            @Override
            protected void updateItem(Category a, boolean empty) {
                super.updateItem(a, empty);
                if (a == null) {
                    setGraphic(null);
                    setText("");
                } else {
                    wv.getEngine().load(this.getClass().getResource("/Packages/Chihab/Scenes/WebView/domaineAPI.html").toString());
                    webengine.getLoadWorker().stateProperty().addListener(
                            (ChangeListener<? super Worker.State>) (observable, oldValue, newValue) -> {
                                if (newValue == Worker.State.SUCCEEDED) {
                                    if (!isLoaded) {
                                        observable.addListener((observableValue, state, t1) -> {
                                            if (t1 == Worker.State.SUCCEEDED) {
                                                //wv.getEngine().executeScript("initCarousel('" + a.getNom() + "')");
                                                isLoaded = true;
                                            }
                                        });
                                    }
                                }
                            }
                    );
                    wv.setPrefSize(200.0, 200.0);
                    setText(a.getNom());
                    setGraphic(wv);
                }
            }
        };
    }

    private TableRow<Association> rowFactoryCall(TableView<Association> row) {
        return new TableRow<>() {
            final JFXSpinner sp = spinner;
            boolean isViewing;
            @Override
            public void updateItem(Association a, boolean empty) {
                super.updateItem(a, empty);
                if(a==null) {
                    setGraphic(sp);
                    setText("");
                } else {
                   // a.addListener((observableValue, association, t1) -> );
                    setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && (!isEmpty())) {
                            if (loggedIn.isAdmin()){
                                dialog(new AssociationProfileUpdateController(a),"Admin update").show();

                            } else if (loggedIn.isAssociationAdmin()){
                                if(UserSession.getInstace().getManagedAss().get().getId()==a.getId()){
                                    dialog(new AssociationProfileUpdateController(a),"Your association :"+ a.getNom()).show();
                                } else {
                                    dialog(new AssociationProfileShowController(a),"Profile :"+ a.getNom()).show();
                                }
                            }else{
                                dialog(new AssociationProfileShowController(a),"Profile :"+ a.getNom()).show();
                            }
                        }
                    });
                }
            }
        };
    }
    private JFXDialog dialog(Node body, String heading){
        close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> dialog.close());
        layout.setActions(close);
        layout.setBody(body);
        layout.setHeading(new Text(heading));
        return dialog;
// in row factory , add to listener to see if deleted, if so dialog.close();
        //dialog.setOnDialogClosed();
    }

    private static void scheduleTask(Duration interval, Runnable task) {
        Timeline timeline = new Timeline(new KeyFrame(interval, event -> task.run()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
