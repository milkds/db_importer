package importer.export;

import importer.Controller;
import importer.HibernateUtil;
import importer.dao.ItemDAO;
import importer.entities.ItemAttribute;
import importer.entities.ProductionItem;
import importer.entities.links.ItemAttributeLink;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

class ExportService {
    private static final Logger logger = LogManager.getLogger(ExportService.class.getName());

    private Session session;

    ExportService() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    List<ExportEntity> getExportEntities(String brand) {
        List<ProductionItem> items = getItems(brand);
        ExportEntityBuilder builder = initExportEntityBuilder(items);

     //   List<ItemAttribute> itemAttributes = getItemAttributes();

        shutDown();
        return null;
    }

    private ExportEntityBuilder initExportEntityBuilder(List<ProductionItem> items) {
        ExportEntityBuilder result = new ExportEntityBuilder();
        result.setItems(items);
        List<ItemAttribute> itemAttributes = getItemAttributes();
        List<ItemAttributeLink> itemAttributeLinks = getItemAttributeLinks();
        result.initItemAttMap(itemAttributes, itemAttributeLinks);


        return result;
    }

    private List<ItemAttributeLink> getItemAttributeLinks() {
        Instant start = Instant.now();
        List<ItemAttributeLink> result = ItemDAO.getAllItemAttributeLinks(session);
        Instant end = Instant.now();
        logger.info (Duration.between(start, end));

        return result;
    }

    private List<ItemAttribute> getItemAttributes() {
        Instant start = Instant.now();
        List<ItemAttribute> result = ItemDAO.getAllItemAttributes(session);
        Instant end = Instant.now();
        logger.info (Duration.between(start, end));

        return result;
    }

    private List<ProductionItem> getItems(String brand) {
        Instant start = Instant.now();
        List<ProductionItem> result = ItemDAO.getAllItemsByMake(brand,session);
        Instant end = Instant.now();
        logger.info(Duration.between(start, end));

        return result;
    }


    private void shutDown(){
        session.close();
        HibernateUtil.shutdown();
    }


}
