package Packages.Issam.Models;


public class Demande {
    private int id;
    private int user;
    private String title;
    private String description;
    private String address;
    private String phone;
    private String creationDate;
    private int ups;
    private Double lat, lon;
    private String image;
    private int category;
    private String rib;

    public Demande(int id, int user, String title, String description, String address, String phone, String creationDate, int ups, Double lat, Double lon, String image, int category, String rib) {
        this.id = id;
        this.user = user;
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
        this.rib = rib;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = 5;
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

    public Double getLat() {
        return lat;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getRib() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }

    @Override
    public String toString() {
        return "Demande{" +
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
