package Packages.Chihab.Models.Entities;

import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValueBase;

public class ObservableAssociationValue extends ObservableValueBase<Association> implements ObservableObjectValue<Association> {
    private final Association association = new Association();

    public ObservableAssociationValue(Association a){
        super();
        association.set(a);
    }

    @Override
    public Association getValue() {
        return association.getValue();
    }
    @Override
    public Association get() {
        return association;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObservableAssociationValue)) return false;
        ObservableAssociationValue that = (ObservableAssociationValue) o;
        return association.equals(that.association);
    }

    @Override
    public int hashCode() {
        return association.hashCode();
    }
}
