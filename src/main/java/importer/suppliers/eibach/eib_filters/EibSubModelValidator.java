package importer.suppliers.eibach.eib_filters;

import importer.entities.CarAttribute;
import importer.entities.ProductionCar;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EibSubModelValidator {
    private ProductionCar prodCar;




    public EibSubModelValidator(ProductionCar prodCar) {
        this.prodCar = prodCar;
    }

    public ProductionCar validateSub() {
        String sub = prodCar.getSubModel();
        sub = checkExcludes(sub);
        sub = checkDrive(sub);
        sub = checkBody(sub);
        sub = checkEngine(sub);
        sub = checkTransmission(sub);
        sub = checkSuspension(sub);
        sub = checkGeneration(sub);
        sub = checkNotes(sub);


        prodCar.setSubModel(sub);

        return prodCar;
    }

    private String checkGeneration(String sub) {
        if (sub.length()==0){
            return sub;
        }
        String result = sub;
        List<String> genList = getGenerationList();
        for (String gen : genList) {
            if (sub.contains(gen)) {
                if (doubleGen(gen)){
                    splitGen(prodCar, gen);
                }
                else {
                    prodCar.getAttributes().add(new CarAttribute("Generation", gen));
                }
                sub = sub.replace(gen, "");
                if (sub.length()>0){
                    sub = sub.replace("  ", " ");
                }
                result = sub;
                break;
            }
        }

        return result;
    }

    private void splitGen(ProductionCar prodCar, String gen) {
        String gen1 = "";
        String gen2 = "";
        if (gen.equals("E36/8")){
            gen1 = "E36";
            gen2 = "E38";
        }
        else if (gen.equals("B3/B4")){
            gen1 = "B3";
            gen2 = "B4";
        }

        else {
            gen1 = "B6";
            gen2 = "B7";
        }

        prodCar.getAttributes().add(new CarAttribute("Generation", gen1));
        prodCar.getAttributes().add(new CarAttribute("Generation", gen2));
    }

    private boolean doubleGen(String gen) {
        if (gen.equals("E36/8")){
            return true;
        }
        if (gen.equals("B3/B4")){
            return true;
        }
        if (gen.equals("B6/B7")){
            return true;
        }

        return false;
    }

    private List<String> getGenerationList() {
        List<String> result = new ArrayList<>();
        result.add("JL");
        result.add("JT");
        result.add("JF");
        result.add("JK");
        result.add("NA");
        result.add("NB");
        result.add("NC");
        result.add("ND");
        result.add("J100");
        result.add("J80");
        result.add("MKVII");
        result.add("MKV");
        result.add("MKIII VIN#...<070449");
        result.add("MKIII VIN#...>070450");
        result.add("MKIV");
        result.add("MKII");
        result.add("MKVI");
        result.add("451");
        result.add("955");
        result.add("964");
        result.add("986");
        result.add("987");
        result.add("991");
        result.add("993");
        result.add("996");
        result.add("997");
        result.add("981");
        result.add("B3/B4");
        result.add("B5");
        result.add("B6/B7");
        result.add("B8");
        result.add("B9");
        result.add("C207");
        result.add("C5");
        result.add("C6");
        result.add("C7");
        result.add("D3");
        result.add("E24");
        result.add("E28");
        result.add("E30");
        result.add("E31");
        result.add("E32");
        result.add("E34");
        result.add("E36/8");
        result.add("E36");
        result.add("E38");
        result.add("E39");
        result.add("E46");
        result.add("E52");
        result.add("E53");
        result.add("E60");
        result.add("E63");
        result.add("E64");
        result.add("E65");
        result.add("E70");
        result.add("E82");
        result.add("E83");
        result.add("E85");
        result.add("E88");
        result.add("E89");
        result.add("E90");
        result.add("E91");
        result.add("E92");
        result.add("E93");
        result.add("F01");
        result.add("Gran F06");
        result.add("F06");
        result.add("F07");
        result.add("F10");
        result.add("F12");
        result.add("F13");
        result.add("F15");
        result.add("F16");
        result.add("F22");
        result.add("F23");
        result.add("F25");
        result.add("F30");
        result.add("F31");
        result.add("F32");
        result.add("F33");
        result.add("F36");
        result.add("F48");
        result.add("F54");
        result.add("F56");
        result.add("F60");
        result.add("F80");
        result.add("F82");
        result.add("F83");
        result.add("F85");
        result.add("F86");
        result.add("F87");
        result.add("F90");
        result.add("G30");
        result.add("G60");
        result.add("KL");
        result.add("R129");
        result.add("R170");
        result.add("R230");
        result.add("R35");
        result.add("R50");
        result.add("R55");
        result.add("R56");
        result.add("R57");
        result.add("R58");
        result.add("R60");
        result.add("R61");
        result.add("S197");
        result.add("S550");
        result.add("SJ");
        result.add("SN95");
        result.add("W124T");
        result.add("W124");
        result.add("W126");
        result.add("W140");
        result.add("W163");
        result.add("W164");
        result.add("W201");
        result.add("W202");
        result.add("W203");
        result.add("W204");
        result.add("W205");
        result.add("W208");
        result.add("W209");
        result.add("W210");
        result.add("W211");
        result.add("W212");
        result.add("W215");
        result.add("W219");
        result.add("WK2");
        result.add("XJ");
        result.add("TF");
        result.add("TJ");
        result.add("YJ");
        result.add("ZJ");
        result.add("A90");
        result.add("C117");
        result.add("A207");
        result.add("GSR");
        result.add("MR");
        result.add("SC");
        result.add("VR6 I");
        result.add("VR6");

        return result;
    }

    private String checkTransmission(String sub) {
        if (sub.length()==0){
            return sub;
        }
        String result = sub;
        List<String> transList = getTransList();
        for (String trans : transList) {
            if (sub.contains(trans)) {
                prodCar.getAttributes().add(new CarAttribute("Transmission", trans));
                sub = sub.replace(trans, "");
                if (sub.length()>0){
                    sub = sub.replace("  ", " ");
                }
                result = sub;
                break;
            }
        }

        return result;
    }

    private List<String> getTransList() {
        List<String> result = new ArrayList<>();
        result.add("Manual Trans Only");
        result.add("PDK Trans Only");
        result.add("DSG Trans Only");

        return result;
    }

    private String checkSuspension(String sub) {
        if (sub.length()==0){
            return sub;
        }
        String result = sub;
        List<String> suspList = getSuspensionList();
        for (String susp : suspList) {
            if (sub.contains(susp)) {
                prodCar.getAttributes().add(new CarAttribute("Suspension", susp));
                sub = sub.replace(susp, "");
                if (sub.length()>0){
                    sub = sub.replace("  ", " ");
                }
                result = sub;
                break;
            }
        }
        if (result.contains("(")){
            result = result.replace("(", "");
            result = result.replace(")", "");
        }

        return result;
    }

    private List<String> getSuspensionList() {
        List<String> result = new ArrayList<>();
        result.add("w/Rear Air Suspension");
        result.add("Competition Package");
        result.add("Rear Coil Spring Model Only");
        result.add("Rear Leaf Spring Model Only");
        result.add("with PASM");
        result.add("without PASM");
        result.add("w/out Leveling Control");
        result.add("(Multi-Link Rear)");
        result.add("(Torsion Beam Rear Axle)");
        result.add("w/Autoride");
        result.add("w/out Autoride");
        result.add("w/out QuadraSteer");
        result.add("Magnetic Ride Shocks Only");
        result.add("w/X-REAS");
        result.add("6-Lug Wheel Only Models");


        return result;
    }

    private String checkNotes(String sub) {
        if (sub.length()==0){
            return sub;
        }
        String result = sub;
        List<String> noteList = getNoteList();
        for (String note : noteList) {
            if (sub.contains(note)) {
                prodCar.getAttributes().add(new CarAttribute("Note", note));
                sub = sub.replace(note, "");
                if (sub.length()>0){
                    sub = sub.replace("  ", " ");
                }
                result = sub;
                break;
            }
        }

        return result;
    }

    private List<String> getNoteList() {
        List<String> result = new ArrayList<>();
        result.add("2-Door");
        result.add("4-Door");
        result.add("Standard Range & Standard Range +");
        result.add("Range Extender");

        return result;
    }

    private String checkEngine(String sub) {
        if (sub.length()==0){
            return sub;
        }
        String result = sub;
        List<String> engineList = getEngineList();
        for (String engine : engineList) {
            if (sub.contains(engine)) {
                prodCar.getAttributes().add(new CarAttribute("Engine", engine));
                sub = sub.replace(engine, "");
                if (sub.length()>0){
                    sub = sub.replace("  ", " ");
                }
                result = sub;
                break;
            }
        }

        return result;
    }

    private List<String> getEngineList() {
        List<String> result = new ArrayList<>();
        result.add("4 Cyl.");
        result.add("1.4L Turbo");
        result.add("1.5L Turbo");
        result.add("1.6L Turbo Diesel");
        result.add("1.6L Turbo");
        result.add("1.6L");
        result.add("1.8L Turbo");
        result.add("1.7L");
        result.add("1.8L");
        result.add("2.0L TDI");
        result.add("2.0L Turbo");
        result.add("2.0L");
        result.add("2.2L");
        result.add("2.3L");
        result.add("2.4L Turbo");
        result.add("2.4L");
        result.add("2.5L");
        result.add("2.7L Turbo");
        result.add("2.8L");
        result.add("3.0 Diesel");
        result.add("3.0L");
        result.add("3.2L");
        result.add("3.3L");
        result.add("3.5L");
        result.add("3.6L V6");
        result.add("3.6L");
        result.add("3.8L");
        result.add("4 & 6 Cyl.");
        result.add("4.2L");
        result.add("4.6L");
        result.add("5.0L");
        result.add("5.3L V8");
        result.add("6 Cyl.");
        result.add("8 Cyl.");
        result.add("8-Valve");
        result.add("V6 Diesel");
        result.add("V8/Big Block");
        result.add("V8/Small Block");
        result.add("Diesel");
        result.add("Gas");
        result.add("EcoBoost");
        result.add("V8");
        result.add("V6");

        return result;
    }

    private String checkBody(String sub) {
        if (sub.length()==0){
            return sub;
        }
        String result = sub;
        List<String> bodyList = getBodyList();
        for (String body : bodyList) {
            if (sub.contains(body)) {
                prodCar.getAttributes().add(new CarAttribute("Body", body));
                sub = sub.replace(body, "");
                if (sub.length()>0){
                    sub = sub.replace("  ", " ");
                }
                result = sub;
                break;
            }
        }

        return result;
    }

    private List<String> getBodyList() {
        List<String> result = new ArrayList<>();
        result.add("Hatchback");
        result.add("Sedan");
        result.add("Convertible");
        result.add("Coupe");
        result.add("Wagon");
        result.add("Avant");
        result.add("Cabriolet");
        result.add("F-Body");
        result.add("W-Body");
        result.add("X-Body");
        result.add("Ext Cab/Stepside");
        result.add("Std Cab/Stepside");
        result.add("Crew Cab");
        result.add("Sportback");
        result.add("FOX");
        result.add("A-Body");

       return result;
    }


    private String checkDrive(String sub) {
        if (sub.length()==0){
            return sub;
        }
        if (!sub.contains("WD")){
            return sub;
        }
        List<String> driveList = getDriveList();
        for (String drive : driveList) {
            if (sub.contains(drive)) {
                if (drive.equals("SH-AWD")) {
                    drive = "AWD";
                    sub = sub.replace("SH-AWD", "").trim();
                }
                prodCar.setDrive(drive);
                sub = sub.replace(drive, "");
                break;
            }
        }
        if (sub.length()>0){
            sub = sub.replace("  ", " ").trim();
        }

        return sub;
    }

    private List<String> getDriveList() {
        List<String> result = new ArrayList<>();
        result.add("2WD/4WD");
        result.add("2WD");
        result.add("4WD");
        result.add("FWD");
        result.add("RWD");
        result.add("SH-AWD");
        result.add("AWD");

        return result;
    }

    private String checkExcludes(String sub) {
        if (sub.length()==0){
            return sub;
        }
        if (!sub.contains("Excludes")){
            return sub;
        }
        String beforeEx = StringUtils.substringBefore(sub, "Excludes");
        String note = StringUtils.substringAfter(sub, beforeEx);
        prodCar.getAttributes().add(new CarAttribute("Note", note));

        return beforeEx.trim();
    }
}
