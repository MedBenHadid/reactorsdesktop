package Packages.Nasri.ui.controllers;

import Packages.Nasri.entities.HebergementOffer;
import Packages.Nasri.enums.HebergementStatus;
import Packages.Nasri.services.ServiceHebergementOffer;
import Packages.Nasri.utils.Helpers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddHebergementOfferController implements Initializable {
    @FXML
    TextArea descriptionInput;
    @FXML
    TextField numberRoomsInput;
    @FXML
    TextField governoratInput;
    @FXML
    TextField durationInput;
    @FXML
    TextField telephoneInput;

    FileChooser imageFileChooser = new FileChooser();
    String imageFilePath = "";

    @FXML
    protected void brownForPictureButton() {
        File selectedFile = imageFileChooser.showOpenDialog(numberRoomsInput.getScene().getWindow());
        this.imageFilePath = selectedFile.getAbsolutePath();
    }
    @FXML
    protected void resetButton() {
        descriptionInput.setText("");
        numberRoomsInput.setText("");
        governoratInput.setText("");
        durationInput.setText("");
        telephoneInput.setText("");
    }
    @FXML
    protected void addButton() {

        if (!inputIsValid()) {
            return;
        }

        HebergementOffer hebergementOffer = new HebergementOffer();

        // TMP_USER_ID is a temporary value of user_id
        // users module is not integrated yet
        hebergementOffer.setUserId(Helpers.TMP_USER_ID);


        hebergementOffer.setDescription(descriptionInput.getText());
        hebergementOffer.setNumberRooms(Integer.parseInt(numberRoomsInput.getText()));
        hebergementOffer.setGovernorat(governoratInput.getText());
        hebergementOffer.setDuration(Integer.parseInt(durationInput.getText()));
        hebergementOffer.setTelephone(telephoneInput.getText());
        hebergementOffer.setCreationDate(LocalDateTime.now());
        hebergementOffer.setState(HebergementStatus.inProcess);
        hebergementOffer.setImage(imageFilePath);
        ServiceHebergementOffer serviceHebergementOffer = new ServiceHebergementOffer();
        serviceHebergementOffer.add(hebergementOffer);
        close();
    }

    private boolean inputIsValid() {
        // checking if the description field is empty
        if (this.descriptionInput.getText().trim().isEmpty()) {
            //display error dialog
            displayErrorDialog("Champ de saisie 'Description' est vide");
            return false;
        }

        // checking if the numberRooms field is < 1 || not numeric
        try {
            int numberRooms = Integer.parseInt(numberRoomsInput.getText());
            if (numberRooms < 1) {
                displayErrorDialog("Valeure inferieure ou égale a 0 pour le champ de saisie 'Nombre de chambres'");
                return false;
            }
        } catch (NumberFormatException e) {
            displayErrorDialog("Valeure incorrect pour le champ de saisie 'Nombre de chambres'");
            return false;
        }

        // checking if the governorat field is empty
        if (this.governoratInput.getText().trim().isEmpty()) {
            displayErrorDialog("Champ de saisie 'Governorat' est vide");
            return false;
        }

        // checking if the durationInput field is < 1 || not numeric
        try {
            int duration = Integer.parseInt(durationInput.getText());
            if (duration < 1) {
                displayErrorDialog("Valeure inferieure ou égale a 0 pour le champ de saisie 'Durée (mois)'");
                return false;
            }
        } catch (NumberFormatException e) {
            displayErrorDialog("Valeure incorrect pour le champ de saisie 'Durée (mois)'");
            return false;
        }

        // checking if the phoneNUmber field is empty || incorrect
        if (this.telephoneInput.getText().trim().isEmpty()) {
            displayErrorDialog("Champ de saisie 'Téléphone' est vide");
            return false;
        } else if (!Helpers.phoneNumberIsValid(this.telephoneInput.getText())) {
            displayErrorDialog("Valeure incorrect pour le champ de saisie 'Téléphone'");
            return false;
        }

        // checking if the image is selected
        if (this.imageFilePath.isEmpty()) {
            displayErrorDialog("Aucune image selectionée");
            return false;
        }

        return true;
    }

    private void displayErrorDialog(String errorMessage) {
        Alert alert =
                new Alert(Alert.AlertType.ERROR, errorMessage);
        alert.showAndWait();
    }

    private void close() {
        final Stage stage = (Stage)descriptionInput.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("xyz");
    }
}
