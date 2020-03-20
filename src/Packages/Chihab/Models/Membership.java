package Packages.Chihab.Models;

import Packages.Chihab.Models.enums.AccessType;

import java.sql.Timestamp;

public class Membership {
    private int id;
    private Timestamp joinDate;
    private String fonction,description;
    private AccessType access;
    public Membership() { }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Timestamp getJoinDate() { return joinDate; }
    public void setJoinDate(Timestamp joinDate) { this.joinDate = joinDate; }
    public String getFonction() { return fonction; }
    public void setFonction(String fonction) { this.fonction = fonction; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public AccessType getAccess() { return access; }
    public void setAccess(AccessType access) { this.access = access; }

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
