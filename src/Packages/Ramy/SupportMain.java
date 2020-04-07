package Packages.Ramy;


import Packages.Ramy.Models.Requete;
import Packages.Ramy.Services.RequeteService;
import SharedResources.URLScenes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;

import static javafx.fxml.FXMLLoader.load;

public class SupportMain extends Application {

    public static void main(String[] args){
        launch(args);
//        RequeteService ros = new RequeteService();
//        Requete r1 = new Requete(72,1,"prob","decri",new Date(2020,1,1),1,2);
//        r1.setId(7);
//
//        for (Requete obj : ros.display()) {
//            System.out.println(obj);
//        }
//        System.out.println("hello");
    }
    private final URL scene = getClass().getResource(URLScenes.Requetefxml);


    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(scene);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
