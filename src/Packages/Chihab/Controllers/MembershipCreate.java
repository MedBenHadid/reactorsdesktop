package Packages.Chihab.Controllers;

import Main.Entities.User;
import Main.Services.UserService;
import Packages.Chihab.Models.Entities.Membership;
import Packages.Chihab.Models.enums.RoleEnum;
import SharedResources.URLServer;
import SharedResources.Utils.BCrypt.BCrypt;
import SharedResources.Utils.BinaryValidator.EmailValidation;
import SharedResources.Utils.BinaryValidator.EmailValidator;
import SharedResources.Utils.BinaryValidator.RegexValidator;
import SharedResources.Utils.FTPInterface.FTPInterface;
import com.jfoenix.controls.*;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MembershipCreate implements Initializable {
    @FXML private JFXTextField emailInput;
    @FXML private JFXSpinner imageSpinner;
    @FXML private ImageView profilePicture;
    @FXML private Label fullNameLabel;
    @FXML private AnchorPane profileAnchor;
    @FXML StackPane stack;
    @FXML private JFXButton inviteButton;
    private final Membership m;
    private final ObservableList<User> users;
    private final JFXDialogLayout l = new JFXDialogLayout();
    private JFXDialog d;
    private final JFXButton c = new JFXButton("Cancel");
    private final JFXButton fermer = new JFXButton("Okay");
    private final JFXButton verify = new JFXButton("Invite");
    private final JFXButton close = new JFXButton("Cancel");
    private final JFXDialogLayout layout = new JFXDialogLayout();
    private final JFXTextArea tr = new JFXTextArea();
    public MembershipCreate() throws SQLException {
        this.m = new Membership();
        this.users = UserService.getInstace().readAll().stream().filter(User::isClient).collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    public Membership getM() {
        return m;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        d = new JFXDialog(stack, l, JFXDialog.DialogTransition.CENTER);
        ObservableObjectValue<User> current;

        emailInput.getValidators().add(new EmailValidation("Not a valid email"));
        TextFields.bindAutoCompletion(emailInput, users.stream().map(User::getEmail).toArray());
        FilteredList<User> filteredName = new FilteredList<>(users, e -> true);
        emailInput.setOnKeyReleased(e -> emailInput.textProperty().addListener((observableValue, s, t1) -> {
            filteredName.setPredicate(user -> {
                if (t1 == null || t1.isEmpty())
                    return true;
                return user.getEmail().toLowerCase().startsWith(t1.toLowerCase());
            });
        }));
        ObservableList<User> finalUsers = users;
        emailInput.setOnKeyTyped(ke -> {
            emailInput.validate();
            Optional<User> u = Optional.empty();
            if (emailInput.getValidators().stream().noneMatch(ValidatorBase::getHasErrors))
                u = finalUsers.stream().filter(user -> user.getEmail().equals(emailInput.textProperty().get())).findFirst();
            if (emailInput.getText().isEmpty()) {
                imageSpinner.setVisible(false);
                profileAnchor.setVisible(false);
                inviteButton.setVisible(false);
            } else if (EmailValidator.isEmail(emailInput.textProperty().get())) {
                inviteButton.setVisible(true);
                imageSpinner.setVisible(false);
                if (u.isPresent()) {
                    profileAnchor.setVisible(true);
                    fullNameLabel.setText((u.get().getProfile().getNom() + " " + u.get().getProfile().getPrenom()).toUpperCase());
                    File imageAss;
                    try {
                        imageAss = FTPInterface.getInstance().downloadFile(URLServer.userImageDir + u.get().getProfile().getImage(), org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                        profilePicture.setImage(new Image(imageAss.toURI().toString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                imageSpinner.setVisible(true);
                profileAnchor.setVisible(false);
                inviteButton.setVisible(false);
            }
        });
        emailInput.setOnKeyPressed(keyEvent -> {
            Optional<User> u = finalUsers.stream().filter(user -> user.getEmail().equals(emailInput.textProperty().get())).findFirst();
            if (keyEvent.getCode().equals(KeyCode.ENTER))
                if (EmailValidator.isEmail(emailInput.textProperty().get()))
                    if (u.isEmpty()) {
                        // TODO : He's not a memeber
                        layout.setHeading(new Text("Inviting on-board " + emailInput.getText()));
                        tr.setPromptText("Provide an invite message");
                        tr.getValidators().add(new RegexValidator("Message must be comprimised between", "^[\\d\\w]{5,255}"));
                        tr.setOnKeyTyped(e -> tr.validate());
                        layout.setBody(tr);
                        JFXDialog dialog = new JFXDialog(stack, layout, JFXDialog.DialogTransition.CENTER);
                        close.getStyleClass().addAll("jfx-button-error");
                        close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> dialog.close());
                        verify.disableProperty().bind(Bindings.createBooleanBinding(() -> !tr.textProperty().get().matches("^[\\d\\w]{5,255}"), tr.textProperty()));
                        verify.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                            dialog.close();
                            User newUser=null;
                            try {
                                String passwordPlain = UUID.randomUUID().toString().substring(0, 8);
                                newUser = new User(emailInput.getText(), emailInput.getText().substring(0, emailInput.getText().indexOf("@")), BCrypt.hashpw(passwordPlain, BCrypt.gensalt(13)),"user.png");
                                newUser.removeRole(RoleEnum.ROLE_CLIENT);
                                newUser.getProfile().setImage("user.png");
                                newUser.setId(UserService.getInstace().create(newUser));
                                if (newUser.getId() == -1) {
                                    l.setHeading(new Text("Member :" + emailInput.getText() + " invited"));
                                    l.setBody(new Text("Member succesfully invited, please provide further info."));
                                    JFXDialog d = new JFXDialog(stack, l, JFXDialog.DialogTransition.CENTER);
                                    c.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> d.close());
                                    l.setActions(c);
                                    d.show();
                                } else {
                                    //MailSender.sendMail(emailInput.getText(), AssociationService.getInstace().searchByManagerId(UserSession.getInstace().getUser().getId()), UserSession.getInstace().getUser(), passwordPlain, tr.getText());
                                    users.add(newUser);
                                }
                            } catch (Exception e) {
                                Logger.getLogger(MembershipCreate.class.getName()).log(Level.INFO, "Error sending email", e);
                            } finally {
                                /**try {
                                    //MembershipService.getInstace().create(new Membership( new Date(Calendar.getInstance().getTime().getTime()), "Edit me", "Edit me", UserSession.getInstace().getManagedAss().get(), newUser, AccessType.DELIVER));
                                } catch (SQLException throwables) {
                                    Logger.getLogger(MembershipCreate.class.getName()).log(Level.INFO, "Error sending email", throwables);
                                    try {
                                        UserService.getInstace().delete(newUser);
                                    } catch (SQLException e) {
                                        Logger.getLogger(MembershipCreate.class.getName()).log(Level.INFO, "Error sending email", e);
                                    }
                                }*/
                                l.setHeading(new Text("Member :" + emailInput.getText() + " invited"));
                                l.setBody(new Text("Member succesfully invited, please provide further info."));
                                JFXDialog d = new JFXDialog(stack, l, JFXDialog.DialogTransition.CENTER);
                                c.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> d.close());
                                l.setActions(c);
                                d.show();
                            }
                        });
                        layout.setActions(close, verify);
                        dialog.show();
                    }
                else {
                    l.setHeading(new Text("Invalid email"));
                    l.setBody(new Label("Please provide a valid email"));
                    fermer.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> d.close());
                    l.setActions(fermer);
                    d.show();
                }
        });
    }
}
