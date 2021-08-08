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
        result.put("del ray", "Del Ray");
        result.put("futura sprint", "Futura Sprint");
        result.put("sst", "SST");
        result.put("60 special", "60 Special");
        result.put("60 special brougham", "60 Special Brougham");
        result.put("mx brougham", "MX Brougham");
        result.put("brougham ls", "Brougham LS");
        result.put("brougham lx", "Brougham LX");
        result.put("brougham sx", "Brougham SX");
        result.put("sportabout", "Sportabout");
        result.put("elite", "Elite");
        result.put("amx", "AMX");
        result.put("z7", "Z7");
        result.put("biarritz", "Biarritz");
        result.put("formal", "Formal");
        result.put("cobra", "Cobra");
        result.put("sundowner", "Sundowner");
        result.put("sundowner base", "Sundowner Base");
        result.put("sundowner dlx", "Sundowner DLX");
        result.put("sundowner se-5", "Sundowner SE-5");
        result.put("024", "024");
        result.put("cadet", "Cadet");
        result.put("pucci", "Pucci");
        result.put("024 miser", "024 Miser");
        result.put("sundowner sport", "Sundowner Sport");
        result.put("black magic", "Black Magic");
        result.put("e-type", "E-Type");
        result.put("bill blass", "Bill Blass");
        result.put("024 charger 2.2", "024 Charger 2.2");
        result.put("turismo", "Turismo");
        result.put("shelby", "Shelby");
        result.put("crimson cat", "Crimson Cat");
        result.put("cruiser lx", "Cruiser LX");
        result.put("cruiser s", "Cruiser S");
        result.put("cruiser sx", "Cruiser SX");
        result.put("turbo gt", "Turbo GT");
        result.put("xx anniversary edition", "XX Anniversary Edition");
        result.put("rs turbo", "RS Turbo");
        result.put("type-10", "Type-10");
        result.put("olympic", "Olympic");
        result.put("d'oro", "d'Oro");
        result.put("1500 s", "1500 S");
        result.put("gt-350 20th anniversary", "GT-350 20th Anniversary");
        result.put("svo", "SVO");
        result.put("lc", "LC");
        result.put("glh", "GLH");
        result.put("elan", "Elan");
        result.put("fila", "Fila");
        result.put("supreme", "Supreme");
        result.put("le limited", "LE Limited");
        result.put("5.0", "5.0");
        result.put("country squire lx", "Country Squire LX");
        result.put("crown victoria lx", "Crown Victoria LX");
        result.put("xr3", "XR3");
        result.put("mt5", "MT5");
        result.put("fx16", "FX16");
        result.put("fx", "FX");
        result.put("vl", "VL");
        result.put("sse", "SSE");
        result.put("fx16 gts", "FX16 GTS");
        result.put("america", "America");
        result.put("ls special edition", "LS Special Edition");
        result.put("dlx all trac", "DLX All Trac");
        result.put("le all trac", "LE All Trac");
        result.put("aerocoupe", "Aerocoupe");
        result.put("aerocoupe yl", "Aerocoupe YL");
        result.put("twin cam", "Twin Cam");
        result.put("2.0 si 4ws", "2.0 Si 4WS");
        result.put("2.0 si", "2.0 Si");
        result.put("2.0 s", "2.0 S");
        result.put("sei", "SEi");
        result.put("gse", "GSE");
        result.put("gtz", "GTZ");
        result.put("gsi", "GSi");
        result.put("park avenue ultra", "Park Avenue Ultra");
        result.put("euro", "Euro");
        result.put("luxury edition", "Luxury Edition");
        result.put("spring edition", "Spring Edition");
        result.put("regency elite", "Regency Elite");
        result.put("4wd", "4WD");
        result.put("trio", "Trio");
        result.put("royale ls", "Royale LS");
        result.put("se-r", "SE-R");
        result.put("lx-e", "LX-E");
        result.put("scx", "SCX");
        result.put("gst", "GST");
        result.put("gsx", "GSX");
        result.put("ssei", "SSEi");
        result.put("z34", "Z34");
        result.put("le value leader", "LE Value Leader");
        result.put("duster", "Duster");
        result.put("super coupe", "Super Coupe");
        result.put("esi", "ESi");
        result.put("jxi", "JXi");
        result.put("ve", "VE");
        result.put("acr", "ACR");
        result.put("dhs", "DHS");
        result.put("dts", "DTS");
        result.put("nautica", "Nautica");
        result.put("royale lss", "Royale LSS");
        result.put("spyder gs", "Spyder GS");
        result.put("spyder gst", "Spyder GST");
        result.put("olympic gold edition", "Olympic Gold Edition");
        result.put("ss/t 5.9l", "SS/T 5.9L");
        result.put("zx2 cool coupe", "ZX2 Cool Coupe");
        result.put("zx2 hot coupe", "ZX2 Hot Coupe");
        result.put("zx2 s/r", "ZX2 S/R");
        result.put("zx2", "ZX2");
        result.put("gt1", "GT1");
        result.put("se1", "SE1");
        result.put("se2", "SE2");
        result.put("ca", "CA");
        result.put("gxp", "GXP");
        result.put("1", "1");
        result.put("style", "Style");
        result.put("cargo", "Cargo");
        result.put("brighton", "Brighton");
        result.put("spyder gt", "Spyder GT");
        result.put("spyder gts", "Spyder GTS");
        result.put("lxi limited", "LXi Limited");
        result.put("ss 35th anniversary edition", "SS 35th Anniversary Edition");
        result.put("ss dale earnhardt signature edition", "SS Dale Earnhardt Signature Edition");
        result.put("neiman marcus", "Neiman Marcus");
        result.put("pro-am", "Pro-Am");
        result.put("l se", "L SE");
        result.put("l 35th anniversary", "L 35th Anniversary");
        result.put("ls competition", "LS Competition");
        result.put("ss high sport", "SS High Sport");
        result.put("ss pace car", "SS Pace Car");
        result.put("james bond edition", "James Bond Edition");
        result.put("maxx", "Maxx");
        result.put("maxx ls", "Maxx LS");
        result.put("maxx ss", "Maxx SS");
        result.put("maxx lt", "Maxx LT");
        result.put("maxx ltz", "Maxx LTZ");
        result.put("srt-10", "SRT-10");
        result.put("xle limited", "XLE Limited");
        result.put("pacific coast roadster", "Pacific Coast Roadster");
        result.put("red line", "Red Line");
        result.put("green line", "Green Line");
        result.put("ex special edition", "EX Special Edition");
        result.put("xs l.l. bean edition", "XS L.L. Bean Edition");
        result.put("rt", "RT");
        result.put("rtl", "RTL");
        result.put("rts", "RTS");
        result.put("lx-p", "LX-P");
        result.put("lx-s", "LX-S");
        result.put("srt-4", "SRT-4");
        result.put("sports 2.5 xt", "Sports 2.5 XT");
        result.put("sports 2.5 x", "Sports 2.5 X");
        result.put("classic lt", "Classic LT");
        result.put("hybrid-l", "Hybrid-L");
        result.put("sport v6", "Sport V6");
        result.put("sport edition", "Sport Edition");
        result.put("2.5 gt", "2.5 GT");
        result.put("1.6", "1.6");
        result.put("1.6 base", "1.6 Base");
        result.put("rush", "Rush");
        result.put("uptown", "Uptown");
        result.put("livery", "Livery");
        result.put("touring gls", "Touring GLS");
        result.put("touring se", "Touring SE");
        result.put("cxl special edition", "CXL Special Edition");
        result.put("walter p. chrysler signature series", "Walter P. Chrysler Signature Series");
        result.put("fe", "FE");
        result.put("crosscabriolet", "CrossCabriolet");
        result.put("sport limited", "Sport Limited");
        result.put("sport premium", "Sport Premium");
        result.put("1.8 s", "1.8 S");
        result.put("1.8 sl", "1.8 SL");
        result.put("1.6 s", "1.6 S");
        result.put("1.6 s plus", "1.6 S Plus");
        result.put("1.6 sl", "1.6 SL");
        result.put("1.6 sv", "1.6 SV");
        result.put("black diamond ltz", "Black Diamond LTZ");
        result.put("sxl", "SXL");
        result.put("rtx", "RTX");
        result.put("ln", "LN");
        result.put("f85", "F85");
        result.put("s-55", "S-55");
        result.put("sxl turbo", "SXL Turbo");
        result.put("sx turbo", "SX Turbo");
        result.put("sr turbo", "SR Turbo");
        result.put("fe+s", "FE+S");
        result.put("fe+sv", "FE+SV");
        result.put("american value package", "American Value Package");
        result.put("se 30th anniversary edition", "SE 30th Anniversary Edition");
        result.put("sxt 30th anniversary edition", "SXT 30th Anniversary Edition");
        result.put("30th anniversary edition", "30th Anniversary Edition");
        result.put("fuel cell", "Fuel Cell");
        result.put("limited platinum", "Limited Platinum");
        result.put("sport special edition", "Sport Special Edition");
        result.put("50th anniversary special edition", "50th Anniversary Special Edition");
        result.put("police responder", "Police Responder");
        result.put("x l.l. bean edition", "X L.L. Bean Edition");
        result.put("couture edition", "Couture Edition");
        result.put("225 limited", "225 Limited");
        result.put("warlock ii", "Warlock II");
        result.put("li'l red express", "Li'l Red Express");
        result.put("voyager ex", "Voyager EX");
        result.put("voyager ex wagon", "Voyager EX Wagon");

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
        result.put("master 85", "Master 85");
        result.put("special deluxe", "Special Deluxe");
        result.put("dj", "DJ");
        result.put("fleetmaster", "Fleetmaster");
        result.put("stylemaster series", "Stylemaster Series");
        result.put("styleline deluxe", "Styleline Deluxe");
        result.put("styleline special", "Styleline Special");
        result.put("nomad", "Nomad");
        result.put("commuter", "Commuter");
        result.put("ranch wagon", "Ranch Wagon");
        result.put("cb300", "CB300");
        result.put("60 special", "60 Special");
        result.put("commercial chassis", "Commercial Chassis");


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
