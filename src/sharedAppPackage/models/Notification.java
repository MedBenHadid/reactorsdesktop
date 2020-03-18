package sharedAppPackage.models;

import de.ailis.pherialize.MixedArray;

import java.sql.Timestamp;

public class Notification {
    private MixedArray route_paramaters;
    private Timestamp notificationDate;
    private String title,desription,icon,route;
    private boolean seen;

}
