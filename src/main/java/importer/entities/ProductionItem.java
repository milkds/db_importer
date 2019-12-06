package importer.entities;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "items")
public class ProductionItem {

    @Id
    @Column(name = "ITEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemID;

    @Column(name = "ITEM_PART_NO")
    private String itemPartNo;

    @Column(name = "ITEM_MANUFACTURER")
    private String itemManufacturer;

    @Column(name = "ITEM_TYPE")
    private String itemType;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "item_attributes_link",
            joinColumns = { @JoinColumn(name = "ITEM_ID") },
            inverseJoinColumns = { @JoinColumn(name = "ITEM_ATT_ID") }
    )
    private Set<ItemAttribute> itemAttributes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    private Set<ProductionFitment> productionFitments = new HashSet<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private Set<ItemPic> pics = new HashSet<>();

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL)
    private ShockParameters params = new ShockParameters();


    @Override
    public String toString() {
        return "ProductionItem{" +
                "itemID=" + itemID +
                ", itemPartNo='" + itemPartNo + '\'' +
                ", itemManufacturer='" + itemManufacturer + '\'' +
                ", itemType='" + itemType + '\'' +
                '}';
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemPartNo() {
        return itemPartNo;
    }

    public void setItemPartNo(String itemPartNo) {
        this.itemPartNo = itemPartNo;
    }

    public String getItemManufacturer() {
        return itemManufacturer;
    }

    public void setItemManufacturer(String itemManufacturer) {
        this.itemManufacturer = itemManufacturer;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Set<ItemAttribute> getItemAttributes() {
        return itemAttributes;
    }

    public void setItemAttributes(Set<ItemAttribute> itemAttributes) {
        this.itemAttributes = itemAttributes;
    }

    public Set<ProductionFitment> getProductionFitments() {
        return productionFitments;
    }

    public void setProductionFitments(Set<ProductionFitment> productionFitments) {
        this.productionFitments = productionFitments;
    }

    public ShockParameters getParams() {
        return params;
    }

    public void setParams(ShockParameters params) {
        this.params = params;
    }

    public Set<ItemPic> getPics() {
        return pics;
    }

    public void setPics(Set<ItemPic> pics) {
        this.pics = pics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductionItem item = (ProductionItem) o;
        return itemPartNo.equals(item.itemPartNo) &&
                itemManufacturer.equals(item.itemManufacturer) &&
                itemType.equals(item.itemType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemPartNo, itemManufacturer, itemType);
    }
}
