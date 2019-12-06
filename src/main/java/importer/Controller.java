package importer;

import importer.dao.ItemDAO;
import importer.entities.ProductionItem;
import importer.service.ItemService;
import importer.suppliers.bilstein.BilConverter;
import importer.suppliers.bilstein.BilHibernateUtil;
import importer.suppliers.bilstein.BilsteinDAO;
import importer.suppliers.bilstein.bilstein_entities.BilShock;
import importer.suppliers.skyjacker.SkyConverter;
import importer.suppliers.skyjacker.SkyDAO;
import importer.suppliers.skyjacker.SkyHibernateUtil;
import importer.suppliers.skyjacker.SkyService;
import importer.suppliers.skyjacker.sky_entities.SkyShock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.Set;

public class Controller {
    private static final Logger logger = LogManager.getLogger(Controller.class.getName());

    public static void main(String[] args) {
        //importBilstein();
        importSkyjacker();
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

    private static void importSkyjacker(){
        Session session = SkyHibernateUtil.getSession();
        Set<SkyShock> skyShocks = SkyDAO.getAllShocks(session);
        Set<ProductionItem> newItems = new HashSet<>();
        int total = skyShocks.size();
        int counter = 0;
        for (SkyShock shock : skyShocks) {
            if (shock.getSku()==null){
                continue;
            }
            ProductionItem item = new SkyConverter().buildItem(shock, session);
            newItems.add(item);
            counter++;
            logger.info("Built item " + counter + " of total " + total);
        }
        session.close();
        new ItemService().saveItems(newItems);
        SkyHibernateUtil.shutdown();
        HibernateUtil.shutdown();


    }


}
