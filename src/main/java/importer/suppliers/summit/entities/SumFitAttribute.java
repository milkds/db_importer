package importer.suppliers.summit.entities;

import javax.persistence.*;
import java.util.StringJoiner;

@Entity
@Table(name = "fit_attributes")
public class SumFitAttribute {

    @Id
    @Column(name = "FIT_ATT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ATT_NAME")
    private String name;

    @Column(name = "ATT_VALUE")
    private String value;

    @ManyToOne
    @JoinColumn(name = "FIT_ID")
    private SumFitment fitment;


    public SumFitAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public SumFitAttribute(){}

    @Override
    public String toString() {
        return new StringJoiner(", ", SumFitAttribute.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("value='" + value + "'")
                .toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFitment(SumFitment fitment) {
        this.fitment = fitment;
    }

    public SumFitment getFitment() {
        return fitment;
    }
}
