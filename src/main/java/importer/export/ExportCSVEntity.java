package importer.export;

public class ExportCSVEntity {
    private String id;
    private String sku;
    private String title;
    private String shortDesc;
    private String longDesc;
    private String allAttributes;
    private ExportEntity exportEntity;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public String getAllAttributes() {
        return allAttributes;
    }

    public void setAllAttributes(String allAttributes) {
        this.allAttributes = allAttributes;
    }

    public ExportEntity getExportEntity() {
        return exportEntity;
    }

    public void setExportEntity(ExportEntity exportEntity) {
        this.exportEntity = exportEntity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
