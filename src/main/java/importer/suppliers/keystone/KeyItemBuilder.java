package importer.suppliers.keystone;

import importer.entities.*;
import importer.service.CarService;
import importer.suppliers.keystone.entities.*;
import importer.suppliers.skyjacker.SkyService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;

import java.util.*;

public class KeyItemBuilder {
    private Map<String, Set<String>> bilMakeModelMap;

    public KeyItemBuilder(Map<String, Set<String>> bilMakeModelMap) {
        this.bilMakeModelMap = bilMakeModelMap;
    }

    public ProductionItem buildItem(KeyItem keyItem, Set<String> subModels, Session keySession) {
        ProductionItem prodItem = new ProductionItem();
        setItemFields(prodItem, keyItem);
        if (wrongItemType(prodItem)){
            return null;
        }

        setFitments(prodItem, keyItem, subModels);

        return prodItem;
    }

    private boolean wrongItemType(ProductionItem prodItem) {
        String type = prodItem.getItemType();
        Set<String> wrongTypes = getWrongItemTypes();
        if (wrongTypes.contains(type)){
            return true;
        }
        return false;
    }

    private Set<String> getWrongItemTypes() {
        Set<String>result = new HashSet<>();
        result.add("Inner Fender");
        result.add("Fender Vent Insert");
        result.add("Auto Trans Shifter Linkage");
        result.add("Shifter Knob");
        result.add("Grease Gun Adapter Needle");
        result.add("Wheel");

        return result;
    }

    private void setFitments(ProductionItem prodItem, KeyItem keyItem, Set<String> subModels) {
        Set<ItemCar> keyFits = new HashSet<>(keyItem.getItemCars());
        Set<ProductionFitment> prodFits = new HashSet<>();
        keyFits.forEach(keyFit->{
            ProductionFitment fitment = getFitment(keyFit, subModels);
            fitment.setItem(prodItem);
            prodFits.addAll(splitFit(fitment)); //in case if position is front and rear simultaneously
        });
        prodItem.setProductionFitments(prodFits);
    }

    private List<ProductionFitment> splitFit(ProductionFitment fitment) {
        List<ProductionFitment> result = new ArrayList<>();
        Set<FitmentAttribute> atts = fitment.getFitmentAttributes();
        boolean front = false;
        boolean rear  = false;
        for (FitmentAttribute att: atts){
            String name = att.getFitmentAttName();
            if (name.equals("Position")){
                String val = att.getFitmentAttValue();
                if (val.equals("Front")){
                    front = true;
                }
                else {
                    rear = true;
                }
            }
        }
        if (front&&rear){
            Set<FitmentAttribute> baseAtts = new HashSet<>();
            for (FitmentAttribute att: atts){
                String name = att.getFitmentAttName();
                if (!name.equals("Position")){
                   baseAtts.add(att);
                }
            }
            ProductionFitment newFit = new ProductionFitment();
            newFit.setCar(fitment.getCar());
            newFit.setItem(fitment.getItem());
            Set<FitmentAttribute> newAtts = new HashSet<>(baseAtts);
            newAtts.add(new FitmentAttribute("Position", "Rear"));
            newFit.setFitmentAttributes(newAtts);
            baseAtts.add(new FitmentAttribute("Position", "Front"));
            fitment.setFitmentAttributes(baseAtts);
        }
        result.add(fitment);

        return result;
    }

    private ProductionFitment getFitment(ItemCar keyFit, Set<String> subModels) {
        ProductionFitment result = new ProductionFitment();
        setFitAttributes(result, keyFit);
        setCar(result, keyFit, subModels);

        return result;
    }

    private void setCar(ProductionFitment prodFit, ItemCar keyFit, Set<String> subModels) {
        KeyCar keyCar = keyFit.getCar();
        ProductionCar prodCar = new ProductionCar();
        setMainCarFields(keyCar, prodCar);
        //setCarAttributes(keyCar, prodCar);
        processCarAttString(keyCar, prodCar, subModels);
      //  verifyModels(keyCar, prodCar);
        prodFit.setCar(prodCar);
    }

    private void verifyModels(KeyCar keyCar, ProductionCar prodCar) {
        ProductionCar existingCar = SkyService.getExistingCar(prodCar, keyCar.getYear());
        if (existingCar!=null){
            return;
        }
        String make = prodCar.getMake();
        if (bilMakeModelMap.containsKey(make)){
            String model = prodCar.getModel();
            if (bilMakeModelMap.get(make).contains(model)){
                return;
            }
        }
        CarMergeEntity entity = CarService.getCarMergeEntity(prodCar);
        if (entity==null){
            System.out.println(prodCar.getMake() + "    " + prodCar.getModel() + "  " + keyCar.getStartFinish());
        }

    }

    private void processCarAttString(KeyCar keyCar, ProductionCar prodCar, Set<String> subModels) {
        String attStr = keyCar.getAttString();
        attStr = checkDrives(attStr, prodCar);
        attStr = checkEngines(attStr, prodCar);
        attStr = checkSuspension(attStr, prodCar);
        attStr = checkBodyStyle(attStr, prodCar);
        attStr = checkCab(attStr, prodCar);
        attStr = checkDoors(attStr, prodCar);
        attStr = checkSubModels(attStr, prodCar, subModels);
        if (attStr!=null&&attStr.length()>0){
          //  System.out.println(attStr);
            CarAttribute attribute = new CarAttribute();
            attribute.setCarAttName("Note");
            attribute.setCarAttValue(attStr);
            prodCar.getAttributes().add(attribute);
        }
    }

    private String checkDoors(String attStr, ProductionCar prodCar) {
        if (attStr==null||attStr.length()==0){
            return "";
        }
        if (attStr.contains("(4 Door)")||attStr.contains("(2 Door)")){
            String doors = "";
            if (attStr.contains("(4 Door)")){
                doors = "4 Door";
                attStr = attStr.replace("(4 Door)", "").trim();
            }
            else {
                doors = "2 Door";
                attStr = attStr.replace("(2 Door)", "").trim();
            }
            CarAttribute attribute = new CarAttribute();
            attribute.setCarAttName("Doors");
            attribute.setCarAttValue(doors);
            prodCar.getAttributes().add(attribute);

        }

        return attStr;
    }

    private String checkSubModels(String attStr, ProductionCar prodCar, Set<String> subModels) {
        if (attStr!=null&&attStr.length()>0){
            if (subModels.contains(attStr)){
                prodCar.setSubModel(attStr);
                attStr = "";
            }
        }

        return attStr;
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
        result.add("Double Cab (Crew)");
        result.add("Club Cab (Extended)");
        result.add("Crew Max");
        result.add("Xtra Cab (Extended)");
        result.add("Crew Cab");
        result.add("Standard Cab");
        result.add("Double Cab");
        result.add("Extended Cab");
        result.add("Cab & Chassis");
        result.add("SuperCrew");
        result.add("(Crew)");

        return result;
    }

    private String checkBodyStyle(String attStr, ProductionCar prodCar) {
        if (attStr==null||attStr.length()==0){
            return attStr;
        }
        List<String> bodyStyle = getBodyStyleList();
        for (String s : bodyStyle) {
            if (attStr.contains(s)) {
                //Checking if att string contains subModel name "Power Wagon", because we have body style Wagon
                if (attStr.contains("Power Wagon")){
                    continue;
                }
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
        if (attStr.startsWith("Valves")){
            engineStr = engineStr + " Valves";
            attStr = attStr.replace("Valves", "").trim();
        }
        if (engineStr.endsWith("VIN")){
            if (attStr.length()==1){
                engineStr = engineStr + " " + attStr;
                attStr = "";
            }
            else {
                engineStr = engineStr + " " + attStr.charAt(0);
                attStr = attStr.substring(1).trim();
            }
        }

        CarAttribute attribute = new CarAttribute();
        attribute.setCarAttName("Engine");
        attribute.setCarAttValue(engineStr);
        prodCar.getAttributes().add(attribute);

        if (attStr.startsWith("ENG.")){
            attStr = attStr.replace("ENG.","").trim();
        }

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
        List<CarAttribute> prodAtts = new ArrayList<>();
        keyAtts.forEach(keyAtt->{
            CarAttribute prodAtt = new CarAttribute();
            prodAtt.setCarAttName("Car Attribute");
            prodAtt.setCarAttValue(keyAtt.getAttValue());
            prodAtts.add(prodAtt);
        });
        prodCar.setAttributes(prodAtts);
    }

    private void setMainCarFields(KeyCar keyCar, ProductionCar prodCar) {
        setMake(keyCar, prodCar);
        String model = replaceSymbols(keyCar.getModel());
        prodCar.setModel(model);
        String[] split = keyCar.getStartFinish().split("-");
        prodCar.setYearStart(Integer.parseInt(split[0]));
        prodCar.setYearFinish(Integer.parseInt(split[1]));
    }

    private void setMake(KeyCar keyCar, ProductionCar prodCar) {
        String make = keyCar.getMake();
        if (make.equals("Willys")){
            String model = keyCar.getModel();
            prodCar.setMake("Jeep");
            keyCar.setModel("Willys");
            prodCar.setSubModel(model);
            return;
        }
        prodCar.setMake(make);
    }

    private String replaceSymbols(String model) {
        String result = model.replace("%26", "&");
        result = result.replace("%2F", "/");

        return result;
    }

    private void setFitAttributes(ProductionFitment prodFit, ItemCar keyFit) {
        Set<ItemCarAttribute> keyAtts = new HashSet<>(keyFit.getAttributes());
        Set<FitmentAttribute> prodAtts = new HashSet<>();
        keyAtts.forEach(keyAtt->{
            FitmentAttribute prodAtt = new FitmentAttribute();
            prodAtt.setFitmentAttName(keyAtt.getAttName());
            prodAtt.setFitmentAttValue(keyAtt.getAttValue());
            List<FitmentAttribute> resAtts = new KeyLiftBuilder().buildLifts(keyAtt);
            addPositionAttribute(resAtts, keyAtt);
            prodAtts.addAll(resAtts);
        });
        prodFit.setFitmentAttributes(prodAtts);
    }

    private void addPositionAttribute(List<FitmentAttribute> resAtts, ItemCarAttribute keyAtt) {
        String name = keyAtt.getAttName();
        if (!name.equals("Position On Vehicle:")){
            return;
        }
        String value = keyAtt.getAttValue();
        if (value.contains("Front")){
            resAtts.add(new FitmentAttribute("Position", "Front"));
        }
        if (value.contains("Rear")){
            resAtts.add(new FitmentAttribute("Position", "Rear"));
        }
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
                if (line.length()==0){
                    continue;
                }
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
            //cutting pic attributes, if present (like &maxheight=250&maxwidth=400)
            for (ItemPic pic: prodItem.getPics()){
                String picUrl = pic.getPicUrl();
                if (!picUrl.endsWith("jpg")){
                    picUrl = StringUtils.substringBefore(picUrl, "&");
                    pic.setPicUrl(picUrl);
                }
            }
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
                case "Upper Mounting Type":
                    params.setUpperMount(spec.getSpecValue());
                    addItemAttribute("Upper Mount", spec.getSpecValue(), prodItem);
                    break;
                case "Lower Mounting type":
                case "Lower Mounting Style":
                    params.setLowerMount(spec.getSpecValue());
                    addItemAttribute("Lower Mount", spec.getSpecValue(), prodItem);
                    break;
                default:
                    addItemAttribute(spec.getSpecName(), spec.getSpecValue(), prodItem);
                    break;
            }
        });
       /* prodItem.setParams(params);
        params.setItem(prodItem);*/
    }

    private String getItemType(KeyItem keyItem) {
        String result = "";
        String rawStr = keyItem.getShortDescription();
        result = StringUtils.substringBefore(rawStr, ";");
        //System.out.println(result);

        return result;
    }

    private double getLength(String specValue) {
        specValue = specValue.replace(" Inch", "");
        if (specValue.contains("/")){
            specValue = specValue.replace("-", ".");
            specValue = modifyFractions(specValue);
        }
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

    private String modifyFractions(String specValue) {
        String fraction = StringUtils.substringAfter(specValue, ".");
        String[] split = fraction.split("/");
        double num = Integer.parseInt(split[0]);
        double den = Integer.parseInt(split[1]);
        double dec = num/den;
        String doub = dec+"";
        doub = StringUtils.substringAfter(doub, ".");
        String result = specValue.replace(fraction, doub);

        return result;
    }
}
