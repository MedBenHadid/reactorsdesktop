package Packages.Ramy.Models;

import javax.management.Descriptor;
import java.sql.Date;

public class Requete {
    private int id ;
    private int user ;
    private int rponse ;
    private String sujet ;
    private String description ;
    private Date derniermaj ;
    private int statut ;
    private int type ;

    public Requete() { }

    public Requete(int id, String sujet, String description, Date derniermaj, int statut, int type) {
        this.id = id;
        this.sujet = sujet;
        this.description = description;
        this.derniermaj = derniermaj;
        this.statut = statut;
        this.type = type;
    }

    public Requete(int user, int rponse, String sujet, String description, Date derniermaj, int statut, int type) {
        this.user = user;
        this.rponse = rponse;
        this.sujet = sujet;
        this.description = description;
        this.derniermaj = derniermaj;
        this.statut = statut;
        this.type = type;
    }

    public Requete(int id, int user, int rponse, String sujet,  Date derniermaj, int statut, int type, String description) {
        this.id = id;
        this.user = user;
        this.rponse = rponse;
        this.sujet = sujet;
        this.description = description;
        this.derniermaj = derniermaj;
        this.statut = statut;
        this.type = type;
    }

    public java.sql.Date getDerniermaj() {
        return derniermaj;
    }

    public int getType() {
        return type;
    }

    public int getRponse() {
        return rponse;
    }

    public int getUser() {
        return user;
    }

    public int getStatut() {
        return statut;
    }

    public String getDescription() {
        return description;
    }

    public String getSujet() {
        return sujet;
    }

    public int getId() {
        return id;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public void setRponse(int rponse) {
        this.rponse = rponse;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDerniermaj(java.sql.Date derniermaj) {
        this.derniermaj = derniermaj;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id +
                ", Sujet='" + sujet + '\'' +
                ", requete='" + rponse + '\'' +
                ", Description='" + description + '\'' +
                ", Dernier MAJ='" + derniermaj + '\'' +
                ", Statut='" + statut + '\'' +
                ", Type='" + type + '\'' +
                '}';



    }
}
