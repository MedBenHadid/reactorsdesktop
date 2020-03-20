package Main;

import Resources.URLScenes;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.fxml.FXMLLoader.load;

public class AuthLayout  extends Application implements Initializable {
    @FXML
    private AnchorPane container;

    private AnchorPane loginPane;
    private Parent root;
    @Override
    public void initialize(URL url, ResourceBundle rb){

        // TODO
        try {
            loginPane = FXMLLoader.load(getClass().getResource(URLScenes.login));
            setNode((Node)loginPane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setNode(Node node) {
        container.getChildren().clear();
        container.getChildren().add((Node) node);
        FadeTransition ft = new FadeTransition(Duration.millis(1500));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }


    private double xOffset = 0;
    private double yOffset = 0;
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = load(getClass().getResource(URLScenes.authLayout));
        stage.initStyle(StageStyle.DECORATED);


        stage.setTitle("ReactorsFX : La bénévolat en desktop");

        // Louled ken fadetkom set them both to false
        // Set them true mba3d
        stage.setAlwaysOnTop(false);
        stage.setMaximized(false);
        stage.setFullScreen(false);

        root.setOnMousePressed(event -> { xOffset = event.getSceneX();yOffset = event.getSceneY(); });
        root.setOnMouseDragged(event -> { stage.setX(event.getScreenX() - xOffset);stage.setY(event.getScreenY() - yOffset); });

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
