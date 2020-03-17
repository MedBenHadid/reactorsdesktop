/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sharedAppPackage.scenes.adminDashboard;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sharedAppPackage.models.User;
import sharedAppPackage.models.UserSession;
import sharedAppPackage.services.UserService;

public class HomeController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private VBox pnl_scroll;

    @FXML
    private JFXButton btnLogout;

    public HomeController() {
    }

    @FXML
    private void handleButtonAction(MouseEvent event) {
        refreshNodes();
        if (event.getSource().equals(btnLogout)) {
            UserSession.getInstace().cleanUserSession();
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Scene scene = null;
            try {
                scene = new Scene(FXMLLoader.load(getClass().getResource("./../login/Login.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setScene(scene);
            stage.show();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
         refreshNodes();
    }    
    
    private void refreshNodes()
    {
        pnl_scroll.getChildren().clear();
        
        Node [] nodes = new  Node[15];

        List<User> users = UserService.getInstace().listUsers();
        for(int i = 0; i<users.size(); i++)
        {
            try {
                nodes[i] = FXMLLoader.load(getClass().getResource("users/Item.fxml"));

                pnl_scroll.getChildren().add(nodes[i]);
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  
    }
    
}
