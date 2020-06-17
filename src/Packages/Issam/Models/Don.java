package Packages.Issam.Models;

import Main.Entities.User;
import Packages.Chihab.Models.Entities.Category;

public class Don {
    private int id ;
    private String title ;
    private String description ;
    private String address ;
    private String phone ;
    private String creationDate ;
    private Double lat=36.0,lon=74.5;
    private String image ;
    private Category category;
    private int userId ;
    public Don(int id, String title, String description, String address, String phone, String creationDate, Double lat, Double lon, String image, Category category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.address = address;
        this.phone = phone;
        this.creationDate = creationDate;
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



    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
    public Double getLat() {
        return lat;
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



    public  String getDomaineName(){

    return category.getNom();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = 4;
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
                ", lat=" + lat +
                ", lon=" + lon +
                ", image='" + image + '\'' +
                ", category=" + category +
                '}';
    }


}
