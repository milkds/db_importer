package importer.suppliers.skyjacker;

import importer.entities.ProductionCar;
import importer.entities.CarMergeEntity;
import importer.suppliers.skyjacker.sky_entities.SkyFitment;
import importer.suppliers.skyjacker.sky_entities.SkyShock;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
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

    public static Set<String> getAllFitLines(Session skySession) {
        CriteriaBuilder builder = skySession.getCriteriaBuilder();
        List<String> fitLines = new ArrayList<>();
        CriteriaQuery<String> crQ = builder.createQuery(String.class);
        Root<SkyFitment> root = crQ.from(SkyFitment.class);
        crQ.select(root.get("fitString")).distinct(true);
        Query q = skySession.createQuery(crQ);
        fitLines = q.getResultList();
        Set<String> makeSet = new HashSet<>(fitLines);

        return makeSet;
    }
}
