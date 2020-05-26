package importer.suppliers.keystone;

import importer.entities.FitmentAttribute;
import importer.suppliers.keystone.entities.ItemCarAttribute;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

class KeyLiftBuilder {
    private static final Logger logger = LogManager.getLogger(KeyLiftBuilder.class.getName());
    List<FitmentAttribute> buildLifts(ItemCarAttribute keyAtt) {
        List<FitmentAttribute> result = new LinkedList<>();
        String value = keyAtt.getAttValue();
        result.add(new FitmentAttribute(keyAtt.getAttName(), value));
        if (noLift(value)){
            return result;
        }
        if (value.contains("=")){
            result.addAll(getEqLifts(value)); //if value contains "lift
        }
        else {
         try {
             result.addAll(getWithLifts(value)); //common case With/To
         }
         catch (NumberFormatException e){
             logger.error("Wrong lift argument " + value);
             System.exit(1);
         }
        }

        return result;
    }

    private List<FitmentAttribute> getWithLifts(String value) {
        List<FitmentAttribute> result = new LinkedList<>();
        String raw = StringUtils.substringBetween(value, "With", "Inch").trim();
        raw = raw.toLowerCase();
        String start = "";
        String finish = "";
        if (!raw.contains("to")){
            if (raw.contains("/")){
                raw = convertFractions(raw);
            }
            start = raw;
            finish = raw;
        }
        else {
            String[] split = raw.split(" to ");
            if (split[0].contains("/")){
                split[0] = convertFractions(split[0]);
            }
            if (split[1].contains("/")){
                split[1] = convertFractions(split[1]);
            }
            start = split[0];
            finish = split[1];
        }
        result.add(new FitmentAttribute("Lift Start", start));
        result.add(new FitmentAttribute("Lift Finish", finish));

        return result;
    }

    private List<FitmentAttribute> getEqLifts(String value) {
        List<FitmentAttribute> result = new ArrayList<>();
        String rawLift = StringUtils.substringBetween(value, "=", ";");
        rawLift = rawLift.trim();
        String[] split = rawLift.split("-");
        int length = split.length;
        //for case when start/finish differ and have fractions in value and higher than 1
        if (length>2){
            String[] tmp = new String[2];
            String start = split[0];
            String finish = "";
            if (split[1].contains("/")){
                start = start + "-" + split[1];
            }
            else {
                finish = split[1];
            }
            if (finish.length()==0){
                finish = split[2];
            }
            else {
                finish = finish + "-" + split[2];
            }
            if (length==4){
                finish = finish + "-" + split[3];
            }
            tmp[0] = start;
            tmp[1] = finish;
            split = tmp;
        }
        if (split[0].contains("/")){
            split[0] = convertFractions(split[0]);
        }
        if (length==2){
            if (split[1].contains("/")){
                split[1] = convertFractions(split[1]);
            }
        }
        result.add(new FitmentAttribute("Lift Start", split[0]));
        if (length==2){
            result.add(new FitmentAttribute("Lift Finish", split[1]));
        }
        else {
            result.add(new FitmentAttribute("Lift Finish", split[0]));
        }

        return result;
    }

    private String convertFractions(String s) {
        String[] split = s.split("-");
        String fraction = "";
        double base = 0;
        int length = split.length;
        if (length==1){
            fraction = split[0];
        }
        else {
            fraction = split[1];
            base = Double.parseDouble(split[0]);
        }
        String[] fSplit = fraction.split("/");
        double up = Double.parseDouble(fSplit[0]);
        double down = Double.parseDouble(fSplit[1]);
        double fr = up/down+base;

        return fr+"";
    }

    private boolean noLift(String value) {
        String tmp = value;
        if (!tmp.contains("Lift")){
            return true;
        }
        if (tmp.contains("=")){
            return false;
        }
        Set<String> noLiftMarkers = getNoLiftMarkers();
        for (String s: noLiftMarkers){
            if (tmp.contains(s)){
                return true;
            }
        }
        tmp = tmp.toLowerCase();
        tmp = tmp.replace("lifted", "");
        if (!tmp.contains("lift")){
            return true;
        }

        return false;
    }

    private Set<String> getNoLiftMarkers() {
        Set<String> result = new HashSet<>();
        result.add("Lift Kit");
        result.add("Pro Comp Lift");
        result.add("Rancho Lift");

        return result;
    }
}
