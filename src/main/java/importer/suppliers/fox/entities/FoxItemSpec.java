package importer.suppliers.fox.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "item_specs")
public class FoxItemSpec {

    @Id
    @Column(name = "SPEC_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int specID;

    @Column(name = "SPEC_NAME")
    private String specName;

    @Column(name = "SPEC_VALUE")
    private String specVal;

    @ManyToMany(mappedBy = "specs")
    private Set<FoxItem> items = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FoxItemSpec)) return false;
        FoxItemSpec spec = (FoxItemSpec) o;
        return Objects.equals(getSpecName(), spec.getSpecName()) &&
                Objects.equals(getSpecVal(), spec.getSpecVal());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSpecName(), getSpecVal());
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

    public String getSpecVal() {
        return specVal;
    }

    public void setSpecVal(String specVal) {
        this.specVal = specVal;
    }

    public Set<FoxItem> getItems() {
        return items;
    }

    public void setItems(Set<FoxItem> items) {
        this.items = items;
    }
}
