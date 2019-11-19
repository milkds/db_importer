package importer.suppliers.skyjacker.sky_entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sk_notes")
public class SpecAndKitNote {

    @Id
    @Column(name = "NOTE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer noteID;

    @Column(name = "NAME")
    private String name;

    @Column(name = "VALUE")
    private String value;

    @ManyToMany(mappedBy = "notes")
    private Set<SkyShock> shocks = new HashSet<>();

    @Override
    public String toString() {
        return "SpecAndKitNote{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<SkyShock> getShocks() {
        return shocks;
    }

    public void setShocks(Set<SkyShock> shocks) {
        this.shocks = shocks;
    }

    public Integer getNoteID() {
        return noteID;
    }

    public void setNoteID(Integer noteID) {
        this.noteID = noteID;
    }
}
