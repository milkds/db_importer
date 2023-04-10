package importer.entities;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "fitment_attributes")
public class FitmentAttribute {

    @Id
    @Column(name = "FIT_ATT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fitmentAttID;

    @Column(name = "FIT_ATT_NAME")
    private String fitmentAttName;

    @Column(name = "FIT_ATT_VALUE")
    private String fitmentAttValue;

    @ManyToMany(mappedBy = "fitmentAttributes")
    private List<ProductionFitment> ProductionFitments = new ArrayList<>();

    @Override
    public String toString() {
        return "FitmentAttribute{" +
                "fitmentAttID=" + fitmentAttID +
                ", fitmentAttName='" + fitmentAttName + '\'' +
                ", fitmentAttValue='" + fitmentAttValue + '\'' +
                '}';
    }

    public int getFitmentAttID() {
        return fitmentAttID;
    }

    public void setFitmentAttID(int fitmentAttID) {
        this.fitmentAttID = fitmentAttID;
    }

    public String getFitmentAttName() {
        return fitmentAttName;
    }

    public void setFitmentAttName(String fitmentAttName) {
        this.fitmentAttName = fitmentAttName;
    }

    public String getFitmentAttValue() {
        return fitmentAttValue;
    }

    public void setFitmentAttValue(String fitmentAttValue) {
        this.fitmentAttValue = fitmentAttValue;
    }

    public List<ProductionFitment> getProductionFitments() {
        return ProductionFitments;
    }

    public void setProductionFitments(List<ProductionFitment> productionFitments) {
        this.ProductionFitments = productionFitments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FitmentAttribute)) return false;
        FitmentAttribute attribute = (FitmentAttribute) o;
        return Objects.equals(getFitmentAttName(), attribute.getFitmentAttName()) &&
                getFitmentAttValue().equals(attribute.getFitmentAttValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFitmentAttName(), getFitmentAttValue());
    }

    public FitmentAttribute(String fitmentAttName, String fitmentAttValue) {
        this.fitmentAttName = fitmentAttName;
        this.fitmentAttValue = fitmentAttValue;
    }

    public FitmentAttribute() {}
}
