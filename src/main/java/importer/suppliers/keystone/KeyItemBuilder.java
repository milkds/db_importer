package importer.suppliers.keystone;

import importer.entities.*;
import importer.service.CarService;
import importer.suppliers.keystone.entities.*;
import importer.suppliers.skyjacker.SkyService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KeyItemBuilder {
    public ProductionItem buildItem(KeyItem keyItem, Session keySession) {
        ProductionItem prodItem = new ProductionItem();
        setItemFields(prodItem, keyItem);
        setFitments(prodItem, keyItem);

        return prodItem;
    }

    private void setFitments(ProductionItem prodItem, KeyItem keyItem) {
        Set<ItemCar> keyFits = new HashSet<>(keyItem.getItemCars());
        Set<ProductionFitment> prodFits = new HashSet<>();
        keyFits.forEach(keyFit->{
            prodFits.add(getFitment(keyFit));
        });
    }

    private ProductionFitment getFitment(ItemCar keyFit) {
        ProductionFitment result = new ProductionFitment();
        setFitAttributes(result, keyFit);
        setCar(result, keyFit);

        return result;
    }

    private void setCar(ProductionFitment prodFit, ItemCar keyFit) {
        KeyCar keyCar = keyFit.getCar();
        ProductionCar prodCar = new ProductionCar();
        setMainCarFields(keyCar, prodCar);
        setCarAttributes(keyCar, prodCar);
        processCarAttString(keyCar, prodCar);
        verifyModels(keyCar, prodCar);
    }

    private void verifyModels(KeyCar keyCar, ProductionCar prodCar) {
        ProductionCar existingCar = SkyService.getExistingCar(prodCar, keyCar.getYear());
        if (existingCar!=null){
            return;
        }
        CarMergeEntity entity = CarService.getCarMergeEntity(prodCar);
        if (entity==null){
            System.out.println(prodCar.getMake() + "    " + prodCar.getModel() + "  " + keyCar);
        }

    }

    private void processCarAttString(KeyCar keyCar, ProductionCar prodCar) {
        String attStr = keyCar.getAttString();
        attStr = checkDrives(attStr, prodCar);
        attStr = checkEngines(attStr, prodCar);
        attStr = checkSuspension(attStr, prodCar);
        attStr = checkBodyStyle(attStr, prodCar);
        attStr = checkCab(attStr, prodCar);
        if (attStr!=null&&attStr.length()>0){
            CarAttribute attribute = new CarAttribute();
            attribute.setCarAttName("Note");
            attribute.setCarAttValue(attStr);
            prodCar.getAttributes().add(attribute);
        }
    }

    private String checkCab(String attStr, ProductionCar prodCar) {
        if (attStr==null||attStr.length()==0){
            return attStr;
        }
        List<String> cabs = getCabsList();
        for (String s : cabs) {
            if (attStr.contains(s)) {
                CarAttribute attribute = new CarAttribute();
                attribute.setCarAttName("Cab");
                attribute.setCarAttValue(s);
                prodCar.getAttributes().add(attribute);
                attStr = attStr.replace(s, "").trim();
                return attStr;
            }
        }

        return attStr;
    }

    private List<String> getCabsList() {
        List<String> result = new ArrayList<>();
        result.add("King Cab (Extended)");
        result.add("SuperCab (Extended)");
        result.add("Mega Cab (Long Crew)");
        result.add("Quad Cab (Crew)");
        result.add("Cab & Chassis - Crew Cab");
        result.add("Cab & Chassis - Extended Cab");
        result.add("Cab & Chassis - Standard Cab");
        result.add("Quad Cab (Extended)");
        result.add("Access Cab (Extended)");
        result.add("Double Cab (Extended)");
        result.add("Club Cab (Extended)");
        result.add("Crew Max");
        result.add("Xtra Cab (Extended)");
        result.add("(Crew)");
        result.add("Crew Cab");
        result.add("Standard Cab");
        result.add("Double Cab");
        result.add("Extended Cab");
        result.add("Cab & Chassis");
        result.add("SuperCrew");

        return result;
    }

    private String checkBodyStyle(String attStr, ProductionCar prodCar) {
        if (attStr==null||attStr.length()==0){
            return attStr;
        }
        List<String> bodyStyle = getBodyStyleList();
        for (String s : bodyStyle) {
            if (attStr.contains(s)) {
                CarAttribute attribute = new CarAttribute();
                attribute.setCarAttName("Body Style");
                attribute.setCarAttValue(s);
                prodCar.getAttributes().add(attribute);
                attStr = attStr.replace(s, "").trim();
                return attStr;
            }
        }

        return attStr;
    }

    private List<String> getBodyStyleList() {
        List<String> result = new ArrayList<>();
        result.add("Extended Cargo Van");
        result.add("Extended Passenger Van");
        result.add("Standard Cargo Van");
        result.add("Standard Passenger Van");
        result.add("Sedan");
        result.add("Wagon");
        result.add("Hardtop");
        result.add("Coupe");
        result.add("Hatchback");
        result.add("Touring");
        result.add("Cargo Van");
        result.add("Passenger Van");
        result.add("Cutaway Van");
        result.add("Spider");
        result.add("Convertible");
        result.add("Limousine");
        result.add("Koupe");
        result.add("Roadster");
        result.add("Estate");
        result.add("Stripped Chassis");

        return result;
    }

    private String checkSuspension(String attStr, ProductionCar prodCar) {
        if (attStr==null||attStr.length()==0){
            return attStr;
        }
        Set<String> suspension = getSuspSet();
        for (String s : suspension) {
            if (attStr.contains(s)) {
                CarAttribute attribute = new CarAttribute();
                attribute.setCarAttName("Suspension");
                attribute.setCarAttValue(s);
                prodCar.getAttributes().add(attribute);
                attStr = attStr.replace(s, "").trim();
                return attStr;
            }
        }
        return attStr;
    }

    private Set<String> getSuspSet() {
        Set<String> result = new HashSet<>();
        result.add("Front Coil");
        result.add("Front Leaf");
        result.add("Front Torsion Bar");
        result.add("Rear Leaf");
        result.add("Rear Coil");
        result.add("Front Air");
        result.add("Rear Air");


        return result;
    }

    private String checkEngines(String attStr, ProductionCar prodCar) {
        if (!attStr.contains(";")){
            return attStr;
        }
        String engineStr = StringUtils.substringBeforeLast(attStr, ";");
        String secondPart = attStr.replace(engineStr, "");
        secondPart = secondPart.replace("; ", "");
        secondPart = StringUtils.substringBefore(secondPart, " ");
        engineStr = engineStr + "; " + secondPart;
        attStr = attStr.replace(engineStr, "").trim();

        CarAttribute attribute = new CarAttribute();
        attribute.setCarAttName("Engine");
        attribute.setCarAttValue(engineStr);
        prodCar.getAttributes().add(attribute);

        return attStr;
    }

    private String checkDrives(String attStr, ProductionCar prodCar) {
        if (attStr==null||attStr.length()==0){
            return attStr;
        }
        if (!attStr.contains("Drive")){
            return attStr;
        }
        if (attStr.contains("Rear Wheel Drive")){
            prodCar.setDrive("RWD");
            attStr = attStr.replace("Rear Wheel Drive", "");
        }
        else if (attStr.contains("Four Wheel Drive")){
            prodCar.setDrive("4WD");
            attStr = attStr.replace("Four Wheel Drive", "");
        }
        else if (attStr.contains("Front Wheel Drive")){
            prodCar.setDrive("FWD");
            attStr = attStr.replace("Front Wheel Drive", "");
        }
        else if (attStr.contains("All Wheel Drive")){
            prodCar.setDrive("AWD");
            attStr = attStr.replace("All Wheel Drive", "");
        }
        attStr = attStr.trim();

        return attStr;
    }

    private void setCarAttributes(KeyCar keyCar, ProductionCar prodCar) {
        Set<KeyCarAttribute> keyAtts = keyCar.getAttributes();
        Set<CarAttribute> prodAtts = new HashSet<>();
        keyAtts.forEach(keyAtt->{
            CarAttribute prodAtt = new CarAttribute();
            prodAtt.setCarAttName("Car Attribute");
            prodAtt.setCarAttValue(keyAtt.getAttValue());
            prodAtts.add(prodAtt);
        });
        prodCar.setAttributes(prodAtts);
    }

    private void setMainCarFields(KeyCar keyCar, ProductionCar prodCar) {
        prodCar.setMake(keyCar.getMake());
        prodCar.setModel(keyCar.getModel());
        String[] split = keyCar.getStartFinish().split("-");
        prodCar.setYearStart(Integer.parseInt(split[0]));
        prodCar.setYearFinish(Integer.parseInt(split[1]));
    }

    private void setFitAttributes(ProductionFitment prodFit, ItemCar keyFit) {
        Set<ItemCarAttribute> keyAtts = new HashSet<>(keyFit.getAttributes());
        Set<FitmentAttribute> prodAtts = new HashSet<>();
        keyAtts.forEach(keyAtt->{
            FitmentAttribute prodAtt = new FitmentAttribute();
            prodAtt.setFitmentAttName(keyAtt.getAttName());
            prodAtt.setFitmentAttValue(keyAtt.getAttValue());
            prodAtts.add(prodAtt);
        });
        prodFit.setFitmentAttributes(prodAtts);
    }

    private void setItemFields(ProductionItem prodItem, KeyItem keyItem) {
        prodItem.setItemManufacturer(keyItem.getMake());
        prodItem.setItemPartNo(keyItem.getPartNo());
        prodItem.setItemType(getItemType(keyItem));

        setItemAttributes(prodItem, keyItem);
        setItemPics(prodItem, keyItem);
    }

    private void setItemPics(ProductionItem prodItem, KeyItem keyItem) {
        String rawPics = keyItem.getImgLinks();
        if (rawPics.startsWith("http")){
            ItemPic itemPic = new ItemPic();
            itemPic.setItem(prodItem);
            String url = StringUtils.substringBefore(rawPics, "jpg");
            itemPic.setPicUrl(url+"jpg");
            prodItem.getPics().add(itemPic);
            String attStr = StringUtils.substringAfter(rawPics, ";;");
            addItemAttribute("Image Validity", attStr, prodItem);
        }
        else {
            String[] split = rawPics.split(";;");
            String imgAtt = "";
            for (String line: split){
                String url = "http" + StringUtils.substringAfter(line, "http");
                String curImgAtt = StringUtils.substringBefore(line, "http");
                ItemPic itemPic = new ItemPic();
                itemPic.setItem(prodItem);
                itemPic.setPicUrl(url);
                prodItem.getPics().add(itemPic);
                if (curImgAtt.contains("differ")){
                    if (!imgAtt.contains("differ")){
                        imgAtt = curImgAtt;
                    }
                }
                else {
                    if (imgAtt.length()==0){
                        imgAtt = curImgAtt;
                    }
                }
            }
            addItemAttribute("Image Validity", imgAtt, prodItem);
        }
    }

    private void setItemAttributes(ProductionItem prodItem, KeyItem keyItem) {
        convertSpecsToAttributes(prodItem, keyItem); //also set params
        addItemAttribute("Short Description", keyItem.getShortDescription(), prodItem);
        addItemAttribute("Full Description", keyItem.getDescription(), prodItem);
        addItemAttribute("Features Description", keyItem.getFeatures(), prodItem);
        addItemAttribute("My Price", keyItem.getMyPrice().toString(), prodItem);
        addItemAttribute("Jobber Price", keyItem.getJobberPrice().toString(), prodItem);
        addItemAttribute("Retail Price", keyItem.getRetailPrice().toString(), prodItem);
    }

    private void convertSpecsToAttributes(ProductionItem prodItem, KeyItem keyItem) {
        ShockParameters params = new ShockParameters();
        List<KeyItemSpec> specs = keyItem.getSpecs();
        specs.forEach(spec->{
            String name = spec.getSpecName();
            switch (name) {
                case "Extended Length (IN)":
                    double extLength = getLength(spec.getSpecValue());
                    params.setExtLength(extLength);
                    if (extLength > 0) {
                        addItemAttribute("Extended Length", extLength + "", prodItem);
                    }
                    break;
                case "Compressed Length (IN)":
                    double comLength = getLength(spec.getSpecValue());
                    params.setColLength(comLength);
                    if (comLength > 0) {
                        addItemAttribute("Compressed Length", comLength + "", prodItem);
                    }
                    break;
                case "Upper Mounting Style":
                    params.setUpperMount(spec.getSpecValue());
                    addItemAttribute("Upper Mount", spec.getSpecValue(), prodItem);
                    break;
                case "Lower Mounting Style":
                    params.setLowerMount(spec.getSpecValue());
                    addItemAttribute("Lower Mount", spec.getSpecValue(), prodItem);
                    break;
                default:
                    addItemAttribute(spec.getSpecName(), spec.getSpecValue(), prodItem);
                    break;
            }
        });
        prodItem.setParams(params);
    }

    private String getItemType(KeyItem keyItem) {
        String result = "";
        String rawStr = keyItem.getShortDescription();
        result = StringUtils.substringBefore(rawStr, ";");

        return result;
    }

    private double getLength(String specValue) {
        specValue = specValue.replace(" Inch", "");
        double result = 0d;
        try {
            result = Double.parseDouble(specValue);
        }
        catch (NumberFormatException ignored){}

        return result;
    }

    private void addItemAttribute(String name, String value, ProductionItem item) {
        ItemAttribute attribute = new ItemAttribute();
        attribute.setItemAttName(name);
        attribute.setItemAttValue(value);
        item.getItemAttributes().add(attribute);
    }
}
