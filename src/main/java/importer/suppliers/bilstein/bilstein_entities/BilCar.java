package importer.suppliers.bilstein.bilstein_entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "bil_cars")
public class BilCar {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer carID;

    @Column(name = "MODEL_YEAR")
    private Integer modelYear;

    @Column(name = "MAKE")
    private String make;

    @Column(name = "MODEL")
    private String model;

    @Column(name = "SUBMODEL")
    private String subModel;

    @Column(name = "BODY")
    private String body;

    @Column(name = "BODY_MANUFACTURER")
    private String bodyMan;

    @Column(name = "DRIVE")
    private String drive;

    @Column(name = "DOORS")
    private String doors;

    @Column(name = "ENGINE")
    private String engine;

    @Column(name = "SUSPENSION")
    private String suspension;

    @Column(name = "TRANSMISSION")
    private String transmission;

    @Column(name = "YEAR_START")
    private Integer yearStart;

    @Column(name = "YEAR_FINISH")
    private Integer yearFinish;

    @Transient
    private Boolean hasShocks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bilCar")
    private List<BilFitment> bilFitments;

    @Override
    public String toString() {
        return "BilCar{" +
                "modelYear=" + modelYear +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", subModel='" + subModel + '\'' +
                ", body='" + body + '\'' +
                ", bodyMan='" + bodyMan + '\'' +
                ", drive='" + drive + '\'' +
                ", doors='" + doors + '\'' +
                ", engine='" + engine + '\'' +
                ", suspension='" + suspension + '\'' +
                ", transmission='" + transmission + '\'' +
                '}';
    }

    public Integer getCarID() {
        return carID;
    }
    public void setCarID(Integer carID) {
        this.carID = carID;
    }
    public Integer getModelYear() {
        return modelYear;
    }
    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
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
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getBodyMan() {
        return bodyMan;
    }
    public void setBodyMan(String bodyMan) {
        this.bodyMan = bodyMan;
    }
    public String getDrive() {
        return drive;
    }
    public void setDrive(String drive) {
        this.drive = drive;
    }
    public String getDoors() {
        return doors;
    }
    public void setDoors(String doors) {
        this.doors = doors;
    }
    public String getEngine() {
        return engine;
    }
    public void setEngine(String engine) {
        this.engine = engine;
    }
    public String getSuspension() {
        return suspension;
    }
    public void setSuspension(String suspension) {
        this.suspension = suspension;
    }
    public String getTransmission() {
        return transmission;
    }
    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }
    public Boolean hasShocks() {
        return hasShocks;
    }
    public void setHasShocks(Boolean hasShocks) {
        this.hasShocks = hasShocks;
    }
    public List<BilFitment> getBilFitments() {
        return bilFitments;
    }
    public void setBilFitments(List<BilFitment> bilFitments) {
        this.bilFitments = bilFitments;
    }
    public Integer getYearStart() {
        return yearStart;
    }
    public void setYearStart(Integer yearStart) {
        this.yearStart = yearStart;
    }
    public Integer getYearFinish() {
        return yearFinish;
    }
    public void setYearFinish(Integer yearFinish) {
        this.yearFinish = yearFinish;
    }
}
