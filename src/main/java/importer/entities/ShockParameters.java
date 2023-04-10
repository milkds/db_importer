package importer.entities;

import javax.persistence.*;

@Entity
@Table(name = "shock_params")
public class ShockParameters {

    @Id
    @Column(name = "PARAM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paramID;

    @Column(name = "EXT_LENGTH")
    private double extLength;

    @Column(name = "COL_LENGTH")
    private double colLength;

    @Column(name = "UPPER_MOUNT")
    private String upperMount;

    @Column(name = "LOWER_MOUNT")
    private String lowerMount;

    /*@OneToOne
    @JoinColumn(name = "ITEM_ID")
    private ProductionItem item;
*/

    @Override
    public String toString() {
        return "ShockParameters{" +
                "paramID=" + paramID +
                ", extLength=" + extLength +
                ", colLength=" + colLength +
                ", upperMount='" + upperMount + '\'' +
                ", lowerMount='" + lowerMount + '\'' +
           //     ", item=" + item +
                '}';
    }

    public int getParamID() {
        return paramID;
    }

    public void setParamID(int paramID) {
        this.paramID = paramID;
    }

    public double getExtLength() {
        return extLength;
    }

    public void setExtLength(double extLength) {
        this.extLength = extLength;
    }

    public double getColLength() {
        return colLength;
    }

    public void setColLength(double colLength) {
        this.colLength = colLength;
    }

    public String getUpperMount() {
        return upperMount;
    }

    public void setUpperMount(String upperMount) {
        this.upperMount = upperMount;
    }

    public String getLowerMount() {
        return lowerMount;
    }

    public void setLowerMount(String lowerMount) {
        this.lowerMount = lowerMount;
    }

  /*  public ProductionItem getItem() {
        return item;
    }

    public void setItem(ProductionItem item) {
        this.item = item;
    }*/
}
