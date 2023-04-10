package importer.entities;

import importer.dao.CarDAO;
import importer.dao.FitmentDAO;
import importer.entities.links.FitmentAttributeLink;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class FitmentSaver {
    private Session session;
    private static final Logger logger = LogManager.getLogger(FitmentSaver.class.getName());
    private Map<String, FitmentAttribute> fitAttMap;//k = att name + att val, v = att
    private Map<Integer, ProductionFitment> fitmentMap;//k = fit ID, v = fit

    public FitmentSaver(Session session) {
        Instant start = Instant.now();
        this.session = session;
        fitAttMap = initFitAttMap(session);
        fitmentMap = initFitmentMap(session);
        initAttFits(session);
        Instant finish = Instant.now();
        logger.info("Initiated Fit Saver in " + Duration.between(start,finish));
    }

    private Map<Integer, ProductionFitment> initFitmentMap(Session session) {
        Instant start = Instant.now();
        List<ProductionFitment> allFits = FitmentDAO.getAllFits(session);
        Map<Integer, ProductionFitment> result  = new HashMap<>();//k = fit ID, v = fit
        allFits.forEach(fit->{
            result.put(fit.getFitmentID(), fit);
        });
        Instant finish = Instant.now();
        logger.info("Initiated Fit map in " + Duration.between(start,finish));

        return result;
    }

    //we need this method to avoid going db each time, when we call att.getFits() to add there new fit.
    private void initAttFits(Session session) {
        Instant start = Instant.now();
        List<FitmentAttributeLink> allLinks = FitmentDAO.getAllFitAttLinks(session);
        Map<Integer, List<ProductionFitment>> attOnFitMap = new HashMap<>();//k = attID, v = list of fits for this att
        allLinks.forEach(link -> {
            int fitID = link.getFitID();
            int attID = link.getAttID();
            List<ProductionFitment> curFits = attOnFitMap.computeIfAbsent(attID, k -> new ArrayList<>());
            curFits.add(fitmentMap.get(fitID));
        });
        //k = name+value, v = att object.
        fitAttMap.forEach((k,v)->{
            int attID = v.getFitmentAttID();
            v.setProductionFitments(attOnFitMap.get(attID));
        });
        Instant finish = Instant.now();
        logger.info("Initiated all fits at atts in " + Duration.between(start,finish));
    }

    //k = att name + att val, v = att
    private Map<String, FitmentAttribute> initFitAttMap(Session session) {
        Instant start = Instant.now();
        List<FitmentAttribute> allAtts = FitmentDAO.getAllFitAttributes(session);
        Map<String, FitmentAttribute> result = new HashMap<>();
        allAtts.forEach(att->{
            String key = att.getFitmentAttName()+att.getFitmentAttValue();
            result.put(key,att);
        });
        Instant finish = Instant.now();
        logger.info("Initiated Fit Attribute map in " + Duration.between(start,finish));

        return result;
    }

    public void saveFitments(Set<ProductionItem> newItems) {
        List<ProductionFitment> allFits = getFits(newItems);
        List<FitmentAttribute> fitAttsToUpdate = new ArrayList<>();
        List<FitmentAttribute> fitAttsToSave = new ArrayList<>();
        List<ProductionFitment> finalFits = new ArrayList<>();

        allFits.forEach(curFitment->{
            Set<FitmentAttribute> curAtts = curFitment.getFitmentAttributes();
            Set<FitmentAttribute> finalAtts = new HashSet<>();

            curAtts.forEach(curAtt->{
                String key = curAtt.getFitmentAttName()+curAtt.getFitmentAttValue();
                FitmentAttribute existingAtt = fitAttMap.get(key);
                if (existingAtt==null){
                    //we have new attribute
                    fitAttsToSave.add(curAtt);
                    fitAttMap.put(key, curAtt);
                    finalAtts.add(curAtt);
                }
                else {
                    int id = existingAtt.getFitmentAttID();
                    if (id!=0){
                        fitAttsToUpdate.add(existingAtt);
                    }
                    existingAtt.getProductionFitments().add(curFitment);
                    finalAtts.add(existingAtt);
                }
            });
            curFitment.setFitmentAttributes(finalAtts);
            finalFits.add(curFitment);
        });

        updateEntities(fitAttsToUpdate);
        saveEntities(fitAttsToSave, finalFits);
    }

    private void saveEntities(List<FitmentAttribute> fitAttsToSave, List<ProductionFitment> finalFits) {
        Instant start = Instant.now();
        logger.info("Saving fit atts...");
        FitmentDAO.saveFitAttsList(session, fitAttsToSave);
        Instant finish = Instant.now();
        logger.info("Saved fit attributes in " + Duration.between(start, finish));
        start = Instant.now();
        FitmentDAO.saveFitList(session, finalFits);
        finish = Instant.now();
        logger.info("Saved fits in " + Duration.between(start, finish));
    }

    private void updateEntities(List<FitmentAttribute> fitAttsToUpdate) {
        Instant start = Instant.now();
        FitmentDAO.updateAttributes(session, fitAttsToUpdate);
        Instant finish = Instant.now();
        logger.info("Updated fit attributes in " + Duration.between(start, finish));
    }

    private List<ProductionFitment> getFits(Set<ProductionItem> newItems) {
        List<ProductionFitment> result = new ArrayList<>();
        newItems.forEach(item -> {
            result.addAll(item.getProductionFitments());
        });

        return result;
    }
}
