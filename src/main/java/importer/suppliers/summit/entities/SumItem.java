package importer.suppliers.summit.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Entity
@Table(name = "sum_items")
public class SumItem {

    @Id
    @Column(name = "ITEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "SHORT_DESC")
    private String shortDesc;

    @Column(name = "PIC_URLS")
    private String picUrls;

    @Column(name = "VIDEO_URLS")
    private String videoUrls;

    @Column(name = "BRAND")
    private String brand;

    @Column(name = "PART_NO")
    private String partNo;

    @Column(name = "ITEM_TYPE")
    private String itemType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.ALL )
    private List<SumItemAttribute> attributes = new ArrayList<>();

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.ALL )
    private List<SumFitment> fitments = new ArrayList<>();


    @Override
    public String toString() {
        return new StringJoiner(System.lineSeparator(), SumItem.class.getSimpleName() + "[", "]")
                .add("title='" + title + "'")
                .add("price=" + price)
                .add("shortDesc='" + shortDesc + "'")
                .add("picUrls='" + picUrls + "'")
                .add("videoUrls='" + videoUrls + "'")
                .add("brand='" + brand + "'")
                .add("partNo='" + partNo + "'")
                .add("itemType='" + itemType + "'")
                .add("attributes=" + attributes)
                .add("fitments=" + fitments)
                .add("description='" + description + "'")
                .toString();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setShortDesc(String shortTitle) {
        this.shortDesc = shortTitle;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setPicUrls(String picUrls) {
        this.picUrls = picUrls;
    }

    public String getPicUrls() {
        return picUrls;
    }

    public void setVideoUrls(String videoUrls) {
        this.videoUrls = videoUrls;
    }

    public String getVideoUrls() {
        return videoUrls;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemType() {
        return itemType;
    }

    public List<SumItemAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<SumItemAttribute> attributes) {
        this.attributes = attributes;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setFitments(List<SumFitment> fitments) {
        this.fitments = fitments;
    }

    public List<SumFitment> getFitments() {
        return fitments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
