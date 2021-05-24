package importer.suppliers.summit.entities;

import javax.persistence.*;
import java.util.StringJoiner;

@Entity
@Table(name = "item_attributes")
public class SumItemAttribute {

    @Id
    @Column(name = "SUM_ATT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ATT_NAME")
    private String name;

    @Column(name = "ATT_VALUE")
    private String value;


    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private SumItem item;

    public SumItemAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }
    public SumItemAttribute(){
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

    @Override
    public String toString() {
        return new StringJoiner(", ", SumItemAttribute.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("value='" + value + "'")
                .toString();
    }

    public void setItem(SumItem item) {
        this.item = item;
    }

    public SumItem getItem() {
        return item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
