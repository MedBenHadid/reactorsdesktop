package sharedAppPackage;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.catalina.realm.MessageDigestCredentialHandler;

import static javafx.fxml.FXMLLoader.load;


public class Main extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws Exception {
        // SINGELTON
        // User newUser = User.getInstance();
        // System.out.println(newUser);
        // SINGELTON

        MessageDigestCredentialHandler credentialHandler = new MessageDigestCredentialHandler();
        credentialHandler.setAlgorithm("SHA-512");
        credentialHandler.setSaltLength(0);
        credentialHandler.setIterations(13);
        credentialHandler.mutate("1ac2620f");


        Parent root = load(getClass().getResource("/sharedAppPackage/scenes/login/fxml/Login.fxml"));

        stage.initStyle(StageStyle.DECORATED);


        stage.setTitle("ReactorsFX : La bénévolat en desktop");

        // Louled ken fadetkom set them both to false
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
