package Packages.Nasri.tests;

import SharedResources.URLScenes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class RefugeeMain extends Application {
//    private final URL scene = getClass().getResource(URLScenes.refugeesDashboardMainScene);
private final URL scene = getClass().getResource(URLScenes.refugeesDashboardMainScene);


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(scene);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);

        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
