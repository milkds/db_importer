package importer.dao;

import importer.entities.FitmentAttribute;
import importer.entities.ProductionFitment;
import importer.entities.ProductionItem;
import importer.entities.links.FitmentAttributeLink;
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

public class FitmentDAO {
    private static final Logger logger = LogManager.getLogger(FitmentDAO.class.getName());
    public static FitmentAttribute checkFitAttribute(FitmentAttribute attribute, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<FitmentAttribute> crQ = builder.createQuery(FitmentAttribute.class);
        Root<FitmentAttribute> root = crQ.from(FitmentAttribute.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("fitmentAttName"), attribute.getFitmentAttName()));
        predicates.add(builder.equal(root.get("fitmentAttValue"), attribute.getFitmentAttValue()));
        Predicate[] preds = predicates.toArray(new Predicate[0]);
        crQ.where(builder.and(preds));
        Query q = session.createQuery(crQ);
        FitmentAttribute testAtt = null;
        logger.debug("checking attribute " + attribute);
        try {
            testAtt = (FitmentAttribute) q.getSingleResult();
            logger.debug("fitment attribute exists " + testAtt);
        } catch (NoResultException e) {
            logger.debug("fitment attribute doesn't exist " + attribute);
            return null;
        }

        return testAtt;
    }
    public static void saveFitment(ProductionFitment fitment, Session session) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            session.save(fitment);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static void updateFit(ProductionFitment prodFit, Session session) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            session.update(prodFit);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static List<ProductionFitment> getAllFits(Session session) {
        List<ProductionFitment> allFitList = new ArrayList<>();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ProductionFitment> crQ = builder.createQuery(ProductionFitment.class);
        Root<ProductionFitment> root = crQ.from(ProductionFitment.class);
        Query q = session.createQuery(crQ);
        allFitList = q.getResultList();

        return allFitList;
    }

    public static List<ProductionFitment> getAllFitsForItems(Session session, List<ProductionItem> itemIDS) {
        List<ProductionFitment> result = new ArrayList<>();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ProductionFitment> crQ = builder.createQuery(ProductionFitment.class);
        Root<ProductionFitment> root = crQ.from(ProductionFitment.class);
        crQ.where(root.get("item").in(itemIDS));
        Query q = session.createQuery(crQ);
        result = q.getResultList();

        return result;

    }

    public static List<FitmentAttribute> getAllFitAttributes(Session session) {
        List<FitmentAttribute> allFitAttList = new ArrayList<>();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<FitmentAttribute> crQ = builder.createQuery(FitmentAttribute.class);
        Root<FitmentAttribute> root = crQ.from(FitmentAttribute.class);
        Query q = session.createQuery(crQ);
        allFitAttList = q.getResultList();

        return allFitAttList;
    }

    public static List<FitmentAttributeLink> getAllFitAttLinks(Session session) {
        List<FitmentAttributeLink> result = new ArrayList<>();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<FitmentAttributeLink> crQ = builder.createQuery(FitmentAttributeLink.class);
        Root<FitmentAttributeLink> root = crQ.from(FitmentAttributeLink.class);
        Query q = session.createQuery(crQ);
        result = q.getResultList();

        return result;
    }

    public static List<FitmentAttributeLink> getFitAttLinksByFitIDs(Session session, Set<Integer> fitIDs) {
        List<FitmentAttributeLink> result = new ArrayList<>();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<FitmentAttributeLink> crQ = builder.createQuery(FitmentAttributeLink.class);
        Root<FitmentAttributeLink> root = crQ.from(FitmentAttributeLink.class);
      //  crQ.where(root.get("fitID").in(fitIDs));
        Query q = session.createQuery(crQ);
        result = q.getResultList();


        return result;
    }

    public static void deleteFitAttributes(Session session, List<FitmentAttribute> attributes) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            attributes.forEach(session::delete);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static void updateFitLinks(Session session, List<FitmentAttributeLink> linksToUpdate) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            linksToUpdate.forEach(session::update);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static void updateFitAtts(Session session, Set<FitmentAttribute> updAtts) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            updAtts.forEach(session::update);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static void saveFitAtts(Session session, Set<FitmentAttribute> newAtts) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            newAtts.forEach(session::save);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static void saveFits(Session session, Set<ProductionFitment> newFits) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            newFits.forEach(session::save);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static void updateAttributes(Session session, List<FitmentAttribute> fitAttsToUpdate) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            fitAttsToUpdate.forEach(attribute -> {
                //        logger.info(attribute);
                session.update(attribute);
            });
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static void saveFitList(Session session, List<ProductionFitment> finalFits) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            finalFits.forEach(session::save);
            logger.info("commiting....");
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static void saveFitAttsList(Session session, List<FitmentAttribute> fitAttsToSave) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            for (int i = 0; i < fitAttsToSave.size(); i++) {
                FitmentAttribute attribute = fitAttsToSave.get(i);
                logger.info(attribute);
                session.save(attribute);
                if (i % 20 == 0) { // 20, same as the JDBC batch size
                    // flush a batch of inserts and release memory:
                    session.flush();
                    session.clear();
                }
            }
            logger.info("commiting transaction at fit att save....");
            transaction.commit();
            logger.info("transaction commited");
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }


  /*  public static void prepareFitment(ProductionItem item, ProductionFitment fitment, Session session) {
        logger.debug("preparing attributes for " + fitment);
        prepareFitmentAttributes(fitment, session);
        logger.debug("attributes prepared for " + fitment);
        CarDAO.prepareCar(fitment.getCar(), session);
        fitment.setItem(item);
        session.save(fitment);
    }
    public static void prepareFitmentAttributes(ProductionFitment fitment, Session session) {
        Set<FitmentAttribute> attributes = fitment.getFitmentAttributes();
        Set<FitmentAttribute> checkedAttributes = new HashSet<>();
        attributes.forEach(logger::debug);
        logger.debug("Size " + attributes.size());
        attributes.forEach(attribute->{
            FitmentAttribute checkedAtt = checkFitAttribute(attribute, session);
            checkedAttributes.add(Objects.requireNonNullElse(checkedAtt, attribute));
        });
        fitment.setFitmentAttributes(checkedAttributes);
    }*/


}
