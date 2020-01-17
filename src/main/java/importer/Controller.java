package importer;

import importer.dao.ItemDAO;
import importer.entities.ProductionItem;
import importer.service.ItemService;
import importer.suppliers.bilstein.BilConverter;
import importer.suppliers.bilstein.BilHibernateUtil;
import importer.suppliers.bilstein.BilsteinDAO;
import importer.suppliers.bilstein.bilstein_entities.BilShock;
import importer.suppliers.fox.FoxHibernateUtil;
import importer.suppliers.fox.FoxSupplier;
import importer.suppliers.fox.dao.FoxItemDAO;
import importer.suppliers.fox.entities.FoxItem;
import importer.suppliers.keystone.KeyHibernateUtil;
import importer.suppliers.keystone.KeySupplier;
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
     //   importBilstein();
     //   importSkyjacker();
      //  updateFromKeystone();
      //  renameProdItemAttribute("","");
   //     TestClass.testFoxProdCarMerge();
        TestClass.testSkyBilCarMerge();

    }

    private static void updateFromKeystone() {
        Session keySession = KeyHibernateUtil.getSession();
        Session prodSession = HibernateUtil.getSessionFactory().openSession();
        new KeySupplier().setLengthAndMounts(keySession, prodSession);
        keySession.close();
        prodSession.close();
        HibernateUtil.shutdown();
        KeyHibernateUtil.shutdown();
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

    private static void importFox(){
        Session foxSession = FoxHibernateUtil.getFoxSessionFactory().openSession();
        Set<FoxItem> foxItems = FoxItemDAO.getAllItems(foxSession);
        Set<ProductionItem> newItems = new HashSet<>();
        int total = foxItems.size();
        int counter = 0;
        for (FoxItem foxItem: foxItems){
            ProductionItem prodItem = new FoxSupplier().buildItem(foxItem, foxSession);
            newItems.add(prodItem);
            counter++;
            logger.info("Built item " + counter + " of total " + total);
        }

        foxSession.close();
        new ItemService().saveItems(newItems);
        SkyHibernateUtil.shutdown();
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
                counter++;
                logger.info("Built item " + counter + " of total " + total);
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

    private static void renameProdItemAttribute(String oldVal, String newVal){
        ItemService.updateItemAttributes(oldVal, newVal);
        HibernateUtil.shutdown();
        logger.info("Update finished");
    }


}
