package importer.suppliers.summit.brandcheckers;

import importer.entities.FitmentAttribute;
import importer.entities.ProductionFitment;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class SumFoxLiftExtractor {
    private String appNote;

    public SumFoxLiftExtractor(String appNote) {

        this.appNote = appNote;
    }

    public String getAppNote() {
        return appNote;
    }

    public void setAppNote(String appNote) {
        this.appNote = appNote;
    }

    public String extractLift(ProductionFitment fit) {
        String[] lifts = appNote.split("Lift");
        int length = lifts.length;
        if (length!=2&&length!=3&&length!=4){
         //   System.out.println("Lift length;;;"+lifts.length);
      //      System.out.println("unsual lift appnote ;;;" + appNote);

            return getLiftFromOnePartNote(lifts[0], fit);
        }
        if (lifts.length==3){
            String liftNotePart = getLiftFromLongNote(lifts[0], fit);
            return liftNotePart+lifts[1]+"Lift"+lifts[2];
        }
        else if (lifts.length==2){
            String liftNotePart = getLiftFromShortNote(lifts[1],fit);
            if (liftNotePart.length()==0){
                return lifts[0];
            }
                     else {
                return lifts[0] +" " + liftNotePart;
            }
        }
        else {
            return getLiftFromFourPartNote(lifts[1], fit);
        }
    }

    //appnote without word "Lift"
    private String getLiftFromOnePartNote(String appNote, ProductionFitment fit) {
        String[] split = appNote.split(",");
        String liftPart = split[split.length-1];
        if (liftPart.contains("and")){
            return appNote;
        }
        liftPart = liftPart.trim();
        liftPart = liftPart. replace("\"","");
        setLifts(liftPart, fit);

        return StringUtils.substringBeforeLast(appNote, ",");
    }

    private String getLiftFromFourPartNote(String lift, ProductionFitment fit) {

        String liftNotePart = StringUtils.substringBetween(lift,": ", " ");
        setLifts(liftNotePart, fit);
        return appNote.replace("Lift: "+liftNotePart, "").trim();
    }

    private String getLiftFromShortNote(String liftNotePart, ProductionFitment fit) {
        liftNotePart = liftNotePart.replace(": ", "");
        String liftPart="";
        if (liftNotePart.contains(" ")){
            liftPart = StringUtils.substringBefore(liftNotePart, " ");
            setLifts(liftPart, fit);
            return StringUtils.substringAfter(liftNotePart, " ");
        }
        else {
            setLifts(liftNotePart, fit);
            return "";
        }
    }

    private String getLiftFromLongNote(String appNotePart, ProductionFitment fit) {
        String liftPart = getLiftPartLongNote(appNotePart);
        setLifts(liftPart, fit);
        String remove = ", "+ liftPart + "\"";
        appNotePart = appNotePart.replace(remove, "");

        return appNotePart;
    }

    private void setLifts(String liftPart, ProductionFitment fit) {
        String[] split = liftPart.split("-");
        String liftStart = split[0];
        String liftFinish = "";
        if (split.length==1){
            liftFinish = liftStart;
        }
        else {
            liftFinish = split[1];
        }
        fit.getFitmentAttributes().add(new FitmentAttribute("Lift Start", liftStart));
        fit.getFitmentAttributes().add(new FitmentAttribute("Lift Finish", liftFinish));
    }

    private String getLiftPartLongNote(String appNotePart) {
        String[] split = appNotePart.split(",");
        String result = split[split.length-1];
        result = result. replace("\"","");
        result = result.trim();

        return result;
    }
}
