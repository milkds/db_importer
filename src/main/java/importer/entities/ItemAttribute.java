package importer.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "item_attributes")
public class ItemAttribute {
    @Id
    @Column(name = "ITEM_ATT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemAttID;

    @Column(name = "ITEM_ATT_NAME")
    private String itemAttName;

    @Column(name = "ITEM_ATT_VALUE")
    private String itemAttValue;

    @ManyToMany(mappedBy = "itemAttributes")
    private Set<ProductionItem> items = new HashSet<>();

    @Override
    public String toString() {
        return "ItemAttribute{" +
                "itemAttID=" + itemAttID +
                ", itemAttName='" + itemAttName + '\'' +
                ", itemAttValue='" + itemAttValue + '\'' +
                '}';
    }

    public int getItemAttID() {
        return itemAttID;
    }

    public void setItemAttID(int itemAttID) {
        this.itemAttID = itemAttID;
    }

    public String getItemAttName() {
        return itemAttName;
    }

    public void setItemAttName(String itemAttName) {
        this.itemAttName = itemAttName;
    }

    public String getItemAttValue() {
        return itemAttValue;
    }

    public void setItemAttValue(String itemAttValue) {
        this.itemAttValue = itemAttValue;
    }

    public Set<ProductionItem> getItems() {
        return items;
    }

    public void setItems(Set<ProductionItem> items) {
        this.items = items;
    }

    public ItemAttribute(String itemAttName, String itemAttValue) {
        this.itemAttName = itemAttName;
        this.itemAttValue = itemAttValue;
    }

    public ItemAttribute() {}
}
