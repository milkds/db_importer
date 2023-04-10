package importer.suppliers.eibach;

import importer.dao.CarDAO;
import importer.entities.*;
import importer.suppliers.eibach.eib_entities.EibCar;
import importer.suppliers.eibach.eib_entities.EibItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class EibToProdConverter {
    private static final Logger logger = LogManager.getLogger(EibToProdConverter.class.getName());
    private Session eibSession;
    private EibItem eibItem;
    private EibCar eibCar;
    private Session prodSession;

    public ProductionItem buildProdItem(Map<String, Set<String>> bilMakeModelMap){
        ProductionItem prodItem = new ProductionItem();
        setPartNo(prodItem);
        setManufacturer(prodItem);
        setItemType(prodItem);
        setItemPics(prodItem);
        //setShockParams(prodItem);
        setItemAttributes(prodItem);
        setFitments(prodItem, bilMakeModelMap);

        return prodItem;
    }

    private void setItemAttributes(ProductionItem prodItem) {
        String note = eibItem.getProdNote();
        String desc = eibItem.getDesc();
        String carNote = eibCar.getNote();
        Set<ItemAttribute> atts = new HashSet<>();
        if (note!=null&&note.length()>0&&!note.equals(carNote)){
            ItemAttribute noteAtt = new ItemAttribute("Note", note);
            atts.add(noteAtt);
        }
       if (desc!=null&&desc.length()>0){
           ItemAttribute descAtt = new ItemAttribute("Description", desc);
           atts.add(descAtt);
       }
       if (atts.size()>0){
           prodItem.setItemAttributes(atts);
       }
    }

    private void setFitments(ProductionItem prodItem, Map<String, Set<String>> bilMakeModelMap) {
        List<ProductionCar> newCars = buildCar();
        List<ProductionFitment> fits = buildFits(newCars, prodItem);
        fits.forEach(prodFit->{
            prodItem.getProductionFitments().add(prodFit);
            prodFit.setItem(prodItem);
        });

        newCars.forEach(car->{
            List<ProductionCar> cars = CarDAO.getSimilarCarsByMM(car, prodSession);
            if (cars.size()==0){
                //Checking if our car complies to Bilstein
                String make = car.getMake();
                if (!bilMakeModelMap.containsKey(make)||!bilMakeModelMap.get(make).contains(car.getModel())){
                    System.out.println(car.getYearStart()+ "\t"+ car.getYearFinish() + "\t" +car.getMake()+ "\t" + car.getModel()+ "\t" + car.getSubModel());
                }
            }
        });  //test block for models check
    }

    private List<ProductionFitment> buildFits(List<ProductionCar> newCars, ProductionItem prodItem) {
        List<ProductionFitment> result;
        result = setLifts(newCars, prodItem);
        setFitNotes(result, eibCar.getNote());



        return result;
    }

    private void setFitNotes(List<ProductionFitment> fits, String note) {
        fits.forEach(fit->{
            if (note==null||note.length()==0){
                return;
            }
            FitmentAttribute attribute = new FitmentAttribute("Note",note);
            fit.getFitmentAttributes().add(attribute);
        });
    }

    private List<ProductionFitment> setLifts(List<ProductionCar> newCars, ProductionItem prodItem) {
        List<ProductionFitment> result = new ArrayList<>();
        String rearL = eibCar.getRear();
        String frontL = eibCar.getFront();
        newCars.forEach(car -> {
            ProductionFitment fitment = new ProductionFitment();
            if (rearL!=null&& !rearL.equals("n/a")){
                if (rearL.contains("mm")){
                    prodItem.getItemAttributes().add(new ItemAttribute("Rear Size", rearL));
                }
                else {
                    double[] liftsR = getLifts(rearL);
                    fitment.getFitmentAttributes().add(new FitmentAttribute("Rear Lift Start", liftsR[0]+""));
                    fitment.getFitmentAttributes().add(new FitmentAttribute("Rear Lift Finish", liftsR[1]+""));
                }
            }
            if (frontL!=null&& !frontL.equals("n/a")){
                if (frontL.contains("mm")){
                    prodItem.getItemAttributes().add(new ItemAttribute("Front Size", frontL));
                }
                else if (frontL.equals("adj.")){
                    prodItem.getItemAttributes().add(new ItemAttribute("Height Adjustable ", "Yes"));
                }
                else {
                    if(frontL.contains("rha")){
                        prodItem.getItemAttributes().add(new ItemAttribute("Height Adjustable ", "Yes"));
                    }
                    double[] liftsF = getLifts(frontL);
                    fitment.getFitmentAttributes().add(new FitmentAttribute("Front Lift Start", liftsF[0]+""));
                    fitment.getFitmentAttributes().add(new FitmentAttribute("Front Lift Finish", liftsF[1]+""));
                }
            }
            fitment.setCar(car);
            car.getProductionFitments().add(fitment);
            result.add(fitment);
            //    System.out.println(car.getYearStart()+ "\t"+ car.getYearFinish() + "\t" +car.getMake()+ "\t" + car.getModel()+ "\t" + car.getSubModel()+"\t"+eibCar.getNote());
        });


        return result;
    }

    /***
     * Returns lifts in inches
     * @param lift
     * @return array of start and finish lifts. 0 - start, 1 - finish
     */
    private double[] getLifts(String lift) {
        double[] result = new double[2];
        if (!lift.contains("-")){
            double inchLift = convertLift(lift);
            result[0] = inchLift;
            result[1] = inchLift;
        }
        else {
            boolean low = false;
            if (lift.startsWith("-")){
                low = true;
                lift = lift.substring(1);
            }
            String[] lifts = lift.split("-");
            double liftStart = convertLift(lifts[0]);
            double liftFinish = 0d;
            if (lifts.length==1){
                liftFinish = liftStart;
            }
            else {
                liftFinish = convertLift(lifts[1]);
            }
            if (low){
                liftStart = - liftStart;
                liftFinish = - liftFinish;
            }
            result[0] = liftStart;
            result[1] = liftFinish;
        }

        return result;
    }

    private double convertLift(String lift) {
        lift = lift.replace("\"", "");
        lift = lift.replace("in", "");
        lift = lift.replace("rha", "");
        lift = lift.replace("adj.", "");
        lift = lift.trim();
        double result = 0d;
        try {
             result = Double.parseDouble(lift);
        }
        catch (NumberFormatException e){
            System.out.println("Rear " + eibCar.getRear()+ " Front " + eibCar.getFront());
            e.printStackTrace();
            System.exit(1);
        }
        result = new BigDecimal(result).setScale(2, RoundingMode.HALF_UP).doubleValue(); //round to 2 digits

        return result;
    }

    private List<ProductionCar> buildCar() {
        List<ProductionCar> result = new EibCarBuilder(eibSession, eibCar).buildProdCar();

        return result;
    }

    private void setShockParams(ProductionItem prodItem) {
        //we have no info for params, need to make blank item
        ShockParameters params = new ShockParameters();
      //  prodItem.setParams(params);
     //   params.setItem(prodItem);

    }

    private void setItemPics(ProductionItem prodItem) {
        String picUrl = eibItem.getImgLink();
        Set<ItemPic> pics = new HashSet<>();

        String[] spl = picUrl.split("DDD");
        for (String s : spl) {
            ItemPic pic = new ItemPic();
            pic.setPicUrl(s);
            pic.setItem(prodItem);
            pics.add(pic);
        }
        prodItem.setPics(pics);
    }

    private void setItemType(ProductionItem prodItem) {
        String itemType = "";
        String fit = eibItem.getFitTitle();
        String title = eibItem.getTitle();
        if (fit!=null&&title.contains(fit)){
            itemType = StringUtils.substringBefore(title, fit).trim();
        }
        if (itemType.contains("(")){
            itemType = StringUtils.substringBefore(itemType, "(").trim();
        }
        Map<String, String> itemTypes = getItemTypesMap();
      /*  if (!itemTypes.containsKey(itemType.trim())){
            System.out.println(itemType);
        }*/
       prodItem.setItemType(itemTypes.get(itemType));
    }

    private Map<String, String> getItemTypesMap() {
        Map<String, String> result = new HashMap<>();
        result.put("PRO-TRUCK SHOCK","Shock absorber");
        result.put("ALL-TERRAIN LIFT KIT","Lift kit");
        result.put("ANTI-ROLL Hardware Kit","Hardware kit");
        result.put("ANTI-ROLL KIT - Front Adjustable End Link System","Anti-Roll Kit");
        result.put("ANTI-ROLL KIT - Rear Adjustable End Link System","Anti-Roll Kit");
        result.put("ANTI-ROLL KIT - Rear Anti-Roll Bar Brace","Anti-Roll Kit");
        result.put("ANTI-ROLL Kit Relocation Bracket","Anti-Roll Kit");
        result.put("ANTI-ROLL-KIT","Anti-Roll Kit");
        result.put("DRAG-LAUNCH Kit","Drag kit");
        result.put("FRONT ANTI-ROLL Kit","Anti-Roll Kit");
        result.put("LOAD-LEVELING SYSTEM","Load-Leveling Kit");
        result.put("MULTI-PRO-R1 Coilover Kit","Coilover");
        result.put("MULTI-PRO-R2 Coilover Kit","Coilover");
        result.put("PRO-ALIGNMENT 1st Gen Mustang Caster Rod","Alignment");
        result.put("PRO-ALIGNMENT Camber Arm Kit","Alignment");
        result.put("PRO-ALIGNMENT Camber Ball Joint Kit","Alignment");
        result.put("PRO-ALIGNMENT Camber Bolt Kit","Alignment");
        result.put("PRO-ALIGNMENT Camber Bushing Kit","Alignment");
        result.put("PRO-ALIGNMENT Camber Plate","Alignment");
        result.put("PRO-ALIGNMENT Camber Plate ","Alignment");
        result.put("PRO-ALIGNMENT Camber Plate/Nut Kit","Alignment");
        result.put("PRO-ALIGNMENT Camber Shim Kit","Alignment");
        result.put("PRO-ALIGNMENT Camber Upper Mount Kit","Alignment");
        result.put("PRO-ALIGNMENT Camber/Caster Kit","Alignment");
        result.put("PRO-ALIGNMENT Front Camber/Caster Kit","Alignment");
        result.put("PRO-ALIGNMENT Jeep JK Adjustable Front Upper Control Arm Kit","Alignment");
        result.put("PRO-ALIGNMENT Jeep JK Adjustable Rear Upper Control Arm Kit","Alignment");
        result.put("PRO-ALIGNMENT Jeep JK Front Lower Arm Kit","Alignment");
        result.put("PRO-ALIGNMENT Jeep JK Rear Lower Arm Kit","Alignment");
        result.put("PRO-ALIGNMENT Nissan Adjustable Front Upper Control Arm Kit","Alignment");
        result.put("PRO-ALIGNMENT Panhard Bar","Alignment");
        result.put("PRO-ALIGNMENT Performance Lower Control Arms","Alignment");
        result.put("PRO-ALIGNMENT Porsche Adjustable Trailing Arm Kit","Alignment");
        result.put("PRO-ALIGNMENT Toyota Adjustable Front Upper Control Arm Kit","Alignment");
        result.put("PRO-ALIGNMENT Toyota Rear Lower Control Arms","Alignment");
        result.put("PRO-ALIGNMENT Toe Link Kit","Alignment");
        result.put("PRO-ALIGNMENT Trailing Arm Kit","Alignment");
        result.put("PRO-DAMPER","Shock absorber");
        result.put("PRO-DAMPER Kit","Shock absorber");
        result.put("PRO-KIT","Lift kit");
        result.put("PRO-KIT Lowering Hardware","Lift kit");
        result.put("PRO-KIT Performance Springs","Lift kit");
        result.put("PRO-LIFT-KIT Springs","Lift kit");
        result.put("PRO-SPACER Kit","Lift kit");
        result.put("PRO-STREET Coilover Kit","Coilover");
        result.put("PRO-SYSTEM Kit","Suspension kit");
        result.put("PRO-TRUCK Front Spring-Kit","Lift kit");
        result.put("PRO-TRUCK Coilover","Coilover");
        result.put("PRO-TRUCK COILOVER SYSTEM","Lift kit");
        result.put("PRO-TRUCK Kit","Lift kit");
        result.put("PRO-TRUCK LIFT SYSTEM","Lift kit");
        result.put("PRO-TRUCK Progressive Bump Stop","Lift kit");
        result.put("PRO-TRUCK Rear Lift Block","Lift kit");
        result.put("PRO-TRUCK Rear Shackle Kit","Lift kit");
        result.put("PRO-TRUCK SPORT SHOCK","Shock absorber");
        result.put("PRO-TRUCK-SHOCK Kit","Shock absorber");
        result.put("REAR ANTI-ROLL Kit","Anti-Roll Kit");
        result.put("SPECIAL EDITION PRO-KIT Performance Springs","Lift kit");
        result.put("SPORTLINE Kit","Suspension kit");
        result.put("SPORT-PLUS Kit","Suspension kit");
        result.put("SPORT-SYSTEM","Suspension kit");
        result.put("SPORT-SYSTEM-PLUS","Lift kit");
        result.put("SUV PRO-KIT","Lift kit");
        result.put("PRO-PLUS Kit","Lift kit");
        result.put("PRO-SYSTEM-PLUS","Lift kit");
        result.put("PRO-STEERING-STABILIZER","Steering stabilizer");

        return result;
    }

    private void setManufacturer(ProductionItem prodItem) {
        prodItem.setItemManufacturer("Eibach");
    }

    private void setPartNo(ProductionItem prodItem) {
        prodItem.setItemPartNo(eibItem.getPartNo());
    }

    public EibToProdConverter(Session eibSession, EibItem eibItem, EibCar eibCar, Session prodSession) {
        this.eibSession = eibSession;
        this.eibItem = eibItem;
        this.eibCar = eibCar;
        this.prodSession = prodSession;
    }
}
