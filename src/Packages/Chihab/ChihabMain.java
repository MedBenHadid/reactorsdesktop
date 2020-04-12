package Packages.Chihab;

import SharedResources.URLScenes;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static javafx.fxml.FXMLLoader.load;

public class ChihabMain extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = load(getClass().getResource(URLScenes.associationSuperAdminDashboard));
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("ReactorsFX : La bénévolat en desktop");
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
