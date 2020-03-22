package Main;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Main.Entities.User;
import Main.Entities.UserSession;
import Main.Services.UserService;
import Resources.URLScenes;
import Resources.URLServer;
import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeController implements Initializable {
    @FXML
    private Button btnLogout,btnAssAdmins,test;

    @FXML
    private Label labelUsername;

    @FXML
    private AnchorPane view,details;

    public HomeController() {
    }

    @FXML
    private void handleButtonAction(MouseEvent event) {
        //refreshNodes();
        if (event.getSource().equals(btnLogout)) {
            UserSession.getInstace().cleanUserSession();
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
        view.setClip((Node) node);
        //view.getChildren().clear();
        //view.getChildren().add(node);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //labelUsername.setText(UserSession.getInstace().getUser().getUsername());
    }    
    

    
}
