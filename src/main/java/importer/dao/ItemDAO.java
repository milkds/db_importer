package importer.dao;

import importer.HibernateUtil;
import importer.entities.ItemAttribute;
import importer.entities.ItemPic;
import importer.entities.ProductionFitment;
import importer.entities.ProductionItem;
import importer.suppliers.keystone.entities.KeyItem;
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

public class ItemDAO {
    private static final Logger logger = LogManager.getLogger(ItemDAO.class.getName());

    public static ItemAttribute checkAttribute(ItemAttribute attribute, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ItemAttribute> crQ = builder.createQuery(ItemAttribute.class);
        Root<ItemAttribute> root = crQ.from(ItemAttribute.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("itemAttName"), attribute.getItemAttName()));
        predicates.add(builder.equal(root.get("itemAttValue"), attribute.getItemAttValue()));
        Predicate[] preds = predicates.toArray(new Predicate[0]);
        crQ.where(builder.and(preds));
        Query q = session.createQuery(crQ);
        ItemAttribute testAtt = null;
        List<ItemAttribute> attributes = q.getResultList();
        if (attributes.size()>0){
            testAtt = attributes.get(0);
            logger.debug("item attribute exists " + testAtt);
        }

        return testAtt;
    }

    public static void saveItem(ProductionItem item, Session session) {
          Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            session.save(item);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static List<ProductionItem> getAllItemsByMake(String itemMake, Session session) {
        List<ProductionItem> allItemList = new ArrayList<>();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ProductionItem> crQ = builder.createQuery(ProductionItem.class);
        Root<ProductionItem> root = crQ.from(ProductionItem.class);
        crQ.where(builder.equal(root.get("itemManufacturer"), itemMake));
        Query q = session.createQuery(crQ);
        allItemList = q.getResultList();

        return allItemList;
    }

    public static void updateItem(ProductionItem item, Session prodSession) {
        Transaction transaction = null;
        try {
            transaction = prodSession.getTransaction();
            transaction.begin();
            prodSession.update(item);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static List<ItemAttribute> getItemAttributesByName(Session session, String name) {
        List<ItemAttribute> result = new ArrayList<>();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ItemAttribute> crQ = builder.createQuery(ItemAttribute.class);
        Root<ItemAttribute> root = crQ.from(ItemAttribute.class);
        crQ.where(builder.equal(root.get("itemAttName"), name));
        Query q = session.createQuery(crQ);
        result = q.getResultList();
        logger.info("Got " + result.size() + " attributes");

        return result;
    }

    public static void updateItemAttribute(Session session, ItemAttribute att) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            session.update(att);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static Set<String> getAllPicLinks() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<String> picUrls = new ArrayList<>();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<String> crQ = builder.createQuery(String.class);
        Root<ItemPic> root = crQ.from(ItemPic.class);
        crQ.select(root.get("picUrl")).distinct(true);
        Query q = session.createQuery(crQ);
        picUrls = q.getResultList();
        session.close();

        return new HashSet<>(picUrls);
    }

    public static List<ProductionItem> getAllItems() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<ProductionItem> allItemList = new ArrayList<>();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ProductionItem> crQ = builder.createQuery(ProductionItem.class);
        Root<ProductionItem> root = crQ.from(ProductionItem.class);
        Query q = session.createQuery(crQ);
        allItemList = q.getResultList();

        return allItemList;

    }

    public static void updateItemPic(ItemPic pic, Session session) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            session.update(pic);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    /*public static void saveItems(Set<ProductionItem> newItems) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        int counter = 0;
        int total = newItems.size();
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            for (ProductionItem item : newItems) {
                saveItem(item, session);
                counter++;
                logger.info("Saved item " + counter + " of total " + total);
            }
            transaction.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static void saveItem(ProductionItem item, Session session){
        prepareItemAttributes(item, session);
        session.save(item);
        prepareFitments(item, session);
    }

      public static void prepareItemAttributes(ProductionItem item, Session session) {
        Set<ItemAttribute> attributes = item.getItemAttributes();
        Set<ItemAttribute> checkedAttributes = new HashSet<>();
        attributes.forEach(attribute->{
            ItemAttribute checkedAtt = checkAttribute(attribute, session);
            checkedAttributes.add(Objects.requireNonNullElse(checkedAtt, attribute));
        });
        item.setItemAttributes(checkedAttributes);
    }



    private static void prepareFitments(ProductionItem item, Session session) {
        Set<ProductionFitment> fitments = item.getProductionFitments();
        for (ProductionFitment fitment : fitments) {
            FitmentDAO.prepareFitment(item, fitment, session);
        }
    }*/
}
