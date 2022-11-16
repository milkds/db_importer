package importer.entities.links;

import javax.persistence.*;

@Entity
@Table(name = "car_attributes_link")
public class CarAttributeLink {

    @Id
    @Column(name = "LINK_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int linkID;

    @Column(name = "CAR_ID")
    private int carID;

    @Column(name = "CAR_ATT_ID")
    private int attID;

    public int getLinkID() {
        return linkID;
    }

    public void setLinkID(int linkID) {
        this.linkID = linkID;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public int getAttID() {
        return attID;
    }

    public void setAttID(int attID) {
        this.attID = attID;
    }
}
