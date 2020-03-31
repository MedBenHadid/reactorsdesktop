package Packages.Nasri.models;

import Packages.Nasri.enums.CivilStatus;
import Packages.Nasri.enums.HebergementState;

import java.sql.Timestamp;
import java.util.Objects;

public class HebergementRequest {
    private int id;
    private String name;
    private String description;
    private String region;
    private HebergementState state;
    private String nativeCountry;
    private Timestamp arrivalDate;
    private String passportNumber;
    private CivilStatus civilStatus;
    private String telephone;
    private Timestamp creationDate;
    private boolean isAnonymous;

    public HebergementRequest() {}

    public HebergementRequest(int id, String name, String description, String region,
                              HebergementState state, String nativeCountry,
                              Timestamp arrivalDate, String passportNumber, CivilStatus civilStatus,
                              String telephone, Timestamp creationDate, boolean isAnonymous) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.region = region;
        this.state = state;
        this.nativeCountry = nativeCountry;
        this.arrivalDate = arrivalDate;
        this.passportNumber = passportNumber;
        this.civilStatus = civilStatus;
        this.telephone = telephone;
        this.creationDate = creationDate;
        this.isAnonymous = isAnonymous;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, region, state, nativeCountry,
                arrivalDate, passportNumber, civilStatus, telephone, creationDate, isAnonymous);
    }

    @Override
    public String toString() {
        String hebergementRequestStr = "Hebergement Request#" + id + ": \n"
                + "Name: " + name + "\n"
                + "Description: " + description + "\n"
                + "Region: " + region + "\n"
                + "State: " + (state == HebergementState.inProcess ? "inProcess" : "Done") + "\n"
                + "Native country: " + nativeCountry + "\n"
                + "Arrival date: " + arrivalDate + "\n"
                + "Passport number: " + passportNumber + "\n"
                + "Civil status: " + (civilStatus == CivilStatus.Married ? "Married" : "Single") + "\n"
                + "Telephone: " + telephone + "\n"
                + "Creation date: " + creationDate + "\n"
                + "Anonymous: " + isAnonymous;

        return hebergementRequestStr;
    }

    public int getId() {
        return id;
    }

//    public void setId(int id) {
//        this.id = id;
//    }

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

    public HebergementState getState() {
        return state;
    }

    public void setState(HebergementState state) {
        this.state = state;
    }

    public String getNativeCountry() {
        return nativeCountry;
    }

    public void setNativeCountry(String nativeCountry) {
        this.nativeCountry = nativeCountry;
    }

    public Timestamp getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Timestamp arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public CivilStatus getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(CivilStatus civilStatus) {
        this.civilStatus = civilStatus;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }
}
