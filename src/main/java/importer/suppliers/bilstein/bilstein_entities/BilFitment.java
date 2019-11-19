package importer.suppliers.bilstein.bilstein_entities;

import javax.persistence.*;

@Entity
@Table(name = "bil_fitments")
public class BilFitment {

    @Id
    @Column(name = "FITMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fitmentID;

    @ManyToOne
    @JoinColumn(name = "CAR_ID")
    private BilCar bilCar;

    @Column(name = "POSITION")
    private String position;

    @Column(name = "NOTES")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "SHOCK_PART", referencedColumnName = "PART_NO")
    private BilShock bilShock;

    @Override
    public String toString() {
        return "BilFitment{" +
                "fitmentID=" + fitmentID +
                ", bilCar=" + bilCar +
                ", position='" + position + '\'' +
                ", notes='" + notes + '\'' +
                ", bilShock=" + bilShock +
                '}';
    }

    public void setBilShock(BilShock bilShock) {
        this.bilShock = bilShock;
    }
    public BilShock getBilShock() {
        return bilShock;
    }
    public Integer getFitmentID() {
        return fitmentID;
    }
    public void setFitmentID(Integer fitmentID) {
        this.fitmentID = fitmentID;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public BilCar getBilCar() {
        return bilCar;
    }
    public void setBilCar(BilCar bilCar) {
        this.bilCar = bilCar;
    }
}
