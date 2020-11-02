package importer.suppliers.eibach.eib_entities;

import javax.persistence.*;

@Entity
@Table(name = "standard_blocks")
public class StdBlock {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "LENGTH_IN")
    private String length_in;

    @Column(name = "DIAM_IN")
    private String diameter_in;

    @Column(name = "RATE_LB_IN")
    private String rate_lbs_to_in;

    @Column(name = "RATE_KG_MM")
    private String rate_kg_to_mm;

    @Column(name = "BLOCK_HEIGHT_IN")
    private String block_height_in;

    @Column(name = "TRAVEL_IN")
    private String travel_in;

    @Column(name = "BLOCK_LOAD")
    private String block_load_lbs;

    @Column(name = "WEIGHT")
    private String weight_lbs;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private EibItem item;

    @Override
    public String toString() {
        return "StdBlock{" +
                "length_in='" + length_in + '\'' +
                ", diameter_in='" + diameter_in + '\'' +
                ", rate_lbs_to_in='" + rate_lbs_to_in + '\'' +
                ", rate_kg_to_mm='" + rate_kg_to_mm + '\'' +
                ", block_height_in='" + block_height_in + '\'' +
                ", travel_in='" + travel_in + '\'' +
                ", block_load_lbs='" + block_load_lbs + '\'' +
                ", weight_lbs='" + weight_lbs + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLength_in() {
        return length_in;
    }

    public void setLength_in(String length_in) {
        this.length_in = length_in;
    }

    public String getDiameter_in() {
        return diameter_in;
    }

    public void setDiameter_in(String diameter_in) {
        this.diameter_in = diameter_in;
    }

    public String getRate_lbs_to_in() {
        return rate_lbs_to_in;
    }

    public void setRate_lbs_to_in(String rate_lbs_to_in) {
        this.rate_lbs_to_in = rate_lbs_to_in;
    }

    public String getRate_kg_to_mm() {
        return rate_kg_to_mm;
    }

    public void setRate_kg_to_mm(String rate_kg_to_mm) {
        this.rate_kg_to_mm = rate_kg_to_mm;
    }

    public String getBlock_height_in() {
        return block_height_in;
    }

    public void setBlock_height_in(String block_height_in) {
        this.block_height_in = block_height_in;
    }

    public String getTravel_in() {
        return travel_in;
    }

    public void setTravel_in(String travel_in) {
        this.travel_in = travel_in;
    }

    public String getBlock_load_lbs() {
        return block_load_lbs;
    }

    public void setBlock_load_lbs(String block_load_lbs) {
        this.block_load_lbs = block_load_lbs;
    }

    public String getWeight_lbs() {
        return weight_lbs;
    }

    public void setWeight_lbs(String weight_lbs) {
        this.weight_lbs = weight_lbs;
    }

    public EibItem getItem() {
        return item;
    }

    public void setItem(EibItem item) {
        this.item = item;
    }
}
