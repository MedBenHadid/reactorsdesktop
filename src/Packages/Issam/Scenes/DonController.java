package Packages.Issam.Scenes;

import Packages.Chihab.Models.Category;
import Packages.Issam.Services.DonService;
import Packages.Issam.Models.Don;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
// 1 : --module-path "C:\javafx-sdk-11.0.2\lib" --add-modules javafx.controls,javafx.fxml --add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED
// 2 : --add-exports=javafx.graphics/com.sun.javafx.util=ALL-UNNAMED
public class DonController implements Initializable {
    @FXML
    private TableView<Don> donTV;
    @FXML
    private TableColumn<Don ,Don> deleteOption;
    @FXML
    private  TableColumn<Don , String>  titleCol , descCol , addrCol ,phoneCol ;
    @FXML
    private TableColumn<Category, Number> idCol;

    public void createDon(Event eve){
        System.out.println("ok");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList donList = FXCollections.observableArrayList();
        try {
            donTV.getItems().addAll(DonService.getInstace().readAll());
        }
        catch (SQLException e) {
            Logger.getLogger(
                    DonController.class.getName()).log(
                    Level.INFO, null, e
            );
            System.out.println("ok");
            showDialog(Alert.AlertType.ERROR, "", "Connexion au serveur échoué:", "Veuillez assurer la bonne connexion");
        }
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        addrCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

    }

    void showDialog(Alert.AlertType t, String title, String header, String context) {
        Alert a = new Alert(t);
        a.setTitle(title);
        a.setHeaderText(header);
        a.setContentText(context);
        a.showAndWait();
    }
}
