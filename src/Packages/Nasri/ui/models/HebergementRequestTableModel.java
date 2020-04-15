package Packages.Nasri.ui.models;

import Packages.Nasri.entities.HebergementRequest;
import Packages.Nasri.services.ServiceHebergementRequest;
import Packages.Nasri.utils.Helpers;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HebergementRequestTableModel {
    private int id;
    private int userId;
    private String userName;
    private String name;
    private String description;
    private String region;
    private String nativeCountry;
    private String arrivalDate;
    private String state;
    private String passportNumber;
    private String civilState;
    private String telephone;
    private boolean anonymous;
    private int childrenNumber;

    public HebergementRequestTableModel() {
    }

    public HebergementRequestTableModel(HebergementRequest entity) {
        this.id = entity.getId();
        this.userId = entity.getUserId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.region = entity.getRegion();
        this.nativeCountry = entity.getNativeCountry();
        this.arrivalDate = DateTimeFormatter.ISO_LOCAL_DATE.format(entity.getArrivalDate());
        // DateTimeFormatter.ISO_LOCAL_DATE.format(entity.getArrivalDate());
        this.state = Helpers.convertHebergementStateToFrench(entity.getState().name());
        this.passportNumber = entity.getPassportNumber();
        this.civilState = Helpers.convertCivilStateToFrench(entity.getCivilStatus().name());
        this.telephone = entity.getTelephone();
        this.childrenNumber = entity.getChildrenNumber();
        this.anonymous = entity.isAnonymous();
    }

    public HebergementRequestTableModel(HebergementRequest entity, String userName) {
        this(entity);
        this.userName = userName;
    }

    public static ArrayList<HebergementRequestTableModel> get(ArrayList<HebergementRequest> entities) {
        ArrayList<HebergementRequestTableModel> models = new ArrayList<HebergementRequestTableModel>();
        for (HebergementRequest entity : entities) {
            String userName = new ServiceHebergementRequest().getUserName(entity.getUserId());
            models.add(new HebergementRequestTableModel(entity, userName));
        }

        return models;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public int getChildrenNumber() {
        return childrenNumber;
    }

    public void setChildrenNumber(int childrenNumber) {
        this.childrenNumber = childrenNumber;
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

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
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
