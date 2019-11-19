package importer.suppliers.bilstein;

import importer.suppliers.bilstein.bilstein_entities.BilFitment;
import importer.suppliers.bilstein.bilstein_entities.BilShock;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BilsteinDAO {
    public static Set<BilShock> getAllShocks(Session session) {
        List<BilShock> shocks;
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BilShock> crQ = builder.createQuery(BilShock.class);
        Root<BilShock> root = crQ.from(BilShock.class);
        Query q = session.createQuery(crQ);
        shocks = q.getResultList();

        return new HashSet<>(shocks);
    }

    public static Set<BilFitment> getFitmentsForShock(BilShock shock, Session session) {
        List<BilFitment> fitments;
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BilFitment> crQ = builder.createQuery(BilFitment.class);
        Root<BilFitment> root = crQ.from(BilFitment.class);
        crQ.where(builder.equal(root.get("bilShock"), shock));
        Query q = session.createQuery(crQ);
        fitments = q.getResultList();

        return new HashSet<>(fitments);
    }
}
