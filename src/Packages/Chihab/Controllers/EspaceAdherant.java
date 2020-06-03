package Packages.Chihab.Controllers;
import Main.Entities.UserSession;
import Packages.Chihab.Models.Entities.Membership;
import Packages.Chihab.Services.MembershipService;
import SharedResources.URLScenes;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.ResourceBundle;

public class EspaceAdherant implements Initializable {
    @FXML private VBox managerVBOX;
    @FXML private VBox memberVBOX;

    public EspaceAdherant() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)  {
        try {
            assert UserSession.getInstace() != null;
            MembershipService.getInstace().searchAffiliateIdByMemberIdReturnArray(UserSession.getInstace().getUser().getId()).stream()
            .sorted(Comparator.comparing(Membership::getStatus)).forEach(e->{
                if(e.getStatus().equals(Membership.ACCEPTED)){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(URLScenes.memberShipItem));
                    MemberItemController controller = new MemberItemController(e);
                    loader.setController(controller);
                    HBox a = null;
                    try {
                        a = loader.load();
                    } catch (IOException ee) {
                        ee.getCause();
                    }
                    memberVBOX.getChildren().add(a);
                }
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
