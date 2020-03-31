package Packages.Nasri.models;

import Packages.Nasri.enums.HebergementState;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

public class HebergementOffer {
    private int id;
    private String description;
    private String governorat;
    private int numberRooms;
    private int duration; // in months
    private Timestamp creationDate;
    private HebergementState state;
    private String telephone;
    private String image; // file path

    public HebergementOffer() {

    }

    public HebergementOffer(int id, String description, String governorat,
                            int numberRooms, int duration, Timestamp creationDate,
                            HebergementState state, String telephone, String image) {
        this.id = id;
        this.description = description;
        this.governorat = governorat;
        this.numberRooms = numberRooms;
        this.duration = duration;
        this.creationDate = creationDate;
        this.state = state;
        this.telephone = telephone;
        this.image = image;
    }


    public int getId() {
        return id;
    }

    // disabled for the moment beeing
//    public void setId(int id) {
//        this.id = id;
//    }

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

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public HebergementState getState() {
        return state;
    }

    public void setState(HebergementState state) {
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

    @Override
    public int hashCode() {
        return Objects.hash(id, description,governorat, numberRooms, duration, creationDate, state, telephone, image);
    }

    @Override
    public String toString() {
        String hebergementOfferStr = "Hebergement Offer#" + id + ": \n"
                + "Description: " + description + "\n"
                + "Governorat: " + governorat + "\n"
                + "Number of rooms: " + numberRooms + "\n"
                + "Duration: " + duration + "\n"
                + "State: " + (state == HebergementState.inProcess ? "inProcess" : "Done") + "\n"
                + "Telephone: " + telephone + "\n";

        return hebergementOfferStr;
    }
}
