package importer.dao;

import importer.entities.CarAttribute;
import importer.entities.ProductionCar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

public class CarDAO {
    private static final Logger logger = LogManager.getLogger(CarDAO.class.getName());


    public static List<ProductionCar> getSimilarCars(ProductionCar car, Session session) {
        List<ProductionCar> result = new ArrayList<>();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ProductionCar> crQ = builder.createQuery(ProductionCar.class);
        Root<ProductionCar> root = crQ.from(ProductionCar.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("yearStart"), car.getYearStart()));
        predicates.add(builder.equal(root.get("yearFinish"), car.getYearFinish()));
        predicates.add(builder.equal(root.get("make"), car.getMake()));
        predicates.add(builder.equal(root.get("model"), car.getModel()));
        predicates.add(builder.equal(root.get("subModel"), car.getSubModel()));
        predicates.add(builder.equal(root.get("drive"), car.getDrive()));
        Predicate[] preds = predicates.toArray(new Predicate[0]);
        crQ.where(builder.and(preds));
        Query q = session.createQuery(crQ);
        result = q.getResultList();

        return result;
    }
    public static void saveCar(ProductionCar car, Session session) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            session.save(car);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
    public static CarAttribute checkAttribute(CarAttribute attribute, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<CarAttribute> crQ = builder.createQuery(CarAttribute.class);
        Root<CarAttribute> root = crQ.from(CarAttribute.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("carAttName"), attribute.getCarAttName()));
        predicates.add(builder.equal(root.get("carAttValue"), attribute.getCarAttValue()));
        Predicate[] preds = predicates.toArray(new Predicate[0]);
        crQ.where(builder.and(preds));
        Query q = session.createQuery(crQ);
        CarAttribute testAtt = null;
        try {
            testAtt = (CarAttribute) q.getSingleResult();
            logger.debug("car attribute exists " + testAtt);
        } catch (NoResultException e) {
            logger.debug("car attribute doesn't exist " + attribute);
            return null;
        }

        return testAtt;
    }


    private static void prepareCarAttributes(ProductionCar car, Session session) {
        Set<CarAttribute> attributes = car.getAttributes();
        Set<CarAttribute> checkedAttributes = new HashSet<>();
        attributes.forEach(attribute->{
            CarAttribute checkedAtt = checkAttribute(attribute, session);
            checkedAttributes.add(Objects.requireNonNullElse(checkedAtt, attribute));
        });
        car.setAttributes(checkedAttributes);
    }



    private static ProductionCar checkCarExistence(ProductionCar car, Session session) {
        List<ProductionCar> similarCars = getSimilarCars(car, session);
        for (ProductionCar dbCar : similarCars) {
            if (dbCar.equals(car)) {
              return dbCar;
            }
        }
        session.save(car);

        return car;
    }
    static void prepareCar(ProductionCar car, Session session) {
        car = checkCarExistence(car, session);
        if (car.getCarID()==0){
            prepareCarAttributes(car, session);
        }
        session.save(car);
    }


}
