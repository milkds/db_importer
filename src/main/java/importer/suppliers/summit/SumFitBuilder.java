package importer.suppliers.summit;

import importer.entities.*;
import importer.suppliers.summit.entities.SumFitAttribute;
import importer.suppliers.summit.entities.SumFitment;
import importer.suppliers.summit.entities.SumItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

class SumFitBuilder {
    private static final Logger logger = LogManager.getLogger(SumFitBuilder.class.getName());
    private SumItem sumItem;
    private Set<String> mainCarAtts = getMainCarAtts();
    private Set<String> secCarAtts = getSecCarAtts();
    private Set<String> itemAtts = getItemAtts();
    private Set<String> fitAtts = getFitAtts();
    private Map<String, String> sumAppNotesMap;


    SumFitBuilder(SumItem sumItem, Map<String, String> sumAppNotesMap) {
        this.sumItem = sumItem;
        this.sumAppNotesMap = sumAppNotesMap;
    }

    private Set<String> getItemAtts() {
        Set<String> result = new HashSet<>();
        result.add("Shock Model:");
        result.add("Usage:");

        return result;
    }


    Set<ProductionFitment> buildFits(ProductionItem prodItem, SummitCarValidator validator, Map<Integer, List<SumFitAttribute>> allSumFitAtts) {
        Set<ProductionFitment> result = new HashSet<>();
        List<SumFitment> sumFits = sumItem.getFitments();
        if (sumFits.size()==0){
            return result;
        }
        sumFits.forEach(sumFit->{
            try {
                List<SumFitAttribute> sumFitAtts = allSumFitAtts.get(sumFit.getId());
                ProductionFitment fitment = processFitAtts(sumFitAtts, prodItem, validator);
                if  (fitment!=null){
                    result.add(fitment);
                }
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        });

        return result;
    }

    private ProductionFitment processFitAtts(List<SumFitAttribute> attributes, ProductionItem prodItem, SummitCarValidator validator) {
        ProductionFitment result = new ProductionFitment();
        ProductionCar car = getCar(attributes, prodItem, validator, result);
        if (car==null){
            return null;
        }
        setProdFitAtts(attributes, result, prodItem.getItemPartNo());
        processAppNotes(attributes, car, result, prodItem);
        result.setCar(car);
        car.getProductionFitments().add(result);

        return result;
    }

    private void processAppNotes(List<SumFitAttribute> attributes, ProductionCar car, ProductionFitment fit, ProductionItem prodItem) {
        String appNote = "";
        for (SumFitAttribute att : attributes) {
            if (att.getName().equals("Application Notes:")) {
                appNote = att.getValue();
                break;
            }
        }
        if (appNote.length()==0){
            return;
        }
        String[] split = appNote.split(";");
        for (String s: split){
            try {
                processNote(s.trim(), car, fit, prodItem);
            } catch (UnknownApplicationNoteError e) {
                logger.error("Unknown Application Note at item ::: https://www.summitracing.com/parts/frs-" +
                        prodItem.getItemPartNo() + " ::: " + appNote + " at car " + car + ". Unknown part is " + e.getUnknownPart());
            }
        }


       /* if (appNote.contains("Incl. Strut Bearing Plates")){
            processNote(appNote, car, fit, prodItem);
        }
        else {
            String[] split = appNote.split(";");
            for (String s: split){
                processNote(s.trim(), car, fit, prodItem);
            }
        }*/
    }

    private void processNote(String appNote, ProductionCar car, ProductionFitment fit, ProductionItem prodItem) throws UnknownApplicationNoteError {
        String noteObj = sumAppNotesMap.get(appNote);
        if (noteObj==null){
            AppNoteBrandChecker checker = new AppNoteBrandChecker(car, fit, prodItem);
            checker.checkAppNote(appNote);
            if (!checker.processingNeeded()){
                return;
            }
            List<String> noteSplit = getNoteSplit(appNote);
            for (String s: noteSplit){
               noteObj = sumAppNotesMap.get(s);
               if (noteObj==null){
                   UnknownApplicationNoteError e = new UnknownApplicationNoteError();
                   e.setUnknownPart(s);
                   throw e;
               }
               else {
                   processAppNote(noteObj, car, fit, prodItem, s);
               }
            }
        }
        else {
            processAppNote(noteObj, car, fit, prodItem, appNote);
        }
        /*if (noteObj==null){
            logger.error("Unknown Application Note " + appNote + " at item " + prodItem.getItemPartNo());
            return;
           // System.exit(1);
        }
        switch (noteObj){
            case "c": car.getAttributes().add(new CarAttribute("Note", appNote));  break;
            case "f": fit.getFitmentAttributes().add(new FitmentAttribute("Note", appNote));  break;
            case "i": prodItem.getItemAttributes().add(new ItemAttribute("Note", appNote));  break;
            case "LIFT":  processLift(appNote, fit); break;
            case "POS":  processPosition(appNote, fit); break;
            case "POS/LIFT":  {
                processPosition(appNote, fit);
                processLift(appNote, fit);
            } break;
            case "delete": break;
            case "Excl": processExclusions(appNote, car, fit, prodItem); break;
            default: processMultiNotes(appNote, car, fit, prodItem, noteObj);
        }*/
    }

    private void processAppNote(String noteObj, ProductionCar car, ProductionFitment fit, ProductionItem prodItem, String appNote) {
        switch (noteObj){
            case "c": car.getAttributes().add(new CarAttribute("Note", appNote));  break;
            case "f": fit.getFitmentAttributes().add(new FitmentAttribute("Note", appNote));  break;
            case "i": prodItem.getItemAttributes().add(new ItemAttribute("Note", appNote));  break;
            case "LIFT":  processLift(appNote, fit); break;
            case "POS":  processPosition(appNote, fit); break;
            case "POS/LIFT":  {
                processPosition(appNote, fit);
                processLift(appNote, fit);
            } break;
            case "delete":   break;
            case "b": new AppNoteManualBreaker(car, fit, prodItem, appNote).processNote(); break;
            case "Excl": processExclusions(appNote, car, fit, prodItem); break;
            //     default: processMultiNotes(appNote, car, fit, prodItem, noteObj);
        }
    }

    private void processMultiNotes(String appNote, ProductionCar car, ProductionFitment fit, ProductionItem prodItem, String noteObj) {
        String[] objSplit = noteObj.split("/");
        String[] noteSplit = getNoteSplit(appNote).toArray(String[]::new);
        for (int i = 0; i < objSplit.length; i++) {
            switch (objSplit[i]){
                case "i": prodItem.getItemAttributes().add(new ItemAttribute("Note", noteSplit[i])); break;
                case "f": fit.getFitmentAttributes().add(new FitmentAttribute("Note", noteSplit[i])); break;
                case "c": car.getAttributes().add(new CarAttribute("Note", noteSplit[i])); break;
                default: break;
            }
        }
    }

    private List<String> getNoteSplit(String appNote) {
        String[] tilda = appNote.split("~");
        if (!appNote.contains(",")&& !appNote.contains(". ")){
          return Arrays.asList(tilda);
        }
        List<String> res = new ArrayList<>();
        for (String s: tilda){
            String[] comSplit = s.split(",");
            String[] dotsplit;
            if (comSplit.length==1){
          //      dotsplit = s.split("\\. ");
                dotsplit = getDotSplit(s);
                if (dotsplit.length==1){
                    res.add(s);
                }
                else {
                    res.addAll(Arrays.asList(dotsplit));
                }
            }
            else {
                for (String s1: comSplit){
                    dotsplit = getDotSplit(s1);
                    if (dotsplit.length==1){
                       res.add(s1);
                    }
                    else {
                        res.addAll(Arrays.asList(dotsplit));
                    }
                }
            }
        }
        List<String> result = new ArrayList<>();
        res.forEach(note-> result.add(note.trim()));

        return result;
    }

    private String[] getDotSplit(String note) {
       String lowerCase = note.toLowerCase();
       if (noAbbreviates(lowerCase)){
           return note.split("\\. ");
       }
       List<String> res = new ArrayList<>();
       String[] split = note.split("\\. ");
       String curStr = "";
       for (String s: split){
         String end = getExclusionEnd(s);
         if (end.length()==0){
             if (curStr.length()==0){
                 res.add(s);
             }
             else {
                 res.add(curStr+". " + s);
                 curStr="";
             }
         }
         else {
             if (curStr.length()==0){
                 curStr = s;
             }
             else {
                 curStr = curStr + ". " + s;
             }
         }
       }

        return res.toArray(String[]::new);
    }

    private String getExclusionEnd(String s) {
        String end = StringUtils.substringAfterLast(s, " ");
        if  (end.length()==0){
            end = s;
        }
        switch (end.toLowerCase()) {
            case "in":
            case "incl":
            case "excl":
            case "lbs":
            case "exc":
            case "mfg":
                return end;
        }

        return "";
    }

    private boolean noAbbreviates(String lowerCase) {
        if (lowerCase.contains("in. ")){
            return false;
        }
        if (lowerCase.contains("lbs. ")){
            return false;
        }
        if (lowerCase.contains("incl.")){
            return false;
        }
        if (lowerCase.contains("excl.")){
            return false;
        }
        if (lowerCase.contains("exc.")){
            return false;
        }
        if (lowerCase.contains("mfg.")){
            return false;
        }

        return true;
    }

    private void processExclusions(String appNote, ProductionCar car, ProductionFitment fit, ProductionItem prodItem) {
        switch (appNote){
            case "Drive Type: 4WD. Nitrocharger": {
                car.setDrive("4WD");
                prodItem.getItemAttributes().add(new ItemAttribute("Note", "Nitrocharger Series"));
                break;
            }
            case "For use with heavy duty springs, 1.50 in. lift models.": {
                fit.getFitmentAttributes().add(new FitmentAttribute("Lift Start", "1.5"));
                fit.getFitmentAttributes().add(new FitmentAttribute("Lift Finish", "1.5"));
                fit.getFitmentAttributes().add(new FitmentAttribute("Note", "For use with heavy duty springs"));
                break;
            }
            case "Incl. Dust Boot Firmer Valving":{
                prodItem.getItemAttributes().add(new ItemAttribute("Note", "Incl. Dust Boot"));
                fit.getFitmentAttributes().add(new FitmentAttribute("Note", "Firmer Valving"));
                break;
            }
            case "Nitrocharger":{
                prodItem.getItemAttributes().add(new ItemAttribute("Note", "Nitrocharger Series"));
            }

        }
    }

    private void processPosition(String appNote, ProductionFitment fit) {
        String pos = "";
        if (appNote.startsWith("Front")){
            pos = "Front";
        }
        else {
            pos = "Rear";
        }
        appNote = appNote.replace(pos, "").trim();
        if (appNote.length()>0){
            if (appNote.startsWith("Left")){
                pos = pos + " Left";
            }
            if (appNote.startsWith("Right")){
                pos = pos + " Right";
            }
        }
        fit.getFitmentAttributes().add(new FitmentAttribute("Position", pos));
    }

    private void processLift(String appNote, ProductionFitment fit) {
        if (appNote.contains("w/")){
            String lift = StringUtils.substringBetween( appNote,"w/", "in. Lift");
            if (lift==null){
                lift = StringUtils.substringBetween( appNote,"w/", "in. lift");
            }
            setLift(lift, fit);
        }
        else if (appNote.toLowerCase().contains("fits")){
            String lift = StringUtils.substringBetween(appNote.toLowerCase(), "fits", " in.");
            setLift(lift, fit);
        }
        else if (appNote.contains("mm")){
            String lift = StringUtils.substringBefore(appNote, "mm");
            double dLift = Double.parseDouble(lift)/2.5;
            dLift = dLift/10;
            String result = String.format("%.1f", dLift);
            setLift(result, fit);
        }
        else {
            String lift = StringUtils.substringBefore(appNote.toLowerCase(), "in. lift");
            lift = lift.replace("to", "-");
            if (lift.startsWith(".")){
                lift = 0+lift;
            }
            setLift(lift, fit);
        }
    }

    private void setLift(String lift, ProductionFitment fit) {
        String[] split = lift.split("-");
        fit.getFitmentAttributes().add(new FitmentAttribute("Lift Start", split[0].trim()));
        if (split.length==1){
            fit.getFitmentAttributes().add(new FitmentAttribute("Lift Finish", split[0].trim()));
        }
        else {
            fit.getFitmentAttributes().add(new FitmentAttribute("Lift Finish", split[1].trim()));
        }
    }

    private void setProdFitAtts(List<SumFitAttribute> attributes, ProductionFitment prodFit, String itemPartNo) {
        attributes.forEach(attribute->{
            String name = attribute.getName();
            if (fitAtts.contains(name)){
                prodFit.getFitmentAttributes().add(new FitmentAttribute(name, attribute.getValue()));
            }
            else {
              //  logger.info("Unknown main page attribute at item " + itemPartNo + " ::: name = " + name + " ::: value = " + attribute.getValue() );
            }
        });
    }

    private ProductionCar getCar(List<SumFitAttribute> attributes, ProductionItem prodItem, SummitCarValidator validator, ProductionFitment fit) {
        ProductionCar result = new ProductionCar();
        String appNote = "";
        if (isUniversal(attributes)){
       //     logger.info("item is Universal " + prodItem.getItemPartNo());
            for (SumFitAttribute sumAtt : attributes) {
                prodItem.getItemAttributes().add(new ItemAttribute(sumAtt.getName(), sumAtt.getValue()));
            }
            return null;
        }
        for (SumFitAttribute sumAtt : attributes) {
            String name = sumAtt.getName();
            String value = sumAtt.getValue();
            if (mainCarAtts.contains(name)) {
                setCarMainAttribute(name, value, result);
            } else if (secCarAtts.contains(name)) {
                result.getAttributes().add(new CarAttribute(name, value));
            } else if (itemAtts.contains(name)) {
                prodItem.getItemAttributes().add(new ItemAttribute(name, value));
            }
            else if (name.equals("Application Notes:")){
                appNote = value;
            }
            else if (fitAtts.contains(name)) {
            }
            else {
                logger.error("Unknown fit attribute " + name + " at item " + prodItem.getItemPartNo());
             //   System.exit(1);
            }
        }
        checkNullMake(result, appNote, attributes, fit);
        if (result.getMake()==null){
            if (appNote.length()==0){
                return null;
            }

            logger.error("No make for car at item " + sumItem.getPartNo());
            attributes.forEach(logger::info);
            //System.exit(1);

            return null;
        }
        try {
            result = validator.validateCar(result);
        } catch (NoSuchModelException e) {
            logger.info("No model for car at item " + sumItem.getPartNo());
            return null;
        }


        return result;
    }

    private boolean isUniversal(List<SumFitAttribute> attributes) {
        for (SumFitAttribute sumAtt : attributes) {
            if (sumAtt.getName().equals("Universal:")){
                return sumAtt.getValue().equals("Yes");
            }
        }
        return false;
    }

    private void checkNullMake(ProductionCar prodCar, String appNote, List<SumFitAttribute> attributes, ProductionFitment fit) {
        if (prodCar.getMake()!=null){
            return;
        }
        setCarFieldsFromAppNote(prodCar, appNote, fit);
    }

    private void setCarFieldsFromAppNote(ProductionCar prodCar, String appNote, ProductionFitment fit) {
        switch (appNote){
            case "For 1984-2007 Toyota Land Cruiser HZJ71 series.": {
                setFields(1984, 2007, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "HZJ71"));
                break;
            }
            case "For Toyota Land Cruiser HZJ71 models.": {
                setFields(1984, 2007, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "HZJ71"));
                break;
            }
            case "For 1984-2007 Toyota Land Cruiser HZJ74 series.": {
                setFields(1984, 2007, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "HZJ74"));
                break;
            }
            case "For Toyota Land Cruiser HZJ74 models.": {
                setFields(1984, 2007, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "HZJ74"));
                break;
            }
            case "For 1984-98 Toyota Land Cruiser 70 series dual cab models.":{
                setFields(1984, 1998, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "70"));
                prodCar.getAttributes().add(new CarAttribute("Note", "dual cab models"));
                break;
            }
            case "For Land Cruiser 75 series models.":{
                setFields(1985, 1999, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "75"));
                break;
            }
            case "For 1985-99 Toyota Land Cruiser 75 series.":{
                setFields(1985, 1999, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "75"));
                break;
            }
            case "For 1986-90 Toyota Land Cruiser 60 series.":{
                setFields(1986, 1990, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "60"));
                break;
            }
            case "For 1986-90 Toyota Land Cruiser 61 series.":{
                setFields(1986, 1990, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "61"));
                break;
            }
            case "For 1986-90 Toyota Land Cruiser 62 series.":{
                setFields(1986, 1990, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "62"));
                break;
            }
            case "For 1990-98 Toyota Land Cruiser 80 series.":{
                setFields(1990, 1998, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "80"));
                break;
            }
            case "For 1996-2006 Toyota Land Cruiser 78 series models. For 6-cylinder models.":{
                setFields(1996, 2006, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "78"));
                prodCar.getAttributes().add(new CarAttribute("Note", "For 6-cylinder models"));
                break;
            }
            case "For 1996-2006 Toyota Land Cruiser 79 series models. For 6-cylinder models.":{
                setFields(1996, 2006, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "79"));
                prodCar.getAttributes().add(new CarAttribute("Note", "For 6-cylinder models"));
                break;
            }
            case "For 1998-2007 Toyota Land Cruiser 100 series. For models with solid front axle suspension.":{
                setFields(1998, 2007, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "100"));
                prodCar.getAttributes().add(new CarAttribute("Note", "For models with solid front axle suspension"));
                break;
            }
            case "For 1998-2007 Toyota Land Cruiser 105 series. For models with solid front axle suspension.":{
                setFields(1998, 2007, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "105"));
                prodCar.getAttributes().add(new CarAttribute("Note", "For models with solid front axle suspension"));
                break;
            }
            case "For 2012-18 Isuzu D-Max models. Use when fitting extra leaf spring.":{
                setFields(2012, 2018, "Isuzu", "D-Max", "Base", prodCar);
                break;
            }
            case "For 1997-2018 Toyota Hilux models.":{
                setFields(1997, 2018, "Toyota", "Hilux", "Base", prodCar);
                break;
            }
            case "For Toyota Land Cruiser 40 series short wheelbase models. For use with 35mm eyelet springs.":{
                setFields(1960, 2001, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "40"));
                prodCar.getAttributes().add(new CarAttribute("Note", "short wheelbase models"));
                fit.getFitmentAttributes().add(new FitmentAttribute("Note", "For use with 35mm eyelet springs"));
                break;
            }
            case "For Toyota Land Cruiser 40 series short wheelbase models. For use with 25mm eyelet springs.":{
                setFields(1960, 2001, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "40"));
                prodCar.getAttributes().add(new CarAttribute("Note", "short wheelbase models"));
                fit.getFitmentAttributes().add(new FitmentAttribute("Note", "For use with 25mm eyelet springs"));
                break;
            }
            case "For Toyota Land Cruiser 42 series short wheelbase models. For use with 25mm eyelet springs.":{
                setFields(1960, 2001, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "42"));
                prodCar.getAttributes().add(new CarAttribute("Note", "short wheelbase models"));
                fit.getFitmentAttributes().add(new FitmentAttribute("Note", "For use with 25mm eyelet springs"));
                break;
            }
            case "For Toyota Land Cruiser 45 series long wheelbase models. For use with 25mm eyelet springs.":{
                setFields(1960, 2001, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "45"));
                prodCar.getAttributes().add(new CarAttribute("Note", "long wheelbase models"));
                fit.getFitmentAttributes().add(new FitmentAttribute("Note", "For use with 25mm eyelet springs"));
                break;
            }
            case "For Toyota Land Cruiser 47 series long wheelbase models. For use with 25mm eyelet springs.":{
                setFields(1960, 2001, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "47"));
                prodCar.getAttributes().add(new CarAttribute("Note", "long wheelbase models"));
                fit.getFitmentAttributes().add(new FitmentAttribute("Note", "For use with 25mm eyelet springs"));
                break;
            }
            case "For Toyota Land Cruiser 42 series short wheelbase models. For use with 35mm eyelet springs.":{
                setFields(1960, 2001, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "42"));
                prodCar.getAttributes().add(new CarAttribute("Note", "short wheelbase models"));
                fit.getFitmentAttributes().add(new FitmentAttribute("Note", "For use with 35mm eyelet springs"));
                break;
            }
            case "For Toyota Land Cruiser 45 series long wheelbase models. For use with 35mm eyelet springs.":{
                setFields(1960, 2001, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "45"));
                prodCar.getAttributes().add(new CarAttribute("Note", "long wheelbase models"));
                fit.getFitmentAttributes().add(new FitmentAttribute("Note", "For use with 35mm eyelet springs"));
                break;
            }
            case "For Toyota Land Cruiser 47 series long wheelbase models. For use with 35mm eyelet springs.":{
                setFields(1960, 2001, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "47"));
                prodCar.getAttributes().add(new CarAttribute("Note", "long wheelbase models"));
                fit.getFitmentAttributes().add(new FitmentAttribute("Note", "For use with 35mm eyelet springs"));
                break;
            }
            case "For Nissan Patrol MQ 260 long wheelbase models.":{
                setFields(1986, 2002, "Nissan", "Patrol", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "MQ 260"));
                prodCar.getAttributes().add(new CarAttribute("Note", "long wheelbase models"));
                break;
            }
            case "For Nissan Patrol MQ M60 models.":{
                setFields(1960, 1980, "Nissan", "Patrol", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "MQ 60"));
                break;
            }
            case "For 1995-2006 Mitsubishi Triton models":{
                setFields(1995, 2006, "Mitsubishi", "Triton", "Base", prodCar);
                break;
            }
            case "For use with 1987-97 Nissan Patrol Y60 ute and cab chassis models with leaf spring suspension. For use with OME leaf springs only.":{
                setFields(1987, 1997, "Nissan", "Patrol", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "Y60"));
                prodCar.getAttributes().add(new CarAttribute("Note", "ute and cab chassis models with leaf spring suspension"));
                fit.getFitmentAttributes().add(new FitmentAttribute("Note", "For use with OME leaf springs only."));
                break;
            }
            case "For 1978-81 Suzuki LJ80.":{
                setFields(1978, 1981, "Suzuki", "LJ80", "Base", prodCar);
                break;
            }
            case "For 1985-98 Holden Drover.":{
                setFields(1985, 1998, "Holden", "Drover", "Base", prodCar);
                break;
            }
            case "For 2015-18 Mitsubishi Triton MQ models.":{
                setFields(2015, 2018, "Mitsubishi", "Triton", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "MQ"));
                break;
            }
            case "For 2012-18 Isuzu D-Max models.":{
                setFields(2012, 2018, "Isuzu", "D-Max", "Base", prodCar);
                break;
            }
            case "For Holden Drover models.":{
                setFields(1985, 1998, "Holden", "Drover", "Base", prodCar);
                break;
            }
            case "For 2010-17 Volkswagen Amarok models with heavy duty springs.":{
                setFields(2010, 2017, "Volkswagen", "Amarok", "Base", prodCar);
                fit.getFitmentAttributes().add(new FitmentAttribute("Note", "For models with heavy duty springs."));
                break;
            }
            case "For 2010-17 Volkswagen Amarok models. For use with medium duty springs.":{
                setFields(2010, 2017, "Volkswagen", "Amarok", "Base", prodCar);
                fit.getFitmentAttributes().add(new FitmentAttribute("Note", "For use with medium duty springs."));
                break;
            }
            case "For Nissan Navara D23 models.":{
                setFields(2015, 2021, "Nissan", "Navara", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "D23"));
                break;
            }
            case "For Toyota Land Cruiser 70 series dual cab models.":{
                setFields(1984, 1995, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body", "Dual Cab"));
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "70"));
                break;
            }
            case "For Toyota Land Cruiser 70 series models.":{
                setFields(1984, 1995, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "70"));
                break;
            }
            case "For Land Cruiser 70 series models.":{
                setFields(1984, 1995, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "70"));
                break;
            }
            case "For Land Cruiser 73 series models.":{
                setFields(1999, 2007, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "73"));
                break;
            }
            case "For Toyota Land Cruiser 73 series models.":{
                setFields(1999, 2007, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "73"));
                break;
            }
            case "For Land Cruiser 74 series models.":{
                setFields(1999, 2007, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "74"));
                break;
            }
            case "For Toyota Land Cruiser 74 series models.":{
                setFields(1999, 2007, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "74"));
                break;
            }
            case "For Toyota Land Cruiser 78 series Troop Carrier models.":{
                setFields(1999, 2007, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "78"));
                prodCar.getAttributes().add(new CarAttribute("Note", "Troop Carrier"));
                break;
            }
            case "For Toyota Land Cruiser 79 series single cab models.":{
                setFields(1999, 2007, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "79"));
                prodCar.getAttributes().add(new CarAttribute("Body", "Single Cab"));
                break;
            }
            case "For Toyota Land Cruiser 45 series long wheelbase models.":{
                setFields(1960, 2001, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "45"));
                prodCar.getAttributes().add(new CarAttribute("Note", "long wheelbase models"));
                break;
            }
            case "For Toyota Land Cruiser 47 series long wheelbase models.":{
                setFields(1960, 2001, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "47"));
                prodCar.getAttributes().add(new CarAttribute("Note", "long wheelbase models"));
                break;
            }
            case "For Toyota Land Cruiser 60 series models.":{
                setFields(1980, 1990, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "60"));
                break;
            }
            case "For Toyota Land Cruiser 61 series models.":{
                setFields(1980, 1990, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "61"));
                break;
            }
            case "For Toyota Land Cruiser 62 series models.":{
                setFields(1980, 1990, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "62"));
                break;
            }
            case "For Toyota Land Cruiser 75 series models.":{
                setFields(1999, 2007, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "75"));
                break;
            }
            case "For Toyota Land Cruiser 76 series wagon models.":{
                setFields(1999, 2007, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body", "Wagon"));
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "76"));
                break;
            }
            case "For 1998-2004 Santana Samurai models.":{
                setFields(1998, 2004, "Santana", "Samurai", "Base", prodCar);
                break;
            }
            case "For Toyota 4Runner models up to 1997.":{
                setFields(1984, 1997, "Toyota", "4Runner", "Base", prodCar);
                break;
            }
            case "For Toyota Hilux models up to 1997.":{
                setFields(1983, 1997, "Toyota", "Hilux", "Base", prodCar);
                break;
            }
            case "For 1997-2004 Toyota Hilux models.":{
                setFields(1997, 2004, "Toyota", "Hilux", "Base", prodCar);
                break;
            }
            case "For Toyota 4Runner solid axle models up to 1997.":{
                setFields(1984, 1997, "Toyota", "4Runner", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Note", "solid axle models"));
                break;
            }
            case "For 1979-97 Toyota Hilux models without IFS.":{
                setFields(1979, 1997, "Toyota", "Hilux", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Note", "models without IFS"));
                break;
            }
            case "For 1960-84 Toyota Land Cruiser 40 series SWB models.":{
                setFields(1960, 1984, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "40"));
                prodCar.getAttributes().add(new CarAttribute("Note", "SWB models"));
                break;
            }
            case "For Toyota Land Cruiser 40 series short wheelbase models.":{
                setFields(1960, 1984, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "40"));
                prodCar.getAttributes().add(new CarAttribute("Note", "short wheelbase models"));
                break;
            }
            case "For Nissan Patrol GQ Y60 ute and cab chassis models.":{
                setFields(1987, 1997, "Nissan", "Patrol", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "GQ Y60"));
                prodCar.getAttributes().add(new CarAttribute("Note", "ute and cab chassis models"));
                break;
            }
            case "For Nissan Patrol GU Y61 cab chassis models.":{
                setFields(1997, 2013, "Nissan", "Patrol", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "GU Y61"));
                prodCar.getAttributes().add(new CarAttribute("Note", "cab chassis models"));
                break;
            }
            case "For 2000-2007 Nissan Patrol GU models.":{
                setFields(2000, 2007, "Nissan", "Patrol", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "GU"));
                break;
            }
            case "For 1960-84 Toyota Land Cruiser 42 series SWB models.":{
                setFields(1960, 1984, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "42"));
                prodCar.getAttributes().add(new CarAttribute("Note", "SWB models"));
                break;
            }
            case "For Toyota Land Cruiser 42 series short wheelbase models.":{
                setFields(1960, 1984, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "42"));
                prodCar.getAttributes().add(new CarAttribute("Note", "short wheelbase models"));
                break;
            }
            case "For 1960-84 Toyota Land Cruiser 45 series LWB models.":{
                setFields(1960, 1984, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "45"));
                prodCar.getAttributes().add(new CarAttribute("Note", "LWB models"));
                break;
            }
            case "For 1960-84 Toyota Land Cruiser 47 series LWB models.":{
                setFields(1960, 1984, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "47"));
                prodCar.getAttributes().add(new CarAttribute("Note", "LWB models"));
                break;
            }
            case "For 2007-17 Toyota Land Cruiser 76 series models with V8 engines.":{
                setFields(2007, 2017, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "76"));
                prodCar.getAttributes().add(new CarAttribute("Engine", "V8"));
                break;
            }
            case "For 2007-17 Toyota Land Cruiser 78 and 79 series models.":{
                setFields(2007, 2017, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "78"));
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "79"));
                break;
            }
            case "For 1999-2017 Toyota Land Cruiser 78 and 79 Series double cab models. For use with OME springs and shackles.":{
                setFields(1999, 2017, "Toyota", "Land Cruiser", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "78"));
                prodCar.getAttributes().add(new CarAttribute("Body Manufacturer", "79"));
                prodCar.getAttributes().add(new CarAttribute("Note", "double cab models"));
                fit.getFitmentAttributes().add(new FitmentAttribute("Note", "For use with OME springs and shackles."));
                break;
            }
            case "For 2006-11 Ford Ranger PX 1 tonne models.":{
                setFields(2006, 2011, "Ford", "Ranger", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Note", "PX"));
                prodCar.getAttributes().add(new CarAttribute("Note", "1 tonne"));
                break;
            }
            case "For 1985-2005 Ford Ranger 1 tonne models.":{
                setFields(1985, 2005, "Ford", "Ranger", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Note", "1 tonne"));
                break;
            }
            case "For 1985-2005 Ford Ranger 1 tonne..":{
                setFields(1985, 2005, "Ford", "Ranger", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Note", "1 tonne"));
                break;
            }
            case "For 2012-17 Ford Ranger PX 1 tonne models.":{
                setFields(2012, 2017, "Ford", "Ranger", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Note", "PX"));
                prodCar.getAttributes().add(new CarAttribute("Note", "1 tonne"));
                break;
            }
            case "For 2006-11 Mazda BT-50 1 tonne models.":{
                setFields(2006, 2011, "Mazda", "BT-50", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Note", "1 tonne"));
                break;
            }
            case "For 1985-2005 Mazda B-series 1 tonne models.":{
                setFields(1985, 2005, "Mazda", "B Series", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Note", "1 tonne"));
                break;
            }
            case "For 2012-17 Mazda BT-50 1 tonne models.":{
                setFields(2012, 2017, "Mazda", "BT-50", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Note", "1 tonne"));
                break;
            }
            case "For 1987-2006 Mazda B Series 1 tonne.":{
                setFields(1987, 2006, "Mazda", "B Series", "Base", prodCar);
                prodCar.getAttributes().add(new CarAttribute("Note", "1 tonne"));
                break;
            }
            case "For 2008-11 Isuzu D-Max models.":{
                setFields(2008, 2011, "Isuzu", "D-Max", "Base", prodCar);
                break;
            }
            case "Fits Workhorse W20-W24":{
                setFields(1999, 2007, "Workhorse", "W24", "Base", prodCar);
                break;
            }
            case "Fits Workhorse W16-W18":{
                setFields(2005, 2007, "Workhorse", "W18", "Base", prodCar);
                break;
            }/*
            case "Fits Workhorse P30,P32 and widetrack IFS.":{
                setFields(2000, 2005, "Workhorse", "P30", "Base", prodCar);
                break;
            }
            case "Fits Country Coach Affinity,Concept,Magna and MAT with Neway IFS suspension.":{
                setFields(1999, 2007, "Country Coach Motorhome", "Affinity", "Base", prodCar);
                break;
            }
            case "Fits Country Coach Allure and Intrigue with Neway IFS suspension.":{
                setFields(1999, 2007, "Country Coach Motorhome", "Allure", "Base", prodCar);
                break;
            }
            case "Fits Monaco Dynasty,Executive,Signature and Windsor with 4 shocks/axle (pin/eye).":{
                setFields(1993, 2002, "Monaco", "Dynasty", "Base", prodCar);
                break;
            }
            case "Fits Monaco Dynasty,Executive,Signature and Windsor with 10 bag coach.":{
                setFields(1993, 2006, "Monaco", "Dynasty", "Base", prodCar);
                break;
            }
            case "Fits Foretravel U-280,U-300, U-320 Series":{
                setFields(1991, 1995, "Foretravel", "U-280", "Base", prodCar);
                break;
            }
            case "Fits Safari Magnum with leaf spring.":{
                setFields(1993, 1999, "Safari", "Magnum", "Base", prodCar);
                break;
            }
            case "Fits up to 2002 Bluebird LTC-40":{
                setFields(1999, 2002, "Bluebird Wanderlodge", "LTC-40", "Base", prodCar);
                break;
            }
            case "Bluebird Wanderlodge tag axle non-steerable":{
                setFields(1982, 1992, "Bluebird Wanderlodge", "PT", "Base", prodCar);
                break;
            }
            case "Fits Spartan MC 2000":{
                setFields(1991, 1995, "Spartan Motors", "2000", "Base", prodCar);
                break;
            }
            case "Fits Mack DM series only.":{
                setFields(1966, 2005, "Mack", "DM", "Base", prodCar);
                break;
            }
            case "Fits Bluebird Wanderlodge M380,450LXi.":{
                setFields(2002, 2007, "Bluebird Wanderlodge", "M380", "Base", prodCar);
                break;
            }
            case "Fits Bluebird Express.":{
                setFields(2003, 2007, "Bluebird", "Express", "Base", prodCar);
                break;
            }*/
        }
    }
    public void setFields(int yearStart, int yearFinish, String make, String model, String subModel, ProductionCar prodCar) {
        prodCar.setYearStart(yearStart);
        prodCar.setYearFinish(yearFinish);
        prodCar.setMake(make);
        prodCar.setModel(model);
        prodCar.setSubModel(subModel);
    }

    private void setCarMainAttribute(String name, String value, ProductionCar result) {
        switch (name){
            case "Make:": result.setMake(value); break;
            case "Model:": result.setModel(value); break;
            case "Submodel:": result.setSubModel(value); break;
            case "Beginning Year:": result.setYearStart(Integer.parseInt(value)); break;
            case "Ending Year:": result.setYearFinish(Integer.parseInt(value)); break;
            case "Drivetrain:": result.setDrive(value); break;
        }
    }

    private Set<String> getMainCarAtts() {
        Set<String> result = new HashSet<>();
        result.add("Make:");
        result.add("Model:");
        result.add("Submodel:");
        result.add("Beginning Year:");
        result.add("Ending Year:");
        result.add("Drivetrain:");

        return result;
    }

    private Set<String> getSecCarAtts() {
        Set<String> result = new HashSet<>();
        result.add("Engine Type:");
        result.add("Engine Size:");
        result.add("Engine Family:");
        result.add("Engine VIN Code:");
        result.add("Number of Lugs:");
        result.add("Fuel Type:");
        result.add("Front Suspension:");
        result.add("Liter:");
        result.add("CC:");
        result.add("CID:");
        result.add("Transmission:");
        result.add("Body Style:");
        result.add("Doors:");

        return result;
    }

    private Set<String> getFitAtts() {
        Set<String> result = new HashSet<>();
        result.add("Shock Position:");
        result.add("Sway Bar Mounting Bracket Location:");
        result.add("Axle Location:");
        result.add("Control Arm Position:");
        result.add("Spring Position:");
        result.add("Amount Raised:");
        result.add("Amount of Lift Front:");
        result.add("Sway Bar End Link Position:");
        result.add("Isolator Position:");
        result.add("Leaf Spring Position:");
        result.add("Leaf Spring Shackle Bolt Position:");
        result.add("Universal:");
        result.add("Bump Stop Position:");
        result.add("Add-A-Leaf Position:");
        result.add("Amount of Lift Rear:");
        result.add("Leaf Spring Type (Application):");
        result.add("Position (application):");
        result.add("Amount of Drop Front:");
        result.add("Amount of Drop Rear:");
        result.add("Strut Mount Position:");

        return result;
    }
}
