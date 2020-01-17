package importer.suppliers.fox.entities;

import javax.persistence.*;

@Entity
@Table(name = "fitments")
public class FoxFit {

    @Id
    @Column(name = "FIT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fitID;

    @Column(name = "FIT_NOTE")
    private String fitNote;

    @Column(name = "LIFT")
    private String lift;

    @Column(name = "POSITION")
    private String position;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private FoxItem item;

    @ManyToOne
    @JoinColumn(name = "CAR_ID")
    private FoxCar car;

    @Override
    public String toString() {
        return "FoxFit{" +
                "fitID=" + fitID +
                ", fitNote='" + fitNote + '\'' +
                ", lift='" + lift + '\'' +
                ", position='" + position + '\'' +
                '}';
    }

    public int getFitID() {
        return fitID;
    }

    public void setFitID(int fitID) {
        this.fitID = fitID;
    }

    public String getFitNote() {
        return fitNote;
    }

    public void setFitNote(String fitNote) {
        this.fitNote = fitNote;
    }

    public String getLift() {
        return lift;
    }

    public void setLift(String lift) {
        this.lift = lift;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public FoxItem getItem() {
        return item;
    }

    public void setItem(FoxItem item) {
        this.item = item;
    }

    public FoxCar getCar() {
        return car;
    }

    public void setCar(FoxCar car) {
        this.car = car;
    }
}
