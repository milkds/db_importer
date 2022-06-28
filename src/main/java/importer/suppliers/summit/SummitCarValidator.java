package importer.suppliers.summit;

import importer.entities.ProductionCar;
import importer.service.CarService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class SummitCarValidator {
    private Map<String, Set<String>> makeModelMap;
    private Set<String> subModelSet;
    private static final Logger logger = LogManager.getLogger(SummitCarValidator.class.getName());

    private Map<String, String> makeCaseMap; //k = lowercase v = correct;
    private Map<String, Map<String, String>> modelCaseMap; //k = lowercase make v = k1-lower/case, v1 - correct;
    private Map<String, String> subModelCaseMap; //k = lowercase v = correct;


    //incorrect - correct maps
    private Map<String, String> makeMap = getMakeMap();
    private Map<String, String> modelMap = getModelMap();
    private Map<String, String> subModelMap = getSubModelMap();


    public ProductionCar validateCar(ProductionCar car) throws NoSuchModelException {
        String make = car.getMake().toLowerCase();
        String correctMake = makeCaseMap.get(make);
        if (correctMake==null){
            correctMake = new SumCarMapper().getCorrectMake(make);
            if (correctMake==null){
                logUnknownCar(car);
                return car;
            }
        }
        car.setMake(correctMake);
        String model = car.getModel();
        if (model==null){
            model = "no model";
         //   logger.info("no model for car " + car);
            throw new NoSuchModelException(car);
        }
        else {
            model = model.toLowerCase();
        }
        Map<String, String> modMap = modelCaseMap.get(make); //we can't get here, if there are no this make in map
        String corModel = null;
        if (modMap != null){
            corModel = modMap.get(model);
        }
        if (corModel==null){
            corModel = new SumCarMapper().getCorrectModel(model);
            if (corModel==null){
                logUnknownCar(car);
                return car;
            }
        }
        car.setModel(corModel);
        String subModel = car.getSubModel().toLowerCase();
        String corSub = subModelCaseMap.get(subModel);
        if (corSub==null){
            corSub = new SumCarMapper().getCorrectSubModel(subModel);
            if (corSub==null){
                logUnknownCar(car);
                return car;
            }
        }
        car.setSubModel(corSub);

        return car;
    }

    /*private void modifyCases(ProductionCar car) {
        String make = changeCase(car.getMake());
        String corMake = makeMap.get(make);
        car.setMake(Objects.requireNonNullElse(corMake, make));

        String model = changeCase(car.getModel());
        String corModel = modelMap.get(model);
        car.setModel(Objects.requireNonNullElse(corModel, model));

        String subModel = changeCase(car.getSubModel());
        String corSubModel = subModelMap.get(subModel);
        car.setSubModel(Objects.requireNonNullElse(corSubModel, subModel));
    }*/

   /* private String changeCase(String s) {
        if (s==null){
            return "";
        }
        String result = "";
        String[] split = s.split(" ");
        for (String sp: split){
            sp = sp.toLowerCase();
            String sp1 = sp.substring(0,1).toUpperCase();
            String sp2 = sp.substring(1);
            result = result + sp1+sp2+" ";
        }

        if (result.endsWith(" ")){
            result = result.substring(0, result.length()-1);
        }

        return result;
    }*/


    private void logUnknownCar(ProductionCar car) {
        System.out.println(car.getYearStart()+"\t"+car.getYearFinish()+"\t" + car.getMake()+"\t" + car.getModel()+"\t" + car.getSubModel());
    }

    private Map<String, String> getMakeMap() {
        Map<String, String> result = new HashMap<>();

        return result;
    }

    private Map<String, String> getSubModelMap() {
        Map<String, String> result = new HashMap<>();
      /*  result.put("Dlx", "DLX");
        result.put("Dlx Turbo", "DLX Turbo");
        result.put("Sr5", "SR5");
        result.put("Sr5 Turbo", "SR5 Turbo");
        result.put("Sr5 Premium", "SR5 Premium");
        result.put("Srt", "SRT");
        result.put("Xlt Sport", "XLT Sport");
        result.put("Xlt No Boundaries", "XLT No Boundaries");
        result.put("S-runner", "S-Runner");
        result.put("Trd Pro", "TRD Pro");
        result.put("Trd Sport", "TRD Sport");
        result.put("Trd Off-road", "TRD Off-Road");
        result.put("Trd Off-road Premium", "TRD Off-Road Premium");
        result.put("Trd Special Edition", "TRD Special Edition");
        result.put("Series Ii", "Series II");
        result.put("Series Ii Le", "Series II LE");
        result.put("Series Ii Sd", "Series II SD");
        result.put("Series Ii Se", "Series II SE");*/

        return result;
    }

    private Map<String, String> getModelMap() {
        Map<String, String> result = new HashMap<>();
       /* result.put("4runner", "4Runner");
        result.put("Land cruiser", "Land Cruiser");
        result.put("Wrangler Jk", "Wrangler JK");
        result.put("Fj Cruiser", "FJ Cruiser");*/

        return result;
    }

    public SummitCarValidator(){
        makeModelMap = CarService.getMakeModelMap();
        subModelSet = CarService.getSubModelSet();

        modelCaseMap = new HashMap<>();
        makeCaseMap = new HashMap<>();
        makeModelMap.forEach((k,v)->{
            String lowerCase = k.toLowerCase();
            makeCaseMap.put(lowerCase, k);
            v.forEach(v1->{
                Map<String, String> v2 = modelCaseMap.get(k.toLowerCase());
                if (v2==null){
                  Map<String, String> v3 = new HashMap<>();
                  v3.put(v1.toLowerCase(), v1);
                  modelCaseMap.put(lowerCase, v3);
                }
                else {
                    v2.put(v1.toLowerCase(), v1);
                }
            });
        });
        subModelCaseMap = new HashMap<>();
        subModelSet.forEach(sub->{
            subModelCaseMap.put(sub.toLowerCase(), sub);
        });
    }
}
