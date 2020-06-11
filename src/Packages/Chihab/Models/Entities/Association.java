package Packages.Chihab.Models.Entities;

import Main.Entities.User;
import Main.Services.UserService;
import Packages.Chihab.Services.CategoryService;
import javafx.beans.Observable;
import javafx.beans.property.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public final class Association extends SimpleObjectProperty<Association> implements Observable, Comparable<Association> {
    private final IntegerProperty id = new SimpleIntegerProperty(-1);
    private final ObjectProperty<User> manager = new SimpleObjectProperty<>(new User());
    private final ObjectProperty<Category> domaine = new SimpleObjectProperty<>(new Category());
    private final StringProperty nom=new SimpleStringProperty("");
    private final StringProperty photoAgence = new SimpleStringProperty("");
    private final StringProperty pieceJustificatif = new SimpleStringProperty("");
    private final StringProperty rue = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");
    private final StringProperty ville= new SimpleStringProperty("");
    private final StringProperty horaireTravail = new SimpleStringProperty("");
    private final IntegerProperty telephone= new SimpleIntegerProperty(0);
    private final IntegerProperty codePostal = new SimpleIntegerProperty(0);
    private final BooleanProperty approuved= new SimpleBooleanProperty(false);
    private final DoubleProperty lat= new SimpleDoubleProperty(0);
    private final DoubleProperty lon= new SimpleDoubleProperty(0);


    public Association(ResultSet r) throws SQLException {
        super();
        this.id.set(r.getInt("id"));
        this.nom.set(r.getString("nom"));
        this.photoAgence.set(r.getString("photo_agence"));
        this.pieceJustificatif.set(r.getString("piece_justificatif"));
        this.rue.set(r.getString("rue"));
        this.description.set(r.getString("description"));
        this.ville.set(r.getString("ville"));
        this.horaireTravail.set(r.getString("horaire_travail"));
        this.telephone.set(r.getInt("telephone"));
        this.codePostal.set(r.getInt("code_postal"));
        this.approuved.set(r.getBoolean("approuved"));
        this.lat.set(r.getDouble("latitude"));
        this.lon.set(r.getDouble("longitude"));
        this.domaine.set(CategoryService.getInstace().readById(r.getInt("domaine_id")));
        this.manager.set(UserService.getInstace().readUserBy(r.getInt("id_manager")));
        setValue(this);
        set(this);
    }

    public Association(){
        super();
    }

    public Association(int id, String nom, String photoAgence, String pieceJustificatif, String rue, String description, String ville, String horaireTravail, int telephone, int codePostal, boolean approuved, double lat, double lon) {
        super();
        this.id.set(id);
        this.nom.set(nom);
        this.photoAgence.set(photoAgence);
        this.pieceJustificatif.set(pieceJustificatif);
        this.rue.set(rue);
        this.description.set(description);
        this.ville.set(ville);
        this.horaireTravail.set(horaireTravail);
        this.telephone.set(telephone);
        this.codePostal.set(codePostal);
        this.approuved.set(approuved);
        this.lat.set(lat);
        this.lon.set(lon);
    }

    public <T> Association(T object) {

    }


    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public User getManager() {
        return manager.get();
    }

    public ObjectProperty<User> managerProperty() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager.set(manager);
    }

    public Category getDomaine() {
        return domaine.get();
    }

    public ObjectProperty<Category> domaineProperty() {
        return domaine;
    }

    public void setDomaine(Category domaine) {
        this.domaine.set(domaine);
    }

    public String getNom() {
        return nom.get();
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getPhotoAgence() {
        return photoAgence.get();
    }

    public StringProperty photoAgenceProperty() {
        return photoAgence;
    }

    public void setPhotoAgence(String photoAgence) {
        this.photoAgence.set(photoAgence);
    }

    public String getPieceJustificatif() {
        return pieceJustificatif.get();
    }

    public StringProperty pieceJustificatifProperty() {
        return pieceJustificatif;
    }

    public void setPieceJustificatif(String pieceJustificatif) {
        this.pieceJustificatif.set(pieceJustificatif);
    }

    public String getRue() {
        return rue.get();
    }

    public StringProperty rueProperty() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue.set(rue);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getVille() {
        return ville.get();
    }

    public StringProperty villeProperty() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville.set(ville);
    }

    public String getHoraireTravail() {
        return horaireTravail.get();
    }

    public StringProperty horaireTravailProperty() {
        return horaireTravail;
    }

    public void setHoraireTravail(String horaireTravail) {
        this.horaireTravail.set(horaireTravail);
    }

    public int getTelephone() {
        return telephone.get();
    }

    public IntegerProperty telephoneProperty() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone.set(telephone);
    }

    public int getCodePostal() {
        return codePostal.get();
    }

    public IntegerProperty codePostalProperty() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal.set(codePostal);
    }

    public boolean isApprouved() {
        return approuved.get();
    }

    public BooleanProperty approuvedProperty() {
        return approuved;
    }

    public void setApprouved(boolean approuved) {
        this.approuved.set(approuved);
    }

    public double getLat() {
        return lat.get();
    }

    public DoubleProperty latProperty() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat.set(lat);
    }

    public double getLon() {
        return lon.get();
    }

    public DoubleProperty lonProperty() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon.set(lon);
    }

    public String getManagerUserName() {
        return this.getManager().getUsername();
    }

    @Override
    public String toString() {
        return "Association{" +
                "id=" + id +
                ", manager=" + manager +
                ", domaine=" + domaine +
                ", nom=" + nom +
                ", photoAgence=" + photoAgence +
                ", pieceJustificatif=" + pieceJustificatif +
                ", rue=" + rue +
                ", description=" + description +
                ", ville=" + ville +
                ", horaireTravail=" + horaireTravail +
                ", telephone=" + telephone +
                ", codePostal=" + codePostal +
                ", approuved=" + approuved +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Association)) return false;
        Association that = (Association) o;
        if (id.get() != (that.getId())) return false;
        if (!manager.get().equals(that.getManager())) return false;
        if (!domaine.get().equals(that.getDomaine())) return false;
        if (!nom.get().equals(that.getNom())) return false;
        if (!photoAgence.get().equals(that.getPhotoAgence())) return false;
        if (!pieceJustificatif.get().equals(that.getPieceJustificatif())) return false;
        if (!rue.get().equals(that.getRue())) return false;
        if (!description.get().equals(that.getDescription())) return false;
        if (!ville.get().equals(that.getVille())) return false;
        if (!horaireTravail.get().equals(that.getHoraireTravail())) return false;
        if (telephone.get() != (that.getTelephone())) return false;
        if (codePostal.get() != (that.getCodePostal())) return false;
        if (approuved.get() != (that.isApprouved())) return false;
        if (lat.get() != (that.getLat())) return false;
        return lon.get() == (that.getLon());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Association association) {
        return Integer.compare(association.getId(), id.get());
    }
}
