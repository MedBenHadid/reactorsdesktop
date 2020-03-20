package Packages.Chihab.Models;

import Main.Entities.User;

public class Association {

    private int id;
    private User manager;
    private Category domaine;
    private String nom,photoAgence,pieceJustificatif,rue,description,ville,horaireTravail;
    private int telephone,codePostal;
    private boolean approuved;
    private Double lat,lon;
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

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
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

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getHoraireTravail() {
        return horaireTravail;
    }

    public void setHoraireTravail(String horaireTravail) {
        this.horaireTravail = horaireTravail;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }

    public boolean isApprouved() {
        return approuved;
    }

    public void setApprouved(boolean approuved) {
        this.approuved = approuved;
    }

    @Override
    public String toString() {
        return "Association{" +
                "id=" + id +
                ", manager=" + manager +
                ", domaine=" + domaine +
                ", nom='" + nom + '\'' +
                ", photoAgence='" + photoAgence + '\'' +
                ", pieceJustificatif='" + pieceJustificatif + '\'' +
                ", rue='" + rue + '\'' +
                ", description='" + description + '\'' +
                ", ville='" + ville + '\'' +
                ", horaireTravail='" + horaireTravail + '\'' +
                ", telephone=" + telephone +
                ", codePostal=" + codePostal +
                ", approuved=" + approuved +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
