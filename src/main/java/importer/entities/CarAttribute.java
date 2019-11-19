package importer.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "car_attributes")
public class CarAttribute {

    @Id
    @Column(name = "CAR_ATT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int carAttID;

    @Transient
    private int carExcelID;

    @Column(name = "CAR_ATT_NAME")
    private String carAttName;

    @Column(name = "CAR_ATT_VALUE")
    private String carAttValue;

    @ManyToMany(mappedBy = "attributes")
    private Set<ProductionCar> ProductionCars = new HashSet<>();

    @Override
    public String toString() {
        return "CarAttribute{" +
                "carAttID=" + carAttID +
                ", carExcelID=" + carExcelID +
                ", carAttName='" + carAttName + '\'' +
                ", carAttValue='" + carAttValue + '\'' +
                '}';
    }

    public int getCarAttID() {
        return carAttID;
    }

    public void setCarAttID(int carAttID) {
        this.carAttID = carAttID;
    }

    public int getCarExcelID() {
        return carExcelID;
    }

    public void setCarExcelID(int carExcelID) {
        this.carExcelID = carExcelID;
    }

    public String getCarAttName() {
        return carAttName;
    }

    public void setCarAttName(String carAttName) {
        this.carAttName = carAttName;
    }

    public String getCarAttValue() {
        return carAttValue;
    }

    public void setCarAttValue(String carAttValue) {
        this.carAttValue = carAttValue;
    }

    public Set<ProductionCar> getProductionCars() {
        return ProductionCars;
    }

    public void setProductionCars(Set<ProductionCar> productionCars) {
        this.ProductionCars = productionCars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarAttribute attribute = (CarAttribute) o;
        return !(!Objects.equals(carAttName, attribute.carAttName) || !carAttValue.equals(attribute.carAttValue));
    }

    @Override
    public int hashCode() {
        return Objects.hash(carAttName, carAttValue);
    }
}
