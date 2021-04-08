package importer.suppliers.eibach;

import importer.entities.CarAttribute;
import importer.entities.ProductionCar;
import importer.suppliers.eibach.eib_entities.EibCar;
import importer.suppliers.eibach.eib_filters.EibSubModelValidator;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;

import java.util.*;

public class EibCarBuilder {
    private Session eibSession;
    private EibCar eibCar;

    public EibCarBuilder(Session eibSession, EibCar eibCar) {
        this.eibSession = eibSession;
        this.eibCar = eibCar;
    }

    public List<ProductionCar> buildProdCar() {
        List<ProductionCar> result = new ArrayList<>();
        ProductionCar baseCar = new ProductionCar();
        setYears(baseCar);
        setMake(baseCar);
        setModel(baseCar, result);
        setSubModel(result);
        setDrive(result);
      //  setNotes(result);
        validateSubModels(result); //to make sure no spare info in subModels
        result = validateModels(result);

        return result;
    }

    private void setNotes(List<ProductionCar> result) {
        String note = eibCar.getNote();
        if (note==null||note.length()==0){
            return;
        }
        CarAttribute attribute = new CarAttribute("Note",note);
        result.forEach(car -> car.getAttributes().add(attribute));
    }

    private void validateSubModels(List<ProductionCar> result) {
        result.forEach(prodCar -> {
            prodCar = new EibSubModelValidator(prodCar).validateSub();
            String sub = prodCar.getSubModel();
            /*if (sub.length()>0){
                System.out.println(prodCar.getYearStart()+ "\t"+ prodCar.getYearFinish() + "\t" +prodCar.getMake()+ "\t" + prodCar.getModel()+ "\t" + prodCar.getSubModel());
            }*/
        });
    }

    private List<ProductionCar> validateModels(List<ProductionCar> result) {
        result = new EibCarValidator().validateCars(result);
        return result;
    }

    private void setDrive(List<ProductionCar> result) {
        result.forEach(car -> car.setDrive("N/A"));
    }

    private void setSubModel(List<ProductionCar> result) {
        String subModel = eibCar.getSubmodel();
        if (subModel==null){
            subModel = "";
        }
        for (ProductionCar car : result) {
            car.setSubModel(subModel);
        }
    }

    private void setModel(ProductionCar baseCar, List<ProductionCar> productionCars) {
        String model = eibCar.getModel();
        if (model.contains(";")){
            splitModel(";", baseCar, productionCars);
        }
        else if (model.contains("|")){
            splitModel("\\|", baseCar, productionCars);
        }
        else if (model.contains("/")){
            splitModel("/", baseCar, productionCars); //Modified BMW Z3 M-Roadster/M-Coupe in DB as its was only case /w complex split
        }
        else {
            baseCar.setModel(model);
            productionCars.add(baseCar);
        }
    }


    private void splitModel(String divider, ProductionCar baseCar, List<ProductionCar> productionCars) {
        String[] split = eibCar.getModel().split(divider);
        for (String s: split){
            s = s.trim();
            ProductionCar curCar = new ProductionCar();
            curCar.setYearStart(baseCar.getYearStart());
            curCar.setYearFinish(baseCar.getYearFinish());
            curCar.setMake(baseCar.getMake());
            curCar.setModel(s);
            productionCars.add(curCar);
        }
    }

    private void setMake(ProductionCar result) {
        String make = eibCar.getMake();
        make = make.toLowerCase();
        make = make.substring(0, 1).toUpperCase() + make.substring(1);
        make = checkMake(make);
        result.setMake(make);
    }

    private String checkMake(String make) {
        String result = make;
        Map<String, String> makeMap = getMakeMap();
        String s = makeMap.get(result);
        if (s!=null){
            result = s;
        }

        return result;
    }

    private Map<String, String> getMakeMap() {
        Map<String, String> makeMape = new HashMap<>(); //k = wrong, v = right
        makeMape.put("Mercedes-benz", "Mercedes-Benz");
        makeMape.put("Mercedes", "Mercedes-Benz");
        makeMape.put("Alfa-romeo", "Alfa Romeo");
        makeMape.put("Gmc", "GMC");
        makeMape.put("Bmw", "BMW");

        return makeMape;
    }

    private void setYears(ProductionCar result) {
        String year = eibCar.getYear();
        String[] split = year.split(" to ");
        if (split.length==1){
            split = year.split("-");
        }
        String start = split[0];
        if (start.contains("/")){
            start = StringUtils.substringAfter(start, "/");
        }
        String finish = "";
        if (split.length==1){
            finish = split[0];
        }
        else {
            finish = split[1];
        }
        if (finish.contains("/")){
            finish = StringUtils.substringAfter(finish, "/");
        }

        result.setYearStart(Integer.parseInt(start));
        result.setYearFinish(Integer.parseInt(finish));
    }
}
