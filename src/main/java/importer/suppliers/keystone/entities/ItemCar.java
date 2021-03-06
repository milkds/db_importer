package importer.suppliers.keystone.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "items_cars")
public class ItemCar {

    @Id
    @Column(name = "ITEM_CAR_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemCarID;

    @ManyToOne
    @JoinColumn(name = "CAR_ID")
    private KeyCar car;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private KeyItem item;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "items_cars_attributes_link",
            joinColumns = { @JoinColumn(name = "ITEM_CAR_ID") },
            inverseJoinColumns = { @JoinColumn(name = "ITEM_CAR_ATTRIBUTE_ID") }
    )
    private List<ItemCarAttribute> attributes = new ArrayList<>();

    public int getItemCarID() {
        return itemCarID;
    }
    public void setItemCarID(int itemCarID) {
        this.itemCarID = itemCarID;
    }
    public KeyCar getCar() {
        return car;
    }
    public void setCar(KeyCar car) {
        this.car = car;
    }
    public KeyItem getItem() {
        return item;
    }
    public void setItem(KeyItem item) {
        this.item = item;
    }
    public List<ItemCarAttribute> getAttributes() {
        return attributes;
    }
    public void setAttributes(List<ItemCarAttribute> attributes) {
        this.attributes = attributes;
    }
}
