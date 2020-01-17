package importer.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "cars")
public class ProductionCar {
    @Id
    @Column(name = "CAR_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int carID;

    @Column(name = "YEAR_START")
    private int yearStart;

    @Column(name = "YEAR_FINISH")
    private int yearFinish;

    @Column(name = "CAR_MAKE")
    private String make;

    @Column(name = "CAR_MODEL")
    private String model;

    @Column(name = "CAR_SUBMODEL")
    private String subModel = "N/A";

    @Column(name = "CAR_DRIVE")
    private String drive = "";

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "car_attributes_link",
            joinColumns = { @JoinColumn(name = "CAR_ID") },
            inverseJoinColumns = { @JoinColumn(name = "CAR_ATT_ID") }
    )
    private Set<CarAttribute> attributes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "car")
    private Set<ProductionFitment> ProductionFitments = new HashSet<>();

    @Override
    public String toString() {
        return "ProductionCar{" +
                "carID=" + carID +
                ", yearStart=" + yearStart +
                ", yearFinish=" + yearFinish +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", subModel='" + subModel + '\'' +
                ", drive='" + drive + '\'' +
                '}';
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public int getYearStart() {
        return yearStart;
    }

    public void setYearStart(int yearStart) {
        this.yearStart = yearStart;
    }

    public int getYearFinish() {
        return yearFinish;
    }

    public void setYearFinish(int yearFinish) {
        this.yearFinish = yearFinish;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSubModel() {
        return subModel;
    }

    public void setSubModel(String subModel) {
        this.subModel = subModel;
    }

    public String getDrive() {
        return drive;
    }

    public void setDrive(String drive) {
        this.drive = drive;
    }

    public Set<CarAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<CarAttribute> attributes) {
        this.attributes = attributes;
    }

    public Set<ProductionFitment> getProductionFitments() {
        return ProductionFitments;
    }
    public void setProductionFitments(Set<ProductionFitment> productionFitments) {
        this.ProductionFitments = productionFitments;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductionCar)) return false;
        ProductionCar car = (ProductionCar) o;
        return getYearStart() == car.getYearStart() &&
                getYearFinish() == car.getYearFinish() &&
                getMake().equals(car.getMake()) &&
                getModel().equals(car.getModel()) &&
                Objects.equals(getSubModel(), car.getSubModel()) &&
                Objects.equals(getDrive(), car.getDrive()) &&
                getAttributes().equals(car.getAttributes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMake(), getModel(), getSubModel());
    }
}
