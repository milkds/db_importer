package importer.suppliers.eibach;

import importer.entities.CarAttribute;
import importer.entities.ProductionCar;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class EibCarValidator {
    public List<ProductionCar> validateCars(List<ProductionCar> rawCars) {
        Set<String> makesToCheck = getMakesToCheck();
        if (makeValid(makesToCheck, rawCars)){
            return rawCars;
        }
        rawCars = getValidCars(rawCars);

        return rawCars;
    }

    private List<ProductionCar> getValidCars(List<ProductionCar> rawCars) {
        List<ProductionCar> result = new ArrayList<>();
        String make = rawCars.get(0).getMake();
        switch (make){
            case "Oldsmobile": result = validateOldsMobile(rawCars); break;
            case "Mercedes-Benz": result = validateMB(rawCars); break;
            case "BMW": result = validateBMW(rawCars); break;
            case "Chevrolet": result = validateChevrolet(rawCars); break;
            case "Chrysler": result = validateChrysler(rawCars); break;
            case "GMC": result = validateGMC(rawCars); break;
            case "Toyota": result = validateToyota(rawCars); break;
            case "Landrover": result = validateLandRover(rawCars); break;
            case "Hyundai": result = validateHyundai(rawCars); break;
        }

        return result;
    }

    private List<ProductionCar> validateHyundai(List<ProductionCar> rawCars) {
        List<ProductionCar> result = new ArrayList<>();
        rawCars.forEach(car->{
            String model = car.getModel();
            if (model.equals("Veloster")){
                Set <CarAttribute> atts = car.getAttributes();
                atts.forEach(att->{
                    String value = att.getCarAttValue();
                    if (value.contains("Turbo")){
                        car.setSubModel("Turbo");
                    }
                });
            }
            result.add(car);
        });

        return result;
    }

    private List<ProductionCar> validateLandRover(List<ProductionCar> rawCars) {
        List<ProductionCar> result = new ArrayList<>();
        rawCars.forEach(car->{
            car.setMake("Land Rover");
            if (car.getModel().equals("Range Rover Evoque")){
                car.setModel("Range Rover");
                car.setSubModel("Evoque");
            }
            result.add(car);
        });
        return result;
    }

    private List<ProductionCar> validateToyota(List<ProductionCar> rawCars) {
        List<ProductionCar> result = new ArrayList<>();
        rawCars.forEach(car -> {
            String model = car.getModel();
            if (model.equals("Rav 4")){
                car.setModel("RAV4");
            }
            result.add(car);
        });

        return result;
    }

    private List<ProductionCar> validateGMC(List<ProductionCar> rawCars) {
        List<ProductionCar> result = new ArrayList<>();
        Set<String> modelSet = getGMCmodelSet();
        rawCars.forEach(prodCar->{
            String model = prodCar.getModel();
            if (!modelSet.contains(model)){
                result.add(prodCar);
            }
            else {
                ProductionCar newCar = duplicateProdCar(prodCar);
                List<ProductionCar> corCars = validateGMCsubmodel(model, newCar);
                result.addAll(corCars);
            }
        });


        return result;
    }

    private List<ProductionCar> validateGMCsubmodel(String model, ProductionCar newCar) {
        List<ProductionCar> result = new ArrayList<>();
        switch (model){
            case "S-15 Ext. Cab Pick-up":{
                newCar.setModel("S15");
                newCar.getAttributes().add(new CarAttribute("Body", "Ext. Cab Pick-up"));
                result.add(newCar);
                break;
            }
            case "S-15 Jimmy":{
                newCar.setModel("S15 Jimmy");
                result.add(newCar);
                break;
            }
            case "S-15 Std. Cab Pick-up":{
                newCar.setModel("S15");
                newCar.getAttributes().add(new CarAttribute("Body", "Std. Cab Pick-up"));
                result.add(newCar);
                break;
            }
            case "Suburban 1500":{
                newCar.setModel("C1500 Suburban");
                result.add(newCar);
                break;
            }
            case "C15 Pickup":{
                int yearFinish = newCar.getYearFinish();
                if (yearFinish==1987){
                  ProductionCar car1 = duplicateProdCar(newCar);
                  car1.setModel("C15/C1500 Pickup");
                  car1.setYearStart(1973);
                  car1.setYearFinish(1974);

                  ProductionCar car2 = duplicateProdCar(newCar);
                  car2.setModel("C1500");
                  car2.setYearStart(1975);
                  car2.setYearFinish(1987);

                  result.add(car1);
                  result.add(car2);
                }
                else {
                    newCar.setModel("C15/C1500 Pickup");
                    result.add(newCar);
                }
                break;
            }
        }

        return result;
    }

    private Set<String> getGMCmodelSet() {
        Set<String> res = new HashSet<>();
        res.add("C15 Pickup");
        res.add("S-15 Ext. Cab Pick-up");
        res.add("S-15 Jimmy");
        res.add("S-15 Std. Cab Pick-up");
        res.add("Suburban 1500");

        return res;
    }

    private List<ProductionCar> validateChrysler(List<ProductionCar> rawCars) {
        List<ProductionCar> result = new ArrayList<>();
        rawCars.forEach(prodCar->{
            String model = prodCar.getModel();
            if (model.equals("300C")||model.equals("300S")){
                prodCar.setModel("300");
                prodCar.setSubModel(StringUtils.substringAfter(model, "300"));
            }
        });

        return result;
    }

    private List<ProductionCar> validateChevrolet(List<ProductionCar> rawCars) {
        List<ProductionCar> result = new ArrayList<>();
        Set<String> modelSet = getChevyModelSet();
        rawCars.forEach(car -> {
            String model = car.getModel();
            if (modelSet.contains(model)){
                car.setModel("S10");
                car.setSubModel(StringUtils.substringAfter(model, "S-10 "));
                result.add(car);
            }
            else {
                result.add(car);
            }
        });

        return result;
    }

    private Set<String> getChevyModelSet() {
        Set<String> result = new HashSet<>();
        result.add("S-10 Ext. Cab Pick-up");
        result.add("S-10 Std. Cab Pick-up");

        return result;
    }

    private List<ProductionCar> validateBMW(List<ProductionCar> rawCars) {
        List<ProductionCar> result = new ArrayList<>();
        rawCars.forEach(prodCar->{
            String model = prodCar.getModel();
            if (!model.equals("Z3 M-Roadster")&&!model.equals("Z3 M-Coupe")){
                result.add(prodCar);
            }
            else {
                prodCar.setModel("Z3");
                prodCar.setSubModel(StringUtils.substringAfter(model, "Z3 "));
                result.add(prodCar);
            }
        });

        return result;
    }

    private Map<String, Set<String>> getMercedesMap() {
        Map<String, Set<String>> result = new HashMap<>();
        Set<String> s1 = new HashSet<>();
        s1.add("SL320");
        result.put("320SL", s1);

        Set<String> s2 = new HashSet<>();
        s2.add("C230");
        result.put("C230K", s2);

        Set<String> s3 = new HashSet<>();
        s3.add("190");
        s3.add("190E");
        s3.add("190D");
        result.put("C-Class", s3);

        Set<String> s4 = new HashSet<>();
        s4.add("CLK230");
        result.put("CLK230K", s4);

        Set<String> s5 = new HashSet<>();
        s5.add("CLS550");
        result.put("CLS", s5);

        Set<String> s6 = getMBSclasseSet();
        result.put("S-CLASS", s6);

        Set<String> s7 = new HashSet<>();
        s7.add("SLK230");
        result.put("SLK230K", s7);

        Set<String> s8 = new HashSet<>();
        s8.add("SL 55");
        result.put("SL55", s8);


        return result;
    }

    private Set<String> getMBSclasseSet() {
        Set<String> result = new HashSet<>();
        result.add("280S");
        result.add("280SE");
        result.add("280SEL");
        result.add("380SE");
        result.add("380SEL");
        result.add("300SD");
        result.add("260SE");
        result.add("300SE");
        result.add("300SEL");
        result.add("420SE");
        result.add("420SEL");
        result.add("420SEC");
        result.add("500SE");
        result.add("500SEL");
        result.add("500SEC");
        result.add("560SE");
        result.add("560SEL");
        result.add("560SEC");
        result.add("300SDL");
        result.add("350SD");
        result.add("350SDL");

        return result;
    }

    private List<ProductionCar> validateMB(List<ProductionCar> rawCars) {
        List<ProductionCar> result = new ArrayList<>();
        Map<String, Set<String>> mercedesMap = getMercedesMap();
        rawCars.forEach(prodCar->{
            String model = prodCar.getModel();
            if (mercedesMap.containsKey(model)){
                Set<String> models = mercedesMap.get(model);
                models.forEach(corModel->{
                    ProductionCar newCar = duplicateProdCar(prodCar);
                    newCar.setModel(corModel);
                    validateMBSubModel(model, newCar); //this made to move info from model to sub
                    result.add(newCar);
                });
            }
            else {
                result.add(prodCar);
            }
        });

        return result;
    }

    private void validateMBSubModel(String model, ProductionCar newCar) {
        switch (model){
            case "C230K":
            case "SLK230K":
            case "CLK230K": {
                newCar.setSubModel("Kompressor");
                break;
            }
        }
    }

    private List<ProductionCar> validateOldsMobile(List<ProductionCar> rawCars) {
        List<ProductionCar> result = new ArrayList<>();
        Map<String, Set<String>> oldsMobileMap = getOldsmobileModelMap();
        for (ProductionCar prCar: rawCars){
            String model = prCar.getModel();
            if (!oldsMobileMap.containsKey(model)){
                result.add(prCar);
            }
            else {
                Set<String> correctModels = oldsMobileMap.get(model);
                correctModels.forEach(corModel->{
                    ProductionCar newCar = duplicateProdCar(prCar);
                    newCar.setModel(corModel);
                    result.add(newCar);
                });
            }
        }

        return result;
    }

    private ProductionCar duplicateProdCar(ProductionCar prCar) {
        ProductionCar result = new ProductionCar();
        result.setYearStart(prCar.getYearStart());
        result.setYearFinish(prCar.getYearFinish());
        result.setMake(prCar.getMake());
        result.setModel(prCar.getModel());
        result.setSubModel(prCar.getSubModel());
        result.setDrive(prCar.getDrive());
        result.getAttributes().addAll(prCar.getAttributes());

        return result;
    }

    private boolean makeValid(Set<String> makesToCheck, List<ProductionCar> rawCars) {
        for (ProductionCar prodCar : rawCars) {
            if (makesToCheck.contains(prodCar.getMake())) {
                return false;
            }
        }

        return true;
    }

    private Map<String, Set<String>> getCheckMap() {
        Map<String, Set<String>> result = new HashMap<>();
        Set<String> oldsmobileSet = getOldsmobileModelMap().keySet(); //k = wrongValue, v = set of correct values.
        result.put("Oldsmobile", oldsmobileSet);

        return result;
    }

    private Map<String, Set<String>> getOldsmobileModelMap() {
        Map<String, Set<String>> result = new HashMap<>();
        Set<String> s1 = new HashSet<>();
        s1.add("Cutlass");
        s1.add("442");
        result.put("Cutlass & 442", s1);

        return result;
    }

    private boolean carsValid(Set<String> makesToCheck, List<ProductionCar> rawCars) {
        rawCars.forEach(prodCar->{
            if (makesToCheck.contains(prodCar.getMake())){

            }
        });

        return true;
    }

    private Set<String> getMakesToCheck() {
        Set<String> result = new HashSet<>();
        result.add("Oldsmobile");
        result.add("Mercedes-Benz");
        result.add("BMW");
        result.add("Chevrolet");
        result.add("Chrysler");
        result.add("GMC");
        result.add("Toyota");
        result.add("Landrover");

        return result;
    }
}
