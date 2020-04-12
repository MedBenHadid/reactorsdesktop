package Packages.Nasri.ui.controllers;

import Packages.Nasri.entities.HebergementOffer;
import Packages.Nasri.enums.HebergementStatus;
import Packages.Nasri.services.ServiceHebergementOffer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
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
    String imageFilePath;

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
        HebergementOffer hebergementOffer = new HebergementOffer();

        // 72 is a temporary value of user_id
        // users module is not integrated yet
        hebergementOffer.setUserId(72);


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

    private void close() {
        final Stage stage = (Stage)descriptionInput.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("xyz");
    }
}