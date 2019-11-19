package importer.suppliers.skyjacker.sky_entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @Column(name = "CAT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryID;

    @Column(name = "CAT_NAME")
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<SkyShock> shocks = new HashSet<>();

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SkyShock> getShocks() {
        return shocks;
    }

    public void setShocks(Set<SkyShock> shocks) {
        this.shocks = shocks;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                '}';
    }
}
