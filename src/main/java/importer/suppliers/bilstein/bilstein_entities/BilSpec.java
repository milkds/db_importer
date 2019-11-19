package importer.suppliers.bilstein.bilstein_entities;


import javax.persistence.*;

@Entity
@Table(name = "specs")
public class BilSpec {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int specID;

    @Column (name = "SPEC_NAME")
    private String specName;

    @Column (name = "SPEC_VALUE")
    private String specValue;

    @ManyToOne
    @JoinColumn(name = "SHOCK_ID")
    private BilShock bilShock;

    @Override
    public String toString() {
        return "BilSpec{" +
                "specID=" + specID +
                ", specName='" + specName + '\'' +
                ", specValue='" + specValue + '\'' +
                '}';
    }

    public int getSpecID() {
        return specID;
    }

    public void setSpecID(int specID) {
        this.specID = specID;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getSpecValue() {
        return specValue;
    }

    public void setSpecValue(String specValue) {
        this.specValue = specValue;
    }

    public BilShock getBilShock() {
        return bilShock;
    }

    public void setBilShock(BilShock bilShock) {
        this.bilShock = bilShock;
    }
}
