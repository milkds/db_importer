package importer.suppliers.fox.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import importer.suppliers.fox.entities.FoxFit;

public class FoxFitDAO {

    public static void saveFit(FoxFit foxFit, Session session) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            session.save(foxFit);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}
