package Main;

import Main.Services.UserService;
import SharedResources.URLScenes;
import SharedResources.Utils.Connector.ConnectionUtil;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static javafx.fxml.FXMLLoader.load;

public class AuthLayout extends Application implements Initializable {
    private final UserService userService = UserService.getInstace();
    private final Connection con;
    private final Alert a = new Alert(Alert.AlertType.ERROR);
    @FXML
    private VBox side;
    @FXML
    private Label lblErrors;
    @FXML
    private JFXTextField txtUsername;
    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private Button btnSignin;
    @FXML
    private StackPane authStack;
    private double xOffset = 0;
    private double yOffset = 0;

    public AuthLayout() {
        con = ConnectionUtil.conDB().conn;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = load(getClass().getResource(URLScenes.authLayout));
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("ReactorsFX : La bénévolat en desktop");
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (con == null) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Server Error : Check");
        } else {
            lblErrors.setTextFill(Color.GREEN);
            lblErrors.setText("Server is up : Good to go");
        }
        FadeTransition ft = new FadeTransition(Duration.millis(15000));
        ft.setNode(side);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    //we gonna use string to check for status
    private String logIn() {
        String status = "Success";
        String credential = txtUsername.getText();
        String password = txtPassword.getText();
        if (credential.isEmpty() || password.isEmpty()) {
            setLblError(Color.TOMATO, "Empty credentials");
            status = "Error1";
        } else {
            try {
                if (!userService.validateLogin(credential, password)) {
                    setLblError(Color.TOMATO, "Enter Correct (Email/Username)-/Password");
                    status = "Error";
                } else {
                    setLblError(Color.GREEN, "Login Successful..Redirecting..");
                }
            } catch (SQLException ex) {
                status = "Exception";
            }
        }
        return status;
    }

    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
    }

    @FXML
    public void handleButtonAction(MouseEvent event) {
        if (event.getSource() == btnSignin) {
            String status = logIn();
            switch (status) {
                case "Success":
                    try {
                        //add you loading or delays - ;-)
                        Node node = (Node) event.getSource();
                        Stage stage = (Stage) node.getScene().getWindow();
                        stage.setMaximized(true);
                        stage.close();
                        Scene scene = new Scene(FXMLLoader.load(getClass().getResource(URLScenes.mainScene)));
                        stage.setScene(scene);
                        stage.setFullScreen(true);
                        stage.setResizable(true);
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {

                    }
                    break;
                case "Error":
                    a.setTitle("Authentification error:");
                    a.setHeaderText("Please check your (Username|Email) - Password combinations");
                    a.setContentText("ReactorsFX");
                    a.show();
                    break;
                case "Error1":
                    a.setTitle("Missing informations:");
                    a.setHeaderText("Please provide your login credentials");
                    a.setContentText("ReactorsFX");
                    a.show();
                    break;
            }
        }
    }
}
