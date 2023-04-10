package importer.suppliers.skyjacker;

import importer.HibernateUtil;
import importer.entities.*;
import importer.service.CarService;
import importer.suppliers.skyjacker.sky_entities.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.*;

public class SkyConverter {
    private static final Logger logger = LogManager.getLogger(SkyConverter.class.getName());
    private boolean fitSplitNeeded = false;
    public ProductionItem buildItem(SkyShock shock, Session session) {
        ProductionItem item = new ProductionItem();
        setItemFields(shock, item);
        setFitments(item, shock, session);

        return item;
    }

    private void setFitments(ProductionItem item, SkyShock shock, Session session) {
        Set<SkyFitment> fitments = shock.getSkyFitments();
        Set<ProductionFitment> prodFits = new HashSet<>();
        fitments.forEach(skyFitment -> {
            ProductionFitment fitment = getFitment(skyFitment, session);
            prodFits.add(fitment);
        });
        verifyModels(prodFits, session);
        CarService.setCarYearPeriods(prodFits);
        Set<ProductionFitment> finalFits = checkFitPositions(prodFits);
        item.setProductionFitments(finalFits);
    }

    //Iterates all fits and splits those, where positions are both front and rear
    private Set<ProductionFitment> checkFitPositions(Set<ProductionFitment> prodFits) {
        Set<ProductionFitment> result = new HashSet<>();
        prodFits.forEach(curFit->{
            if (fitSplitNeeded){
                result.addAll(getSplittedFits(curFit));
            }
            else {
                renameFitAtts(curFit);
                result.add(curFit);
            }
        });
        
        return result;
    }

    private Set<ProductionFitment> getSplittedFits(ProductionFitment curFit) {
        Set<ProductionFitment> result = new HashSet<>();
        ProductionFitment frontFit = new ProductionFitment();
        frontFit.setCar(curFit.getCar());
        Set<FitmentAttribute> frontAtts = new HashSet<>();
        curFit.getFitmentAttributes().forEach(curFitAtt->{
            String attName = curFitAtt.getFitmentAttName();
            if (attName.equals("r_Lift_s")||attName.equals("r_Lift_f")){
                return;
            }
            if (attName.equals("Position")&&curFitAtt.getFitmentAttValue().equals("Rear")){
                return;
            }
            if (attName.contains("Rear")){
                return;
            }
            if (attName.equals("f_Lift_s")){
                frontAtts.add(new FitmentAttribute("Lift Start", curFitAtt.getFitmentAttValue()+""));
                return;
            }
            if (attName.equals("f_Lift_f")){
                frontAtts.add(new FitmentAttribute("Lift Finish", curFitAtt.getFitmentAttValue()+""));
                return;
            }
            frontAtts.add(curFitAtt);
        });
        frontFit.setFitmentAttributes(frontAtts);
        result.add(frontFit);

        ProductionFitment rearFit = new ProductionFitment();
        rearFit.setCar(curFit.getCar());
        Set<FitmentAttribute> rearAtts = new HashSet<>();
        curFit.getFitmentAttributes().forEach(curFitAtt->{
            String attName = curFitAtt.getFitmentAttName();
            if (attName.equals("f_Lift_s")||attName.equals("f_Lift_f")){
                return;
            }
            if (attName.equals("Position")&&curFitAtt.getFitmentAttValue().equals("Front")){
                return;
            }
            if (attName.contains("Front")){
                return;
            }
            if (attName.equals("r_Lift_s")){
                rearAtts.add(new FitmentAttribute("Lift Start", curFitAtt.getFitmentAttValue()+""));
                return;
            }
            if (attName.equals("r_Lift_f")){
                rearAtts.add(new FitmentAttribute("Lift Finish", curFitAtt.getFitmentAttValue()+""));
                return;
            }
            rearAtts.add(curFitAtt);
        });
        rearFit.setFitmentAttributes(rearAtts);
        result.add(rearFit);

        return result;
    }

    private void renameFitAtts(ProductionFitment curFit) {
        Set<FitmentAttribute> atts = curFit.getFitmentAttributes();
        atts.forEach(att->{
            String attName = att.getFitmentAttName();
            switch (attName){
                case "f_Lift_s":
                case "r_Lift_s":
                    att.setFitmentAttName("Lift Start"); break;
                case "f_Lift_f":
                case "r_Lift_f":
                    att.setFitmentAttName("Lift Finish"); break;
            }
        });
    }

    private void verifyModels(Set<ProductionFitment> prodFits, Session session) {
        prodFits.forEach(productionFitment -> {
            ProductionCar prodCar = productionFitment.getCar();
            ProductionCar testCar = SkyService.getExistingCar(prodCar, prodCar.getYearStart());
            if (testCar!=null){
                return; //it means that make and model exist and its ok for us
            }
            CarMergeEntity carMergeEntity = CarService.getCarMergeEntity(prodCar);
            if (carMergeEntity!=null){
                prodCar.setMake(carMergeEntity.getProdMake());
                prodCar.setModel(carMergeEntity.getProdModel());
                String att = carMergeEntity.getProdCarAttribute();
                if (att!=null&&att.length()>0){
                    CarAttribute attribute = new CarAttribute();
                    attribute.setCarAttName("Model Attribute");
                    attribute.setCarAttValue(att);
                    prodCar.getAttributes().add(attribute);
                }
            }
            else {
                logger.error("No car merge entity for car " + prodCar);
            }
        });
    }

    private ProductionFitment getFitment(SkyFitment skyFitment, Session session) {
        ProductionFitment fitment = new ProductionFitment();
        ProductionCar car = buildProductionCar(skyFitment.getFitString(), session);
        Set<FitmentAttribute> attributes = getFitmentAttributes(skyFitment);
        fitSplitNeeded = new LiftBuilder().buildLifts(attributes);
        fitment.setCar(car);
        fitment.setFitmentAttributes(attributes);

        return fitment;
    }

    private Set<FitmentAttribute> getFitmentAttributes(SkyFitment skyFitment) {
        Set<FitmentNote> notes = skyFitment.getFitNotes();
        Set<FitmentAttribute> attributes = new HashSet<>();
        Map<String, String> fNoteMap = getFitNoteMap();//k = wrong value, v = right value;
        notes.forEach(note->{
            FitmentAttribute attribute = new FitmentAttribute();
            attribute.setFitmentAttName("Note");
            String fNote = note.getFitNote();
            if (fNoteMap.containsKey(fNote)){
                fNote = fNoteMap.get(fNote);
            }
            if (fNote.equals("DELETE")){
                return;
            }
            if (fNote.contains("Dual OE ")){
                FitmentAttribute a1 = new FitmentAttribute();
                a1.setFitmentAttName("Note");
                a1.setFitmentAttValue("Dual OE");
                attributes.add(a1);
                fNote = fNote.replace("Dual OE ", "");
                if (fNote.length()==0){
                   return;
                }
            }
            if (fNote.contains(" Forward of Axle")){
                FitmentAttribute a1 = new FitmentAttribute();
                a1.setFitmentAttName("Note");
                a1.setFitmentAttValue("Forward of Axle");
                attributes.add(a1);
                fNote = fNote.replace(" Forward of Axle", "");
            }

            attribute.setFitmentAttValue(fNote);
            attributes.add(attribute);
        });

        return attributes;
    }

    private Map<String, String> getFitNoteMap() {
        Map<String, String> result = new HashMap<>();
        result.put("Rear with 7.5 in. Lif", "Rear with 7.5 in. Lift");
        result.put("Rear with 5.5 in. Lif", "Rear with 5.5 in. Lift");
        result.put("4WD", "DELETE");
        result.put("Rear with 3 in.", "Rear with 3 in. Lift");
        result.put("Rear with 2 in. L", "Rear with 2 in. Lift");
        result.put("Rear with 2 in. Lif", "Rear with 2 in. Lift");
        result.put("Rear with 1 in. L", "Rear with 1 in. Lift");
        result.put("Rear with 4.5 in. Lif", "Rear with 4.5 in. Lift");
        result.put("Rear with 4 in. Lif", "Rear with 4 in. Lift");
        result.put("2WD", "DELETE");
        result.put("1-2.5 inches of lift", "1-2.5 in. Lift");
        result.put("RWD", "DELETE");
        result.put("Rear with 5 in", "Rear with 5 in. Lift");
        result.put("Front with 3.5 in.", "Front with 3.5 in. Lift");
        result.put("Front Dual OE", "Dual OE "); //attention
        result.put("Rear with 0.5", "Rear with 0.5 in. Lift");
        result.put("Front with 5.5 in. Li", "Front with 5.5 in. Lift");
        result.put("Front Dual OE with 2 in. Lift Forward o", "Front Dual OE with 2 in. Lift Forward of Axle");
        result.put("Rear with 0 in.", "Rear with 0 in. Lift");
        result.put("Rear with 4.5 i", "Rear with 4.5 in. Lift");
        result.put("Rear with 5 in. Lif", "Rear with 5 in. Lift");
        result.put("Rear with 3.5 in. Lif", "Rear with 3.5 in. Lift");
        result.put("Rear with 8.5 in. Lif", "Rear with 8.5 in. Lift");
        result.put("Rear with 6 in. L", "Rear with 6 in. Lift");
        result.put("Rear with 1.5", "Rear with 1.5 in. Lift");
        result.put("Rear with 5 in.", "Rear with 5 in. Lift");
        result.put("Rear with 8 in", "Rear with 8 in. Lift");
        result.put("Rear with 4 in. Li", "Rear with 4 in. Lift");
        result.put("Rear with 8 in. Lif", "Rear with 8 in. Lift");
        result.put("Rear with 9 in. Li", "Rear with 9 in. Lift");
        result.put("Rear with 6.5 in", "Rear with 6.5 in. Lift");
        result.put("Rear with 7 in. Li", "Rear with 7 in. Lift");
        result.put("Rear with 5 in. Li", "Rear with 5 in. Lift");
        result.put("Front with 9 in", "Front with 9 in. Lift");
        result.put("Front Dual OE w", "Dual OE "); //attention
        result.put("Front with 6 in", "Front with 6 in. Lift");
        result.put("Rear with 0 in. Lif", "Rear with 0 in. Lift");
        result.put("Front Dual OE with 5 in", "Front Dual OE with 5 in. Lift");
        result.put("Front Dual OE with 7", "Front Dual OE with 7 in. Lift");
        result.put("Front Dual OE with 7.5", "Front Dual OE with 7.5 in. Lift");
        result.put("Front Dual OE with 4.", "Front Dual OE with 4.5 in. Lift");
        result.put("Rear with 4.5 in.", "Rear with 4.5 in. Lift");
        result.put("Rear with 1.5 in.", "Rear with 1.5 in. Lift");
        result.put("Rear with 3 in. Li", "Rear with 3 in. Lift");
        result.put("Front with 3 in.", "Front with 3 in. Lift");
        result.put("Front Dual OE with", "Dual OE "); //attention
        result.put("Front with 3", "Front with 3 in. Lift");
        result.put("Rear with 4.5 in. Li", "Rear with 4.5 in. Lift");
        result.put("Front with 5", "Front with 5 in. Lift");
        result.put("Front with 6", "Front with 6 in. Lift");
        result.put("Front with 6 in.", "Front with 6 in. Lift");
        result.put("Front with 0.5 in. L", "Front with 0.5 in. Lift");
        result.put("Front Dual OE with 2", "Front Dual OE with 2 in. Lift");
        result.put("Front with 0", "Front with 0 in. Lift");
        result.put("Rear with 3.5 in. L", "Rear with 3.5 in. Lift");
        result.put("Rear with 3 in. Lif", "Rear with 3 in. Lift");
        result.put("Front Dual OE with 6.5", "Front Dual OE with 6.5 in. Lift");
        result.put("Rear with 1.5 in", "Rear with 1.5 in. Lift");
        result.put("Rear with 2.5 in", "Rear with 2.5 in. Lift");
        result.put("Rear with 8 in.", "Rear with 8 in. Lift");
        result.put("Front Dual OE with 8.5 in. Lift Forw", "Front Dual OE with 8.5 in. Lift Forward Of Axle");
        result.put("Rear with 4.5 in", "Rear with 4.5 in. Lift");
        result.put("Rear with 0-1 in.", "Rear with 0-1 in. Lift");
        result.put("Rear with 4 in.", "Rear with 4 in. Lift");
        result.put("Rear with 1 in. Li", "Rear with 1 in. Lift");
        result.put("Front with 7", "Front with 7 in. Lift");
        result.put("Rear with 6 in. Li", "Rear with 6 in. Lift");
        result.put("Front with 2.5 in.", "Front with 2.5 in. Lift");
        result.put("Front with 7 in.", "Front with 7 in. Lift");
        result.put("Rear with 3 in. L", "Rear with 3 in. Lift");
        result.put("Rear with 2 in. Li", "Rear with 2 in. Lift");
        result.put("Rear with 0.5 in. L", "Rear with 0.5 in. Lift");
        result.put("Rear with 4.5-10 in.", "Rear with 4.5-10 in. Lift");
        result.put("Rear 0 - 2 in. Lift", "Rear with 0-2 in. Lift");

        return result;
    }

    public ProductionCar buildProductionCar(String fitString, Session session) {
        ProductionCar car = new ProductionCar();
        //field setting order is essential.
        setDrive(car, fitString);
        setMake(car, fitString);
        setModel(car, fitString, session);
        setStartFinish(car, fitString, session);//sets year from fitStr to start and finish
        setCarAttributes(car, fitString);
        checkDrive(car);

      /*  int carYear = Integer.parseInt(fitString.split(" ")[0]);;
        getModelCarMergeInfo(car, carYear, session);

        logger.info("Built car " + car);*/

        return car;
    }

    private void checkDrive(ProductionCar car) {
        String drive = car.getDrive();
        if (drive!=null&&drive.equals("2WD")){
            car.setDrive("RWD");
        }
    }

    private void getModelCarMergeInfo(ProductionCar car, int carYear, Session session) {
        //years differ only when car exists in db
        if (car.getYearStart()!=car.getYearFinish()){
            return;
        }
        //checking if car exists, but start and finish are equal
        ProductionCar simCar = SkyService.getExistingCar(car, carYear);
        if (simCar!=null){
            return;
        }
        CarMergeEntity entity = CarService.getCarMergeEntity(car);
        if (entity==null){
            logger.error("NO CarMergeEntity for " + car);
            HibernateUtil.shutdown();
            SkyHibernateUtil.shutdown();
            System.exit(1);
        }

        car.setYearStart(entity.getProdStart());
        car.setYearFinish(entity.getProdFinish());
        car.setMake(entity.getProdMake());
        car.setModel(entity.getProdModel());

        String carAttribute = entity.getProdCarAttribute();
        if (carAttribute!=null&&carAttribute.length()>0){
            CarAttribute attribute = new CarAttribute();
            attribute.setCarAttName("Model attribute");
            attribute.setCarAttName(carAttribute);
            car.getAttributes().add(attribute);
        }
    }

    private void setCarAttributes(ProductionCar car, String fitString) {
        String attPart = StringUtils.substringAfter(fitString, car.getDrive());
        if (attPart.length()==0){
            return;
        }
        if (attPart.contains("GAS")){
            CarAttribute attribute = new CarAttribute("Engine", "GAS");
            attPart = attPart.replace("GAS", "");
            attPart = attPart.trim();
            car.getAttributes().add(attribute);
        }
        if (attPart.contains("DIESEL")){
            CarAttribute attribute = new CarAttribute("Engine", "DIESEL");
            attPart = attPart.replace("DIESEL", "");
            attPart = attPart.trim();
            car.getAttributes().add(attribute);
        }
        if (attPart.length()>0){
            CarAttribute attribute = new CarAttribute("For", attPart);
            car.getAttributes().add(attribute);
        }
    }

    private void setStartFinish(ProductionCar car, String fitString, Session session) {
        int carYear = Integer.parseInt(fitString.split(" ")[0]);
        car.setYearStart(carYear);
        car.setYearFinish(carYear);

      /*  ProductionCar existingCar = SkyService.getExistingCar(car, carYear);
        if (existingCar==null){
            car.setYearStart(carYear);
            car.setYearFinish(carYear);
            return;
        }
        car.setYearStart(existingCar.getYearStart());
        car.setYearFinish(existingCar.getYearFinish());*/
    }

    private void setModel(ProductionCar car, String fitString, Session session) {
        String rawModelStr = StringUtils.substringBetween(fitString, car.getMake(), car.getDrive());
        rawModelStr = rawModelStr.trim();
        logger.debug("raw model string " + rawModelStr);
        car.setModel(rawModelStr);/*
        if (SkyService.modelExists(rawModelStr, session)){
            car.setModel(rawModelStr);
         //   return;
        }
        else{
            car.setModel("NO MODEL");
        }*/
        //equalizing sky models to existing models:

       /* checkIfModelPresent(rawModelStr, car);
        if (car.getModel()!=null&&car.getModel().length()!=0){
            logger.info(car.getModel());
            return;
        }
        checkIfReplacementNeeded(rawModelStr, car);
        if (car.getModel()!=null&&car.getModel().length()!=0){
            logger.info(car.getModel());
            return;
        }
        car.setModel(rawModelStr);*/
    }

    private void checkIfReplacementNeeded(String rawModelStr, ProductionCar car) {
        if (rawModelStr.equals("C2500 Pickup")){
            car.setModel("C25/C2500 Pickup");
            return;
        }
        if (rawModelStr.equals("K35 Pickup")||rawModelStr.equals("K3500 Pickup")){
            car.setModel("K35/K3500 Pickup");
            return;
        }
        if (rawModelStr.equals("C1500 Pickup")){
            car.setModel("C15/C1500 Pickup");
            return;
        }
        if (rawModelStr.equals("Blazer K5")){
            car.setModel("K5 Blazer");
            return;
        }
        if (rawModelStr.equals("K2500 Pickup")||rawModelStr.equals("K25 Pickup")){
            car.setModel("K25/K2500 Pickup");
            return;
        }
        if (rawModelStr.equals("K15/K1500")){
            car.setModel("K15/K1500 Suburban");
            return;
        }
        if (rawModelStr.equals("K1500 Pickup")){
            car.setModel("K15/K1500 Pickup");
            return;
        }
        if (rawModelStr.equals("C3500 Pickup")){
            car.setModel("C35/C3500 Pickup");
        }
    }

    private void checkIfModelPresent(String rawModelStr, ProductionCar car) {
        checkSubPart(rawModelStr,"Wrangler", car);
        if (car.getModel() != null ) {
            return;
        }
        checkSubPart(rawModelStr,"Grand Cherokee", car);
        if (car.getModel() != null ) {
           return;
        }
        checkSubPart(rawModelStr,"Cherokee", car);
        if (car.getModel() != null ) {
            return;
        }
        checkSubPart(rawModelStr,"W150", car);
        if (car.getModel() != null ) {
            return;
        }
        checkSubPart(rawModelStr,"W250", car);
        if (car.getModel() != null ) {
            return;
        }
        checkSubPart(rawModelStr,"W350", car);
        if (car.getModel() != null ) {
            return;
        }
        checkSubPart(rawModelStr,"Ram 50", car);
        if (car.getModel() != null ) {
            return;
        }
        checkSubPart(rawModelStr,"Trooper", car);
        if (car.getModel() != null ) {
            return;
        }
        checkSubPart(rawModelStr,"F-250", car);
        if (car.getModel() != null ) {
            return;
        }
        checkSubPart(rawModelStr,"S10", car);
        if (car.getModel() != null ) {
            return;
        }
        checkSubPart(rawModelStr,"K2500 Suburban", car);
        if (car.getModel() != null ) {
            return;
        }
        checkSubPart(rawModelStr,"D50", car);
    }

    private void checkSubPart(String rawModelStr, String modelName, ProductionCar car) {
        if (rawModelStr.contains(modelName)){
            car.setModel(modelName);
            String attValue = rawModelStr.replace(modelName, "");
            attValue = attValue.trim();
            CarAttribute attribute = new CarAttribute("Model properties", attValue);
            car.getAttributes().add(attribute);
        }
    }

    private void setMake(ProductionCar car, String fitString) {
        String[] split = fitString.split(" ");
        logger.debug(fitString);
        car.setMake(split[1]);
    }

    private void setDrive(ProductionCar car, String fitLine) {
        String drive = "";
        if (fitLine.contains("2WD/4WD")){
            drive = "2WD/4WD";
        }
        else if (fitLine.contains("AWD")){
            drive = "AWD";
        }
        else if (fitLine.contains("4WD")){
            drive = "4WD";
        }
        else {
            drive = "2WD";
        }

        car.setDrive(drive);
    }

    private void setItemFields(SkyShock shock, ProductionItem item) {
        setItemPartNo(shock, item);
        setItemManufacturer(shock, item);
        setItemType(shock, item);
        setItemAttributes(shock, item);
        setItemPics(shock, item);
        //setShockParams(shock, item);
    }

    private void setShockParams(SkyShock shock, ProductionItem item) {
        Set<ItemAttribute> itemAttributes = item.getItemAttributes();
        ShockParameters params = new ShockParameters();
      //  params.setItem(item);
    //    item.setParams(params);
        Map<String, String> mountMap = getMountMap(); //k = mount name from sj site, v = mount name from prod. db.
        itemAttributes.forEach(itemAttribute -> {
            String attName = itemAttribute.getItemAttName();
            if (attName.equals("Collapsed Length")){
                String val = itemAttribute.getItemAttValue();
                double length = getLength(val, shock);
                params.setColLength(length);
            }
            if (attName.equals("Extended Length")){
                String val = itemAttribute.getItemAttValue();
                double length = getLength(val, shock);
                params.setExtLength(length);
            }
            if (attName.equals("Lower Mounting Code")){
                String val = mountMap.get(itemAttribute.getItemAttValue());
                if (val!=null&&val.length()>0){
                    params.setLowerMount(val);
                }
            }
            if (attName.equals("Upper Mounting Code")){
                String val = mountMap.get(itemAttribute.getItemAttValue());
                if (val!=null&&val.length()>0){
                    params.setUpperMount(val);
                }
            }
        });
    }

    private Map<String, String> getMountMap() {
        Map<String, String> result = new HashMap<>();
        result.put("ES25", "Eyelet");
        result.put("ES34", "Eyelet");
        result.put("ES82", "Eyelet");
        result.put("ES24", "Eyelet");
        result.put("ES36", "Eyelet");
        result.put("ES113", "Eyelet");
        result.put("Clevis", "Clevis");
        result.put("EB4", "Eyelet");
        result.put("BP4", "Bar Pin");
        result.put("ES34, ES60", "Eyelet");
        result.put("S1", "Stem");
        result.put("ES60, ES113", "Eyelet");
        result.put("ES22", "Eyelet");
        result.put("ES37", "Eyelet");
        result.put("BP13", "Bar Pin");
        result.put("BP8", "Bar Pin");
        result.put("ES33", "Eyelet");
        result.put("BP12", "Bar Pin");
        result.put("EB7", "Eyelet");
        result.put("S38", "Stem");
        result.put("ES31", "Eyelet");
        result.put("S59", "Stem");
        result.put("BP18", "Bar Pin");
        result.put("ES27", "Eyelet");

        return result;
    }

    private double getLength(String val, SkyShock s) {
        double d = 0d;
        try {
            d = Double.parseDouble(val);
        }
        catch (NumberFormatException e){
            logger.error("wrong length format for " + val + " at shock " + s);
        }

        return d;
    }

    private void setItemPics(SkyShock shock, ProductionItem item) {
        String itemPics = shock.getImgLinks();
        if (itemPics!=null&&itemPics.length()>0){
            Set<ItemPic> pics = new HashSet<>();
            item.setPics(pics);
            String[] split = itemPics.split("\r\n");
            for (String s : split) {
                if (s.length()>0){
                    ItemPic pic = convertToPic(s, item);
                    pics.add(pic);
                }
            }
        }
    }

    private void setItemAttributes(SkyShock shock, ProductionItem item) {
        Set<SpecAndKitNote> skNotes = shock.getNotes();
        Set<ItemAttribute> attributes = new HashSet<>();
        skNotes.forEach(note->{
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName(note.getName());
            attribute.setItemAttValue(note.getValue());
            attributes.add(attribute);
            logger.debug(attribute);
        });
        Set<Category> categories = shock.getCategories();
        categories.forEach(category -> {
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Category");
            attribute.setItemAttValue(category.getName());
            attributes.add(attribute);
            logger.debug(attribute);
        });
        item.setItemAttributes(attributes);
    }

    private void setItemType(SkyShock shock, ProductionItem item) {
        item.setItemType("Shock Absorber");
    }

    private void setItemManufacturer(SkyShock shock, ProductionItem item) {
        item.setItemManufacturer("Skyjacker");
    }

    private void setItemPartNo(SkyShock shock, ProductionItem item) {
        item.setItemPartNo(shock.getSku());
    }

    private ItemPic convertToPic(String imgLink, ProductionItem item) {
        ItemPic result = new ItemPic();
        result.setPicUrl(imgLink);
        result.setItem(item);

        return result;
    }
}
