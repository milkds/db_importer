package importer.entities;

import javax.persistence.*;

@Entity
@Table(name = "car_merge_data")
public class CarMergeEntity {

    @Id
    @Column(name = "ENTITY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int entityID;

    @Column(name = "SKY_YEAR")
    private int excelYear;

    @Column(name = "SKY_MAKE")
    private String excelMake;

    @Column(name = "SKY_MODEL")
    private String excelModel;

    @Column(name = "PROD_START")
    private int prodStart;

    @Column(name = "PROD_FINISH")
    private int prodFinish;

    @Column(name = "PROD_MAKE")
    private String prodMake;

    @Column(name = "PROD_MODEL")
    private String prodModel;

    @Column(name = "PROD_CAR_ATT")
    private String prodCarAttribute;

    @Column(name = "PROD_SUBMODEL")
    private String prodSubModel;

    @Column(name = "PROD_BODY")
    private String prodBody;


    @Override
    public String toString() {
        return "CarMergeEntity{" +
                "excelYear=" + excelYear +
                ", excelMake='" + excelMake + '\'' +
                ", excelModel='" + excelModel + '\'' +
                ", prodStart=" + prodStart +
                ", prodFinish=" + prodFinish +
                ", prodMake='" + prodMake + '\'' +
                ", prodModel='" + prodModel + '\'' +
                ", prodCarAttribute='" + prodCarAttribute + '\'' +
                ", prodSubModel='" + prodSubModel + '\'' +
                ", prodBody='" + prodBody + '\'' +
                '}';
    }

    public CarMergeEntity() {
    }

    public CarMergeEntity(CarMergeEntity entity) {
        this.excelMake = entity.getExcelMake();
        this.excelModel = entity.getExcelModel();
        this.prodStart = entity.getProdStart();
        this.prodFinish = entity.getProdFinish();
        this.prodMake = entity.getProdMake();
        this.prodModel = entity.getProdModel();
        this.prodCarAttribute = entity.getProdCarAttribute();
    }

    public int getExcelYear() {
        return excelYear;
    }

    public void setExcelYear(int excelYear) {
        this.excelYear = excelYear;
    }

    public String getExcelMake() {
        return excelMake;
    }

    public void setExcelMake(String excelMake) {
        this.excelMake = excelMake;
    }

    public String getExcelModel() {
        return excelModel;
    }

    public void setExcelModel(String excelModel) {
        this.excelModel = excelModel;
    }

    public int getProdStart() {
        return prodStart;
    }

    public void setProdStart(int prodStart) {
        this.prodStart = prodStart;
    }

    public int getProdFinish() {
        return prodFinish;
    }

    public void setProdFinish(int prodFinish) {
        this.prodFinish = prodFinish;
    }

    public String getProdMake() {
        return prodMake;
    }

    public void setProdMake(String prodMake) {
        this.prodMake = prodMake;
    }

    public String getProdModel() {
        return prodModel;
    }

    public void setProdModel(String prodModel) {
        this.prodModel = prodModel;
    }

    public String getProdCarAttribute() {
        return prodCarAttribute;
    }

    public void setProdCarAttribute(String prodCarAttribute) {
        this.prodCarAttribute = prodCarAttribute;
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public String getProdSubModel() {
        return prodSubModel;
    }

    public void setProdSubModel(String prodSubModel) {
        this.prodSubModel = prodSubModel;
    }

    public String getProdBody() {
        return prodBody;
    }

    public void setProdBody(String prodBody) {
        this.prodBody = prodBody;
    }
}
