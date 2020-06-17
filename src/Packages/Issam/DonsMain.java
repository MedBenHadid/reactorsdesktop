package Packages.Issam;

import Main.Entities.UserSession;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;


public class DonsMain extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        if(UserSession.login("issam@gmail.com","07480835")) {

            Parent root = FXMLLoader.load(getClass().getResource("/Packages/Issam/Scenes/mainUser.fxml"));
            primaryStage.initStyle(StageStyle.UNDECORATED);


            root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            root.setOnMouseDragged(event -> {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            });


            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

        }
   }

    public static void main(String[] args) {


        launch(args);
    }

}
