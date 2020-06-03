package Main.SubScenes.Login;

import Main.Entities.UserSession;
import SharedResources.URLScenes;
import SharedResources.Utils.Connector.ConnectionUtil;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML private Label lblErrors;
    @FXML private JFXTextField txtUsername;
    @FXML private JFXPasswordField txtPassword;
    @FXML private Button btnSignin;
    public LoginController() { }
    private final FadeTransition ft = new FadeTransition(Duration.millis(1500));

    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
    }


    public void login(Event e){
        if(!txtPassword.getText().isEmpty()&&!txtUsername.getText().isEmpty()){
            try {
                if (!UserSession.login(txtUsername.getText(), txtPassword.getText())) {
                    setLblError(Color.TOMATO, "Enter Correct (Email/Username)-/Password");
                } else {
                    setLblError(Color.GREEN, "Login Successful..Redirecting..");
                    openMain(e);
                    // TODO : Pre-loader
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else{
            setLblError(Color.TOMATO, "Empty credentials");
        }
    }
    public void openMain(Event e){
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource(URLScenes.mainScene)));
            //add you loading or delays - ;-)
            stage.setMaximized(true);
            stage.close();
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setResizable(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ConnectionUtil.getInstance();
        if(ConnectionUtil.isIsConnected())
            setLblError(Color.GREEN, "Server status : Good to go");
        else
            setLblError(Color.TOMATO, "Server status : Ureachable");

        ConnectionUtil.isConnectedProperty().addListener((observableValue, aBoolean, t1) -> {
            System.out.println("Detected databse change : updating");
            if(t1)
                setLblError(Color.GREEN, "Server status : Good to go");
            else
                setLblError(Color.TOMATO, "Server status : Ureachable");
        });
        btnSignin.visibleProperty().bind(ConnectionUtil.isConnectedProperty());
        txtPassword.disableProperty().bind(ConnectionUtil.isConnectedProperty().not());
        ft.setNode(lblErrors);
        ft.setFromValue(0.4);
        ft.setToValue(1);
        ft.setCycleCount(FadeTransition.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();
        txtPassword.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER))
            {
                if(!ConnectionUtil.isIsConnected()){
                    System.out.println("Server unreachable");
                } else {
                    login(keyEvent);
                }
            }
        });
        btnSignin.setOnMouseClicked(event -> {
            if(!ConnectionUtil.isIsConnected()){
                System.out.println("Server unreachable");
            } else {
                login(event);
            }
        });
    }
}
