package Packages.Nasri.entities;

import Packages.Nasri.enums.HebergementStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class HebergementOffer {
    private int id;
    private int userId;
    private String description;
    private String governorat;
    private int numberRooms;
    private int duration; // in months
    private LocalDateTime creationDate;
    private HebergementStatus state;
    private String telephone;
    private String image; // file path

    public HebergementOffer() {

    }

    public HebergementOffer(int id, int userId, String description, String governorat,
                            int numberRooms, int duration,
                            HebergementStatus state, String telephone, String image) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.governorat = governorat;
        this.numberRooms = numberRooms;
        this.duration = duration;
        this.creationDate = new Timestamp(System.currentTimeMillis()).toLocalDateTime();
        this.state = state;
        this.telephone = telephone;
        this.image = image;
    }

    public HebergementOffer(int id, int userId, String description, String governorat,
                            int numberRooms, int duration, LocalDateTime creationDate,
                            HebergementStatus state, String telephone, String image) {
        this(id, userId, description, governorat, numberRooms, duration, state, telephone, image);
        this.creationDate = creationDate;
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public HebergementStatus getState() {
        return state;
    }

    public void setState(HebergementStatus state) {
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, description,governorat, numberRooms, duration, creationDate, state, telephone, image);
    }

    @Override
    public String toString() {
        String hebergementOfferStr = "Hebergement Offer#" + id + ": \n"
                + "Description: " + description + "\n"
                + "Governorat: " + governorat + "\n"
                + "Number of rooms: " + numberRooms + "\n"
                + "Duration: " + duration + "\n"
                + "State: " + (state == HebergementStatus.inProcess ? "inProcess" : "Done") + "\n"
                + "Telephone: " + telephone + "\n";

        return hebergementOfferStr;
    }
}
