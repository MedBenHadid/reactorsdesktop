package Packages.Ramy.views;

import Main.Entities.UserSession;
import Packages.Ramy.Models.Rponse;
import Packages.Ramy.Services.RponseService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class RponseController implements Initializable {
    @FXML
    private javafx.scene.control.TextArea txtrponse, txtsujet;
    @FXML
    private Button sendbut;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void ajoutButton() {
        Rponse rep = new Rponse();
        rep.setUser(UserSession.getInstace().getUser().getId());
        rep.setRep(txtrponse.getText());
        rep.setSujet(txtsujet.getText());
        java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        rep.setDate(sqlDate);
        RponseService serrep = new RponseService();
        serrep.add(rep);
        close();


    }

    private void close() {
        final Stage stage = (Stage) txtrponse.getScene().getWindow();
        stage.close();
    }
}
