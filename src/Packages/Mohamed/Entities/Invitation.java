package Packages.Mohamed.Entities;


import Main.Entities.User;
import Packages.Mohamed.Entities.enums.EtatEnum;


public class Invitation {
    private int id, id_notification;
    private Mission id_mission;
    private User id_user;
    private EtatEnum etat;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_notification() {
        return id_notification;
    }

    public void setId_notification(int id_notification) {
        this.id_notification = id_notification;
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

    public EtatEnum getEtat() {
        return etat;
    }

    public void setEtat(EtatEnum etat) {
        this.etat = etat;
    }

}
