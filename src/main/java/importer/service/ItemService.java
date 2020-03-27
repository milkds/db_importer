package importer.service;

import importer.HibernateUtil;
import importer.dao.ItemDAO;
import importer.entities.ItemAttribute;
import importer.entities.ItemPic;
import importer.entities.ProductionItem;
import importer.entities.ShockParameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

public class ItemService {
    private static final Logger logger = LogManager.getLogger(ItemService.class.getName());

    public static Set<ProductionItem> getAllItemsByMake(String itemMake, Session prodSession) {
        List<ProductionItem> itemList = ItemDAO.getAllItemsByMake(itemMake, prodSession);

        return new HashSet<>(itemList);
    }

    public void updateItem(ProductionItem item, Session prodSession) {
        checkMounts(item);
        checkLengths(item);
        ItemDAO.updateItem(item, prodSession);
    }

    public static void updateItemAttributes(String oldVal, String newVal) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<ItemAttribute> itemAtts = ItemDAO.getItemAttributesByName(session, oldVal);
        itemAtts.forEach(itemAttribute -> {
            itemAttribute.setItemAttName(newVal);
            ItemDAO.updateItemAttribute(session, itemAttribute);
        });
        session.close();
    }

    public static Set<String> getAllPicLinks() {
        Set<String> result = new HashSet<>();
        result = ItemDAO.getAllPicLinks();
        result.remove("https://productdesk.cart.bilsteinus.com/media/products/bilstein/image_generic_02_1.jpg");
        result.remove("NO IMG LINK");

        return result;
    }

    public static List<ProductionItem> getAllItems() {
        List<ProductionItem> result = new ArrayList<>();
        result = ItemDAO.getAllItems();

        return result;
    }

    public static void updateItemPics(Set<ItemPic> pics) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        pics.forEach(pic-> ItemDAO.updateItemPic(pic, session));
        session.close();
    }

    public void saveItems(Set<ProductionItem> newItems) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        int counter = 0;
        int total = newItems.size();
        for (ProductionItem item : newItems) {
             prepareItemAttributes(item, session);
             ItemDAO.saveItem(item, session);
             item.getProductionFitments().forEach(fitment->{
                 fitment.setItem(item);
                 new FitmentService().saveFitment(fitment, session);
             });
             counter++;
            logger.info("Saved item " + counter + " of total " + total);
        }
        session.close();




         /* Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();

            transaction.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }*/
    }

    private void prepareItemAttributes(ProductionItem item, Session session) {
        checkMounts(item);
        checkLengths(item);
        Set<ItemAttribute> attributes = item.getItemAttributes();
        Set<ItemAttribute> checkedAttributes = new HashSet<>();
        attributes.forEach(attribute->{
            new ProdItemAttChecker().checkAttributeContent(attribute);
            ItemAttribute checkedAtt = ItemDAO.checkAttribute (attribute, session); //checks existence
            checkedAttributes.add(Objects.requireNonNullElse(checkedAtt, attribute));
        });
        item.setItemAttributes(checkedAttributes);
    }

    private void checkLengths(ProductionItem item) {
        ShockParameters params = item.getParams();
        if (params==null){
            return;
        }
        double colLength = params.getColLength();
        if (colLength!=0){
            item.getItemAttributes().add(new ItemAttribute("Fully Collapsed Length", colLength+""));
        }
        double extLength = params.getExtLength();
        if (extLength!=0){
            item.getItemAttributes().add(new ItemAttribute("Fully Extended Length", extLength+""));
        }
    }

    private void checkMounts(ProductionItem item) {
        ShockParameters params = item.getParams();
        if (params==null){
            return;
        }
        String upperMount = params.getUpperMount();
        if (upperMount!=null&&upperMount.length()>0){
            if (upperMount.equals("Eye")){
                upperMount = "Eyelet";
            }
            item.getItemAttributes().add(new ItemAttribute("Upper Mount Full", upperMount));
        }
        String lowerMount = params.getLowerMount();
        if (lowerMount!=null&&lowerMount.length()>0){
            if (lowerMount.equals("Eye")){
                lowerMount = "Eyelet";
            }
            item.getItemAttributes().add(new ItemAttribute("Lower Mount Full", lowerMount));
        }
    }
}
