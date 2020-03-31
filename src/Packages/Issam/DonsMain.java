package Packages.Issam;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class DonsMain extends Application {


    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/Packages/Issam/Scenes/Don.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Scene scene=new Scene(root, 400,450);
        primaryStage.setScene(scene);

        primaryStage.show();
   }

    public static void main(String[] args) {


        launch(args);
    }

}
