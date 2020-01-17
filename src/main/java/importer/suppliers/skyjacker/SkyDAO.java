package importer.suppliers.skyjacker;

import importer.entities.ProductionCar;
import importer.suppliers.bilstein.bilstein_entities.BilShock;
import importer.suppliers.skyjacker.sky_entities.CarMergeEntity;
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



    public static List<CarMergeEntity> getMergeEntities(ProductionCar car, Session skySession) {
        List<CarMergeEntity> result = new ArrayList<>();
        CriteriaBuilder builder = skySession.getCriteriaBuilder();
        CriteriaQuery<CarMergeEntity> crQ = builder.createQuery(CarMergeEntity.class);
        Root<CarMergeEntity> root = crQ.from(CarMergeEntity.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.lessThanOrEqualTo(root.get("skyYear"), car.getYearStart()));
        predicates.add(builder.greaterThanOrEqualTo(root.get("skyYear"), car.getYearFinish()));
        predicates.add(builder.equal(root.get("skyMake"), car.getMake()));
        predicates.add(builder.equal(root.get("skyModel"), car.getModel()));
        Predicate[] preds = predicates.toArray(new Predicate[0]);
        crQ.where(builder.and(preds));
        Query q = skySession.createQuery(crQ);
        result = q.getResultList();

        return result;
    }
}
