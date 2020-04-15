package Main.SubScenes.Login;

import Main.Services.UserService;
import SharedResources.URLScenes;
import SharedResources.Utils.Connector.ConnectionUtil;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private final Connection con;
    private final UserService userService = UserService.getInstace();
    @FXML
    private Label lblErrors;
    @FXML
    private JFXTextField txtUsername;
    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private Button btnSignin;

    public LoginController() {
        this.con = ConnectionUtil.conDB().conn;
    }

    private String logIn() {
        String status = "Success";
        String credential = txtUsername.getText();
        String password = txtPassword.getText();
        if (credential.isEmpty() || password.isEmpty()) {
            setLblError(Color.TOMATO, "Empty credentials");
            status = "Error1";
        } else {
            try {
                if (!userService.validateLogin(credential, password)) {
                    setLblError(Color.TOMATO, "Enter Correct (Email/Username)-/Password");
                    status = "Error";
                } else {
                    setLblError(Color.GREEN, "Login Successful..Redirecting..");
                }
            } catch (SQLException ex) {
                status = "Exception";
            }
        }
        return status;
    }

    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
    }

    @FXML
    public void handleButtonAction(MouseEvent event) {
        if (event.getSource() == btnSignin) {
            String status = logIn();
            switch (status) {
                case "Success":
                    try {
                        //add you loading or delays - ;-)
                        Node node = (Node) event.getSource();
                        Stage stage = (Stage) node.getScene().getWindow();
                        stage.setMaximized(true);
                        stage.close();
                        Scene scene = new Scene(FXMLLoader.load(getClass().getResource(URLScenes.mainScene)));
                        stage.setScene(scene);
                        stage.setFullScreen(true);
                        stage.setResizable(true);
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {

                    }
                    break;
                case "Error":
                    // TODO : JFX Dialog
                    break;
                case "Error1":
                    // TODO : JFX Dialog
                    break;
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (con == null) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Server Error : Check");
        } else {
            lblErrors.setTextFill(Color.GREEN);
            lblErrors.setText("Server is up : Good to go");
        }
    }
}
