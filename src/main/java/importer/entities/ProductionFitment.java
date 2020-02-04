package importer.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "fitments")
public class ProductionFitment {

    @Id
    @Column(name = "FIT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fitmentID;

    @ManyToOne
    @JoinColumn(name = "CAR_ID")
    private ProductionCar car;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private ProductionItem item;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "fitment_attributes_link",
            joinColumns = { @JoinColumn(name = "FIT_ID") },
            inverseJoinColumns = { @JoinColumn(name = "FIT_ATT_ID") }
    )
    private Set<FitmentAttribute> fitmentAttributes = new HashSet<>();

    public int getFitmentID() {
        return fitmentID;
    }

    public void setFitmentID(int fitmentID) {
        this.fitmentID = fitmentID;
    }

    public ProductionCar getCar() {
        return car;
    }

    public void setCar(ProductionCar car) {
        this.car = car;
    }

    public ProductionItem getItem() {
        return item;
    }

    public void setItem(ProductionItem item) {
        this.item = item;
    }

    public Set<FitmentAttribute> getFitmentAttributes() {
        return fitmentAttributes;
    }

    public void setFitmentAttributes(Set<FitmentAttribute> fitmentAttributes) {
        this.fitmentAttributes = fitmentAttributes;
    }

    @Override
    public String toString() {
        return "ProductionFitment{" +
                "car=" + car +
                ", item=" + item +
                ", fitmentAttributes=" + fitmentAttributes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductionFitment)) return false;
        ProductionFitment fitment = (ProductionFitment) o;
        return Objects.equals(getCar(), fitment.getCar()) &&
                Objects.equals(getItem(), fitment.getItem()) &&
                Objects.equals(getFitmentAttributes(), fitment.getFitmentAttributes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCar());
    }
}
