package importer.export;

import java.util.List;
import java.util.StringJoiner;

public class ExportEntity {
    private String id;
    private String partNo;
    private String itemType;
    private String brand;
    private String extendedLength; //itemAtts
    private String collapsedLength; //itemAtts
    private String upperMount; //itemAtts
    private String lowerMount; //itemAtts
    private String imgFileNames;
    private String itemAttributes; //itemAtts
    private String itemPics;
    private String carCategories;
    private List<ExportFitEntity> fitEntities;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getExtendedLength() {
        return extendedLength;
    }

    public void setExtendedLength(String extendedLength) {
        this.extendedLength = extendedLength;
    }

    public String getCollapsedLength() {
        return collapsedLength;
    }

    public void setCollapsedLength(String collapsedLength) {
        this.collapsedLength = collapsedLength;
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

    public String getImgFileNames() {
        return imgFileNames;
    }

    public void setImgFileNames(String imgFileNames) {
        this.imgFileNames = imgFileNames;
    }

    public String getItemAttributes() {
        return itemAttributes;
    }

    public void setItemAttributes(String itemAttributes) {
        this.itemAttributes = itemAttributes;
    }

    public void setItemPics(String itemPics) {
        this.itemPics = itemPics;
    }

    public String getItemPics() {
        return itemPics;
    }

    public void setCarCategories(String allCarCats) {
        this.carCategories = allCarCats;
    }

    public String getCarCategories() {
        return carCategories;
    }

    public List<ExportFitEntity> getFitEntities() {
        return fitEntities;
    }

    public void setFitEntities(List<ExportFitEntity> fitEntities) {
        this.fitEntities = fitEntities;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ExportEntity.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("partNo='" + partNo + "'")
                .add("itemType='" + itemType + "'")
                .add("brand='" + brand + "'")
                .toString();
    }
}
