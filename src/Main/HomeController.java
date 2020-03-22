package Main;

import Main.Entities.UserSession;
import SharedResources.URLScenes;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private Button btnLogout,btnAssAdmins,test;

    @FXML
    private Label labelUsername;

    @FXML
    private AnchorPane view, details, holderPane;

    public HomeController() {
    }

    @FXML
    private void handleButtonAction(MouseEvent event) {
        //refreshNodes();
        if (event.getSource().equals(btnLogout)) {
            if (UserSession.getInstace() != null) {
                UserSession.getInstace().cleanUserSession();
            }
            //add you loading or delays - ;-)
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            //stage.setMaximized(true);
            stage.close();
            Scene scene = null;
            try {
                scene = new Scene(FXMLLoader.load(getClass().getResource(URLScenes.login)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setResizable(false);
            stage.setFullScreen(false);
            stage.setMaximized(false);
            stage.setScene(scene);
            stage.show();
        } else if (event.getSource().equals(btnAssAdmins)){
            System.out.println("TEST");
        } else if (event.getSource().equals(test)) {
            try {
                AnchorPane test = FXMLLoader.load(getClass().getResource(URLScenes.authLayout));
                setNode(test);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setNode(AnchorPane node) {
        holderPane.getChildren().clear();
        holderPane.getChildren().add(node);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //labelUsername.setText(UserSession.getInstace().getUser().getUsername());
    }    
    

    
}
