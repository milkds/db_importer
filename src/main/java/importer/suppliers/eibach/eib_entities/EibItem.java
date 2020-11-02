package importer.suppliers.eibach.eib_entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "eib_items")
public class EibItem {

    @Id
    @Column(name = "ITEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "FIT_TITLE")
    private String fitTitle;

    @Column(name = "URL")
    private String url;

    @Column(name = "PART_NO")
    private String partNo;

    @Column(name = "RIDE_HEIGHT")
    private String rideHeight;

    @Column(name = "PROD_NOTE")
    private String prodNote;

    @Column(name = "STOCK")
    private String stock;

    @Column(name = "SHIPMENT")
    private String shipment;

    @Column(name = "IMG_LINK")
    private String imgLink;

    @Column(name = "DESCRIPTION")
    private String desc;

    @Column(name = "OLD_PRICE")
    private BigDecimal oldPrice;

    @Column(name = "NEW_PRICE")
    private BigDecimal newPrice;

    @Column(name = "BOLT_DIAM")
    private String boltDiam;

    @Column(name = "EL_LENGTH")
    private String elLength;

    @Column(name = "EL_WIDTH")
    private String elWidth;

    @Column(name = "DUTY")
    private String duty;

    @Column(name = "QUANTITY")
    private String quantity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    private Set<EibCar> car = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    private Set<StdBlock> stdBlock = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    private Set<MetBlock> metBlock = new HashSet<>();

    @Override
    public String toString() {
        return "EibItem{" +
                "title='" + title + '\'' +
                ", fitTitle='" + fitTitle + '\'' +
                ", url='" + url + '\'' +
                ", partNo='" + partNo + '\'' +
                ", rideHeight='" + rideHeight + '\'' +
                ", prodNote='" + prodNote + '\'' +
                ", stock='" + stock + '\'' +
                ", shipment='" + shipment + '\'' +
                ", imgLink='" + imgLink + '\'' +
                ", desc='" + desc + '\'' +
                ", oldPrice=" + oldPrice +
                ", newPrice=" + newPrice +
                ", car=" + car +
                '}';
    }

    public Set<EibCar> getCar() {
        return car;
    }

    public void setCar(Set<EibCar> car) {
        this.car = car;
    }

    public Set<StdBlock> getStdBlock() {
        return stdBlock;
    }

    public void setStdBlock(Set<StdBlock> stdBlock) {
        this.stdBlock = stdBlock;
    }

    public Set<MetBlock> getMetBlock() {
        return metBlock;
    }

    public void setMetBlock(Set<MetBlock> metBlock) {
        this.metBlock = metBlock;
    }

    public String getProdNote() {
        return prodNote;
    }

    public void setProdNote(String prodNote) {
        this.prodNote = prodNote;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFitTitle() {
        return fitTitle;
    }

    public void setFitTitle(String fitTitle) {
        this.fitTitle = fitTitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getRideHeight() {
        return rideHeight;
    }

    public void setRideHeight(String rideHeight) {
        this.rideHeight = rideHeight;
    }

    public BigDecimal getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
    }

    public BigDecimal getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(BigDecimal newPrice) {
        this.newPrice = newPrice;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getShipment() {
        return shipment;
    }

    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBoltDiam() {
        return boltDiam;
    }

    public void setBoltDiam(String boltDiam) {
        this.boltDiam = boltDiam;
    }

    public String getElLength() {
        return elLength;
    }

    public void setElLength(String elLength) {
        this.elLength = elLength;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getElWidth() {
        return elWidth;
    }

    public void setElWidth(String elWidth) {
        this.elWidth = elWidth;
    }
}
