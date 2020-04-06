package Packages.Nasri.ui.models;

import Packages.Nasri.entities.HebergementRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class HebergementRequestTableModel {
    private int userId;
    private String name;
    private String description;
    private String region;
    private String nativeCountry;
    private LocalDateTime arrivalDate;
    private String state;
    private String passportNumber;
    private String civilState;
    private String telephone;

    public HebergementRequestTableModel() {}

    public HebergementRequestTableModel(HebergementRequest entity) {
        this.userId = entity.getUserId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.region = entity.getRegion();
        this.nativeCountry = entity.getNativeCountry();
        this.arrivalDate = entity.getArrivalDate();
        this.state = entity.getState().name();
        this.passportNumber = entity.getPassportNumber();
        this.civilState = entity.getCivilStatus().name();
        this.telephone = entity.getTelephone();
    }

    public static ArrayList<HebergementRequestTableModel> get(ArrayList<HebergementRequest> entities) {
        ArrayList<HebergementRequestTableModel> models = new ArrayList<HebergementRequestTableModel>();
        for (HebergementRequest entity : entities) {
            models.add(new HebergementRequestTableModel(entity));
        }

        return models;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getNativeCountry() {
        return nativeCountry;
    }

    public void setNativeCountry(String nativeCountry) {
        this.nativeCountry = nativeCountry;
    }

    public LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getCivilState() {
        return civilState;
    }

    public void setCivilState(String civilState) {
        this.civilState = civilState;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
