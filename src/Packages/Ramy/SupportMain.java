package Packages.Ramy;


import Packages.Ramy.Models.Requete;
import Packages.Ramy.Models.Rponse;
import Packages.Ramy.Services.RequeteService;
import Packages.Ramy.Services.RponseService;
import SharedResources.URLScenes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;

import static javafx.fxml.FXMLLoader.load;


//extends Application ***********

public class SupportMain  extends Application {

    public static void main(String[] args){
        launch(args);
        //java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        //RponseService ros = new RponseService();
        //Requete r1 = new Requete(72,1,"prob","decri",new Date(2020,1,1),1,2);
        //Rponse r1 = new Rponse(72,9,"sujet","reponse",new Date(2020,1,1),2);
        //r1.setId(7);
        //ros.add(r1);

        /*for (Rponse obj : ros.display()) {
            System.out.println(obj);
            System.out.println("hello");
        }
        System.out.println("hello");*/
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
