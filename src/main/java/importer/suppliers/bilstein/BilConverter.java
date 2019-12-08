package importer.suppliers.bilstein;

import importer.entities.*;
import importer.suppliers.bilstein.bilstein_entities.BilCar;
import importer.suppliers.bilstein.bilstein_entities.BilFitment;
import importer.suppliers.bilstein.bilstein_entities.BilShock;
import importer.suppliers.bilstein.bilstein_entities.BilSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.Set;

public class BilConverter {
    private static final Logger logger = LogManager.getLogger(BilConverter.class.getName());
    public ProductionItem buildItem(BilShock shock, Session session) {
        ProductionItem item = new ProductionItem();
        setItemFields(item, shock);
        setFitments(item, shock, session);
       // logItem(item);

        return item;
    }

    private void logItem(ProductionItem item) {
        logger.debug(item);
        item.getItemAttributes().forEach(logger::debug);
        Set<ProductionFitment> fitments = item.getProductionFitments();
        fitments.forEach(fitment->{
            logger.debug(fitment);
            fitment.getFitmentAttributes().forEach(logger::debug);
            logger.debug(fitment.getCar());
            fitment.getCar().getAttributes().forEach(logger::debug);
        });

        BilHibernateUtil.shutdown();
        System.exit(0);
    }

    private void setItemFields(ProductionItem item, BilShock shock) {
        setItemPartNo(item, shock);
        setItemManufacturer(item, shock);
        setItemType(item, shock);
        setItemAttributes(item, shock);
        setItemPics(item, shock);
      //  setItemParams(item, shock);
    }

       private void setItemPics(ProductionItem item, BilShock shock) {
        Set<ItemPic> pics = new HashSet<>();
        item.setPics(pics);

        String mainImgLink = shock.getMainImgLink();
        if (mainImgLink!=null&&mainImgLink.length()>0){
           ItemPic mainPic = convertToPic(mainImgLink, item);
           pics.add(mainPic);
       }

        String allPics = shock.getImgLinks();
        if (allPics==null){
            return;
        }

        String[] split = allPics.split("\r\n");
        for (String s : split) {
           if (s.length()>0){
               ItemPic pic = convertToPic(s, item);
               pics.add(pic);
           }
        }
    }

    private ItemPic convertToPic(String imgLink, ProductionItem item) {
        ItemPic result = new ItemPic();
        result.setPicUrl(imgLink);
        result.setItem(item);

        return result;
    }

    private void setFitments(ProductionItem item, BilShock shock, Session session) {
        Set<BilFitment> bilFitments = BilsteinDAO.getFitmentsForShock(shock, session);
        Set<ProductionFitment> prodFitments = new HashSet<>();
        bilFitments.forEach(bilFitment -> {
           ProductionFitment prodFit = new ProductionFitment();
           BilCar bilcar = bilFitment.getBilCar();
           ProductionCar prodCar = buildProdCar(bilcar);
           Set<FitmentAttribute> attributes = getFitmentAttributes(bilFitment);
           prodFit.setCar(prodCar);
           prodFit.setFitmentAttributes(attributes);
           prodFitments.add(prodFit);
        });
        item.setProductionFitments(prodFitments);
    }

    private Set<FitmentAttribute> getFitmentAttributes(BilFitment bilFitment) {
        Set<FitmentAttribute> result = new HashSet<>();
        String position = bilFitment.getPosition();
        if (position!=null&&position.length()>0){
            FitmentAttribute attribute = new FitmentAttribute();
            attribute.setFitmentAttName("Position");
            attribute.setFitmentAttValue(position);
            result.add(attribute);
        }
        String notes = bilFitment.getNotes();
        if (notes!=null&&notes.length()>0){
            FitmentAttribute noteAtt = new FitmentAttribute();
            noteAtt.setFitmentAttName("Notes");
            noteAtt.setFitmentAttValue(notes);
            result.add(noteAtt);
        }

        return result;
    }

    private ProductionCar buildProdCar(BilCar bilcar) {
        ProductionCar resCar = new ProductionCar();
        setStartFinish(bilcar, resCar);
        setMainFields(bilcar, resCar);
        setDrive(bilcar, resCar);
        setCarAttributes(bilcar, resCar);

        return resCar;
    }

    private void setCarAttributes(BilCar bilcar, ProductionCar resCar) {
        Set<CarAttribute> carAttributes = new HashSet<>();

        String body = bilcar.getBody();
        if (body!=null&&body.length()!=0){
            CarAttribute attribute = new CarAttribute();
            attribute.setCarAttName("Body");
            attribute.setCarAttValue(body);
            carAttributes.add(attribute);
        }

        String bodyMan = bilcar.getBodyMan();
        if (bodyMan!=null&&bodyMan.length()!=0){
            CarAttribute attribute = new CarAttribute();
            attribute.setCarAttName("Body Manufacturer");
            attribute.setCarAttValue(bodyMan);
            carAttributes.add(attribute);
        }

        String doors = bilcar.getDoors();
        if (doors!=null&&doors.length()!=0){
            CarAttribute attribute = new CarAttribute();
            attribute.setCarAttName("Doors");
            attribute.setCarAttValue(doors);
            carAttributes.add(attribute);
        }

        String engine = bilcar.getEngine();
        if (engine!=null&&engine.length()!=0){
            CarAttribute attribute = new CarAttribute();
            attribute.setCarAttName("Engine");
            attribute.setCarAttValue(engine);
            carAttributes.add(attribute);
        }

        String suspension = bilcar.getSuspension();
        if (suspension!=null&&suspension.length()!=0){
            CarAttribute attribute = new CarAttribute();
            attribute.setCarAttName("Suspension");
            attribute.setCarAttValue(suspension);
            carAttributes.add(attribute);
        }

        String transmission = bilcar.getTransmission();
        if (transmission!=null&&transmission.length()!=0){
            CarAttribute attribute = new CarAttribute();
            attribute.setCarAttName("Transmission");
            attribute.setCarAttValue(transmission);
            carAttributes.add(attribute);
        }

        resCar.setAttributes(carAttributes);
    }

    private void setDrive(BilCar bilcar, ProductionCar resCar) {
        String drive = bilcar.getDrive();
        if (drive==null||drive.length()==0){
            drive = "N/A";
        }
        resCar.setDrive(drive);
    }

    private void setMainFields(BilCar bilcar, ProductionCar resCar) {
        resCar.setMake(bilcar.getMake());
        resCar.setModel(bilcar.getModel());
        resCar.setSubModel(bilcar.getSubModel());
    }

    private void setStartFinish(BilCar bilcar, ProductionCar resCar) {
        Integer start = bilcar.getYearStart();
        Integer finish = bilcar.getYearFinish();
        if (start==null||finish==null){
            int year = bilcar.getModelYear();
            resCar.setYearStart(year);
            resCar.setYearFinish(year);
            return;
        }
        resCar.setYearStart(start);
        resCar.setYearFinish(finish);
    }

    private void setItemAttributes(ProductionItem item, BilShock shock) {
       // Set<ItemAttribute> shockParameters = getShockParameters(shock);
        Set<ItemAttribute> shockParameters = new HashSet<>();
        Set<BilSpec>specs = shock.getBilSpecs();
        initParams(item);
        specs.forEach(spec->{
            ItemAttribute attribute = new ItemAttribute();
            checkForLength(spec, item);//sets lengths to item, if present. Also modifies spec name in that case
            attribute.setItemAttName(spec.getSpecName());
            attribute.setItemAttValue(spec.getSpecValue());
          //  modifyLengthAttribute(spec, attribute);
            shockParameters.add(attribute);
        });

        ItemAttribute attribute = new ItemAttribute();
        attribute.setItemAttName("Series");
        attribute.setItemAttValue(shock.getSeries());
        shockParameters.add(attribute);

        item.setItemAttributes(shockParameters);
    }

    private void initParams(ProductionItem item) {
        ShockParameters params = new ShockParameters();
        item.setParams(params);
        params.setItem(item);
    }

    private void checkForLength(BilSpec spec, ProductionItem item) {
        String name = spec.getSpecName();
        if (name.equals("Extended Length (IN)")){
           spec.setSpecName("Extended Length");
           double length = checkLengthFormat(spec);
           item.getParams().setExtLength(length);
        }
        if (name.equals("Collapsed Length (IN)")){
            spec.setSpecName("Collapsed Length");
            double length = checkLengthFormat(spec);
            item.getParams().setColLength(length);
        }
    }

    private double checkLengthFormat(BilSpec spec) {
        double length = 0d;
        try {
            length = Double.parseDouble(spec.getSpecValue());
        }
        catch (NumberFormatException ignored){
            logger.error("illegal length format at spec " + spec);
        }

        return length;
    }


    private void modifyLengthAttribute(BilSpec spec, ItemAttribute attribute) {
        String name = spec.getSpecName();
        if (name.equals("Collapsed Length (IN)")||name.equals("Extended Length (IN)")){
            name = name.replace(" (IN)", "");
            attribute.setItemAttName(name);
        }
    }

    private Set<ItemAttribute> getShockParameters(BilShock shock) {
        Set<ItemAttribute> result = new HashSet<>();

        String series = shock.getSeries();
        if (series!=null&&series.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Series");
            attribute.setItemAttValue(series);
            result.add(attribute);
        }

        String mainImgLink = shock.getMainImgLink();
        if (mainImgLink!=null&&mainImgLink.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Main Image Link");
            attribute.setItemAttValue(mainImgLink);
            result.add(attribute);
        }

        String colLength = shock.getColLength();
        if (colLength!=null&&colLength.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Collapsed Length");
            attribute.setItemAttValue(colLength);
            result.add(attribute);
        }

        String extLength = shock.getExtLength();
        if (extLength!=null&&extLength.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Extended Length");
            attribute.setItemAttValue(extLength);
            result.add(attribute);
        }

        String bodyDiam = shock.getBodyDiameter();
        if (bodyDiam!=null&&bodyDiam.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Body Diameter");
            attribute.setItemAttValue(bodyDiam);
            result.add(attribute);
        }

        String outerHousDiam = shock.getOuterHousingDiameter();
        if (outerHousDiam!=null&&outerHousDiam.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Outer Housing Diameter");
            attribute.setItemAttValue(outerHousDiam);
            result.add(attribute);
        }

        String chaMan = shock.getChassisManufacturer();
        if (chaMan!=null&&chaMan.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Chassis Manufacturer");
            attribute.setItemAttValue(chaMan);
            result.add(attribute);
        }

        String chaMod = shock.getChassisModel();
        if (chaMod!=null&&chaMod.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Chassis Model");
            attribute.setItemAttValue(chaMod);
            result.add(attribute);
        }

        String chaModExt = shock.getChassisModelExt();
        if (chaModExt!=null&&chaModExt.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Chassis Model Extended");
            attribute.setItemAttValue(chaModExt);
            result.add(attribute);
        }

        String chaClass = shock.getChassisClass();
        if (chaClass!=null&&chaClass.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Chassis Class");
            attribute.setItemAttValue(chaClass);
            result.add(attribute);
        }

        String chaYearRange = shock.getChassisYearRange();
        if (chaYearRange!=null&&chaYearRange.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Chassis Year Range");
            attribute.setItemAttValue(chaYearRange);
            result.add(attribute);
        }

        String appNote1 = shock.getAppNote1();
        if (appNote1!=null&&appNote1.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Application Note 1");
            attribute.setItemAttValue(appNote1);
            result.add(attribute);
        }

        String appNote2 = shock.getAppNote2();
        if (appNote2!=null&&appNote2.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Application Note 2");
            attribute.setItemAttValue(appNote2);
            result.add(attribute);
        }

        String comp52 = shock.getComp52();
        if (comp52!=null&&comp52.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Compression @52m/s");
            attribute.setItemAttValue(comp52);
            result.add(attribute);
        }

        String comp26 = shock.getComp26();
        if (comp26!=null&&comp26.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Compression @26m/s");
            attribute.setItemAttValue(comp26);
            result.add(attribute);
        }

        String reb52 = shock.getRebound52();
        if (reb52!=null&&reb52.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Rebound @52m/s");
            attribute.setItemAttValue(reb52);
            result.add(attribute);
        }

        String reb26 = shock.getRebound26();
        if (reb26!=null&&reb26.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Rebound @26m/s");
            attribute.setItemAttValue(reb26);
            result.add(attribute);
        }

        String suspType = shock.getSuspensionType();
        if (suspType!=null&&suspType.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Suspension Type");
            attribute.setItemAttValue(suspType);
            result.add(attribute);
        }

        String grossVehWeight = shock.getGrossVehicleWeight();
        if (grossVehWeight!=null&&grossVehWeight.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Gross Vehicle Weight");
            attribute.setItemAttValue(grossVehWeight);
            result.add(attribute);
        }

        String includesOutTieRods = shock.getIncludesOutTieRods();
        if (includesOutTieRods!=null&&includesOutTieRods.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Includes Outer Tie Rods");
            attribute.setItemAttValue(includesOutTieRods);
            result.add(attribute);
        }

        String imgLinks = shock.getImgLinks();
        if (imgLinks!=null&&imgLinks.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Image Links");
            attribute.setItemAttValue(imgLinks);
            result.add(attribute);
        }

        String prodDesc = shock.getProductDesc();
        if (prodDesc!=null&&prodDesc.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Product Description");
            attribute.setItemAttValue(prodDesc);
            result.add(attribute);
        }

        String buyersGuide = shock.getBuyersGuide();
        if (buyersGuide!=null&&buyersGuide.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Buyers Guide");
            attribute.setItemAttValue(buyersGuide);
            result.add(attribute);
        }

        String docLinks = shock.getDocLinks();
        if (docLinks!=null&&docLinks.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Documents Links");
            attribute.setItemAttValue(docLinks);
            result.add(attribute);
        }

        String warranty = shock.getWarranty();
        if (warranty!=null&&warranty.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Warranty");
            attribute.setItemAttValue(warranty);
            result.add(attribute);
        }

        String finish = shock.getWarranty();
        if (finish!=null&&finish.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Finish");
            attribute.setItemAttValue(finish);
            result.add(attribute);
        }

        String reservoir = shock.getReservoir();
        if (reservoir!=null&&reservoir.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Reservoir");
            attribute.setItemAttValue(reservoir);
            result.add(attribute);
        }

        String bodyDes = shock.getBodyDesign();
        if (bodyDes!=null&&bodyDes.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Body Design");
            attribute.setItemAttValue(bodyDes);
            result.add(attribute);
        }

        String optResClamp = shock.getOptResClamp();
        if (optResClamp!=null&&optResClamp.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Optional Reservoir Clamp");
            attribute.setItemAttValue(optResClamp);
            result.add(attribute);
        }

        String kitContents = shock.getKitContents();
        if (kitContents!=null&&kitContents.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Kit Contents");
            attribute.setItemAttValue(kitContents);
            result.add(attribute);
        }

        String optHLSprings = shock.getOptHLSprings();
        if (optHLSprings!=null&&optHLSprings.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Optional Heavy Load Springs");
            attribute.setItemAttValue(optHLSprings);
            result.add(attribute);
        }

        String adjDamping = shock.getAdjDamping();
        if (adjDamping!=null&&adjDamping.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Adjustable Dumping");
            attribute.setItemAttValue(adjDamping);
            result.add(attribute);
        }

        String itemTypeSteerRacks = shock.getItemTypeSteerRacks();
        if (itemTypeSteerRacks!=null&&itemTypeSteerRacks.length()>0){
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Item Type Steering Racks");
            attribute.setItemAttValue(itemTypeSteerRacks);
            result.add(attribute);
        }

        return result;
    }

    private void setItemType(ProductionItem item, BilShock shock) {
        item.setItemType(shock.getProductType());
    }

    private void setItemManufacturer(ProductionItem item, BilShock shock) {
        item.setItemManufacturer(shock.getMake());
    }

    private void setItemPartNo(ProductionItem item, BilShock shock) {
        item.setItemPartNo(shock.getPartNo());
    }
}
