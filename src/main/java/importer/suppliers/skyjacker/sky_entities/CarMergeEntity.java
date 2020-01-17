package importer.suppliers.skyjacker.sky_entities;

import javax.persistence.*;

@Entity
@Table(name = "car_merge_data")
public class CarMergeEntity {

    @Id
    @Column(name = "ENTITY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int entityID;

    @Column(name = "SKY_YEAR")
    private int skyYear;

    @Column(name = "SKY_MAKE")
    private String skyMake;

    @Column(name = "SKY_MODEL")
    private String skyModel;

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


    @Override
    public String toString() {
        return "CarMergeEntity{" +
                "skyYear=" + skyYear +
                ", skyMake='" + skyMake + '\'' +
                ", skyModel='" + skyModel + '\'' +
                ", prodStart=" + prodStart +
                ", prodFinish=" + prodFinish +
                ", prodMake='" + prodMake + '\'' +
                ", prodModel='" + prodModel + '\'' +
                ", prodCarAttribute='" + prodCarAttribute + '\'' +
                '}';
    }

    public CarMergeEntity() {
    }

    public CarMergeEntity(CarMergeEntity entity) {
        this.skyMake = entity.getSkyMake();
        this.skyModel = entity.getSkyModel();
        this.prodStart = entity.getProdStart();
        this.prodFinish = entity.getProdFinish();
        this.prodMake = entity.getProdMake();
        this.prodModel = entity.getProdModel();
        this.prodCarAttribute = entity.getProdCarAttribute();
    }

    public int getSkyYear() {
        return skyYear;
    }

    public void setSkyYear(int skyYear) {
        this.skyYear = skyYear;
    }

    public String getSkyMake() {
        return skyMake;
    }

    public void setSkyMake(String skyMake) {
        this.skyMake = skyMake;
    }

    public String getSkyModel() {
        return skyModel;
    }

    public void setSkyModel(String skyModel) {
        this.skyModel = skyModel;
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
}
