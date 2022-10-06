package importer.suppliers.summit.brandcheckers;

import importer.entities.FitmentAttribute;
import importer.entities.ProductionCar;
import importer.entities.ProductionFitment;
import importer.entities.ProductionItem;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class FoxAppNoteChecker {
    public boolean checkAppNote(ProductionCar car, ProductionFitment fit, ProductionItem prodItem, String appNote) {
        String curNote = extractManBodyCode(appNote, fit);
        curNote = extractLift(curNote, fit);
        curNote = extractReservoir(curNote, fit);
        if (curNote!=null&&curNote.length()>0){
            fit.getFitmentAttributes().add(new FitmentAttribute("Note",curNote));
        }

        return false;
    }

    private String extractReservoir(String curNote, ProductionFitment fit) {
        if (curNote==null||curNote.length()==0){
            return curNote;
        }
        if (curNote.contains("RESERVOIR: REMOTE")||curNote.contains("RESERVOIR: EXTERNAL")){
            fit.getFitmentAttributes().add(new FitmentAttribute("Reservoir", "Yes"));
        }



        return curNote;
    }

    private String extractLift(String appNote, ProductionFitment fit) {
        if (!appNote.toLowerCase(Locale.ROOT).contains("lift")){
            return appNote;
        }
        //will set lift notes to product fit and return appNote without lift


        return new SumFoxLiftExtractor(appNote.replace("lift","Lift")).extractLift(fit);
    }

    private String extractManBodyCode(String appNote, ProductionFitment fit) {
        if (!appNote.contains("Manufacturer Body Code")){
            return appNote;
        }
        else {
            String codeValue = StringUtils.substringBetween(appNote, "Manufacturer Body Code: ", ".");
            if (codeValue==null){
                System.out.println(appNote);
                System.exit(1);
            }
            fit.getFitmentAttributes().add(new FitmentAttribute("Manufacturer Body Code",codeValue));
            appNote = appNote.replace("Manufacturer Body Code: "+codeValue+".","").trim();
        }

        return appNote;
    }
}
