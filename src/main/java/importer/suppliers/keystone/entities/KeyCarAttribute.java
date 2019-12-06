package importer.suppliers.keystone.entities;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "car_attributes")
public class KeyCarAttribute {

    @Id
    @Column(name = "CAR_ATT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int carAttID;

    @Column(name = "ATT_VALUE")
    private String attValue;

    @ManyToMany(mappedBy = "attributes")
    private List<KeyCar> cars = new ArrayList<>();

    @Override
    public String toString() {
        return "CarAttribute{" +
                "attValue='" + attValue + '\'' +
                '}';
    }

    public List<KeyCar> getCars() {
        return cars;
    }
    public void setCars(List<KeyCar> cars) {
        this.cars = cars;
    }
    public String getAttValue() {
        return attValue;
    }
    public void setAttValue(String attValue) {
        this.attValue = attValue;
    }
    public int getCarAttID() {
        return carAttID;
    }
    public void setCarAttID(int carAttID) {
        this.carAttID = carAttID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyCarAttribute)) return false;
        KeyCarAttribute attribute = (KeyCarAttribute) o;
        return Objects.equals(attValue, attribute.attValue);
    }

    @Override
    public int hashCode() {

        return Objects.hash(attValue);
    }
}
