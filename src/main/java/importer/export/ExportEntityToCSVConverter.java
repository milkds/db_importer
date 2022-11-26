package importer.export;

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
            //we group by carLine
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
        String itemPart = getItemPart(itemAttributes); //returns itemPart with Gen Divider at the end
        StringBuilder attBuilder = new StringBuilder(itemPart);

        //we group by carLine and then by LiftLine
        Map<String, List<ExportFitEntity>> groupedFitEntities = groupFitEntities(fitEntities); //k = carLine, v = fitEntities
        groupedFitEntities.forEach((k,v)->{
            Map<String, List<ExportFitEntity>> liftMap = groupFitEntitiesByLift(v); //k = lift, v = entities for lift
            liftMap.forEach((k1,v1)->{
               ExportCarFitEntity carFitEntity = new AttributeGrouper(v1, GEN_DIVIDER, ATT_DIVIDER).getGroupedAttributes();//car and fit
                addCarAttributes(carFitEntity, attBuilder);
                addFitAttributes(carFitEntity, attBuilder);
                addFixedAttributes(carFitEntity, attBuilder, carFitEntity.getCarLine());
            });
        });

        String allAttributes = attBuilder.toString();
        csvEntity.setAllAttributes(allAttributes);
    }

    private void addFixedAttributes(ExportCarFitEntity carFitEntity, StringBuilder attBuilder, String carLine) {
        Set<String> positions = carFitEntity.getPositions();
        appendCounters(positions, attBuilder, carLine, "Position");
        Set<String> drives = carFitEntity.getDrives();
        appendCounters(drives, attBuilder, carLine, "Drive");
        attBuilder.append(carFitEntity.getCarYearAttributes()).append(GEN_DIVIDER);
        attBuilder.append(carFitEntity.getCarLiftAttributes()).append(GEN_DIVIDER);
    }

    private void addFitAttributes(ExportCarFitEntity carFitEntity, StringBuilder attBuilder) {
        Map<String, Set<String>> fitAttributeMap = carFitEntity.getFitAttMap();
        fitAttributeMap = new ExportAttributeFilter().filterFitAttributes(fitAttributeMap);
        appendAttributeMap(fitAttributeMap, attBuilder, carFitEntity.getCarLine());
    }

    private void addCarAttributes(ExportCarFitEntity carFitEntity, StringBuilder attBuilder) {
        Map<String, Set<String>> carAttributesMap = carFitEntity.getCarAttMap();
        carAttributesMap = new ExportAttributeFilter().filterCarAttributes(carAttributesMap);
        appendAttributeMap(carAttributesMap, attBuilder, carFitEntity.getCarLine());
    }

    private void appendAttributeMap(Map<String, Set<String>> attributesMap, StringBuilder attBuilder, String carLine) {
        //k = name of attribute, v = list of attribute values
        attributesMap.forEach((k,v)->{
           appendCounters(v, attBuilder, carLine, k);
        });
    }

    private void appendCounters(Set<String> attValues, StringBuilder attBuilder, String carLine, String attName) {
        int counter = 1;
        for (String attValue : attValues) {
            attBuilder.append(carLine).append(" ");
            attBuilder.append(attName).append(ATT_DIVIDER);
            attBuilder.append(attValue).append(ATT_DIVIDER);
            attBuilder.append(counter++).append(ATT_DIVIDER);
            attBuilder.append(0).append(GEN_DIVIDER);
        }
    }

    private Map<String, List<ExportFitEntity>> groupFitEntitiesByLift(List<ExportFitEntity> entities) {
        Map<String, List<ExportFitEntity>> result = new HashMap<>();
        entities.forEach(entity->{
            String liftString = entity.getLiftStart()+entity.getLiftFinish();
            List<ExportFitEntity> curEntities = result.computeIfAbsent(liftString, k -> new ArrayList<>());
            curEntities.add(entity);
        });

        return result;
    }

    private String getItemPart(String itemAttributes) {
        String result = "";
        String tempCarLine = "Carline"; //this needed because att building is common procedure including carLine
        String itemAttsLine = getAttributeLine(itemAttributes, tempCarLine);
        itemAttsLine = itemAttsLine.replaceAll(tempCarLine + " ", "");
        result = itemAttsLine +GEN_DIVIDER;

        return result;
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
        Map<String, Set<String>> itemAttributes = groupItemAttributes(attributeLine);
        StringBuilder attBuilder = new StringBuilder();
        appendAttributeMap(itemAttributes, attBuilder, carLine);
        String desc = attBuilder.toString();
        desc = StringUtils.substringBeforeLast(desc, GEN_DIVIDER);

        return desc;
    }

    private Map<String, Set<String>> groupItemAttributes(String attributeLine) {
        Map<String, Set<String>> result = new HashMap<>();
        String[] genSplit = attributeLine.split(GEN_DIVIDER);
        for (String s: genSplit){
            String[] attSplit = s.split(ATT_DIVIDER);
            if (new ExportAttributeFilter().itemValueIsValid(attSplit[0], attSplit[1])){
                Set<String> curAtts = result.computeIfAbsent(attSplit[0], k -> new HashSet<>());
                curAtts.add(attSplit[1]);
            }

        }

        return result;
    }

    private void setLongDescription(List<ExportFitEntity> fitEntities, ExportCSVEntity csvEntity,
                                                                  String itemAttributes) {
        Map<String, List<ExportFitEntity>> result = groupFitEntities(fitEntities);
        StringBuilder descBuilder = new StringBuilder();
        ExportAttributeFilter filter = new ExportAttributeFilter();
        //k = carLine, v = list of grouped Entities
        result.forEach((k,v)->{
            Map<String, List<ExportFitEntity>> liftMap = groupFitEntitiesByLift(v); //k = lift, v = entities for lift
            liftMap.forEach((k1,v1)-> {
                ExportCarFitEntity carFitEntity = new AttributeGrouper(v1, GEN_DIVIDER, ATT_DIVIDER).getGroupedAttributes();//car and fit
                descBuilder.append(carFitEntity.getCarLine()).append(GEN_DIVIDER);
                appendCarAttributes(carFitEntity.getCarAttMap(), descBuilder, filter);
                appendFitAttributes(carFitEntity.getFitAttMap(), descBuilder, filter);
                /*fitEntities.forEach(fitEntity -> {
                    String carLine = getCarLine(fitEntity);
                    descBuilder.append(carLine).append(GEN_DIVIDER);
                    descBuilder.append(fitEntity.getAllCarAttributes()).append(GEN_DIVIDER);
                    descBuilder.append(fitEntity.getAllOtherFitAttributes()).append(GEN_DIVIDER);
                });*/
                String desc = descBuilder.toString();
                desc = StringUtils.substringBeforeLast(desc, GEN_DIVIDER);
                desc = itemAttributes + GEN_DIVIDER + desc;
                csvEntity.setLongDesc(desc);
            });
        });
    }

    private Map<String, Set<String>> appendFitAttributes(Map<String, Set<String>> fitAttMap, StringBuilder descBuilder,
                                                         ExportAttributeFilter filter) {
        Map<String, Set<String>> result = new HashMap<>();
        //k = attribute name, v = list of attribute values
        fitAttMap.forEach((k,v)->{
            v.forEach(value->{
                descBuilder.append(k).append(ATT_DIVIDER).append(value);
              /*  if (filter.fitValueIsValid(value)){
                    Set<String> curValues = result.computeIfAbsent(k, k1 -> new HashSet<>());
                    curValues.add(value);
                }*/
                descBuilder.append("{}");
            });
        });
        descBuilder.append(GEN_DIVIDER);

        return result;
    }


    private Map<String, Set<String>> appendCarAttributes(Map<String, Set<String>> carAttMap, StringBuilder descBuilder,
                                                         ExportAttributeFilter filter) {
        Map<String, Set<String>> result = new HashMap<>();
        //k = attribute name, v = list of attribute values
        carAttMap.forEach((k,v)->{
            v.forEach(value->{
                descBuilder.append(k).append(ATT_DIVIDER).append(value);
               /* if (filter.carValueIsValid(value)){
                    Set<String> curValues = result.computeIfAbsent(k, k1 -> new HashSet<>());
                    curValues.add(value);
                }*/
                descBuilder.append("{}");
            });
        });
        descBuilder.append(GEN_DIVIDER);

        return result;
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
