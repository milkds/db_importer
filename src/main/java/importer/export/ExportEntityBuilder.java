package importer.export;

import importer.entities.*;
import importer.entities.links.CarAttributeLink;
import importer.entities.links.FitmentAttributeLink;
import importer.entities.links.ItemAttributeLink;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class ExportEntityBuilder {
    private static final Logger logger = LogManager.getLogger(ExportEntityBuilder.class.getName());
    private String GEN_DIVIDER;
    private static final String GEN_DIVIDER_SUBSTITUTE = "-_ ";
    private String ATT_DIVIDER;
    private static final String ATT_DIVIDER_SUBSTITUTE = "_- ";
    private List<ProductionItem> items;
    private Map<Integer, List<ItemAttribute>> itemAttMap; //k = item ID, v = List of attributes for this item
    private Map<Integer, List<FitmentAttribute>> fitAttMap; //k = fit ID, v = List of attributes for this fitment
    private Map<Integer, List<CarAttribute>> carAttMap; //k = car ID, v = List of attributes for this fitment
    private Map<Integer, List<String>> itemPicMap; //k = item ID, v = List of pic fileNames for this item
    private Map<Integer, ProductionFitment> fitMap; //k = fit ID, v = fitment (this will be used for fitAtts and Cars
    private Map<Integer, ProductionCar> carMap; //k = car ID, v = car
    private List<ProductionFitment> fits;
    private List<ProductionCar> cars;

    /*public ExportEntityBuilder() {
        GEN_DIVIDER = "; ";
        ATT_DIVIDER = ": ";
    }*/

    ExportEntity buildExportEntity(ProductionItem item) {
        ExportEntity result = new ExportEntity();
        setBasicFields(item, result);
        processItemAttributes(item, result);
        processItemPics(item, result);
        setCarCategories(item, result);
        processFits(item, result);
     //   new AttributeGrouper(result, GEN_DIVIDER).removeAttributeDuplicates();//car

        return result;
    }

    private void processFits(ProductionItem item, ExportEntity result) {
        int itemID = item.getItemID();
        List<ProductionFitment> fitsForItem = getFitsForItem(itemID);
        List<ExportFitEntity> fitEntities = new ArrayList<>();
        fitsForItem.forEach(fit->{
            ExportFitEntity fitEntity = new ExportFitEntity();
            processFitmentAttributes(fit, fitEntity);
            setDefaultLifts(fitEntity);
            setCarFields(fit, fitEntity);
            setLiftCarField(fitEntity);
            setCarYearField(fitEntity);
            fitEntities.add(fitEntity);
        });
        result.setFitEntities(fitEntities);
    }

    private void setDefaultLifts(ExportFitEntity fitEntity) {
        String liftStart = fitEntity.getLiftStart();
        if (liftStart==null||liftStart.length()==0){
            fitEntity.setLiftStart(0+"");
        }
        else {
            try {
                double st = Double.parseDouble(liftStart);
            }
            catch (NumberFormatException e){
                fitEntity.setLiftStart(0+"");
            }
        }
        String liftFinish = fitEntity.getLiftFinish();
        if (liftFinish==null||liftFinish.length()==0){
            fitEntity.setLiftFinish(0+"");
        }
        else {
            try {
                double fn = Double.parseDouble(liftFinish);
            }
            catch (NumberFormatException e){
                fitEntity.setLiftFinish(0+"");
            }
        }
    }

    private void setCarYearField(ExportFitEntity fitEntity) {
        int yearStart = Integer.parseInt(fitEntity.getYearStart());
        int yearFinish = Integer.parseInt(fitEntity.getYearFinish());
        String make = fitEntity.getCarMake();
        String model = fitEntity.getCarModel();
        String carLine = make + " " + model + " " + yearStart + "-" + yearFinish+ " Year" + ATT_DIVIDER;
        StringBuilder carYearBuilder = new StringBuilder();
        int counter = 1;
        do {
            carYearBuilder.append(carLine);
            carYearBuilder.append(yearStart);
            carYearBuilder.append(ATT_DIVIDER);
            carYearBuilder.append(counter);
            carYearBuilder.append(ATT_DIVIDER);
            carYearBuilder.append("0");
            carYearBuilder.append(GEN_DIVIDER);
         //   carYearBuilder.append(System.lineSeparator());
            counter++;
            yearStart++;
        }
        while (yearStart<=yearFinish);
        String carYearString = carYearBuilder.toString();
        carYearString = StringUtils.substringBeforeLast(carYearString, GEN_DIVIDER);
        fitEntity.setCarYearAttributes(carYearString);
    }

    private void setLiftCarField(ExportFitEntity entity) {
       String liftStart = entity.getLiftStart();
       if (liftStart==null){
           entity.setCarLiftAttributes("");
           return;
       }
       String liftFinish = entity.getLiftFinish();
       if (liftFinish==null){
           entity.setCarLiftAttributes("");
           return;
       }
       double start = Double.parseDouble(liftStart);
       double finish = Double.parseDouble(liftFinish);
       String carLine = entity.getCarMake()+ " " +
                        entity.getCarModel()+ " " +
                        entity.getYearStart()+ "-" +
                        entity.getYearFinish()+ " Lift" + ATT_DIVIDER;
       StringBuilder liftBuilder = new StringBuilder();
       int counter = 1;
        do {
           liftBuilder.append(generateCarline(carLine, start, counter));
           counter++;
           start = start + 0.25;
        }
        while (start<finish);
        //checking if step isn't standard
        if (start!=finish&&start-finish!=0.25){
            liftBuilder.append(generateCarline(carLine, finish, counter));
        }
        String carLiftAttributes = liftBuilder.toString();
        carLiftAttributes = StringUtils.substringBeforeLast(carLiftAttributes, GEN_DIVIDER);
        entity.setCarLiftAttributes(carLiftAttributes);
    }

    private String generateCarline(String carLine, double liftAmount, int counter) {
        return carLine + liftAmount+ "\"" + ATT_DIVIDER + counter + ATT_DIVIDER
                + "0" + GEN_DIVIDER /*+ System.lineSeparator()*/;
    }

    private void setCarFields(ProductionFitment fit, ExportFitEntity fitEntity) {
        int carID = fit.getCar().getCarID();
        ProductionCar car = carMap.get(carID);
        fitEntity.setCarMake(car.getMake());
        fitEntity.setCarModel(car.getModel());
        fitEntity.setCarSubModel(car.getSubModel());
        fitEntity.setYearStart(car.getYearStart()+"");
        fitEntity.setYearFinish(car.getYearFinish()+"");
        fitEntity.setDrive(car.getDrive());
        processCarAttributes(car, fitEntity);
    }

    private void processCarAttributes(ProductionCar car, ExportFitEntity fitEntity) {
        List<CarAttribute> attributes = carAttMap.get(car.getCarID());
        if (attributes==null){
            fitEntity.setAllCarAttributes("");
            return;
        }
        StringBuilder carAttBuilder = new StringBuilder();
        attributes.forEach(carAttribute -> {
            carAttBuilder.append(carAttribute.getCarAttName());
            carAttBuilder.append(ATT_DIVIDER);
            carAttBuilder.append(carAttribute.getCarAttValue().replaceAll(GEN_DIVIDER, GEN_DIVIDER_SUBSTITUTE).replaceAll(ATT_DIVIDER, ATT_DIVIDER_SUBSTITUTE));
            carAttBuilder.append(GEN_DIVIDER);
        });
        String allCarAtts = carAttBuilder.toString();
        allCarAtts = StringUtils.substringBeforeLast(allCarAtts, GEN_DIVIDER);
        fitEntity.setAllCarAttributes(allCarAtts);
    }

    private void processFitmentAttributes(ProductionFitment fit, ExportFitEntity fitEntity) {
        List<FitmentAttribute> fitmentAttributes = fitAttMap.get(fit.getFitmentID());
        if (fitmentAttributes==null){
            fitmentAttributes = new ArrayList<>();
            fitmentAttributes.add(new FitmentAttribute("Lift Start", "0"));
            fitmentAttributes.add(new FitmentAttribute("Lift Finish", "0"));
        }
        StringBuilder fitAttBuilder = new StringBuilder();
        fitmentAttributes.forEach(fitmentAttribute -> {
            String attName = fitmentAttribute.getFitmentAttName();
            String attValue = fitmentAttribute.getFitmentAttValue();
            attValue =  attValue.replaceAll("\"","''");
            switch (attName){
                case "Position": fitEntity.setPosition(attValue); break;
                case "Lift Start": fitEntity.setLiftStart(attValue); break;
                case "Lift Finish": fitEntity.setLiftFinish(attValue); break;
                default: {
                    fitAttBuilder.append(attName);
                    fitAttBuilder.append(": ");
                    fitAttBuilder.append(attValue.replaceAll(GEN_DIVIDER, GEN_DIVIDER_SUBSTITUTE).replaceAll(ATT_DIVIDER, ATT_DIVIDER_SUBSTITUTE));
                    fitAttBuilder.append(GEN_DIVIDER);
                    break;
                }
            }
        });
        String allFitAtts = fitAttBuilder.toString();
        allFitAtts = StringUtils.substringBeforeLast(allFitAtts, GEN_DIVIDER);
        fitEntity.setAllOtherFitAttributes(allFitAtts);
    }

    private List<ProductionFitment> getFitsForItem(int itemID) {
        List<ProductionFitment> result = new ArrayList<>();
        fits.forEach(fit->{
            int fitItemID = fit.getItem().getItemID();
            if (itemID==fitItemID){
                result.add(fit);
            }
        });

        return result;
    }

    private void setCarCategories(ProductionItem item, ExportEntity result) {
        int itemID = item.getItemID();
        List<ProductionFitment> fitsForItem = getFitsForItem(itemID);
        Set<String> carCategories = new HashSet<>();
        fitsForItem.forEach(fit->{
            int carID = fit.getCar().getCarID();
            ProductionCar car = carMap.get(carID);
            String carCategory = car.getMake() +
                    " " +
                    car.getModel() +
                    " " +
                    car.getYearStart() +
                    "-" +
                    car.getYearFinish();
            carCategories.add(carCategory);
        });
        Set<String> treeCarCategories = new TreeSet<>(carCategories);
        StringBuilder carCatBuilder = new StringBuilder();
        treeCarCategories.forEach(carCat->{
            carCatBuilder.append(carCat);
            carCatBuilder.append(GEN_DIVIDER);
        });
        String allCarCats = carCatBuilder.toString();
        allCarCats = StringUtils.substringBeforeLast(allCarCats, GEN_DIVIDER);
        int length = allCarCats.length();
        //checking if quantity of car exceeds excel cell capacity
        if (length>32767){
            logger.info("Item " + item.getItemPartNo() + " has excessive car categories length of " + length);
        }
        result.setCarCategories(allCarCats);
    }

    private void processItemPics(ProductionItem item, ExportEntity result) {
        List<String> fNames = itemPicMap.get(item.getItemID());
        if (fNames==null||fNames.size()==0){
            result.setItemPics("");
            return;
        }
        StringBuilder itemPicBuilder = new StringBuilder();
        fNames.forEach(fName->{
            itemPicBuilder.append("http://presta1784/img/").
                    append(item.getItemManufacturer().toLowerCase()).
                    append("/").
                    append(fName).
                    append(GEN_DIVIDER);
        });
        String allItemPics = itemPicBuilder.toString();
        allItemPics = StringUtils.substringBeforeLast(allItemPics, GEN_DIVIDER);
        result.setItemPics(allItemPics);
    }

    private void processItemAttributes(ProductionItem item, ExportEntity entity) {
        List<ItemAttribute> itemAttributes = itemAttMap.get(item.getItemID());
        itemAttributes = fixNullFieldsForItemAtts(itemAttributes);
        StringBuilder itemAttFieldBuilder = new StringBuilder();
        itemAttributes.forEach(attribute -> {
            String name = attribute.getItemAttName();
            String value = attribute.getItemAttValue();
            value = value.replaceAll(GEN_DIVIDER, GEN_DIVIDER_SUBSTITUTE).replaceAll(ATT_DIVIDER, ATT_DIVIDER_SUBSTITUTE);
            switch (name){
                case "Upper Mount": entity.setUpperMount(value);  break;
                case "Lower Mount": entity.setLowerMount(value); break;
                case "Extended Length": entity.setExtendedLength(value); break;
                case "Collapsed Length": entity.setCollapsedLength(value); break;
                default:{
                    itemAttFieldBuilder.append(name);
                    itemAttFieldBuilder.append(": ");
                    itemAttFieldBuilder.append(processWrongSymbols(value));
                    itemAttFieldBuilder.append(GEN_DIVIDER);
                    break;
                }
            }
        });

        String allItemAttributes = itemAttFieldBuilder.toString();
        allItemAttributes = StringUtils.substringBeforeLast(allItemAttributes, GEN_DIVIDER);
        allItemAttributes = allItemAttributes.replaceAll(System.lineSeparator(),"");
        entity.setItemAttributes(allItemAttributes);
    }

    private String processWrongSymbols(String value) {
        String result = value.replaceAll(System.lineSeparator(), " ");
         result = result.replaceAll("®", "");
         result = result.replaceAll("™", "");
         result = result.replaceAll("\t", "");
         result = result.replaceAll("\n", "");
         result = result.replaceAll("\r", "");
         result = result.replaceAll("!", " ");

        return result;
    }

    private List<ItemAttribute> fixNullFieldsForItemAtts(List<ItemAttribute> itemAttributes) {
        List<ItemAttribute> result = new ArrayList<>();
        if (itemAttributes==null){
            return result;
        }
        itemAttributes.forEach(attribute -> {
            String value = attribute.getItemAttValue();
            if (value!=null&&value.length()>0){
                String name = attribute.getItemAttName();
                if (name==null||name.length()==0){
                    attribute.setItemAttName("Note");
                }
                result.add(attribute);
            }
        });

        return result;
    }

    private void setBasicFields(ProductionItem item, ExportEntity entity) {
        entity.setId(item.getItemID()+"");
        entity.setPartNo(item.getItemPartNo());
        entity.setItemType(item.getItemType());
        entity.setBrand(item.getItemManufacturer());
    }


    void initItemAttMap(List<ItemAttribute> itemAttributes, List<ItemAttributeLink> itemAttributeLinks) {
        Instant start = Instant.now();
        Map<Integer, ProductionItem> itemMap = new HashMap<>();
        items.forEach(item -> {
            itemMap.put(item.getItemID(),item);
        });
        itemAttMap = new HashMap<>();
        Map<Integer, ItemAttribute> tempAttMap = getTempItemAttMap(itemAttributes);//k = att id, v = itemAtt
        itemAttributeLinks.forEach(link->{
            int itemID = link.getItemID();
            ProductionItem item = itemMap.get(itemID);
            if (item==null){
                return;
            }
            int attID = link.getAttID();
            List<ItemAttribute> attributes = itemAttMap.computeIfAbsent(itemID, k -> new ArrayList<>());
            attributes.add(tempAttMap.get(attID));
        });

        /*items.forEach(item -> {
            int itemID = item.getItemID();
            itemAttributeLinks.forEach(link->{
                if (link.getItemID() == itemID){
                    List<ItemAttribute> attributes = itemAttMap.computeIfAbsent(itemID, k -> new ArrayList<>());
                    attributes.add(tempAttMap.get(link.getAttID()));
                }
            });
        });*/

        Instant end = Instant.now();
        logger.info (Duration.between(start, end));
    }

    private Map<Integer, ItemAttribute> getTempItemAttMap(List<ItemAttribute> itemAttributes) {
        Map<Integer, ItemAttribute> result = new HashMap<>();
        itemAttributes.forEach(attribute -> result.put(attribute.getItemAttID(), attribute));

        return result;
    }

    public void setItems(List<ProductionItem> items) {
        this.items = items;
    }

    //0 - itemID; 1 - fileName String
    void setItemPicMap(List<Object[]> picIds, String brand) {
        itemPicMap = new HashMap<>();
        picIds.forEach(pair->{
            String name = (String)pair[1];
            if (name==null||name.length()==0){
                return;
            }
            //name = "http://presta1784/img/" + brand.toLowerCase()+"/" + name;
            Integer itemID = (Integer)pair[0];
            List<String> fNames = itemPicMap.computeIfAbsent(itemID, k -> new ArrayList<>());
            fNames.add(name);
        });
    }

    void setFits(List<ProductionFitment> fitsByItems) {
        this.fits = fitsByItems;
    }

    List<ProductionFitment> getFits() {
        return fits;
    }

    void setFitAttributeMap(List<FitmentAttribute> allFitAttributes, List<FitmentAttributeLink> links) {
        Instant start = Instant.now();
        fitAttMap = new HashMap<>();
        fitMap = new HashMap<>();
        fits.forEach(fit-> fitMap.put(fit.getFitmentID(), fit));
        Map<Integer,FitmentAttribute> attributeMap = new HashMap<>();
        allFitAttributes.forEach(attribute-> attributeMap.put(attribute.getFitmentAttID(), attribute));
        links.forEach(link->{
            int fitID = link.getFitID();
            int attID = link.getAttID();
            List<FitmentAttribute> curAtts = fitAttMap.computeIfAbsent(fitID, k -> new ArrayList<>());
            curAtts.add(attributeMap.get(attID));
        });
        Instant end = Instant.now();
        logger.info("Grouped fits with attributes " + Duration.between(start, end));
    }

    public void setCars(List<ProductionCar> cars) {
        this.cars = cars;
    }

    public List<ProductionCar> getCars() {
        return cars;
    }

    void setCarAttributeMap(List<CarAttribute> attributes, List<CarAttributeLink> links) {
        Instant start = Instant.now();
        carAttMap = new HashMap<>();
        Map<Integer, CarAttribute> currentAttributeMap = new HashMap<>();
        attributes.forEach(att->currentAttributeMap.put(att.getCarAttID(), att));
        links.forEach(link->{
            int carID = link.getCarID();
            List<CarAttribute> curAtts = carAttMap.computeIfAbsent(carID, k -> new ArrayList<>());
            curAtts.add(currentAttributeMap.get(link.getAttID()));
        });
        Instant end = Instant.now();
        logger.info("Grouped fits with attributes " + Duration.between(start, end));
        carMap = new HashMap<>();
        cars.forEach(car->carMap.put(car.getCarID(), car));
    }

    public void setGEN_DIVIDER(String GEN_DIVIDER) {
        this.GEN_DIVIDER = GEN_DIVIDER;
    }

    public void setATT_DIVIDER(String ATT_DIVIDER) {
        this.ATT_DIVIDER = ATT_DIVIDER;
    }
}
