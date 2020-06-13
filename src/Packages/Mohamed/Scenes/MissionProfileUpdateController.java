package Packages.Mohamed.Scenes;

import Main.Entities.User;
import Main.Services.UserService;
import Packages.Chihab.Models.Entities.Association;
import Packages.Chihab.Models.Entities.Category;
import Packages.Chihab.Models.Entities.Membership;
import Packages.Chihab.Services.AssociationService;
import Packages.Chihab.Services.CategoryService;
import Packages.Chihab.Services.MembershipService;
import Packages.Mohamed.Entities.Invitation;
import Packages.Mohamed.Entities.Mission;
import Packages.Mohamed.Services.MissionService;
import SharedResources.URLScenes;
import SharedResources.URLServer;
import SharedResources.Utils.BinaryValidator.RegexValidator;
import SharedResources.Utils.BinaryValidator.StringLengthValidator;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.binding.Binding;
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
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.StringConverter;
import netscape.javascript.JSObject;
import org.apache.commons.net.ftp.FTP;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MissionProfileUpdateController extends StackPane implements Initializable {
    @FXML JFXComboBox<Category> domaineComboBox;
    @FXML JFXComboBox<String> vComboBox;
    @FXML JFXTextField adresseInput;
    @FXML JFXSlider objectifSlider;
    @FXML JFXTextArea descriptionInput;
    @FXML JFXDatePicker dateDeb,dateFin;
    @FXML ImageView imageImageView;
    @FXML StackPane stack;
    @FXML WebView gmapWebView;
    @FXML JFXListView<User> membersList;
    @FXML Label MissionNameLabel;
    @FXML JFXButton validateButton, photoButton, deleteButton;
    private final JFXButton statusButton = new JFXButton("Yes, i'm sure, dissaprove.");
    private final JFXButton close = new JFXButton("Cancel");
    private final JFXTextArea reason = new JFXTextArea("Reason for dissaproving :");
    private final JFXDialogLayout layout = new JFXDialogLayout();
    private final FXMLLoader createMembership = new FXMLLoader(getClass().getResource(URLScenes.addMembershipSelectUSer));
    private final RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator("Missing field");
    private final FileChooser photoChooser = new FileChooser();
    private final JFXButton confirmDelete = new JFXButton("Yes, i'm sure.");
    private final Mission mission;
    private final JFXDialog dialog;
    private StackPane c;
    private File photo;
    final  ObservableList<User> users =FXCollections.observableArrayList();
    final ArrayList<Invitation> InviMember=new ArrayList<>();
    final ArrayList<User> invitedUsers = new ArrayList<>();
    public MissionProfileUpdateController(Mission mission) throws IOException {
        this.mission = mission;
        try {
            InviMember.addAll(MissionService.getInstace().getInvitationsByMission(mission));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FXMLLoader thisLoader = new FXMLLoader(getClass().getResource(URLScenes.missionUpdate));
        thisLoader.setRoot(this);
        thisLoader.setController(this);
        thisLoader.load();
        this.dialog = new JFXDialog(this, layout, JFXDialog.DialogTransition.CENTER);
        reason.getValidators().add(new RegexValidator("Please provide a valid reason 8..255", "\\b\\w{8,255}\\b"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vComboBox.setItems(FXCollections.observableArrayList("Ariana", "Béja", "Ben Arous", "Bizerte", "Gabes", "Gafsa", "Jendouba", "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine", "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan"));
        MissionNameLabel.setText(mission.getTitleMission());
        BooleanBinding descriptionFieldValid = Bindings.createBooleanBinding(() -> descriptionInput.getText().isBlank(), descriptionInput.textProperty());
        BooleanBinding adresseFieldValid = Bindings.createBooleanBinding(() -> rueValidCheck(adresseInput.getText()), adresseInput.textProperty());
        dateFin.valueProperty().addListener((e,r,event) -> {
         //   System.out.println("test");
            if (dateDeb.getValue().compareTo(dateFin.getValue()) != -1) {

                dialog(new Label("La date de fin mission doit etre supérieur à date fin"), "Invalid date").show();
               // vers.setValue(de.getValue().plusHours(8));
            } else {
                mission.setDateFin((java.sql.Date) Date.from(Instant.from(dateFin.getValue())));
                mission.setDateCreation((java.sql.Date) Date.from(Instant.from(dateDeb.getValue())));
            }
        });
        System.out.println(mission);


        descriptionInput.setText(mission.getDescription());

        adresseInput.getValidators().addAll(requiredFieldValidator, new StringLengthValidator(10, -1, "La taille doit etre supérieure a "));



        try {
            File imageAss = FTPInterface.getInstance().downloadFile(URLServer.missionImageDir + mission.getPicture(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            imageImageView.setImage(new Image(Objects.requireNonNull(imageAss).toURI().toString()));
        } catch (IOException e) {
            Logger.getLogger(MissionProfileController.class.getName()).log(Level.SEVERE, "Error whilst fetching image", e);
        }

      /*  mailManager.setOnMouseClicked(event -> {
            try {
                Desktop.getDesktop().mail(URI.create("mailto:" + association.getManager().getEmail()));
            } catch (IOException e) {
                dialog(new Text("Error whilst opening mailer."), "Couldnt open mailer").show();
            }
        });*/

        photoButton.setTooltip(new Tooltip("Veuillez choisir la photo de l'association"));

        photoChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        SimpleBooleanProperty photoValid = new SimpleBooleanProperty(false);
        photoButton.setOnAction(e -> {
            photoValid.set(false);
            photo = photoChooser.showOpenDialog(stack.getScene().getWindow());
            if (photo != null) {
                if(FTPInterface.getInstance().send(photo,URLServer.missionImageDir)) {
                    mission.setPicture(photo.getName());
                    try {
                        if (MissionService.getInstace().update(mission)) {
                            imageImageView.setImage(new Image(photo.toURI().toString()));
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }else {
                    // TODO : Error
                }
            } else {
                dialog(new Text("Veuillez choisir une photo avec un format supporté"), "Wrong !").show();
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
            domaineComboBox.getSelectionModel().select(mission.getDomaine());
        } catch (Exception e) {
            Logger.getLogger(MissionCreateController.class.getName()).log(Level.SEVERE, null, e);
        }

        /**gmapWebView.getEngine().load(this.getClass().getResource("/Packages/MohamedM/Scenes/WebView/showAndModifyMissionLocation.html").toString());
        gmapWebView.getEngine().getLoadWorker().stateProperty().addListener(
                (ChangeListener<? super Worker.State>) (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        gmapWebView.setVisible(true);
                        gmapWebView.getEngine().executeScript("initMap(" + mission.getLat() + "," + mission.getLon() + ")");
                        JSObject window = (JSObject) gmapWebView.getEngine().executeScript("window");
                        window.setMember("mission", mission);
                    }
                }
        );*/
        ObservableList<JFXTextField> textFields = FXCollections.observableArrayList();
        textFields.add(adresseInput);

        for (JFXTextField field : textFields) {
            field.focusedProperty().addListener(observable -> {
                field.validate();
            });
        }
        validateButton.disableProperty().bind(descriptionFieldValid.or(adresseFieldValid));
        validateButton.setOnAction(actionEvent -> {
            mission.setLocation(adresseInput.getText());
            mission.setDescription(descriptionInput.getText());
            mission.setDomaine(domaineComboBox.getValue());
            mission.setObjectif(objectifSlider.getValue());
            try {
                if (MissionService.getInstace().update(mission)) {
                    dialog(new Text("Succesfully updated " + mission.getTitleMission()), "Success!");
                } else {
                    dialog(new Text("Couldnt update !"), "Error");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
        deleteButton.setOnMouseClicked(event -> deleteDialog().show());


  /*      try {
            for (Invitation i : MissionService.getInstace().getInvitationsByMission(mission)) {
                users.add(i.getId_user());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        try {
            for (Membership m :MembershipService.getInstace().searchByAssociationId(MissionService.getInstace().serchAssociationByid_manager(mission.getCretedBy().getId()))){
                users.add(m.getMember());
                System.out.println(users);
            }
            membersList.setItems(users);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        membersList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<User> call(ListView<User> param) {
                return new JFXListCell<>() {
                    private final HBox hbox = new HBox();
                    private final JFXCheckBox checkbox = new JFXCheckBox();
                    private final ImageView profilePicture = new ImageView();
                    private final Label username = new Label();
                    private final Tooltip invitedTooltip = new Tooltip();
                    @Override
                    public void updateItem(User item, boolean empty) {
                        super.updateItem(item, empty);
                        profilePicture.setFitWidth(30);
                        profilePicture.setFitHeight(30);
                        checkbox.setDisable(true);
                        if (item != null && !empty) {
                            InviMember.forEach(invi -> {if(invi.getId_user().getId()==item.getId()){
                                checkbox.setSelected(true);
                                invitedUsers.add(item);
                            }
                            });
                            username.setText(item.getUsername());
                            try {
                                profilePicture.setImage( new Image(FTPInterface.getInstance().downloadFile(URLServer.userImageDir+item.getProfile().getImage() , FTP.BINARY_FILE_TYPE).toURI().toString()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            invitedTooltip.textProperty().bind(Bindings.createStringBinding(() -> (checkbox.isSelected() ? "Invited": " Not invited") ,checkbox.selectedProperty()));
                            setTooltip(invitedTooltip);
                            setOnMouseClicked(event -> {
                                checkbox.setSelected(!checkbox.isSelected());
                                if(checkbox.isSelected()){
                                    invitedUsers.add(item);
                                }else{
                                    invitedUsers.remove(item);
                                    System.out.println("removed");
                                }
                            });
                            if(hbox.getChildren().size()==0){
                                hbox.getChildren().add(checkbox);
                                hbox.getChildren().add(profilePicture);
                                hbox.getChildren().add(username);
                            }
                            setGraphic(hbox);
                            setText("");
                        }
                    }
                };
            }
        });



/*
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
            layout.setHeading(new Text("Ajout d'un nouveau member"));
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
        */
    }

    boolean rueValidCheck(String rue) {
        if (!rue.isBlank() && rue.length() > 8) {
            if (rue.contains(" ")) {
                return !rue.substring(0, rue.indexOf(' ')).matches(".*\\d.*");
            }
        }
        return true;
    }

    private JFXDialog dialog(Node body, String heading) {
        close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> dialog.close());
        layout.setActions(close);
        body.prefWidth(this.getWidth() / 2);
        body.prefHeight(this.getHeight() / 2);
        layout.setBody(body);
        layout.setHeading(new Text(heading));
        return dialog;
        // in row factory , add to listener to see if deleted, if so dialog.close();
        //dialog.setOnDialogClosed();
    }

    private JFXDialog deleteDialog() {
        close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> dialog.close());
        confirmDelete.setOnMouseClicked(event -> {
            try {
                if (MissionService.getInstace().delete(mission)) {
                    MissionController.getDialog().close();
                } else {
                    System.out.println("Couldnt delete");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        layout.setActions(confirmDelete, close);
        layout.setBody(new Text("Are you sure you want to delete " + mission.getTitleMission() + " !"));
        layout.setHeading(new Label("Confirm !"));
        return dialog;
    }


}
