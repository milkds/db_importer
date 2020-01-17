package importer.suppliers.fox.dao;

import importer.suppliers.fox.FoxHibernateUtil;
import importer.suppliers.fox.entities.FoxCar;
import importer.suppliers.fox.entities.FoxItem;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.Set;

public class FoxCarDAO {
    public static void saveCar(FoxCar foxCar, Session session) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            session.save(foxCar);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static int getEarliestYear(Session session) {
        int result = 0;
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Integer> crQ = builder.createQuery(Integer.class);
        Root<FoxCar> root = crQ.from(FoxCar.class);
        crQ.select(builder.min(root.get("year"))).distinct(true);
        Query q = session.createQuery(crQ);
        result = (int)q.getSingleResult();

        return result;
    }

    public static String getLastMakeForYear(int year, Session session) {
        String result = null;
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<String> crQ = builder.createQuery(String.class);
        Root<FoxCar> root = crQ.from(FoxCar.class);
        crQ.select(root.get("make"));
        crQ.where(builder.equal(root.get("year"), year));
        crQ.orderBy(builder.desc(root.get("carID")));
        Query q = session.createQuery(crQ).setMaxResults(1);

        return (String) q.getSingleResult();
    }

    public static Set<FoxCar> getAllCars() {
        Session session = FoxHibernateUtil.getFoxSessionFactory().openSession();
        Set<FoxCar> result = new HashSet<>();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<FoxCar> crQ = builder.createQuery(FoxCar.class);
        Root<FoxCar> root = crQ.from(FoxCar.class);
        Query q = session.createQuery(crQ);
        result = new HashSet<>(q.getResultList());
        session.close();
        return result;
    }
}
