package Packages.Mohamed.Entities;

import Main.Entities.User;
import Packages.Chihab.Models.Category;

import java.sql.Date;


public class Mission {

    private int id;
    private Category domaine;
    private String TitleMission,picture,description,location;
    private int ups;
    private double lat,lon;
    private Double objectif,sumCollected;
    private User CretedBy;
    private Date DateCreation,DateFin;

    public Mission() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category getDomaine() {
        return domaine;
    }

    public void setDomaine(Category domaine) {
        this.domaine = domaine;
    }

    public String getTitleMission() {
        return TitleMission;
    }

    public void setTitleMission(String titleMission) {
        TitleMission = titleMission;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getUps() {
        return ups;
    }

    public void setUps(int ups) {
        this.ups = ups;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public Double getObjectif() {
        return objectif;
    }

    public void setObjectif(Double objectif) {
        this.objectif = objectif;
    }

    public Double getSumCollected() {
        return sumCollected;
    }

    public void setSumCollected(Double sumCollected) {
        this.sumCollected = sumCollected;
    }

    public String getCretedBy() {
        return CretedBy.getUsername();
    }

    public void setCretedBy(User cretedBy) {
        CretedBy = cretedBy;
    }

    public Date getDateCreation() {
        return DateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        DateCreation = dateCreation;
    }

    public Date getDateFin() {
        return DateFin;
    }

    public void setDateFin(Date dateFin) {
        DateFin = dateFin;
    }

    @Override
    public String toString() {
        return "Mission{" +
                "id=" + id +
                ", domaine=" + domaine +
                ", TitleMission='" + TitleMission + '\'' +
                ", picture='" + picture + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", ups=" + ups +
                ", lat=" + lat +
                ", lon=" + lon +
                ", objectif=" + objectif +
                ", sumCollected=" + sumCollected +
                ", CretedBy=" + CretedBy +
                ", DateCreation=" + DateCreation +
                ", DateFin=" + DateFin +
                '}';
    }
}
