package associationPackage;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static javafx.fxml.FXMLLoader.load;

public class AssociationMemberMain extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = load(getClass().getResource("/sharedAppPackage/scenes/login/Login.fxml"));
        //        "/sharedAppPackage/scenes/login/fxml/Login.fxml"
        // "./scenes/adminDashboard/Home.fxml"
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
