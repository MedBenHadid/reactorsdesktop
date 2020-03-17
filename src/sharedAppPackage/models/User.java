package sharedAppPackage.models;

import java.sql.Timestamp;
import java.util.Objects;

public class User {
    private int id;
    private String username,email,plainPassword;
    private Timestamp last_login;
    private Boolean enabled,approuved,is_admin=false,is_ass_admin=false,is_member=false;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", plainPassword='" + plainPassword + '\'' +
                ", last_login=" + last_login + '\'' +
                ", enabled=" + enabled +'\'' +
                ", approuved=" + approuved +'\'' +
                ", is_admin=" + is_admin +'\'' +
                ", is_ass_admin=" + is_ass_admin +'\'' +
                ", is_member=" + is_member +'\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public Timestamp getLast_login() {
        return last_login;
    }

    public void setLast_login(Timestamp last_login) {
        this.last_login = last_login;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getApprouved() {
        return approuved;
    }

    public void setApprouved(Boolean approuved) {
        this.approuved = approuved;
    }

    public Boolean getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(Boolean is_admin) {
        this.is_admin = is_admin;
    }

    public Boolean getIs_ass_admin() {
        return is_ass_admin;
    }

    public void setIs_ass_admin(Boolean is_ass_admin) {
        this.is_ass_admin = is_ass_admin;
    }

    public Boolean getIs_member() {
        return is_member;
    }

    public void setIs_member(Boolean is_member) {
        this.is_member = is_member;
    }



    // SINGELTON
    public User() {
    }

}
