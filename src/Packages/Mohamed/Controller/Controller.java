package Packages.Mohamed.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class Controller implements Initializable {
    @FXML
    private Label labelTest;
    @java.lang.Override
    public void initialize(java.net.URL url, java.util.ResourceBundle resourceBundle) {
        labelTest.textProperty().setValue("Nik om l viris");
    }
}
