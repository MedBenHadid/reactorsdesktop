package Packages.Chihab.Models;

import Main.Entities.User;
import Packages.Chihab.Models.enums.AccessType;

import java.sql.Date;

public class Membership {
    private int id;
    private Date joinDate;
    private String fonction, description;
    private Association association;
    private User member;

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

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
