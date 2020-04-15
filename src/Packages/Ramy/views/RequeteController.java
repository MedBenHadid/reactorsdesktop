package Packages.Ramy.views;

import Packages.Ramy.Models.Requete;
import Packages.Ramy.Models.Rponse;
import Packages.Ramy.Services.RequeteService;
import Packages.Ramy.Services.RponseService;
import SharedResources.Utils.Connector.ConnectionUtil;
import com.jfoenix.controls.*;
import com.sun.mail.imap.protocol.Item;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RequeteController implements Initializable {

    @FXML
    private TableView<Requete> catTV;
    @FXML
    private TableColumn<Requete, Requete> deleteOption,addrep;
    @FXML
    private TableColumn<Requete, String> nomCol, descCol;
    @FXML
    private TableColumn<Requete, Number> idCol, statutCol, typeCol;
    @FXML
    private TableColumn<Requete, Date> dateCol;
    @FXML
    private javafx.scene.control.TextArea description;
    @FXML
    private javafx.scene.control.TextField input, nom;
    @FXML
    private TabPane tabPane;
    @FXML
    private Label size;
    @FXML
    private Tab newTab;
    @FXML
    private ComboBox<String> combobox;
    @FXML
    private StackPane rootStack;
    ObservableList<String> listt = FXCollections.observableArrayList("Question générale sur Reactors","J'ai un problem concernant cette application","Autre");
    ObservableList<String> l = FXCollections.observableArrayList("Question générale sur Reactors","J'ai un problem concernant cette application","Autre");
    ObservableList<Requete> oblist = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = ConnectionUtil.conDB().getConnection();
        combobox.setItems(listt);

        try {
            ResultSet rs = connection.createStatement().executeQuery("select * from requete");
            while (rs.next()){
                oblist.add(new Requete(rs.getInt("id"),rs.getString("Sujet"),rs.getString("Description"),rs.getDate("DernierMAJ"),rs.getInt("Statut"),rs.getInt("Type")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //System.out.println("Hello world");



        nomCol.setCellValueFactory(new PropertyValueFactory<>("sujet"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id")); 
        statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("derniermaj"));
        statutCol.setCellFactory(column -> {
            TableCell<Requete, Number> cell = new TableCell<Requete, Number>() {
                @Override
                protected void updateItem(Number statut, boolean empty) {
                    super.updateItem(statut, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        Label label = new Label();
                        label.setText(statut.intValue() == 0 ? "non Resolu" : "Resolu");
                        setGraphic(label);
                    }
                }
            };

            return cell;
        });
        typeCol.setCellFactory(column -> {
            TableCell<Requete, Number> cell = new TableCell<Requete, Number>() {
                @Override
                protected void updateItem(Number statut, boolean empty) {
                    super.updateItem(statut, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        Label label = new Label();
                        label.setText(statut.intValue() == 2 ? "J'ai un problem concernant cette application" : statut.intValue() == 1 ?"Question générale sur Reactors" : "Autre");
                        setGraphic(label);
                    }
                }
            };

            return cell;
        });




        catTV.setItems(oblist);

        /*typeCol.setCellFactory(ComboBoxTableCell.forTableColumn(
                "Question générale sur Reactors","J'ai un problem concernant cette application","Autre"
        ));
        typeCol.setOnEditCommit(requeteStringCellEditEvent -> {
            Requete r = catTV.getItems().get(requeteStringCellEditEvent.getTablePosition().getRow());
            if (requeteStringCellEditEvent.getOldValue()!=requeteStringCellEditEvent.getNewValue()) {
                System.out.println(requeteStringCellEditEvent.getNewValue());
            }
        });*/
        //add2
        addrep.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        addrep.setCellFactory(param -> new TableCell<>() {
            private final JFXButton repButton = new JFXButton("Repondre");

            @Override
            protected void updateItem(Requete cat, boolean empty) {
                super.updateItem(cat, empty);
                if (cat == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(repButton);
                repButton.setOnAction(event -> {
                    JFXDialogLayout layout = new JFXDialogLayout();
                    //layout.setHeading(new Text("Dissaproving association "+a.getNom()));
                    VBox v = new VBox();
                    JFXTextField tf = new JFXTextField();
                    tf.setPromptText("Sujet");
                    JFXTextArea tr = new JFXTextArea();
                    tr.setPromptText("Ecrire la reponse");
                    v.getChildren().addAll(tf,tr);
                    //tr.getValidators().add(new RegexValidator("La raison doit etre comprise entre 5 et 255","^[\\d\\w]{5,255}"));
                   //tr.setOnKeyTyped(e->tr.validate());
                    layout.setBody(v);
                    JFXDialog dialog = new JFXDialog(rootStack,layout,JFXDialog.DialogTransition.CENTER);
                    JFXButton verify = new JFXButton("Envoyer");
                    JFXButton close = new JFXButton("Fermer");
                    close.getStyleClass().addAll("jfx-button-error");
                    close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> dialog.close());
                    verify.disableProperty().bind(Bindings.createBooleanBinding(() -> !tr.textProperty().get().matches("^[\\d\\w\\s]{5,255}"),tr.textProperty()));
                    verify.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
                        Rponse r = new Rponse();
                        r.setUser(72);
                        r.setRating(2);
                        r.setRequete(cat.getId());
                        r.setSujet(tf.getText());
                        r.setRep(tr.getText());
                        java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
                        r.setDate( sqlDate);
                        System.out.println(r.getRequete());
                        try {
                            RponseService rep = new RponseService();
                            System.out.println(r.getId());
                            rep.add(r);
                            int l = rep.getidrep(r) ;
                            r.setId(l);
                            RequeteService req = new RequeteService();
                            System.out.println(r.getId());
                            Requete reqq = new Requete(72,r.getId(),"prob","decri",sqlDate,1,2);
                            reqq.setId(cat.getId());
                            req.updaterponse(reqq);

                        } finally {
                            dialog.close();
                            JFXDialogLayout l = new JFXDialogLayout();
                            //l.setHeading(new Text(a.getNom()+" : Approval"));
                            l.setBody(new Label("Reponse envoyer"));
                            JFXDialog d = new JFXDialog(rootStack,l,JFXDialog.DialogTransition.CENTER);
                            JFXButton fermer = new JFXButton("Fermer");
                            fermer.addEventHandler(MouseEvent.MOUSE_CLICKED,e->d.close());
                            l.setActions(fermer);
                            d.show();
                        }
                    });
                    layout.setActions(close,verify);
                    dialog.show();
                });
            }
        });

        //delete
        deleteOption.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteOption.setCellFactory(param -> new TableCell<>() {
            private final JFXButton deleteButton = new JFXButton("Supprimer");

            @Override
            protected void updateItem(Requete cat, boolean empty) {
                super.updateItem(cat, empty);
                if (cat == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationDialog.setTitle("Suppression");
                    confirmationDialog.setHeaderText("Vous aller supprimer la Requete " + cat.getSujet() + "!");
                    confirmationDialog.setContentText("Etes vous sure?");
                    Optional<ButtonType> confirmationResult = confirmationDialog.showAndWait();
                    if (confirmationResult.isPresent())
                        if (confirmationResult.get() == ButtonType.OK) {
                            RequeteService req = new RequeteService();
                            req.delete(cat);
                            catTV.getItems().remove(cat);
                            showDialog(Alert.AlertType.CONFIRMATION, "", "", "Requete supprimée !!");
                        }
                });
            }
        });
    }
    /*public void resetButton(){
        nom.setText("");
        description.setText("");
    }*/
    public void ajoutButton(){
        Requete req = new Requete();
        req.setUser(72);
        req.setDescription(description.getText());
        req.setSujet(nom.getText());
        if(combobox.getValue() == "Question générale sur Reactors"){

        req.setType(1);}
        if(combobox.getValue() == "J'ai un problem concernant cette application"){

            req.setType(2);}
        if(combobox.getValue() == "Autre"){

            req.setType(3);}
        java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        req.setDerniermaj( sqlDate);
        req.setStatut(0);
        RequeteService servicereq = new RequeteService();
        servicereq.add(req);
        nom.setText("");
        description.setText("");
        /*Notifications.create()
                .darkStyle()
                .title("Title")
                .graphic(null) // sets node to display
                .hideAfter(Duration.seconds(10))
                .showWarning();*/
        /*Notifications notificationBuilder = Notifications.create().title("Requete envoyer !")
                .text("Votre Requete a ete recu par un administrateur.").graphic(null).hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_LEFT).onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        System.out.println("clicked on notification1");
                    }
                });
        notificationBuilder.show();*/
        //close();




    }
    private void close() {
        final Stage stage = (Stage)nom.getScene().getWindow();
        stage.close();
    }
    void showDialog(Alert.AlertType t, String title, String header, String context) {
        Alert a = new Alert(t);
        a.setTitle(title);
        a.setHeaderText(header);
        a.setContentText(context);
        a.showAndWait();
    }
    //refresh
    public void handlebuttonAction(ActionEvent event){
       /* Notifications.create()
                .darkStyle()
                .title("Title")
                .graphic(null) // sets node to display
                .hideAfter(Duration.seconds(10))
                .showWarning();*/
       oblist.clear();
        Connection connection = ConnectionUtil.conDB().getConnection();


        try {
            ResultSet rs = connection.createStatement().executeQuery("select * from requete");
            while (rs.next()){
                oblist.add(new Requete(rs.getInt("id"),rs.getString("Sujet"),rs.getString("Description"),rs.getDate("DernierMAJ"),rs.getInt("Statut"),rs.getInt("Type")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //System.out.println("Hello world");



        nomCol.setCellValueFactory(new PropertyValueFactory<>("sujet"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("derniermaj"));
        statutCol.setCellFactory(column -> {
            TableCell<Requete, Number> cell = new TableCell<Requete, Number>() {
                @Override
                protected void updateItem(Number statut, boolean empty) {
                    super.updateItem(statut, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        Label label = new Label();
                        label.setText(statut.intValue() == 0 ? "non Resolu" : "Resolu");
                        setGraphic(label);
                    }
                }
            };

            return cell;
        });
        typeCol.setCellFactory(column -> {
            TableCell<Requete, Number> cell = new TableCell<Requete, Number>() {
                @Override
                protected void updateItem(Number statut, boolean empty) {
                    super.updateItem(statut, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        Label label = new Label();
                        label.setText(statut.intValue() == 2 ? "J'ai un problem concernant cette application" : statut.intValue() == 1 ?"Question générale sur Reactors" : "Autre");
                        setGraphic(label);
                    }
                }
            };

            return cell;
        });




        catTV.setItems(oblist);

        /*typeCol.setCellFactory(ComboBoxTableCell.forTableColumn(
                "Question générale sur Reactors","J'ai un problem concernant cette application","Autre"
        ));
        typeCol.setOnEditCommit(requeteStringCellEditEvent -> {
            Requete r = catTV.getItems().get(requeteStringCellEditEvent.getTablePosition().getRow());
            if (requeteStringCellEditEvent.getOldValue()!=requeteStringCellEditEvent.getNewValue()) {
                System.out.println(requeteStringCellEditEvent.getNewValue());
            }
        });*/
        //add2
        addrep.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        addrep.setCellFactory(param -> new TableCell<>() {
            private final JFXButton repButton = new JFXButton("Repondre");

            @Override
            protected void updateItem(Requete cat, boolean empty) {
                super.updateItem(cat, empty);
                if (cat == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(repButton);
                repButton.setOnAction(event -> {
                    JFXDialogLayout layout = new JFXDialogLayout();
                    //layout.setHeading(new Text("Dissaproving association "+a.getNom()));
                    VBox v = new VBox();
                    JFXTextField tf = new JFXTextField();
                    tf.setPromptText("Sujet");
                    JFXTextArea tr = new JFXTextArea();
                    tr.setPromptText("Ecrire la reponse");
                    v.getChildren().addAll(tf,tr);
                    //tr.getValidators().add(new RegexValidator("La raison doit etre comprise entre 5 et 255","^[\\d\\w]{5,255}"));
                    //tr.setOnKeyTyped(e->tr.validate());
                    layout.setBody(v);
                    JFXDialog dialog = new JFXDialog(rootStack,layout,JFXDialog.DialogTransition.CENTER);
                    JFXButton verify = new JFXButton("Envoyer");
                    JFXButton close = new JFXButton("Fermer");
                    close.getStyleClass().addAll("jfx-button-error");
                    close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> dialog.close());
                    verify.disableProperty().bind(Bindings.createBooleanBinding(() -> !tr.textProperty().get().matches("^[\\d\\w\\s]{5,255}"),tr.textProperty()));
                    verify.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
                        Rponse r = new Rponse();
                        r.setUser(72);
                        r.setRating(2);
                        r.setRequete(cat.getId());
                        r.setSujet(tf.getText());
                        r.setRep(tr.getText());
                        java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
                        r.setDate( sqlDate);
                        System.out.println(r.getRequete());
                        try {
                            RponseService rep = new RponseService();
                            System.out.println(r.getId());
                            rep.add(r);
                            int l = rep.getidrep(r) ;
                            r.setId(l);
                            RequeteService req = new RequeteService();
                            System.out.println(r.getId());
                            Requete reqq = new Requete(72,r.getId(),"prob","decri",sqlDate,1,2);
                            reqq.setId(cat.getId());
                            req.updaterponse(reqq);

                        } finally {
                            dialog.close();
                            JFXDialogLayout l = new JFXDialogLayout();
                            //l.setHeading(new Text(a.getNom()+" : Approval"));
                            l.setBody(new Label("Reponse envoyer"));
                            JFXDialog d = new JFXDialog(rootStack,l,JFXDialog.DialogTransition.CENTER);
                            JFXButton fermer = new JFXButton("Fermer");
                            fermer.addEventHandler(MouseEvent.MOUSE_CLICKED,e->d.close());
                            l.setActions(fermer);
                            d.show();
                        }
                    });
                    layout.setActions(close,verify);
                    dialog.show();
                });
            }
        });

        //delete
        deleteOption.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteOption.setCellFactory(param -> new TableCell<>() {
            private final JFXButton deleteButton = new JFXButton("Supprimer");

            @Override
            protected void updateItem(Requete cat, boolean empty) {
                super.updateItem(cat, empty);
                if (cat == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationDialog.setTitle("Suppression");
                    confirmationDialog.setHeaderText("Vous aller supprimer la Requete " + cat.getSujet() + "!");
                    confirmationDialog.setContentText("Etes vous sure?");
                    Optional<ButtonType> confirmationResult = confirmationDialog.showAndWait();
                    if (confirmationResult.isPresent())
                        if (confirmationResult.get() == ButtonType.OK) {
                            RequeteService req = new RequeteService();
                            req.delete(cat);
                            catTV.getItems().remove(cat);
                            showDialog(Alert.AlertType.CONFIRMATION, "", "", "Requete supprimée !!");
                        }
                });
            }
        });
    }

    }






