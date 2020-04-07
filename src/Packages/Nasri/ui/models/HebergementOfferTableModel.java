package Packages.Nasri.ui.models;

import Packages.Nasri.entities.HebergementOffer;
import Packages.Nasri.entities.HebergementRequest;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class HebergementOfferTableModel {
    private int id;
    private int userId;
    private String description;
    private String governorat;
    private int numberRooms;
    private int duration;
    private String state;
    private String telephone;
    private String image;

    public HebergementOfferTableModel() {}

    private HebergementOfferTableModel(HebergementOffer entity) {
        this.id = entity.getId();
        this.userId = entity.getId();
        this.description = entity.getDescription();
        this.governorat = entity.getGovernorat();
        this.numberRooms = entity.getNumberRooms();
        this.duration = entity.getDuration();
        this.state = entity.getState().name();
        this.telephone = entity.getTelephone();
        this.image = entity.getImage();
    }

    public static ArrayList<HebergementOfferTableModel> get(ArrayList<HebergementOffer> entities) {
        ArrayList<HebergementOfferTableModel> models = new ArrayList<HebergementOfferTableModel>();
        for (HebergementOffer entity : entities) {
            models.add(new HebergementOfferTableModel(entity));
        }

        return models;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGovernorat() {
        return governorat;
    }

    public void setGovernorat(String governorat) {
        this.governorat = governorat;
    }

    public int getNumberRooms() {
        return numberRooms;
    }

    public void setNumberRooms(int numberRooms) {
        this.numberRooms = numberRooms;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
