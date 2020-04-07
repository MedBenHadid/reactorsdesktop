package Packages.Nasri.ui.controllers.admin;

import Packages.Nasri.entities.HebergementRequest;
import Packages.Nasri.enums.CivilStatus;
import Packages.Nasri.enums.HebergementStatus;
import Packages.Nasri.services.ServiceHebergementRequest;
import Packages.Nasri.utils.Helpers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.tomcat.jni.Local;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddHebergementRequestController implements Initializable {

    @FXML
    TextField userIdInput;
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
        HebergementRequest hebergementRequest = new HebergementRequest();
        hebergementRequest.setUserId(Integer.parseInt(userIdInput.getText()));
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

    private void close() {
        final Stage stage = (Stage)userIdInput.getScene().getWindow();
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
