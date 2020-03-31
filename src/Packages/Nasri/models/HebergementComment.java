package Packages.Nasri.models;

import Packages.Nasri.enums.CivilStatus;
import Packages.Nasri.enums.HebergementState;

import java.sql.Timestamp;
import java.util.Objects;

public class HebergementComment {
    private int id;
    private String content;
    private Timestamp creationDate;

    public HebergementComment() {}

    public HebergementComment(int id, String content, Timestamp creationDate) {
        this.id = id;
        this.content = content;
        this.creationDate = creationDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, creationDate);
    }

    @Override
    public String toString() {
        String hebergementCommentStr = "Hebergement Comment#" + id + ": \n"
                + "Content: " + content  + "\n"
                + "Creation date: " + creationDate + "\n";

        return hebergementCommentStr;
    }
}
