package Packages.Chihab.Models;

import Packages.Chihab.Models.enums.AccessType;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Timestamp;

public class Membership {
    private SimpleIntegerProperty id;
    private Timestamp joinDate;
    private SimpleStringProperty fonction, description;
    private AccessType access;

    public Membership() { }

    public SimpleIntegerProperty getId() {
        return id;
    }

    public void setId(int id) {
        this.id = new SimpleIntegerProperty(id);
    }

    public Timestamp getJoinDate() { return joinDate; }
    public void setJoinDate(Timestamp joinDate) { this.joinDate = joinDate; }

    public SimpleStringProperty getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = new SimpleStringProperty(fonction);
    }

    public SimpleStringProperty getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = new SimpleStringProperty(description);
    }
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
