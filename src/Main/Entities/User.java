package Main.Entities;

import Packages.Chihab.Models.enums.RoleEnum;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import de.ailis.pherialize.MixedArray;

import java.sql.Timestamp;
import java.util.Objects;

public class User extends RecursiveTreeObject<User> {
    private int id;
    private String username, email, password;
    private Timestamp last_login;
    private Boolean enabled,approuved,banned;
    private MixedArray roles;
    private Profile profile;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public MixedArray getRoles() {
        return roles;
    }

    public void setRoles(MixedArray roles) {
        this.roles = roles;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
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
    public boolean isAdmin() {
        return this.roles.contains(RoleEnum.ROLE_SUPER_ADMIN);
    }
    public boolean isAssociationAdmin() {
        return this.roles.contains(RoleEnum.ROLE_ADMIN_ASSOCIATION);
    }
    public boolean isMember() { return this.roles.contains(RoleEnum.ROLE_MEMBER); }
    public boolean isClient() {
        return this.roles.contains(RoleEnum.ROLE_CLIENT);
    }

    public User() {
        this.profile = new Profile();
    }

}
