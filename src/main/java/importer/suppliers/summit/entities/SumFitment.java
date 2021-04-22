package importer.suppliers.summit.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Entity
@Table(name = "sum_fits")
public class SumFitment {

    @Id
    @Column(name = "SUM_FIT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fitment", cascade = CascadeType.ALL)
    private List<SumFitAttribute> attributes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private SumItem item;

    public void setAttributes(List<SumFitAttribute> attributes) {
        this.attributes = attributes;
    }

    public List<SumFitAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return new StringJoiner(System.lineSeparator(), SumFitment.class.getSimpleName() + "[", "]")
                .add("attributes=" + attributes)
                .toString();
    }

    public void setItem(SumItem item) {
        this.item = item;
    }

    public SumItem getItem() {
        return item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
