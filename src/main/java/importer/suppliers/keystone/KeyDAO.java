package importer.suppliers.keystone;

import importer.suppliers.keystone.entities.KeyItem;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class KeyDAO {

    public static List<KeyItem> getAllItems(Session session) {
        List<KeyItem> allItemList = new ArrayList<>();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<KeyItem> crQ = builder.createQuery(KeyItem.class);
        Root<KeyItem> root = crQ.from(KeyItem.class);
        Query q = session.createQuery(crQ);
        allItemList = q.getResultList();

        return allItemList;
    }
}
