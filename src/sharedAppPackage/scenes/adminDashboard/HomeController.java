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
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
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

    @FXML
    private JFXButton btnUsers;

    @FXML
    private JFXButton btnMembers;

    @FXML
    private JFXButton btnAssAdmins;

    @FXML
    private Label labelUsername;

    private Node [] nodes;
    public HomeController() {
    }

    @FXML
    private void handleButtonAction(MouseEvent event) {
        //refreshNodes();
        if (event.getSource().equals(btnLogout)) {
            if(UserSession.getInstace()!=null)
            UserSession.getInstace().cleanUserSession();
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            try {
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("./../login/Login.fxml")));
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (event.getSource().equals(btnAssAdmins)){
            System.out.println("TEST");
        }


    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //btnUsers.ripplerFillPropert(new ObjectProperty<Paint>());
       // labelUsername.setText(labelUsername.getText()+" "+UserSession.getInstace().getUser().getUsername());
        //refreshNodes();
    }    
    
    private void refreshNodes()
    {
        pnl_scroll.getChildren().clear();
        List<User> users = UserService.getInstace().listUsers();
        nodes = new  Node[users.size()];


        for(int i = 0; i<users.size(); i++)
        {
            try {
                nodes[i] = FXMLLoader.load(getClass().getResource("users/Item.fxml"));
                pnl_scroll.getChildren().add(nodes[i]);
                //pnl_scroll.getChildren().removeAll();
                //pnl_scroll.getChildren().add(new TreeTableView<User>());
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  
    }
    
}
