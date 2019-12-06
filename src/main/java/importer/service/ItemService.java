package importer.service;

import importer.HibernateUtil;
import importer.dao.ItemDAO;
import importer.entities.ItemAttribute;
import importer.entities.ProductionItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ItemService {
    private static final Logger logger = LogManager.getLogger(ItemService.class.getName());

    public static Set<ProductionItem> getAllItemsByMake(String itemMake, Session prodSession) {
        List<ProductionItem> itemList = ItemDAO.getAllItemsByMake(itemMake, prodSession);

        return new HashSet<>(itemList);
    }

    public static void updateItem(ProductionItem item, Session prodSession) {
        ItemDAO.updateItem(item, prodSession);
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
        Set<ItemAttribute> attributes = item.getItemAttributes();
        Set<ItemAttribute> checkedAttributes = new HashSet<>();
        attributes.forEach(attribute->{
            ItemAttribute checkedAtt = ItemDAO.checkAttribute (attribute, session);
            checkedAttributes.add(Objects.requireNonNullElse(checkedAtt, attribute));
        });
        item.setItemAttributes(checkedAttributes);
    }
}
