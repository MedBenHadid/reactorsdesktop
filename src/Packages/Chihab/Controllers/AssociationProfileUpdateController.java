package Packages.Chihab.Controllers;

import Packages.Chihab.Models.Entities.Association;
import Packages.Chihab.Models.Entities.Category;
import Packages.Chihab.Models.Entities.Membership;
import Packages.Chihab.Services.AssociationService;
import Packages.Chihab.Services.CategoryService;
import Packages.Chihab.Services.MembershipService;
import SharedResources.URLScenes;
import SharedResources.URLServer;
import SharedResources.Utils.AutoCompleteBox;
import SharedResources.Utils.BinaryValidator.PhoneNumberValidator;
import SharedResources.Utils.BinaryValidator.StringLengthValidator;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.*;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import netscape.javascript.JSObject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AssociationProfileUpdateController extends StackPane implements Initializable {
    @FXML JFXComboBox<Category> domaineComboBox;
    @FXML JFXComboBox<String> villeComboBox;
    @FXML JFXTextField rueInput, zipInput, phoneNumberInput;
    @FXML JFXTextArea descriptionInput;
    @FXML ImageView imageImageView;
    @FXML JFXButton validateButton, photoButton, modpieceButton, addMembers,deleteButton;
    @FXML StackPane stack;
    @FXML WebView gmapWebView;
    @FXML VBox membersVbox;
    @FXML Label associationNameLabel;
    private File photo, piece;
    private FXMLLoader loader;
    private StackPane c;
    private final JFXButton close = new JFXButton("Cancel");
    private final FXMLLoader createMembership = new FXMLLoader(getClass().getResource(URLScenes.addMembershipSelectUSer));
    private final Association association;
    private final RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator("Missing field");
    private final PhoneNumberValidator phoneValidator = new PhoneNumberValidator("Please provide a valid phone number");
    private final FileChooser fileChooser = new FileChooser();
    private final FileChooser photoChooser = new FileChooser();

    public AssociationProfileUpdateController(Association association) {
        this.association = association;
        FXMLLoader thisLoader = new FXMLLoader(getClass().getResource(URLScenes.associationUpdateProfile));
        thisLoader.setRoot( this );
        thisLoader.setController( this );
        try {
            thisLoader.load();
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
        init(association);
    }
    private void init(Association a){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //ArrayList<? extends TextInputControl> a = new ArrayList<>();
        ObservableList<JFXTextField> textFields = FXCollections.observableArrayList();
        textFields.add(rueInput);
        textFields.add(zipInput);
        textFields.add(phoneNumberInput);

        associationNameLabel.textProperty().bind(association.get().nomProperty());
        association.addListener((observableValue, association1, t1) -> {
            System.out.println("DETECTED CHANAZEAZEAZE");
        });

        BooleanBinding descriptionFieldValid = Bindings.createBooleanBinding(() -> descriptionInput.getText().isBlank(), descriptionInput.textProperty());
        BooleanBinding zipFieldValid = Bindings.createBooleanBinding(() -> !zipInput.getText().matches(".*\\d.*") || zipInput.getText().length() != 4, zipInput.textProperty());
        BooleanBinding phoneNumberFieldValid = Bindings.createBooleanBinding(() -> !phoneNumberInput.getText().matches(".*\\d.*") || phoneNumberInput.getText().length() != 8, phoneNumberInput.textProperty());
        BooleanBinding rueFieldValid = Bindings.createBooleanBinding(() -> rueValidCheck(rueInput.getText()), rueInput.textProperty());
        BooleanBinding villeComboSelected = Bindings.createBooleanBinding(() -> villeComboBox.getSelectionModel().getSelectedIndex() == -1, villeComboBox.valueProperty());


        phoneNumberInput.setText(String.valueOf(association.getTelephone()));
        descriptionInput.setText(association.getDescription());
        rueInput.setText(association.getRue());


        phoneNumberInput.getValidators().addAll(requiredFieldValidator, phoneValidator);
        rueInput.getValidators().addAll(requiredFieldValidator, new StringLengthValidator(8, -1, "La taille doit etre supérieure a "));
        zipInput.getValidators().addAll(new NumberValidator("Veuillez saisir un code postale numérique"), requiredFieldValidator, new StringLengthValidator(4, 0, "La taille du code postale est "));




        //association.villeProperty().bind(villeComboBox.getSelectionModel().selectedItemProperty().asString());
        //phoneNumberInput.setText(String.valueOf(a.getTelephone()));
        villeComboBox.setItems(FXCollections.observableArrayList("Ariana", "Béja", "Ben Arous", "Bizerte", "Gabes", "Gafsa", "Jendouba", "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine", "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan"));
        //villeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> a.setVille(newValue));
        villeComboBox.setTooltip(new Tooltip("Where is your organization located!"));

        villeComboBox.getSelectionModel().select(association.getVille());
        new AutoCompleteBox<String>(villeComboBox);
        try {
            File imageAss = FTPInterface.getInstance().downloadFile(URLServer.associationImageDir + association.getPhotoAgence(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            imageImageView.setImage(new Image(imageAss.toURI().toString()));
        } catch (IOException e) {
            Logger.getLogger(AssociationProfileShowController.class.getName()).log(Level.SEVERE, "Error whilst fetching image", e);
        }
        // Piece justificative
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Veuillez choisir la photo de l'association");
        SimpleBooleanProperty pieceValid = new SimpleBooleanProperty(false);
        try {
            piece = FTPInterface.getInstance().downloadFile(URLServer.associationPieceDir + association.getPieceJustificatif(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        modpieceButton.setOnAction(e -> {
            pieceValid.set(false);
            piece = fileChooser.showOpenDialog(stack.getScene().getWindow());
            if (piece != null) {
                String[] names = ImageIO.getReaderFormatNames();
                for (String exst : names) {
                    if (piece.getName().endsWith(exst) || piece.getName().endsWith(".pdf") || piece.getName().endsWith(".doc") || piece.getName().endsWith(".docx")) {
                        association.get().setPieceJustificatif(piece.getName());
                        pieceValid.set(true);
                        break;
                    }
                }
                if (!pieceValid.get()) {
                    Alert pieceAlert = new Alert(Alert.AlertType.WARNING);
                    pieceAlert.setContentText("Veuillez choisir un document avec un format supporté");
                    pieceAlert.showAndWait();
                }
            }
        });
        fileChooser.setTitle("Veuillez choisir la photo de l'association");
        photoButton.setTooltip(new Tooltip("Veuillez choisir la photo de l'association"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("DOCX", "*.docx,*.doc")
        );
        photoChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        SimpleBooleanProperty photoValid = new SimpleBooleanProperty(false);
        photoButton.setOnAction(e -> {
            photoValid.set(false);
            photo = fileChooser.showOpenDialog(stack.getScene().getWindow());
            if (photo != null) {

                if(FTPInterface.getInstance().send(photo,URLServer.associationImageDir)){
                    try {
                        if(AssociationService.getInstance().update(association)){
                            imageImageView.setImage(new Image(photo.toURI().toString()));
                        }
                    } catch (SQLException throwables) {
                        // TODO : Error
                    }
                }else {
                    // TODO : Error
                }
            } else {
                Alert pieceAlert = new Alert(Alert.AlertType.WARNING);
                pieceAlert.setContentText("Veuillez choisir une photo avec un format supporté");
                pieceAlert.showAndWait();
            }
        });
        try {
            domaineComboBox.getItems().addAll(CategoryService.getInstace().readAll());
            domaineComboBox.setVisibleRowCount(6);
            domaineComboBox.setTooltip(new Tooltip());
            domaineComboBox.setConverter(new StringConverter<>() {
                @Override public String toString(Category c) {
                    return c.getNom();
                }
                @Override public Category fromString(String s) { return domaineComboBox.getItems().stream().filter(c -> c.getNom().equals(s)).findFirst().get(); }
            });
            domaineComboBox.getSelectionModel().select(association.getDomaine());
        } catch (Exception e) {
            Logger.getLogger(AssociationCreateController.class.getName()).log(Level.SEVERE, null, e);
        }

        gmapWebView.getEngine().load(this.getClass().getResource("/Packages/Chihab/Scenes/WebView/showAndModifyAssociationLocation.html").toString());
        gmapWebView.getEngine().getLoadWorker().stateProperty().addListener(
                (ChangeListener<? super Worker.State>) (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        gmapWebView.setVisible(true);
                        gmapWebView.getEngine().executeScript("initMap(" + association.getLat() + "," + association.getLon() + ")");
                        JSObject window = (JSObject) gmapWebView.getEngine().executeScript("window");
                        window.setMember("association", association);
                    }
                }
        );
        //validateButton.disableProperty().bind(descriptionFieldValid.or(zipFieldValid.or(rueFieldValid.or(phoneNumberFieldValid.or(villeComboSelected)))));
        validateButton.setOnAction(actionEvent -> {
            boolean isValid = true;
            for(JFXTextField field : textFields){
               field.validate();
               for(ValidatorBase validator : field.getValidators()){
                   isValid = isValid&&validator.getHasErrors();
               }
            }
            if(isValid){
                System.out.println(association.getLat());
                System.out.println(association.getLon());
                association.setRue(rueInput.getText());
                association.setVille(villeComboBox.getValue());
                association.setCodePostal(Integer.parseInt(zipInput.getText()));
                association.setDescription(descriptionInput.getText());
                association.setDomaine(domaineComboBox.getValue());
                association.setTelephone(Integer.parseInt(phoneNumberInput.getText()));
                association.setPhotoAgence(photo.getName());
                association.setPieceJustificatif(piece.getName());
            }
            //JSObject window = (JSObject) gmapWebView.getEngine().executeScript("window");
            //association.setLat(Double.valueOf(((ObservableAssociationValue) window.getMember("association"))..getLat()));
            //association.setLon(Double.valueOf(((ObservableAssociationValue) window.getMember("association")).get().getLon()));


                if(1==1) {
                    // TODO : Fire event and have it subscribe
                }else{
                    // TODO : Show error dialog
                }

        });

        try {
            for (Membership m : MembershipService.getInstace().searchByAssociationId(association.getId())) {
               // if(m.getStatus().equals(Membership.ACCEPTED)){
                    loader = new FXMLLoader(getClass().getResource(URLScenes.memberShipUpdateItem));
                    MemberUpdateItemController controller = new MemberUpdateItemController(m);
                    loader.setController(controller);
                    membersVbox.getChildren().add(loader.load());
                //}
            }
        } catch (SQLException | IOException e) {
            Logger.getLogger(AssociationCreateController.class.getName()).log(Level.SEVERE, null, e);
        }
        addMembers.setOnMouseClicked(mouseEvent -> {
            try {
                c = createMembership.load();
            } catch (IOException e) {
                Logger.getLogger(AssociationCreateController.class.getName()).log(Level.SEVERE, null, e);
            }
            JFXDialogLayout layout = new JFXDialogLayout();
            //layout.setPrefWidth(200);
            //layout.setPrefHeight(200);
            layout.setBody(c);
            layout.setHeading(new Text("Ajout d'une nouvelle adhérance"));
            JFXDialog dialog = new JFXDialog(stack, layout, JFXDialog.DialogTransition.CENTER);
            JFXButton membershipAdd = (JFXButton) createMembership.getNamespace().get("inviteButton");
            membershipAdd.setOnMouseClicked(e -> {
                FXMLLoader load = new FXMLLoader(getClass().getResource(URLScenes.memberShipItem));
                MemberItemController controller = new MemberItemController(((MembershipCreate) load.getController()).getM());
                load.setController(controller);
                try {
                    membersVbox.getChildren().add(load.load());
                } catch (IOException ioException) {
                    Logger.getLogger(AssociationCreateController.class.getName()).log(Level.SEVERE, null, ioException);
                }
                dialog.close();
            });

            close.addEventHandler(MouseEvent.MOUSE_CLICKED, m -> dialog.close());
            layout.setActions(close);
            dialog.show();
        });
    }

    boolean rueValidCheck(String rue) {
        if (!rue.isBlank() && rue.length() > 8) {
            if (rue.contains(" ")) {
                return !rue.substring(0, rue.indexOf(' ')).matches(".*\\d.*");
            }
        }
        return true;
    }

}
