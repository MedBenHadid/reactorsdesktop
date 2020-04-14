package Packages.Nasri.ui.models;

import Packages.Nasri.entities.HebergementOffer;
import Packages.Nasri.services.ServiceHebergementOffer;
import Packages.Nasri.utils.Helpers;

import java.util.ArrayList;

public class UserHebergementOfferTableModel {
    private int id;
    private int userId;
    private String userName;
    private String description;
    private String governorat;
    private String numberRooms;
    private String duration;
    private String telephone;
    private String state;
    private String image;

    private UserHebergementOfferTableModel(HebergementOffer entity) {
        this.id = entity.getId();
        this.userId = entity.getUserId();
        this.description = entity.getDescription();
        this.governorat = entity.getGovernorat();
        this.telephone = entity.getTelephone();
        this.numberRooms = Integer.toString(entity.getNumberRooms());
        this.duration = Integer.toString(entity.getDuration());
        this.state = Helpers.convertHebergementStateToFrench(entity.getState().name());
        this.image = entity.getImage();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public UserHebergementOfferTableModel() {}



    private UserHebergementOfferTableModel(HebergementOffer entity, String userName) {
        this(entity);
        this.userName = userName;
    }

    public static ArrayList<UserHebergementOfferTableModel> get(ArrayList<HebergementOffer> entities) {
        ArrayList<UserHebergementOfferTableModel> models = new ArrayList<UserHebergementOfferTableModel>();
        for (HebergementOffer entity : entities) {
            String userName = new ServiceHebergementOffer().getUserName(entity.getUserId());
            models.add(new UserHebergementOfferTableModel(entity, userName));
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getNumberRooms() {
        return numberRooms;
    }

    public void setNumberRooms(String numberRooms) {
        this.numberRooms = numberRooms;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
