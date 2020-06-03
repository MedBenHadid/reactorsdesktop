package Main;

import Packages.Chihab.Controllers.AssociationCreateController;
import SharedResources.URLScenes;
import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthLayout extends Application implements Initializable {
    @FXML private Label side;
    @FXML private JFXButton loginButton, registerButton;
    @FXML private VBox mainVBox;
    @FXML private ImageView logo;
    private final Parent login = FXMLLoader.load(getClass().getResource(URLScenes.login));
    private final AssociationCreateController register = new AssociationCreateController();
    private final FadeTransition ft = new FadeTransition(Duration.millis(1500));
    private double xOffset = 0;
    private double yOffset = 0;

    public AuthLayout() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        RotateTransition trans = new RotateTransition(Duration.seconds(2), logo);
        ft.setNode(side);
        ft.setFromValue(0.1);
        ft.setToValue(0.4);
        ft.setCycleCount(20);
        ft.setAutoReverse(true);
        ft.play();
        mainVBox.getChildren().setAll(login);
        registerButton.setOnMouseClicked(mouseEvent -> mainVBox.getChildren().setAll(register));
        loginButton.setOnMouseClicked(mouseEvent -> mainVBox.getChildren().setAll(login));
        trans.setFromAngle(0.0);
        trans.setToAngle(10.0);
        trans.setCycleCount(RotateTransition.INDEFINITE);
        trans.setAutoReverse(true);
        trans.play();
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("ReactorsFX : La bénévolat en desktop");
        AnchorPane root = FXMLLoader.load(getClass().getResource(URLScenes.authLayout));
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
