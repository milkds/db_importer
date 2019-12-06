package importer.suppliers.skyjacker;

import importer.entities.*;
import importer.suppliers.skyjacker.sky_entities.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.Set;

public class SkyConverter {
    private static final Logger logger = LogManager.getLogger(SkyConverter.class.getName());
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
        item.setProductionFitments(prodFits);
    }

    private ProductionFitment getFitment(SkyFitment skyFitment, Session session) {
        ProductionFitment fitment = new ProductionFitment();
        ProductionCar car = buildProductionCar(skyFitment.getFitString(), session);
        Set<FitmentAttribute> attributes = getFitmentAttributes(skyFitment);
        fitment.setCar(car);
        fitment.setFitmentAttributes(attributes);

        return fitment;
    }

    private Set<FitmentAttribute> getFitmentAttributes(SkyFitment skyFitment) {
        Set<FitmentNote> notes = skyFitment.getFitNotes();
        Set<FitmentAttribute> attributes = new HashSet<>();
        notes.forEach(note->{
            FitmentAttribute attribute = new FitmentAttribute();
            attribute.setFitmentAttName("Note");
            attribute.setFitmentAttValue(note.getFitNote());
            attributes.add(attribute);
        });

        return attributes;
    }

    private ProductionCar buildProductionCar(String fitString, Session session) {
        ProductionCar car = new ProductionCar();
        //field setting order is essential.
        setDrive(car, fitString);
        setMake(car, fitString);
        setModel(car, fitString, session);
        setStartFinish(car, fitString, session);
        setCarAttributes(car, fitString);
        logger.info("Built car " + car);

        return car;
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
            CarAttribute attribute = new CarAttribute("NoNameAttribute", attPart);
            car.getAttributes().add(attribute);
        }
    }

    private void setStartFinish(ProductionCar car, String fitString, Session session) {
        int carYear = Integer.parseInt(fitString.split(" ")[0]);
        ProductionCar existingCar = SkyService.getExistingCar(car, carYear, session);
        if (existingCar==null){
            car.setYearStart(carYear);
            car.setYearFinish(carYear);
            return;
        }
        car.setYearStart(existingCar.getYearStart());
        car.setYearFinish(existingCar.getYearFinish());
    }

    private void setModel(ProductionCar car, String fitString, Session session) {
        String rawModelStr = StringUtils.substringBetween(fitString, car.getMake(), car.getDrive());
        rawModelStr = rawModelStr.trim();
        logger.debug("raw model string " + rawModelStr);
        if (SkyService.modelExists(rawModelStr, session)){
            car.setModel(rawModelStr);
            return;
        }
        //equalizing sky models to existing models:

        checkIfModelPresent(rawModelStr, car);
        if (car.getModel()!=null&&car.getModel().length()!=0){
            logger.info(car.getModel());
            return;
        }
        checkIfReplacementNeeded(rawModelStr, car);
        if (car.getModel()!=null&&car.getModel().length()!=0){
            logger.info(car.getModel());
            return;
        }
        car.setModel(rawModelStr);
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
