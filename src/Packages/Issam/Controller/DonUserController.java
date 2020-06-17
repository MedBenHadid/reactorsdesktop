package Packages.Issam.Controller;

import Packages.Chihab.Services.AssociationService;
import Packages.Issam.Models.Don;
import Packages.Issam.Services.DonService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DonUserController implements Initializable {

    ObservableList<Don> as;
    @FXML
    private Pagination p;
    @FXML
    private TextField n;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**try {
            as = DonService.getInstace().readAll();
            p.setPageCount(10);
            p.setMaxPageIndicatorCount(as.size() / 10);

            p.setPageFactory(this::refresh);
        } catch (SQLException e) {
            e.printStackTrace();

        }*/




    }

    private VBox refresh(Integer integer) {
        VBox cont = new VBox();

        return cont;
    }

}
