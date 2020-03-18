package associationPackage.Entities;

import sharedAppPackage.models.User;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class Association {

    private int id;
    private User manager;
    private Category domaine;
    private List<Membership> memberships;
    private String nom,telephone,photoAgence,pieceJustificatif,rue,codePostal,lat,lon,description;

}
