package importer;

import importer.dao.CarDAO;
import importer.entities.CarMergeEntity;
import importer.entities.ProductionItem;
import importer.entities.ShockParameters;
import importer.service.CarService;
import importer.service.ItemService;
import importer.suppliers.bilstein.BilConverter;
import importer.suppliers.bilstein.BilHibernateUtil;
import importer.suppliers.bilstein.BilService;
import importer.suppliers.bilstein.BilsteinDAO;
import importer.suppliers.bilstein.bilstein_entities.BilShock;
import importer.suppliers.eibach.EibController;
import importer.suppliers.fox.FoxHibernateUtil;
import importer.suppliers.fox.FoxSupplier;
import importer.suppliers.fox.dao.FoxItemDAO;
import importer.suppliers.fox.entities.FoxItem;
import importer.suppliers.keystone.KeyHibernateUtil;
import importer.suppliers.keystone.KeyService;
import importer.suppliers.keystone.KeySupplier;
import importer.suppliers.keystone.entities.KeyItem;
import importer.suppliers.keystone.KeyItemBuilder;
import importer.suppliers.skyjacker.SkyConverter;
import importer.suppliers.skyjacker.SkyDAO;
import importer.suppliers.skyjacker.SkyHibernateUtil;
import importer.suppliers.skyjacker.sky_entities.SkyShock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Controller {
    private static final Logger logger = LogManager.getLogger(Controller.class.getName());

    public static void main(String[] args) {
        importKeystone();
     //   downloadAllPics();

         /* //  importFox();
      //  importSkyjacker();
   //    updateFromKeystone();
     //   importBilstein();

     //   fillMergingTable();
      //
     // EibController.importEibach();
      //  importKeystone();
       // checkAlreadyParsedShocks("Bilstein");*/
    }

    private static void importKeystone(){
        Session keySession = KeyHibernateUtil.getSession();
        Set<KeyItem> items = KeyService.getAllItems(keySession);
        Set<ProductionItem> newItems = new HashSet<>();
        Set<String> subModels = CarService.getSubModelSet();
        Map<String, Set<String>> bilMakeModelMap = BilService.getMakeModelMap();
        int total = items.size();
        int counter = 0;
        for (KeyItem keyItem : items) {
            ProductionItem item = new KeyItemBuilder(bilMakeModelMap).buildItem(keyItem, subModels, keySession);
           if (item!=null){
               newItems.add(item);
           }
            counter++;
            logger.info("Built item " + counter + " of total " + total);
        }
      //  new ItemService().saveItems(newItems);
        keySession.close();
        HibernateUtil.shutdown();
        KeyHibernateUtil.shutdown();
        SkyHibernateUtil.shutdown();
    }

    private static void downloadAllPics() {
        Utils.downloadAllPics();
        HibernateUtil.shutdown();
    }

    private static void fillMergingTable() {
        String filePath = "C:\\Users\\jackson\\Desktop\\car_merge.xlsx";
        Set<CarMergeEntity> entities = ExcelUtil.getMergeInfoFromFile(filePath);
        CarDAO.saveCarMergeEntities(entities);
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
        SkyHibernateUtil.shutdown(); //under question - seems like we use sky methods with sky session
        HibernateUtil.shutdown();
        FoxHibernateUtil.shutdown();
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

    private static void checkAlreadyParsedShocks(String make){
        Set<ProductionItem> allItemsForMake = new HashSet<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        allItemsForMake = ItemService.getAllItemsByMake(make, session);
        List<String> allKeyItemLinks = Utils.readImportFile();
        Set<String> linksToRemove = new HashSet<>();
        allItemsForMake.forEach(prodItem->{
            ShockParameters params = prodItem.getParams();
            if (Utils.paramsFilled(params)){
                String partNo = prodItem.getItemPartNo();
                allKeyItemLinks.forEach(link->{
                    if (link.contains(partNo)){
                        linksToRemove.add(link);
                        return;
                    }
                });
            }
        });
        allKeyItemLinks.removeAll(linksToRemove);
        allKeyItemLinks.forEach(System.out::println);
        session.close();
        HibernateUtil.shutdown();
    }




}
