package Packages.Issam.Models;

import Packages.Chihab.Models.Entities.Category;

public class Don {
    private int id ;
    private String title ;
    private String description ;
    private String address ;
    private String phone ;
    private String creationDate ;
    private int ups ;
    private String lat,lon;
    private String image ;
    private Category category;

    public Don(int id, String title, String description, String address, String phone, String creationDate, int ups, String lat, String lon, String image, Category category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.address = address;
        this.phone = phone;
        this.creationDate = creationDate;
        this.ups = ups;
        this.lat = lat;
        this.lon = lon;
        this.image = image;
        this.category = category;
    }

    public Don() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getUps() {
        return ups;
    }

    public void setUps(int ups) {
        this.ups = ups;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Don{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", creationDate=" + creationDate +
                ", ups=" + ups +
                ", lat=" + lat +
                ", lon=" + lon +
                ", image='" + image + '\'' +
                ", category=" + category +
                '}';
    }
}
