package importer.entities.links;

import javax.persistence.*;

@Entity
@Table(name = "item_attributes_link")
public class ItemAttributeLink {


    @Id
    @Column(name = "LINK_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int linkID;

    @Column(name = "ITEM_ID")
    private int itemID;

    @Column(name = "ITEM_ATT_ID")
    private int attID;


    public int getLinkID() {
        return linkID;
    }

    public void setLinkID(int linkID) {
        this.linkID = linkID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getAttID() {
        return attID;
    }

    public void setAttID(int attID) {
        this.attID = attID;
    }
}
