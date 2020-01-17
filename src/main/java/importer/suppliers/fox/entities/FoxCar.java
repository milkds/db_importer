package importer.suppliers.fox.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "cars")
public class FoxCar {

    @Id
    @Column(name = "CAR_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int carID;

    @Column(name = "YEAR")
    private int year;

    @Column(name = "MAKE")
    private String make;

    @Column(name = "MODEL")
    private String model;

    @Column(name = "DRIVE")
    private String drive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "car")
    private Set<FoxFit> fits = new HashSet<>();


    public FoxCar(FoxCar otherCar){
        this.year = otherCar.getYear();
        this.make = otherCar.getMake();
        this.model = otherCar.getModel();
    }

    public FoxCar() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FoxCar)) return false;
        FoxCar foxCar = (FoxCar) o;
        return getYear() == foxCar.getYear() &&
                Objects.equals(getMake(), foxCar.getMake()) &&
                Objects.equals(getModel(), foxCar.getModel()) &&
                Objects.equals(getDrive(), foxCar.getDrive());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getYear(), getMake(), getModel(), getDrive());
    }

    @Override
    public String toString() {
        return "FoxCar{" +
                "carID=" + carID +
                ", year=" + year +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", drive='" + drive + '\'' +
                '}';
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public String getDrive() {
        return drive;
    }

    public void setDrive(String drive) {
        this.drive = drive;
    }

    public Set<FoxFit> getFits() {
        return fits;
    }

    public void setFits(Set<FoxFit> fits) {
        this.fits = fits;
    }
}
