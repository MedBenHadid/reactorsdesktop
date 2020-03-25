package Packages.Chihab.Scenes;

import Packages.Chihab.Models.Association;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AssociationProfileController implements Initializable {
    @FXML
    Label firstName;
    @FXML
    Label lastName;
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
        firstName.setText(passedAssociation.getNom());
        lastName.setText(passedAssociation.getVille());
    }

    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                    AssociationProfileController.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }
}
