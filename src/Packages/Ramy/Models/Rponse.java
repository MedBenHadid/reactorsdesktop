package Packages.Ramy.Models;

import java.util.Date;

public class Rponse {
    private int id;
    private int user;
    private int requete;
    private String sujet;
    private String rep;
    private Date date;
    private int rating;

    public Rponse() {
    }

    public Rponse(int user, int requete, String sujet, String rep, Date date, int rating) {
        this.user = user;
        this.requete = requete;
        this.sujet = sujet;
        this.rep = rep;
        this.date = date;
        this.rating = rating;
    }

    public Rponse(int id, int user, int requete, String sujet, String rep, Date date, int rating) {
        this.id = id;
        this.user = user;
        this.requete = requete;
        this.sujet = sujet;
        this.rep = rep;
        this.date = date;
        this.rating = rating;
    }


    public int getRequete() {
        return requete;
    }

    public void setRequete(int requete) {
        this.requete = requete;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getRep() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id +
                ",Requete" + requete + '\'' +
                ", Sujet='" + sujet + '\'' +
                ", Reponse='" + rep + '\'' +
                ", Date='" + date + '\'' +
                ", Rating='" + rating + '\'' +
                '}';


    }
}
