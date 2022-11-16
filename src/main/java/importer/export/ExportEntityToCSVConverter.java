package importer.export;

import importer.suppliers.skyjacker.sky_entities.Category;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

class ExportEntityToCSVConverter {
    private static final Logger logger = LogManager.getLogger(ExportEntityToCSVConverter.class.getName());
    private String GEN_DIVIDER;
    private String ATT_DIVIDER;

    public ExportEntityToCSVConverter() {
        try (InputStream input = new FileInputStream("src\\main\\resources\\exportProperties.xml")) {
         /*   Properties prop = new Properties();
            prop.load(input);
            ATT_DIVIDER = prop.getProperty("att_divider");
            GEN_DIVIDER = prop.getProperty("gen_divider");*/
         ATT_DIVIDER = ": ";
         GEN_DIVIDER = "; ";
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    List<ExportCSVEntity> getCSVEntityList(List<ExportEntity> entityList) {
        Instant start = Instant.now();
        List<ExportCSVEntity> result = new ArrayList<>();
        entityList.forEach(entity -> {
            ExportCSVEntity csvEntity = new ExportCSVEntity();
            setItemFields (csvEntity, entity);
            List<ExportFitEntity> fitEntities = entity.getFitEntities();
            setMixedCategories(fitEntities, csvEntity); //mixed categories = short desc
            setLongDescription(fitEntities, csvEntity, entity.getItemAttributes());
            setAllAttributes(fitEntities, csvEntity, entity.getItemAttributes());
            csvEntity.setExportEntity(entity);
            result.add(csvEntity);
        });
        Instant end = Instant.now();
        logger.info("Built all CSV Export Entities in " + Duration.between(start, end));

        return result;
    }

    private void setAllAttributes(List<ExportFitEntity> fitEntities, ExportCSVEntity csvEntity, String itemAttributes) {
        StringBuilder attBuilder = new StringBuilder();
        String tempCarLine = "Carline"; //this needed because att building is common procedure including carLine
        String itemAttsLine = getAttributeLine(itemAttributes, tempCarLine);
        itemAttsLine = itemAttsLine.replaceAll(tempCarLine + " ", "");
        attBuilder.append(itemAttsLine).append(GEN_DIVIDER);
        Map<String, List<ExportFitEntity>> groupedFitEntities = groupFitEntities(fitEntities); //k = carLine, v = fitEntities
        groupedFitEntities.forEach((k,v)->{
        new DuplicateAttributeRemover(v, GEN_DIVIDER).removeAttributeDuplicates();//car and fit
        });

        fitEntities.forEach(fitEntity->{
            String carLine = getCarLine(fitEntity);
            setCarPart(attBuilder, fitEntity, carLine);
            setFitPart(attBuilder, fitEntity, carLine);
            attBuilder.append(fitEntity.getCarYearAttributes()).append(GEN_DIVIDER);
            attBuilder.append(fitEntity.getCarLiftAttributes());
            attBuilder.append(GEN_DIVIDER);
        });
        String allAttributes = attBuilder.toString();
        csvEntity.setAllAttributes(allAttributes);
    }

    private Map<String, List<ExportFitEntity>> groupFitEntities(List<ExportFitEntity> fitEntities) {
        Map<String, List<ExportFitEntity>> result = new HashMap<>();
        fitEntities.forEach(fitEntity->{
            String carLine = getCarLine(fitEntity);
            List<ExportFitEntity> curList = result.computeIfAbsent(carLine, k -> new ArrayList<>());
            curList.add(fitEntity);
        });

        return result;
    }

    private void setFitPart(StringBuilder attBuilder, ExportFitEntity fitEntity, String carLine) {
        String position = fitEntity.getPosition();
        if (position!=null&&position.length()>0){
            attBuilder.append(carLine).append(" Position").append(ATT_DIVIDER).append(position);
            attBuilder.append(ATT_DIVIDER).append(1).append(ATT_DIVIDER).append(0).append(GEN_DIVIDER);
        }
        String fitAtts = getAttributeLine(fitEntity.getAllOtherFitAttributes(), carLine);
        attBuilder.append(fitAtts).append(GEN_DIVIDER);
    }

    private void setCarPart(StringBuilder attBuilder, ExportFitEntity fitEntity, String carLine) {
        attBuilder.append(carLine).append(" Drive").append(ATT_DIVIDER).append(fitEntity.getDrive());
        attBuilder.append(ATT_DIVIDER).append(1).append(ATT_DIVIDER).append(0).append(GEN_DIVIDER);
        String carAtts = getAttributeLine(fitEntity.getAllCarAttributes(), carLine);
        attBuilder.append(carAtts).append(GEN_DIVIDER);
    }

    private String getAttributeLine(String attributeLine, String carLine) {
        if (attributeLine==null||attributeLine.length()==0){
            return "";
        }
        String[] split = attributeLine.split(GEN_DIVIDER);
        int counter = 1;
        StringBuilder attBuilder = new StringBuilder();
        for (String attPair: split){
            attBuilder.append(carLine).append(" ");
            attBuilder.append(attPair).append(ATT_DIVIDER);
            attBuilder.append(counter++).append(ATT_DIVIDER);
            attBuilder.append("0").append(GEN_DIVIDER);
        }
        String desc = attBuilder.toString();
        desc = StringUtils.substringBeforeLast(desc, GEN_DIVIDER);

        return desc;
    }

    private void setLongDescription(List<ExportFitEntity> fitEntities, ExportCSVEntity csvEntity, String itemAttributes) {
        StringBuilder descBuilder = new StringBuilder();
        fitEntities.forEach(fitEntity->{
            String carLine = getCarLine(fitEntity);
            descBuilder.append(carLine).append(GEN_DIVIDER);
            descBuilder.append(fitEntity.getAllCarAttributes()).append(GEN_DIVIDER);
            descBuilder.append(fitEntity.getAllOtherFitAttributes()).append(GEN_DIVIDER);
        });
        String desc = descBuilder.toString();
        desc = StringUtils.substringBeforeLast(desc, GEN_DIVIDER);
        desc = itemAttributes + GEN_DIVIDER + desc;
        csvEntity.setLongDesc(desc);
    }

    private void setMixedCategories(List<ExportFitEntity> fitEntities, ExportCSVEntity csvEntity) {
        StringBuilder carBuilder = new StringBuilder();
        fitEntities.forEach(fitEntity->{
            String liftLine = getLiftLine(fitEntity);
            String carLine = getCarLine(fitEntity);
            String position = fitEntity.getPosition();
            carBuilder.append(carLine);
            if (liftLine.length()>0){
                carBuilder.append(" ").append(liftLine);
            }
            if (position!=null&&position.length()>0){
                carBuilder.append(" ").append(position);
            }
            if (liftLine.length()>0){
                carBuilder.append(" lift");
            }
            carBuilder.append(GEN_DIVIDER);
        });
        String shortDesc = carBuilder.toString();
        shortDesc = StringUtils.substringBeforeLast(shortDesc, GEN_DIVIDER);
        shortDesc = csvEntity.getTitle() + " " + shortDesc;
        csvEntity.setShortDesc(shortDesc);
    }

    private String getCarLine(ExportFitEntity fitEntity) {
        return fitEntity.getCarMake() + " " +
                fitEntity.getCarModel() + " " +
                fitEntity.getYearStart() + "-" + fitEntity.getYearFinish();
    }

    private String getLiftLine(ExportFitEntity fitEntity) {
        String result = "";
        String liftStart = fitEntity.getLiftStart();
        String liftFinish = fitEntity.getLiftFinish();
        if (liftStart.equals("0")&&liftFinish.equals("0")){
            return "";
        }
        result = liftStart + "-" + liftFinish+"\"";

        return result;
    }

    private void setItemFields(ExportCSVEntity csvEntity, ExportEntity entity) {
        csvEntity.setId(entity.getId());
        csvEntity.setSku(entity.getPartNo());
        String title = getTitle(entity);
        csvEntity.setTitle(title);
    }

    private String getTitle(ExportEntity entity) {

        return entity.getPartNo()+ " " +
                        entity.getItemType() + " " +
                        entity.getBrand();
    }

    public void setGEN_DIVIDER(String GEN_DIVIDER) {
        this.GEN_DIVIDER = GEN_DIVIDER;
    }

    public void setATT_DIVIDER(String ATT_DIVIDER) {
        this.ATT_DIVIDER = ATT_DIVIDER;
    }
}
