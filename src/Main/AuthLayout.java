package Main;

import SharedResources.URLScenes;
import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.fxml.FXMLLoader.load;

public class AuthLayout extends Application implements Initializable {
    private final Parent forgot = null;
    @FXML
    private Label side;
    @FXML
    private JFXButton loginButton, registerButton;
    @FXML
    private VBox mainVBox;
    private Parent login = null;
    private Parent register = null;
    private double xOffset = 0;
    private double yOffset = 0;

    public AuthLayout() {
        try {
            this.login = FXMLLoader.load(getClass().getResource(URLScenes.login));
            this.register = FXMLLoader.load(getClass().getResource(URLScenes.register));
            //this.forgot=FXMLLoader.load(getClass().getResource(URLScenes.forgotPW));
        } catch (IOException e) {
            e.getCause();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FadeTransition ft = new FadeTransition(Duration.millis(1500));
        ft.setNode(side);
        ft.setFromValue(0.1);
        ft.setToValue(0.4);
        ft.setCycleCount(20);
        ft.setAutoReverse(true);
        ft.play();
        mainVBox.getChildren().setAll(login);
        registerButton.setOnMouseClicked(mouseEvent -> mainVBox.getChildren().setAll(register));
        loginButton.setOnMouseClicked(mouseEvent -> mainVBox.getChildren().setAll(login));
    }

    @Override
    public void start(Stage stage) throws IOException {
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
        stage.setScene(new Scene(root));
        stage.show();
    }

}
