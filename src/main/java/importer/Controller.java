package importer;

import importer.dao.ItemDAO;
import importer.entities.ProductionItem;
import importer.service.ItemService;
import importer.suppliers.bilstein.BilConverter;
import importer.suppliers.bilstein.BilHibernateUtil;
import importer.suppliers.bilstein.BilsteinDAO;
import importer.suppliers.bilstein.bilstein_entities.BilShock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.Set;

public class Controller {
    private static final Logger logger = LogManager.getLogger(Controller.class.getName());

    public static void main(String[] args) {
        importBilstein();
      //  TestClass.TestItemSave();
      //  TestClass.testSplit();
    }

    private static void importBilstein() {
        Session session = BilHibernateUtil.getSession();
        Set<BilShock> shocks = BilsteinDAO.getAllShocks(session);
        Set<ProductionItem> newItems = new HashSet<>();
        int total = shocks.size();
        int counter = 0;
        for (BilShock shock : shocks) {
            ProductionItem item = new BilConverter().buildItem(shock, session);
            newItems.add(item);
            counter++;
            logger.info("Built item " + counter + " of total " + total);
        }
        session.close();
        new ItemService().saveItems(newItems);
        BilHibernateUtil.shutdown();
        HibernateUtil.shutdown();
    }


}
