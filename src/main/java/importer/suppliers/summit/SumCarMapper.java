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
        result.put("gran sport", "Gran Sport");
        result.put("225 custom", "225 Custom");
        result.put("club wagon", "Club Wagon");
        result.put("custom wagon", "Custom Wagon");
        result.put("chateau wagon", "Chateau Wagon");
        result.put("type s-3", "Type S-3");
        result.put("gto", "GTO");
        result.put("pony", "Pony");
        result.put("mpg runabout", "MPG Runabout");
        result.put("runabout", "Runabout");
        result.put("s runabout", "S Runabout");
        result.put("lxxv anniversary edition", "LXXV Anniversary Edition");
        result.put("z7 gs", "Z7 GS");
        result.put("eurosport", "Eurosport");
        result.put("x-11", "X-11");
        result.put("cruiser ls", "Cruiser LS");
        result.put("ste", "STE");
        result.put("olympia limited", "Olympia Limited");
        result.put("lx brougham", "LX Brougham");
        result.put("xr5", "XR5");
        result.put("international", "International");
        result.put("value leader", "Value Leader");
        result.put("lts", "LTS");
        result.put("sts", "STS");
        result.put("z26", "Z26");
        result.put("z24", "Z24");
        result.put("etc", "ETC");
        result.put("g", "G");
        result.put("sho", "SHO");
        result.put("montana", "Montana");
        result.put("esc", "ESC");
        result.put("se comfort", "SE Comfort");
        result.put("ls premium", "LS Premium");
        result.put("warner bros.", "Warner Bros.");
        result.put("svg", "SVG");
        result.put("cx plus", "CX Plus");
        result.put("dream", "Dream");
        result.put("ls sport", "LS Sport");
        result.put("executive protection series", "Executive Protection Series");
        result.put("ultra", "Ultra");
        result.put("protection series", "Protection Series");
        result.put("ultimate l", "Ultimate L");
        result.put("sv6", "SV6");
        result.put("2", "2");
        result.put("3", "3");
        result.put("dream cruiser", "Dream Cruiser");
        result.put("street cruiser", "Street Cruiser");
        result.put("signature l", "Signature L");
        result.put("signature limited", "Signature Limited");
        result.put("gsl", "GSL");
        result.put("ultimate edition", "Ultimate Edition");
        result.put("r l.l. bean edition", "R L.L. Bean Edition");
        result.put("vdc limited", "VDC Limited");
        result.put("gtp", "GTP");
        result.put("fleet", "Fleet");
        result.put("designer series", "Designer Series");
        result.put("r vdc limited", "R VDC Limited");
        result.put("lx special edition", "LX Special Edition");
        result.put("2.5i basic", "2.5i Basic");
        result.put("2.5i l.l. bean edition", "2.5i L.L. Bean Edition");
        result.put("2.5i limited l.l. bean edition", "2.5i Limited L.L. Bean Edition");
        result.put("mainstreet", "Mainstreet");
        result.put("avp", "AVP");
        result.put("crossroad", "Crossroad");



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
