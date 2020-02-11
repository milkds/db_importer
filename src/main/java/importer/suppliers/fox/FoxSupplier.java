package importer.suppliers.fox;

import importer.entities.*;
import importer.service.CarService;
import importer.suppliers.fox.entities.FoxCar;
import importer.suppliers.fox.entities.FoxFit;
import importer.suppliers.fox.entities.FoxItem;
import importer.suppliers.fox.entities.FoxItemSpec;
import importer.suppliers.skyjacker.SkyHibernateUtil;
import importer.suppliers.skyjacker.SkyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FoxSupplier {
    private static final Logger logger = LogManager.getLogger(FoxSupplier.class.getName());

    public ProductionItem buildItem(FoxItem foxItem, Session foxSession) {
        ProductionItem result = new ProductionItem();
        setItemFields(foxItem, result);
        setFitments(foxItem, result);

        return result;
    }

    private void setFitments(FoxItem foxItem, ProductionItem prodItem) {
        Set<FoxFit> foxFits = foxItem.getFitments();
        Set<ProductionFitment> prodFits = prodItem.getProductionFitments();
        foxFits.forEach(foxFit -> {
            ProductionFitment fitment = new ProductionFitment();
            setFitmentAtts(foxFit, fitment);
            setCar(foxFit, fitment);
            fitment.setItem(prodItem);
            prodFits.add(fitment);
        });
    }

    private void setCar(FoxFit foxFit, ProductionFitment prodFit) {
        ProductionCar prodCar = new ProductionCar();
        FoxCar foxCar = foxFit.getCar();
        prodCar.setMake(foxCar.getMake());
        prodCar.setModel(foxCar.getModel().trim());
        prodCar.setDrive(foxCar.getDrive());
        // prodCar.setSubModel("N/A");
        int carYear = foxCar.getYear();
        ProductionCar existingCar = SkyService.getExistingCar(prodCar, carYear); //we use Sky method as it does exactly what we need
        if (existingCar!=null){
            prodCar.setYearStart(carYear);
            prodCar.setYearFinish(carYear);
        }
        else {
            prodCar.setYearStart(carYear);
            prodCar.setYearFinish(carYear);
            CarMergeEntity entity = CarService.getCarMergeEntity(prodCar);
            if (entity==null){
                System.out.println(prodCar.getMake() + "  " + prodCar.getModel() + "  " + carYear);
            }
        }
        /*else {
            prodCar.setYearStart(existingCar.getYearStart());
            prodCar.setYearFinish(existingCar.getYearFinish());
        }
        prodFit.setCar(prodCar);
        prodCar.getProductionFitments().add(prodFit);*/
    }

    private void setFitmentAtts(FoxFit foxFit, ProductionFitment prodFit) {
        Set<FitmentAttribute> prodAtts = new HashSet<>();
        prodAtts.add(new FitmentAttribute("Position",foxFit.getPosition()));
        prodAtts.add(new FitmentAttribute("Lift", foxFit.getLift()));
        String fitNote = foxFit.getFitNote();
        if (!fitNote.equals("N/A")){
            fitNote = fitNote.replace("(", "");
            fitNote = fitNote.replace(")", "");
            prodAtts.add(new FitmentAttribute("Note", fitNote));
        }

        prodFit.setFitmentAttributes(prodAtts);
    }

    private void setItemFields(FoxItem foxItem, ProductionItem prodItem) {
        setPartNo(foxItem, prodItem);
        setManufacturer(prodItem);
        setItemType(foxItem, prodItem);
        setPics(foxItem, prodItem);
        setParamsAndAttributes(foxItem, prodItem);
    }

    private void setParamsAndAttributes(FoxItem foxItem, ProductionItem prodItem) {
        Set<FoxItemSpec> specs = foxItem.getSpecs();
        Set<ItemAttribute> prodAtts = prodItem.getItemAttributes();
        ShockParameters params = new ShockParameters();
        Map<String, String> mountMap = getMountMap(); //k = fox mount, v = prod mount
        specs.forEach(spec->{
            String parName = spec.getSpecName();
            String parVal = spec.getSpecVal();
            if (parName==null){
                return;
            }
            if (parName.equals("Extended (in)")){
                params.setExtLength(Double.parseDouble(parVal));
                prodAtts.add(new ItemAttribute("Extended Length", parVal));
            }
            else if (parName.equals("Compressed (in)")){
                params.setColLength(Double.parseDouble(parVal));
                prodAtts.add(new ItemAttribute("Collapsed Length", parVal));
            }
            else if (parName.equals("Bottom Mount")){
                String mount = mountMap.get(parVal);
                params.setLowerMount(mount);
                prodAtts.add(new ItemAttribute("Lower Mount", parVal));
            }
            else if (parName.equals("Top Mount")){
                String mount = mountMap.get(parVal);
                params.setUpperMount(mount);
                prodAtts.add(new ItemAttribute("Upper Mount", parVal));
            }
            else {
                prodAtts.add(new ItemAttribute(parName, parVal));
            }
        });
        prodItem.setParams(params);
    }

    private Map<String, String> getMountMap() {
        Map<String, String> result = new HashMap<>();
        result.put("OEM","OEM");
        result.put("Eyelet","Eyelet");
        result.put("Top","Top Mount");
        result.put("ES15","Eyelet");
        result.put("Stem Top","Stem");
        result.put("Bar Pin","Bar-Pin");
        result.put("Axle Mount","Axle Mount");
        result.put("Taper Pin","Bar-Pin");
        result.put("Clevis","Clevis");
        result.put("Tie Rod Clamp","Tie Rod Clamp");
        result.put("Reuses Stock Mounting Hardware","");
        result.put("Front: SM3 / Rear: BP4","");

        return result;
    }

    private void setPics(FoxItem foxItem, ProductionItem prodItem) {
        Set<ItemPic> pics = new HashSet<>();
        String foxPicsBulk = foxItem.getItemPicUrls();
        String[] split = foxPicsBulk.split(";;");
        for (String url: split){
            ItemPic pic = new ItemPic();
            pic.setPicUrl(url);
            pic.setItem(prodItem);
            pics.add(pic);
        }
        prodItem.setPics(pics);
    }

    private void setItemType(FoxItem foxItem, ProductionItem prodItem) {
        String title = foxItem.getTitle();
        if (title.contains("SHOCK")){
            prodItem.setItemType("Shock Absorber");
        }
        else if (title.contains("STABILIZER")){
            prodItem.setItemType("Steering Stabilizer");
        }
        else {
            prodItem.setItemType("Other");
        }
    }

    private void setManufacturer(ProductionItem prodItem) {
        prodItem.setItemManufacturer("FOX");
    }

    private void setPartNo(FoxItem foxItem, ProductionItem prodItem) {
        prodItem.setItemPartNo(foxItem.getPartNo());
    }
}
