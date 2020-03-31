package Packages.Chihab;

import SharedResources.URLScenes;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import static javafx.fxml.FXMLLoader.load;

public class ChihabMain extends Application implements Initializable {


    @FXML
    public Group group;

    @FXML
    private AnchorPane side;
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FadeTransition ft = new FadeTransition(Duration.millis(15000));
        ft.setNode(side);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
        // TODO
        try {
            AnchorPane loginPane = FXMLLoader.load(getClass().getResource(URLScenes.login));
            setNode(loginPane);
        } catch (IOException e) {
            Logger.getLogger(
                    ChihabMain.class.getName()).log(
                    Level.SEVERE, null, e
            );
            e.printStackTrace();
        }

    }

    void setNode(Node node) {
        group.getChildren().clear();
        group.getChildren().add(node);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = load(getClass().getResource(URLScenes.associationCreate));
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("ReactorsFX : La bénévolat en desktop");
        // Louled ken fadetkom set them both to false
        // Set them true mba3d
        //stage.setAlwaysOnTop(true);
        //stage.setMaximized(true);
        //stage.setFullScreen(true);
        //stage.setResizable(false);
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
}
