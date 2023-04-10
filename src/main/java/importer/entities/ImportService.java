package importer.entities;

import importer.HibernateUtil;
import importer.dao.FitmentDAO;
import importer.dao.ItemDAO;
import importer.service.ItemService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class ImportService {
    private static final Logger logger = LogManager.getLogger(ImportService.class.getName());
    private static Session session;



    public ImportService() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public void saveItems(Set<ProductionItem> newItems) {
        new ItemSaver(session).saveItems(newItems);
        new CarSaver(session).saveCars(newItems);
        new FitmentSaver(session).saveFitments(newItems);

       /* start = Instant.now();
        prepareFitmentAttributes(newItems);
        finish = Instant.now();
        logger.info("prepared and saved Fits in " + Duration.between(start,finish));*/
    }

    private void prepareFitmentAttributes(Set<ProductionItem> items) {
        Instant start = Instant.now();
        Set<FitmentAttribute> fitUpdateAtts = new HashSet<>();
        Set<FitmentAttribute> newFitAtts = new HashSet<>();
        Set<ProductionFitment> newFitments = new HashSet<>();
        Map<String, FitmentAttribute> fitAttMap = getFitAttMap(); //k = attName+AttValue/ v = fitAttribute
        items.forEach(curItem->{
            Set<ProductionFitment> curFits = curItem.getProductionFitments();
            curFits.forEach(curFit->{
                Set<FitmentAttribute> curFitAtts = curFit.getFitmentAttributes();
                Set<FitmentAttribute> finalFitAtts = new HashSet<>();
                curFitAtts.forEach(curFitAtt->{
                    String key = curFitAtt.getFitmentAttName()+curFitAtt.getFitmentAttValue();
                    FitmentAttribute existingAttribute = fitAttMap.get(key);
                    //new attribute
                    if (existingAttribute==null){
                        finalFitAtts.add(curFitAtt);
                        newFitAtts.add(curFitAtt);
                        fitAttMap.put(key,curFitAtt);
                    }
                    //existing attribute
                    else {
                        finalFitAtts.add(existingAttribute);
                        int id = existingAttribute.getFitmentAttID();
                        //checking if attribute isn't new
                        if (id != 0){
                            fitUpdateAtts.add(existingAttribute);
                        }
                        existingAttribute.getProductionFitments().add(curFit);
                    }
                });
                curFit.setFitmentAttributes(finalFitAtts);
                newFitments.add(curFit);
            });
        });
       Instant finish = Instant.now();
        logger.info("prepared fits in " + Duration.between(start,finish));
        start = Instant.now();
        saveFitments(fitUpdateAtts, newFitAtts, newFitments);
        finish = Instant.now();
        logger.info("saved fits in " + Duration.between(start,finish));
    }

    private void saveFitments(Set<FitmentAttribute> updAtts, Set<FitmentAttribute> newAtts, Set<ProductionFitment> newFits) {
            FitmentDAO.updateFitAtts(session,updAtts);
            FitmentDAO.saveFitAtts(session, newAtts);
            FitmentDAO.saveFits(session, newFits);
    }

    private Map<String, FitmentAttribute> getFitAttMap() {
        Instant start = Instant.now();
        Map<String, FitmentAttribute> result = new HashMap<>();
        List<FitmentAttribute> allAtts = FitmentDAO.getAllFitAttributes(session);
        allAtts.forEach(curAtt-> result.put(curAtt.getFitmentAttName()+curAtt.getFitmentAttValue(),curAtt));
        Instant finish = Instant.now();
        logger.info("prepared Fit Attributes in " + Duration.between(start,finish));

        return result;
    }

}
