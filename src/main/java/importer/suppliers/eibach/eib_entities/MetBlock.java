package importer.suppliers.eibach.eib_entities;

import javax.persistence.*;

@Entity
@Table(name = "metric_blocks")
public class MetBlock {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "LENGTH_MM")
    private String length_mm;

    @Column(name = "DIAMETER_MM")
    private String diameter_mm;

    @Column(name = "RATE_LBS_IN")
    private String rate_lbs_to_in;

    @Column(name = "RATE_KG_MM")
    private String rate_kg_to_mm;

    @Column(name = "RATE_N_MM")
    private String rate_N_to_mm;

    @Column(name = "BLOCK_HEIGHT_MM")
    private String block_height_mm;

    @Column(name = "TRAVEL_MM")
    private String travel_mm;

    @Column(name = "BLOCK_LOAD_N")
    private String block_load_N;

    @Column(name = "WEIGHT_KGS")
    private String weight_kgs;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private EibItem item;

    @Override
    public String toString() {
        return "MetBlock{" +
                "length_mm='" + length_mm + '\'' +
                ", diameter_mm='" + diameter_mm + '\'' +
                ", rate_lbs_to_in='" + rate_lbs_to_in + '\'' +
                ", rate_kg_to_mm='" + rate_kg_to_mm + '\'' +
                ", rate_N_to_mm='" + rate_N_to_mm + '\'' +
                ", block_height_mm='" + block_height_mm + '\'' +
                ", travel_mm='" + travel_mm + '\'' +
                ", block_load_N='" + block_load_N + '\'' +
                ", weight_kgs='" + weight_kgs + '\'' +
                '}';
    }

    public String getLength_mm() {
        return length_mm;
    }

    public void setLength_mm(String length_mm) {
        this.length_mm = length_mm;
    }

    public String getDiameter_mm() {
        return diameter_mm;
    }

    public void setDiameter_mm(String diameter_mm) {
        this.diameter_mm = diameter_mm;
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

    public String getRate_N_to_mm() {
        return rate_N_to_mm;
    }

    public void setRate_N_to_mm(String rate_N_to_mm) {
        this.rate_N_to_mm = rate_N_to_mm;
    }

    public String getBlock_height_mm() {
        return block_height_mm;
    }

    public void setBlock_height_mm(String block_height_mm) {
        this.block_height_mm = block_height_mm;
    }

    public String getTravel_mm() {
        return travel_mm;
    }

    public void setTravel_mm(String travel_mm) {
        this.travel_mm = travel_mm;
    }

    public String getBlock_load_N() {
        return block_load_N;
    }

    public void setBlock_load_N(String block_load_N) {
        this.block_load_N = block_load_N;
    }

    public String getWeight_kgs() {
        return weight_kgs;
    }

    public void setWeight_kgs(String weight_kgs) {
        this.weight_kgs = weight_kgs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EibItem getItem() {
        return item;
    }

    public void setItem(EibItem item) {
        this.item = item;
    }
}
