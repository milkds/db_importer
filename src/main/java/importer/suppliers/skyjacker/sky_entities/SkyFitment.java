package importer.suppliers.skyjacker.sky_entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "fitments")
public class SkyFitment {

    @Id
    @Column(name = "FIT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fitID;

    @Column(name = "FIT_STRING")
    private String fitString;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "fit_2_fit_notes",
            joinColumns = { @JoinColumn(name = "FIT_ID") },
            inverseJoinColumns = { @JoinColumn(name = "NOTE_ID") }
    )
    private Set <FitmentNote> fitNotes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "SHOCK_ID")
    private SkyShock shock;

    public String getFitString() {
        return fitString;
    }
    public void setFitString(String fitString) {
        this.fitString = fitString;
    }
    public Set<FitmentNote> getFitNotes() {
        return fitNotes;
    }
    public void setFitNotes(Set<FitmentNote> fitNotes) {
        this.fitNotes = fitNotes;
    }
    public Integer getFitID() {
        return fitID;
    }
    public void setFitID(Integer fitID) {
        this.fitID = fitID;
    }
    public SkyShock getSkyShock() {
        return shock;
    }
    public void setSkyShock(SkyShock shock) {
        this.shock = shock;
    }
}
