package importer.entities;

import importer.entities.links.FitmentAttributeLink;
import org.hibernate.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectFromSessionDetacher {
    private Session session;

    public ObjectFromSessionDetacher(Session session) {
        this.session = session;
    }

    public void detachFitLinks(List<FitmentAttributeLink> allLinks) {
        allLinks.forEach(session::evict);
    }

    public void detachFitAndFitAtts(List<FitmentAttribute> fitAttsToUpdate,
                                    List<FitmentAttribute> fitAttsToSave,
                                    Map<String, FitmentAttribute> fitAttMap,
                                    Map<Integer, ProductionFitment> fitmentMap,
                                    Map<Integer, List<ProductionFitment>> attOnFitMap) {

        //k = att id, v = att
        Map<Integer, FitmentAttribute> attsToKeepMap = getFitAttToKeepMap(fitAttsToUpdate, fitAttsToSave);
        //k = att id, v = att
        Map<Integer, FitmentAttribute> allFitAttsMap = getAllFitAttsMap(fitAttMap);
        //k = attID, v = list of fits for this att
        attOnFitMap.forEach((k,v)->{
            FitmentAttribute att = attsToKeepMap.get(k);
            if (att==null){
              v.forEach(session::evict);
              session.evict(allFitAttsMap.get(k));
            }
        });
    }

    private Map<Integer, FitmentAttribute> getAllFitAttsMap(Map<String, FitmentAttribute> fitAttMap) {
        Map<Integer, FitmentAttribute> result = new HashMap<>();
        fitAttMap.forEach((k,v)-> result.put(v.getFitmentAttID(), v));

        return result;
    }

    private Map<Integer, FitmentAttribute> getFitAttToKeepMap(List<FitmentAttribute> update, List<FitmentAttribute> save) {
        Map<Integer, FitmentAttribute> result = new HashMap<>();
        update.forEach(att-> {
            int id = att.getFitmentAttID();
            if (id!=0){
                result.put(id, att); }

        });


        return result;
    }
}
