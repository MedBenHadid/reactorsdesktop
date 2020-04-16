package Packages.Chihab.Controllers;

import Packages.Chihab.Models.Association;
import Packages.Chihab.Models.Category;
import Packages.Chihab.Models.Membership;
import Packages.Chihab.Services.AssociationService;
import Packages.Chihab.Services.CategoryService;
import Packages.Chihab.Services.MembershipService;
import SharedResources.URLScenes;
import SharedResources.URLServer;
import SharedResources.Utils.AutoCompleteBox;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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

public class AssociationProfileUpdateController implements Initializable {
    @FXML
    private JFXComboBox<Category> domaineComboBox;
    @FXML
    private JFXComboBox<String> villeComboBox;
    @FXML
    private JFXTextField nomInput, rueInput, zipInput, phoneNumberInput;
    @FXML
    private JFXTextArea descriptionInput;
    @FXML
    private ImageView imageImageView;
    @FXML
    private JFXButton validateButton, photoButton, modpieceButton, addMembers;
    @FXML
    private StackPane stack;
    @FXML
    private WebView gmapWebView;
    @FXML
    private VBox membersVbox;
    private File photo = null, piece = null;
    private Association a;
    private FTPInterface ftpInterface;

    public AssociationProfileUpdateController(Association association) {
        try {
            this.a = association;
            this.ftpInterface = FTPInterface.getInstance(URLServer.ftpServerLink, URLServer.ftpSocketPort, URLServer.ftpUser, URLServer.ftpPassword);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BooleanBinding nomFieldValid = Bindings.createBooleanBinding(() -> !checkValidStringInput(nomInput.getText(), false, 6, 30), nomInput.textProperty());
        BooleanBinding descriptionFieldValid = Bindings.createBooleanBinding(() -> descriptionInput.getText().isBlank(), descriptionInput.textProperty());
        BooleanBinding zipFieldValid = Bindings.createBooleanBinding(() -> !zipInput.getText().matches(".*\\d.*") || zipInput.getText().length() != 4, zipInput.textProperty());
        BooleanBinding phoneNumberFieldValid = Bindings.createBooleanBinding(() -> !phoneNumberInput.getText().matches(".*\\d.*") || phoneNumberInput.getText().length() != 8, phoneNumberInput.textProperty());
        BooleanBinding rueFieldValid = Bindings.createBooleanBinding(() -> rueValidCheck(rueInput.getText()), rueInput.textProperty());
        BooleanBinding villeComboSelected = Bindings.createBooleanBinding(() -> villeComboBox.getSelectionModel().getSelectedIndex() == -1, villeComboBox.valueProperty());

        nomInput.setText(a.getNom());
        descriptionInput.setText(a.getDescription());
        rueInput.setText(a.getRue());
        zipInput.setText(String.valueOf(a.getCodePostal()));
        phoneNumberInput.setText(String.valueOf(a.getTelephone()));
        villeComboBox.setItems(FXCollections.observableArrayList("Ariana", "Béja", "Ben Arous", "Bizerte", "Gabes", "Gafsa", "Jendouba", "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine", "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan"));
        villeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> a.setVille(newValue));
        villeComboBox.setTooltip(new Tooltip());
        villeComboBox.getSelectionModel().select(a.getVille());
        new AutoCompleteBox<String>(villeComboBox);
        try {
            File imageAss = ftpInterface.downloadFile(URLServer.associationImageDir + a.getPhotoAgence(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            imageImageView.setImage(new Image(imageAss.toURI().toString()));
        } catch (IOException e) {
            Logger.getLogger(AssociationProfileShowController.class.getName()).log(Level.SEVERE, "Error whilst fetching image", e);
        }
        // Piece justificative
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Veuillez choisir la photo de l'association");
        SimpleBooleanProperty pieceValid = new SimpleBooleanProperty(false);
        try {
            piece = ftpInterface.downloadFile(URLServer.associationPieceDir + a.getPieceJustificatif(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
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
                        a.setPieceJustificatif(piece.getName());
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
        SimpleBooleanProperty photoValid = new SimpleBooleanProperty(false);
        photoButton.setOnAction(e -> {
            photoValid.set(false);
            photo = fileChooser.showOpenDialog(stack.getScene().getWindow());
            if (photo != null) {
                String[] names = ImageIO.getReaderFormatNames();
                //ImageIO.read(file);
                for (String exst : names) {
                    if (photo.getName().endsWith(exst)) {
                        photoValid.set(true);
                        break;
                    }
                }
            }
            if (!photoValid.get()) {
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
                @Override
                public String toString(Category c) {
                    return c.getNom();
                }

                @Override
                public Category fromString(String s) {
                    return domaineComboBox.getItems().stream().filter(c -> c.getNom().equals(s)).findFirst().get();
                }
            });
            domaineComboBox.getSelectionModel().select(a.getDomaine());
        } catch (Exception e) {
            Logger.getLogger(
                    AssociationCreateController.class.getName()).log(
                    Level.SEVERE, null, e
            );
            e.printStackTrace();
        }
        JSObject window = (JSObject) gmapWebView.getEngine().executeScript("window");
        gmapWebView.getEngine().load(this.getClass().getResource("/Packages/Chihab/Scenes/WebView/showAndModifyAssociationLocation.html").toString());
        gmapWebView.getEngine().getLoadWorker().stateProperty().addListener(
                (ChangeListener<? super Worker.State>) (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        gmapWebView.setVisible(true);
                        gmapWebView.getEngine().executeScript("initMap(" + a.getLat() + "," + a.getLon() + ")");
                        window.setMember("association", a);
                    }
                }
        );
        validateButton.disableProperty().bind(nomFieldValid.or(descriptionFieldValid.or(zipFieldValid.or(rueFieldValid.or(phoneNumberFieldValid.or(villeComboSelected))))));
        validateButton.setOnAction(actionEvent -> {
            a.setLat(Double.valueOf(((Association) window.getMember("association")).getLat()));
            a.setLon(Double.valueOf(((Association) window.getMember("association")).getLon()));
            a.setRue(rueInput.getText());
            a.setVille(villeComboBox.getValue());
            a.setCodePostal(Integer.parseInt(zipInput.getText()));
            a.setNom(nomInput.getText());
            a.setDescription(descriptionInput.getText());
            a.setDomaine(domaineComboBox.getValue());
            a.setTelephone(Integer.parseInt(phoneNumberInput.getText()));
            a.setPhotoAgence(photo.getName());
            a.setPieceJustificatif(piece.getName());
            try {
                ftpInterface.fileUpload(piece, URLServer.associationPieceDir);
                ftpInterface.fileUpload(photo, URLServer.associationImageDir);
                AssociationService.getInstace().update(a);
            } catch (IOException | SQLException ex) {
                ex.printStackTrace();
            } finally {

            }
        });
        FXMLLoader loader = null;
        try {
            for (Membership m : MembershipService.getInstace().readAll()) {
                loader = new FXMLLoader(getClass().getResource(URLScenes.memberShipUpdateItem));
                MemberUpdateItemController controller = new MemberUpdateItemController(m);
                loader.setController(controller);
                membersVbox.getChildren().add(loader.load());
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        addMembers.setOnMouseClicked(mouseEvent -> {
            FXMLLoader createMembership = new FXMLLoader(getClass().getResource(URLScenes.addMembershipSelectUSer));
            StackPane c = null;
            try {
                c = createMembership.load();
            } catch (IOException e) {
                e.printStackTrace();
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
                    ioException.printStackTrace();
                }
                dialog.close();
            });
            JFXButton close = new JFXButton("Cancel");
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

    boolean checkValidStringInput(String input, Boolean isAlphaNumerical, int minLength, int maxLength) {
        if (input.isEmpty() || input.isBlank()) {
            return false;
        } else if (input.length() > maxLength || input.length() < minLength) {
            return false;
        } else if (!isAlphaNumerical) {
            return !input.matches(".*\\d.*");
        }
        return true;
    }
}
