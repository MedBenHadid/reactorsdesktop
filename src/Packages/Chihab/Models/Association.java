package Packages.Chihab.Models;

import Main.Entities.User;

import java.util.List;

public class Association {

    private int id;
    private User manager;
    private Category domaine;
    private String nom,telephone,photoAgence,pieceJustificatif,rue,codePostal,lat,lon,description,ville;

    public Association() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public Category getDomaine() {
        return domaine;
    }

    public void setDomaine(Category domaine) {
        this.domaine = domaine;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPhotoAgence() {
        return photoAgence;
    }

    public void setPhotoAgence(String photoAgence) {
        this.photoAgence = photoAgence;
    }

    public String getPieceJustificatif() {
        return pieceJustificatif;
    }

    public void setPieceJustificatif(String pieceJustificatif) {
        this.pieceJustificatif = pieceJustificatif;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville){
        this.ville=ville;
    }
}
