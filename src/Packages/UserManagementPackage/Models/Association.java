package Packages.UserManagementPackage.Models;

import java.util.List;

public class Association {

    private int id;
    private User manager;
    private Category domaine;
    private List<Membership> memberships;
    private String nom,telephone,photoAgence,pieceJustificatif,rue,codePostal,lat,lon,description;

}
