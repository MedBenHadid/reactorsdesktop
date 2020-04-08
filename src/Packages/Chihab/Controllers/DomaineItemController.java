package Packages.Chihab.Controllers;

import Packages.Chihab.Models.Category;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class DomaineItemController implements Initializable {
    @FXML
    private WebView imageWebView;
    @FXML
    private Label nameLabel;
    @FXML
    private Label descriptionLabel;

    private Category c;

    public DomaineItemController(Category c) {
        this.c = c;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameLabel.setText(c.getNom());
        descriptionLabel.setText(c.getDescription());
        imageWebView.getEngine().load(this.getClass().getResource("/Packages/Chihab/Scenes/WebView/domaineAPI.html").toString());
        imageWebView.getEngine().getLoadWorker().stateProperty().addListener(
                (ChangeListener<? super Worker.State>) (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        imageWebView.getEngine().executeScript("initCarousel(" + c.getNom() + ")");
                    }
                }
        );
    }
}
