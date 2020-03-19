import SharedResources.Urls.Scenes.URLScenes;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import static javafx.fxml.FXMLLoader.load;


public class Main extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = load(getClass().getResource(URLScenes.authScene));
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
    public static void main(String[] args) {
        launch(args);
    }
}
