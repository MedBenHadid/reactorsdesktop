package Packages.Mohamed.Entities;

import Main.Entities.User;

import Main.Services.UserService;
import Packages.Chihab.Models.Entities.Association;
import Packages.Chihab.Services.AssociationService;
import Packages.Mohamed.Services.MissionService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;



public class Notification {

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return id == that.id &&
                seen == that.seen;
    }

    private int id;
    private boolean seen;
    private String title,description,icon;
    private User CretedBy;
    private Date notification_date;
    private Association id_association;
    private Mission id_mission;
    private User id_user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public User getCretedBy() {
        return CretedBy;
    }

    public void setCretedBy(User CretedBy) {
        this.CretedBy = CretedBy;
    }

    public Date getNotification_date() {
        return notification_date;
    }

    public void setNotification_date(Date notification_date) {
        this.notification_date = notification_date;
    }

    public Association getId_association() {
        return id_association;
    }

    public void setId_association(Association id_association) {
        this.id_association = id_association;
    }

    public Mission getId_mission() {
        return id_mission;
    }

    public void setId_mission(Mission id_mission) {
        this.id_mission = id_mission;
    }

    public User getId_user() {
        return id_user;
    }

    public void setId_user(User id_user) {
        this.id_user = id_user;
    }

    public Notification(int id, String title) {
        this.id = id;
        this.title = title;
     
    }

    public Notification(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.seen = rs.getInt("seen") == 1;
        this.title = rs.getString("title");
        this.description = rs.getString("description");
        this.icon = rs.getString("icon");
        this.notification_date = rs.getDate("notification_date");
    }

    @Override
    public String toString() {
        return "Notification{" + "id=" + id + ", seen=" + seen + ", title=" + title + ", description=" + description + ", icon=" + icon + ", CretedBy=" + CretedBy + ", notification_date=" + notification_date + ", id_association=" + id_association + ", id_mission=" + id_mission + ", id_user=" + id_user + '}';
    }

    
}
 