package importer.suppliers.fox.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "items")
public class FoxItem {

    @Id
    @Column(name = "ITEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemID;

    @Column(name = "ITEM_PART_NO")
    private String partNo;

    @Column(name = "ITEM_TITLE")
    private String title;

    @Column(name = "ITEM_SERIES")
    private String series;

    @Column(name = "ITEM_SUP_QTY")
    private String supplyQty;

    @Column(name = "ITEM_DESC")
    private String description;

    @Column(name = "ITEM_WEBLINK")
    private String webLink;

    @Column(name = "ITEM_PIC_URLS")
    private String itemPicUrls;

    @Column(name = "ITEM_PRICE")
    private String price;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "items_specs_link",
            joinColumns = { @JoinColumn(name = "ITEM_ID") },
            inverseJoinColumns = { @JoinColumn(name = "SPEC_ID") }
    )
    private Set<FoxItemSpec> specs = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    private Set<FoxFit> fitments = new HashSet<>();

    public Set<FoxItemSpec> getSpecs() {
        return specs;
    }
    public void setSpecs(Set<FoxItemSpec> specs) {
        this.specs = specs;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSeries() {
        return series;
    }
    public void setSeries(String series) {
        this.series = series;
    }
    public String getSupplyQty() {
        return supplyQty;
    }
    public void setSupplyQty(String supplyQty) {
        this.supplyQty = supplyQty;
    }
    public int getItemID() {
        return itemID;
    }
    public void setItemID(int itemID) {
        this.itemID = itemID;
    }
    public String getPartNo() {
        return partNo;
    }
    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getWebLink() {
        return webLink;
    }
    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }
    public String getItemPicUrls() {
        return itemPicUrls;
    }
    public void setItemPicUrls(String itemPicUrls) {
        this.itemPicUrls = itemPicUrls;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public Set<FoxFit> getFitments() {
        return fitments;
    }
    public void setFitments(Set<FoxFit> fitments) {
        this.fitments = fitments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FoxItem)) return false;
        FoxItem item = (FoxItem) o;
        return getPartNo().equals(item.getPartNo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPartNo());
    }
}
