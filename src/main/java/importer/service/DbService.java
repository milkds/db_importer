package importer.service;

import importer.HibernateUtil;
import importer.dao.ItemDAO;
import importer.entities.ItemAttribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
}
