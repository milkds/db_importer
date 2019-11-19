package importer.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "item_pics")
public class ItemPic {

    @Id
    @Column(name = "PIC_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int picID;

    @Column(name = "PIC_URL")
    private String picUrl;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private ProductionItem item;

    public int getPicID() {
        return picID;
    }

    public void setPicID(int picID) {
        this.picID = picID;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public ProductionItem getItem() {
        return item;
    }

    public void setItem(ProductionItem item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemPic)) return false;
        ItemPic itemPic = (ItemPic) o;
        return getPicUrl().equals(itemPic.getPicUrl()) &&
                Objects.equals(getItem(), itemPic.getItem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPicUrl());
    }
}
