package importer.suppliers.summit;

import java.util.HashMap;
import java.util.Map;

class SumCarMapper {
    private Map<String, String> makeMap;
    private Map<String, String> modelMap;
    private Map<String, String> subModelMap;

    String getCorrectMake(String make) {
        return makeMap.get(make);
    }

    SumCarMapper() {
       makeMap = getMakeMap();
       modelMap = getModelMap();
       subModelMap = getSubModelMap();
    }

    private Map<String, String> getSubModelMap() {
        Map<String, String> result = new HashMap<>();
        result.put("hybrid xle premium", "Hybrid XLE Premium");
        result.put("jls","JLS");
        result.put("jls plus","JLS Plus");
        result.put("jlx plus","JLX Plus");
        result.put("jlx plus se","JLX Plus SE");
        result.put("jls plus se","JLS Plus SE");
        result.put("js plus","JS Plus");
        result.put("jx plus","JX Plus");
        result.put("jx se","JX SE");
        result.put("le eco", "LE Eco");
        result.put("le eco plus", "LE Eco Plus");
        result.put("limited x", "Limited X");
        result.put("se sport", "SE Sport");
        result.put("s-runner", "S-Runner");
        result.put("ultimate adventure edition","Ultimate Adventure Edition");
        result.put("xl-7","XL-7");
        result.put("xl-7 limited","XL-7 Limited");
        result.put("xl-7 plus","XL-7 Plus");
        result.put("xl-7 touring","XL-7 Touring");
        result.put("xle premium", "XLE Premium");
        result.put("xle touring", "XLE Touring");
        result.put("xle touring se", "XLE Touring SE");
        result.put("hybrid xle touring", "Hybrid XLE Touring");
        result.put("xls sport", "XLS Sport");
        result.put("xlt no boundaries", "XLT No Boundaries");
        result.put("x-runner", "X-Runner");
        result.put("xse", "XSE");
        result.put("4xe", "4xe");
        result.put("4xe rubicon", "4xe Rubicon");
        result.put("4xe sahara", "4xe Sahara");
        result.put("80th anniversary", "80th Anniversary");
        result.put("freedom", "Freedom");
        result.put("unlimited 80th anniversary", "Unlimited 80th Anniversary");
        result.put("unlimited freedom", "Unlimited Freedom");
        result.put("unlimited high altitude", "Unlimited High Altitude");
        result.put("unlimited islander", "Unlimited Islander");
        result.put("unlimited rubicon 392", "Unlimited Rubicon 392");
        result.put("unlimited sport altitude", "Unlimited Sport Altitude");
        result.put("unlimited sahara altitude", "Unlimited Sahara Altitude");
        result.put("unlimited willys", "Unlimited Willys");
        result.put("unlimited willys sport", "Unlimited Willys Sport");
        result.put("willys sport", "Willys Sport");


        return result;
    }

    private Map<String, String> getModelMap() {
        Map<String, String> result = new HashMap<>();
        result.put("land rover","Land Rover"); //old land rovers 1948-1974
        result.put("hi-lux","Hi-Lux");
        result.put("lj80","LJ80");
        result.put("navara","Navara");
        result.put("triton","Triton");
        result.put("drover","Drover");
        result.put("amarok","Amarok");
        result.put("samurai","Samurai");
        result.put("b series", "B Series");
        result.put("bt-50", "BT-50");

        return result;
    }

    //k = incorrect, v = correct
    //makes will be always lowcase
    private Map<String, String> getMakeMap() {
        Map<String, String> result = new HashMap<>();
        result.put("santana", "Santana");
        result.put("holden", "Holden");

        return result;
    }

    String getCorrectModel(String model) {
        return modelMap.get(model);
    }

    String getCorrectSubModel(String subModel) {
        return subModelMap.get(subModel);
    }
}
