package importer.suppliers.summit;

import importer.suppliers.summit.entities.SumItem;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.Set;

public class SumService {
    public Set<SumItem> getAllItems(Session sumSession) {
        Set<SumItem> result = new HashSet<>();
        result.addAll(new SumDAO().getAllItems(sumSession));

        return result;
    }
}
