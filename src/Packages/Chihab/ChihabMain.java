package Packages.Chihab;

import Main.Entities.UserSession;
import Packages.Chihab.Controllers.AssociationsBackofficeController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.SQLException;

public class ChihabMain extends Application {

    @Override
    public void start(Stage stage) throws SQLException {
        if(UserSession.login("azeaze@po.com","1ac2620f")) {
            AssociationsBackofficeController root = new AssociationsBackofficeController();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("ReactorsFX : La bénévolat en desktop");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}
