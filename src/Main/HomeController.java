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
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

    @FXML
    private ImageView userImage;

    private Node [] nodes;
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
                scene = new Scene(FXMLLoader.load(getClass().getResource(URLScenes.authLayout)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setScene(scene);
            stage.show();
        } else if (event.getSource().equals(btnAssAdmins)){
            System.out.println("TEST");
        }


    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelUsername.setText(labelUsername.getText()+" "+UserSession.getInstace().getUser().getUsername());
        Image profilePicture;
        try {
            //profilePicture = SwingFXUtils.toFXImage(ImageIO.read(new URL(URLServer.userImage+UserSession.getInstace().getUser().getProfile().getImage()).openStream()),null) ;
            //userImage.setImage();
            BufferedImage d = ImageIO.read(new URL(URLServer.userImage+UserSession.getInstace().getUser().getProfile().getImage()));
            BufferedImage tempCard = d.getSubimage( 50, 50, 50, 50 );
            Image card = SwingFXUtils.toFXImage(tempCard, null );
            userImage.setImage(card);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                nodes[i] = FXMLLoader.load(getClass().getResource("LoggedInStage/users/Item.fxml"));
                pnl_scroll.getChildren().add(nodes[i]);
                //pnl_scroll.getChildren().removeAll();
                //pnl_scroll.getChildren().add(new TreeTableView<User>());
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  
    }
    
}
