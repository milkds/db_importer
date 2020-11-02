package importer.suppliers.eibach.eib_entities;

import javax.persistence.*;

@Entity
@Table(name = "eib_cars")
public class EibCar {

    @Id
    @Column(name = "CAR_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "YEAR")
    private String year;

    @Column(name = "MAKE")
    private String make;

    @Column(name = "MODEL")
    private String model;

    @Column(name = "SUBMODEL")
    private String submodel;

    @Column(name = "FRONT")
    private String front;

    @Column(name = "REAR")
    private String rear;

    @Column(name = "RATE")
    private String rate;

    @Column(name = "NOTE")
    private String note;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID" )
    private EibItem item;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
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

    public String getSubmodel() {
        return submodel;
    }

    public void setSubmodel(String submodel) {
        this.submodel = submodel;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getRear() {
        return rear;
    }

    public void setRear(String rear) {
        this.rear = rear;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "EibCar{" +
                "year='" + year + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", submodel='" + submodel + '\'' +
                ", front='" + front + '\'' +
                ", rear='" + rear + '\'' +
                ", note='" + note + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EibItem getItem() {
        return item;
    }

    public void setItem(EibItem item) {
        this.item = item;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
