package importer.suppliers.summit;


public class UnknownApplicationNoteError extends Throwable {
    private String unknownPart;


    public String getUnknownPart() {
        return unknownPart;
    }

    public void setUnknownPart(String unknownPart) {
        this.unknownPart = unknownPart;
    }
}
