package importer.suppliers.summit;

import importer.HibernateUtil;
import importer.entities.ProductionItem;
import importer.suppliers.summit.entities.SumItem;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SumDAO {

    public List<SumItem> getAllItems(Session sumSession) {
        List<SumItem> result = new ArrayList<>();
        CriteriaBuilder builder = sumSession.getCriteriaBuilder();
        CriteriaQuery<SumItem> crQ = builder.createQuery(SumItem.class);
        Root<SumItem> root = crQ.from(SumItem.class);
        Query q = sumSession.createQuery(crQ);
        result = q.getResultList();

        return result;
    }
}
