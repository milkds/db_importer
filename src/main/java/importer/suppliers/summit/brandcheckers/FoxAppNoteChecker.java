package importer.suppliers.summit.brandcheckers;

import importer.entities.ProductionCar;
import importer.entities.ProductionFitment;
import importer.entities.ProductionItem;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class FoxAppNoteChecker {
    public boolean checkAppNote(ProductionCar car, ProductionFitment fit, ProductionItem prodItem, String appNote) {
        String curNote = extractManBodyCode(appNote, fit);
        curNote = extractLift(appNote, fit);

        return false;
    }

    private String extractLift(String appNote, ProductionFitment fit) {
        if (!appNote.toLowerCase(Locale.ROOT).contains("lift")){
            return appNote;
        }
        //will set lift notes to product fit and return appNote without lift
        String result = new SumFoxLiftExtractor(appNote.replace("lift","Lift")).extractLift(fit);


        return result;
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
        }

        return appNote;
    }
}
