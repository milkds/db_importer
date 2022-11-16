package importer.entities.links;
import javax.persistence.*;


@Entity
@Table(name = "fitment_attributes_link")
public class FitmentAttributeLink {
    @Id
    @Column(name = "LINK_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int linkID;

    @Column(name = "FIT_ID")
    private int fitID;

    @Column(name = "FIT_ATT_ID")
    private int attID;


    public int getLinkID() {
        return linkID;
    }

    public void setLinkID(int linkID) {
        this.linkID = linkID;
    }

    public int getFitID() {
        return fitID;
    }

    public void setFitID(int fitID) {
        this.fitID = fitID;
    }

    public int getAttID() {
        return attID;
    }

    public void setAttID(int attID) {
        this.attID = attID;
    }
}
