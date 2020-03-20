package Packages.Chihab.Models;

public class Category {
    private int id;
    private String nom,description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Category{" +
                ", Nom='" + nom + '\'' +
                ", Description ='" + description + '\'' +
                '}';
    }
}
