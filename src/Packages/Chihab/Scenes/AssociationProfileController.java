package Packages.Chihab.Scenes;

import Packages.Chihab.Models.Association;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class AssociationProfileController implements Initializable {
    @FXML
    Label nameLabel, descriptionLabel;
    @FXML

    private Association passedAssociation;
    private Desktop desktop = Desktop.getDesktop();

    /**
     * Accepts an Association type and stores it to specific instance variables
     * in order to show its profile ( Preferably in a new TabPane )
     *
     * @param association
     */
    public AssociationProfileController(Association association) {
        this.passedAssociation = association;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameLabel.setText(passedAssociation.getNom());
        descriptionLabel.setText(passedAssociation.getDescription());
    }

}
