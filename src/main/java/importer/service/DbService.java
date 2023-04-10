package importer.service;

import importer.HibernateUtil;
import importer.dao.CarDAO;
import importer.dao.FitmentDAO;
import importer.dao.ItemDAO;
import importer.entities.CarAttribute;
import importer.entities.FitmentAttribute;
import importer.entities.ItemAttribute;
import importer.entities.links.CarAttributeLink;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class DbService {
    private static final Logger logger = LogManager.getLogger(DbService.class.getName());

    public void updateItemAttributes(){
        Instant start = Instant.now();
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<ItemAttribute> attributes = ItemDAO.getAllItemAttributesValueContaining(session, "https://productdesk.cart.bilsteinus.com");
        logger.info("got attributes in quantity of " + attributes.size());
        attributes = updateItemAttributeValues(attributes);
        ItemDAO.updateItemAttributes(session, attributes);
        session.close();
        HibernateUtil.shutdown();
        Instant finish = Instant.now();
        logger.info("updated attributes in " + Duration.between(start, finish));
    }

    private List<ItemAttribute> updateItemAttributeValues(List<ItemAttribute> attributes) {
        List<ItemAttribute> result = new ArrayList<>();
        attributes.forEach(attribute -> {
            String value = attribute.getItemAttValue().replaceAll("Material","").trim();
            attribute.setItemAttValue(value);
            result.add(attribute);
        });

        return result;
    }

    public void removeDuplicates(){
        Instant start = Instant.now();
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<CarAttribute> allAtts = CarDAO.getAllCarAttributes(session);
        Instant finish = Instant.now();
        logger.info("got " + allAtts.size()+ " car attributes in " + Duration.between(start, finish));
     //   List<ProductionItem>  allItems = ItemDAO.getAllItemsByMake("Bilstein", session);
         start = Instant.now();
        List<CarAttributeLink> allLinks = CarDAO.getAllCarAttributeLinks(session);
        finish = Instant.now();
        logger.info("got " +allLinks.size()+ " car attribute links " + Duration.between(start, finish));
        Map<Integer, List<CarAttributeLink>> attLinkMap = groupItemAttributeLinks(allAtts, allLinks);// k = ItemAttribute ID. v = ItemAttLinks for this Attribute;
        Map<String, CarAttribute> checkedAttMap = new HashMap<>();//k = name+value combo and v is attribute
        List<CarAttributeLink> linksToUpdate = new ArrayList<>();
        List<CarAttribute> attributesToDelete = new ArrayList<>();
        allAtts.forEach(attribute -> {
            String nameValue = attribute.getCarAttName()+attribute.getCarAttValue();
            CarAttribute curAtt = checkedAttMap.get(nameValue);
            if (curAtt==null){
                checkedAttMap.put(nameValue, attribute);
            }
            else {
//                logger.info("Got duplicate item attribute: " + curAtt);
                //we change item links from current attribute id to other att id, which is already in map
                attributesToDelete.add(attribute);
                linksToUpdate.addAll( updateLinks(curAtt, session, attLinkMap.get(attribute.getCarAttID())));
            }
        });
        deleteAttributesFromDB(attributesToDelete, session);
        CarDAO.updateCarLinks(session, linksToUpdate);

        session.close();
        finish = Instant.now();
        logger.info("finished in " + Duration.between(start, finish));
        HibernateUtil.shutdown();
    }
   /* public void backUpRemoveDupeMethod(){
        Instant start = Instant.now();
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<ItemAttribute> allAtts = ItemDAO.getAllItemAttributes(session);
        Instant finish = Instant.now();
        logger.info("got " + allAtts.size()+ " item attributes in " + Duration.between(start, finish));
     //   List<ProductionItem>  allItems = ItemDAO.getAllItemsByMake("Bilstein", session);
         start = Instant.now();
        List<ItemAttributeLink> allLinks = ItemDAO.getAllItemAttributeLinks(session);
        finish = Instant.now();
        logger.info("got " +allLinks.size()+ " item attribute links " + Duration.between(start, finish));
        Map<Integer, List<ItemAttributeLink>> attLinkMap = groupItemAttributeLinks(allAtts, allLinks);// k = ItemAttribute ID. v = ItemAttLinks for this Attribute;
        Map<String, ItemAttribute> checkedAttMap = new HashMap<>();//k = name+value combo and v is attribute
        List<ItemAttributeLink> linksToUpdate = new ArrayList<>();
        List<ItemAttribute> attributesToDelete = new ArrayList<>();
        allAtts.forEach(attribute -> {
            String nameValue = attribute.getItemAttName()+attribute.getItemAttValue();
            ItemAttribute curAtt = checkedAttMap.get(nameValue);
            if (curAtt==null){
                checkedAttMap.put(nameValue, attribute);
            }
            else {
//                logger.info("Got duplicate item attribute: " + curAtt);
                //we change item links from current attribute id to other att id, which is already in map
                attributesToDelete.add(attribute);
                linksToUpdate.addAll( updateLinks(curAtt, session, attLinkMap.get(attribute.getItemAttID())));
            }
        });
        deleteAttributesFromDB(attributesToDelete, session);
        ItemDAO.updateItemAttLinks(session, linksToUpdate);

        session.close();
        finish = Instant.now();
        logger.info("finished in " + Duration.between(start, finish));
        HibernateUtil.shutdown();
    }
*/
    private void deleteAttributesFromDB(List<CarAttribute> attributes, Session session) {
        attributes.forEach(attribute -> {
            attribute.setProductionCars(null);
        });
        CarDAO.deleteCarAttributes(session, attributes);
    }

    private  List<CarAttributeLink> updateLinks(CarAttribute attToKeep, Session session, List<CarAttributeLink> attributeLinks) {
        List<CarAttributeLink> linksToUpdate = new ArrayList<>();
        int correctAttID = attToKeep.getCarAttID();
        attributeLinks.forEach(attributeLink->{
            attributeLink.setAttID(correctAttID);
            linksToUpdate.add(attributeLink);
        });

        return linksToUpdate;
    }

    // k = Attribute ID. v = AttLinks for this Attribute;
    private Map<Integer, List<CarAttributeLink>> groupItemAttributeLinks(List<CarAttribute> allAtts, List<CarAttributeLink> allLinks) {
        Map<Integer, List<CarAttributeLink>> result = new HashMap<>(); //k = attID, v = List of links for it;
        allLinks.forEach(link->{
            int attID = link.getAttID();
            List<CarAttributeLink> curList = result.computeIfAbsent(attID, k -> new ArrayList<>());
            curList.add(link);
        });

        return result;
    }
}
