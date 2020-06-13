package Packages.Mohamed;

import Main.Entities.UserSession;
import SharedResources.URLScenes;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

import static javafx.fxml.FXMLLoader.load;

public class MissionsMain extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        if(UserSession.login("benhdid21@gmail.com","1ac2620f")) {

            Parent root = load(getClass().getResource(URLScenes.missionDashbord));
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
}
