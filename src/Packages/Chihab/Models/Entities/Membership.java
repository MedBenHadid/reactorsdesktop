package Packages.Chihab.Models.Entities;

import Main.Entities.User;
import Packages.Chihab.Models.enums.AccessType;

import java.sql.Date;

public class Membership {
    public static final String INVITE_PENDING = "INVITE_PENDING";
    public static final String REQUEST_PENDING = "REQUEST_PENDING";
    public static final String DENIED_BY_USER = "DENIED_BY_USER";
    public static final String DENIED_BY_ASS = "DENIED_BY_ASS";
    public static final String ACCEPTED = "ACCEPTED";
    public static final String ALL = "ALL";
    private int id;
    private Date joinDate;
    private String fonction;
    private String description;
    private float lon;

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setAccess(AccessType access) {
        this.access = access;
    }

    private float lat;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Membership(Date joinDate, String fonction, String description, User member, AccessType access) {
        this.joinDate = joinDate;
        this.fonction = fonction;
        this.description = description;
        this.status = INVITE_PENDING;
        this.member = member;
        this.access = access;
    }

    private String status;
    private User member;

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }
    private AccessType access;

    public Membership() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public AccessType getAccess() { return access; }

    public void setAccess(int access) {
        if (access == 3)
            this.access = AccessType.DELIVER;
        else if (access == 2)
            this.access = AccessType.READ;
        else if (access == 1)
            this.access = AccessType.WRITE;
    }

    @Override
    public String toString() {
        return "Membership{" +
                "id=" + id +
                ", joinDate=" + joinDate +
                ", fonction='" + fonction + '\'' +
                ", description='" + description + '\'' +
                ", access=" + access +
                '}';
    }
}
