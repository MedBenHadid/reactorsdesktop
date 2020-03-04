package associationPackage.Entities;

import sharedAppPackage.models.User;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Association {

    private SimpleIntegerProperty id;
    private SimpleStringProperty  name;
    private SimpleStringProperty  description;
    private ObjectProperty<User> manager;
    private ListProperty<Membership> membershipList;
}
