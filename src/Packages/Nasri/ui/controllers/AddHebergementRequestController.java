package Packages.Nasri.ui.controllers;

import Packages.Nasri.entities.HebergementRequest;
import Packages.Nasri.enums.CivilStatus;
import Packages.Nasri.enums.HebergementStatus;
import Packages.Nasri.services.ServiceHebergementRequest;
import Packages.Nasri.utils.Helpers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AddHebergementRequestController implements Initializable {
    @FXML
    TextField nameInput;
    @FXML
    TextArea descriptionInput;
    @FXML
    TextField regionInput;
    @FXML
    TextField nativeCountryInput;
    @FXML
    TextField passportInput;
    @FXML
    ComboBox civilStatusInput;
    @FXML
    TextField numberChildrenInput;
    @FXML
    DatePicker arrivalDateInput;
    @FXML
    TextField telephoneInput;
    @FXML
    ComboBox anonymityInput;

    @FXML
    protected void addButton() {
        System.out.println(arrivalDateInput.getValue());
//        return;

        if (!inputIsValid()) {
            return;
        }


        HebergementRequest hebergementRequest = new HebergementRequest();

        // 72 is a temporary value of user_id
        // users module is not integrated yet
        hebergementRequest.setUserId(72);


        hebergementRequest.setName(nameInput.getText());
        hebergementRequest.setDescription(descriptionInput.getText());
        hebergementRequest.setRegion(regionInput.getText());
        hebergementRequest.setNativeCountry(nativeCountryInput.getText());
        hebergementRequest.setPassportNumber(passportInput.getText());
        hebergementRequest.setCivilStatus(civilStatusInput.getValue().equals("Marié(e)") ? CivilStatus.Married : CivilStatus.Single);
        hebergementRequest.setChildrenNumber(Integer.parseInt(numberChildrenInput.getText()));

        Date arrivalDate = Date.valueOf(arrivalDateInput.getValue());
        hebergementRequest.setArrivalDate(Helpers.convertDateToLocalDateTime(arrivalDate));
        hebergementRequest.setCreationDate(LocalDateTime.now());
        hebergementRequest.setState(HebergementStatus.inProcess);
        hebergementRequest.setTelephone(telephoneInput.getText());
        hebergementRequest.setAnonymous(anonymityInput.getValue().equals("Oui") ? true : false);


        ServiceHebergementRequest serviceHebergementRequest = new ServiceHebergementRequest();
        serviceHebergementRequest.add(hebergementRequest);
        close();
    }

    private boolean inputIsValid() {
        // checking if the name field is empty
        if (this.nameInput.getText().trim().isEmpty()) {
            //display error dialog
            displayErrorDialog("Champ de saisie 'Nom de demandeur' est vide");
            return false;
        }

        // checking if the description field is empty
        if (this.descriptionInput.getText().trim().isEmpty()) {
            //display error dialog
            displayErrorDialog("Champ de saisie 'Description' est vide");
            return false;
        }

        // checking if the region field is empty
        if (this.regionInput.getText().trim().isEmpty()) {
            //display error dialog
            displayErrorDialog("Champ de saisie 'Region' est vide");
            return false;
        }

        // checking if the nativeCountry field is empty
        if (this.nativeCountryInput.getText().trim().isEmpty()) {
            displayErrorDialog("Champ de saisie 'Pays d'origine' est vide");
            return false;
        }

        // checking if the passport field is empty
        if (this.passportInput.getText().trim().isEmpty()) {
            displayErrorDialog("Champ de saisie 'Numero de passport' est vide");
            return false;
        }

        // checking if the numberRooms field is < 1 || not numeric
        try {
            int numberChildren = Integer.parseInt(numberChildrenInput.getText());
            if (numberChildren < 1) {
                displayErrorDialog("Valeure inferieure ou égale a 0 pour le champ de saisie 'Nombre d'enfants'");
                return false;
            }
        } catch (NumberFormatException e) {
            displayErrorDialog("Valeure incorrect pour le champ de saisie 'Nombre d'enfants'");
            return false;
        }

        // checking if the arrivalDate field is empty
        if (this.arrivalDateInput.getValue() == null) {
            displayErrorDialog("Champ de saisie 'Date d'arrivée' est vide");
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

    @FXML
    protected void resetButton() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        anonymityInput.getItems().add("Oui");
        anonymityInput.getItems().add("Non");
        anonymityInput.getSelectionModel().selectFirst();

        civilStatusInput.getItems().add("Marié(e)");
        civilStatusInput.getItems().add("Celibataire");
        civilStatusInput.getSelectionModel().selectFirst();
    }
}
