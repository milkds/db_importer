package importer.suppliers.summit;

import importer.entities.FitmentAttribute;
import importer.suppliers.summit.entities.SumFitAttribute;
import importer.suppliers.summit.entities.SumItem;
import org.hibernate.Session;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class SumService {
    public Set<SumItem> getAllItems(Session sumSession) {
        Set<SumItem> result = new HashSet<>();
        result.addAll(new SumDAO().getAllItems(sumSession));

        return result;
    }

    public Map<Integer, List<SumFitAttribute>> getAllFitAttributes(Session session){
        Instant start = Instant.now();
        List<SumFitAttribute> sumAtts = new SumDAO().getAllFitAttributes(session);
        Instant mid = Instant.now();
        System.out.println("got items from DB in " + Duration.between(start, mid).toMillis());
        Map<Integer, List<SumFitAttribute>> result = groupAtts(sumAtts);
        Instant finish = Instant.now();
        System.out.println("grouped items in " + Duration.between(mid, finish).toMillis());

        return result;
    }

    private Map<Integer, List<SumFitAttribute>> groupAtts(List<SumFitAttribute> sumAtts) {
        Map<Integer, List<SumFitAttribute>> result = new HashMap<>();
        sumAtts.forEach(sumFitAttribute -> {
            int id = sumFitAttribute.getFitment().getId();
            List<SumFitAttribute> curAtts = result.get(id);
            if (curAtts==null){
                curAtts = new ArrayList<>();
                curAtts.add(sumFitAttribute);
                result.put(id, curAtts);
            }
            else {
                curAtts.add(sumFitAttribute);
            }
        });

        return result;
    }
}
