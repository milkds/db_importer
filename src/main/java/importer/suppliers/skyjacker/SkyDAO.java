package importer.suppliers.skyjacker;

import importer.suppliers.bilstein.bilstein_entities.BilShock;
import importer.suppliers.skyjacker.sky_entities.SkyShock;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SkyDAO {
    public static Set<SkyShock> getAllShocks(Session session) {
        List<SkyShock> shocks;
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<SkyShock> crQ = builder.createQuery(SkyShock.class);
        Root<SkyShock> root = crQ.from(SkyShock.class);
        Query q = session.createQuery(crQ);
        shocks = q.getResultList();


        return new HashSet<>(shocks);

    }
}
