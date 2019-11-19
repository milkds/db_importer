package importer.suppliers.skyjacker.sky_entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "fitment_notes")
public class FitmentNote {

    @Id
    @Column(name = "NOTE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fitNoteID;

    @Column(name = "NOTE_NAME")
    private String fitNote;

    @ManyToMany(mappedBy = "fitNotes")
    private Set<SkyFitment> skyFitments = new HashSet<>();

    public String getFitNote() {
        return fitNote;
    }
    public void setFitNote(String fitNote) {
        this.fitNote = fitNote;
    }
    public Integer getFitNoteID() {
        return fitNoteID;
    }
    public void setFitNoteID(Integer fitNoteID) {
        this.fitNoteID = fitNoteID;
    }
    public Set<SkyFitment> getSkyFitments() {
        return skyFitments;
    }
    public void setSkyFitments(Set<SkyFitment> skyFitments) {
        this.skyFitments = skyFitments;
    }

    @Override
    public String toString() {
        return "FitmentNote{" +
                "fitNote='" + fitNote + '\'' +
                ", skyFitments=" + skyFitments +
                '}';
    }
}
