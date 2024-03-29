package importer;

import importer.dao.CarDAO;
import importer.entities.*;
import importer.export.ExportController;
import importer.service.CarService;
import importer.service.DbService;
import importer.service.ItemService;
import importer.suppliers.bilstein.BilConverter;
import importer.suppliers.bilstein.BilHibernateUtil;
import importer.suppliers.bilstein.BilService;
import importer.suppliers.bilstein.BilsteinDAO;
import importer.suppliers.bilstein.bilstein_entities.BilShock;
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
import importer.suppliers.summit.SumController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.IOException;
import java.util.*;

public class Controller {
    private static final Logger logger = LogManager.getLogger(Controller.class.getName());

    public static void main(String[] args) throws IOException {
      //  new ExportController().exportToExcel("Skyjacker");
      //  new ExportController().exportToCSV("Fox Racing Shox - Truck & Offroad");
      //  new ExportController().testCSVexport();
        //new DbService().updateItemAttributes();
       // new DbService().removeDuplicates();
        new SumController().saveSummitToDB();
    }

    private static void controllerStash(){
        // new SumController().saveSummitToDB();
        //  new SumController().processSumLogsUnkAppNotes();


        // updateMounts();

        //  fillMergingTable();
        //EibController.updateFitNotes();
        //     importKeystone();
        // downloadAllPics();

         /* //  importFox();
      //  importSkyjacker();
   //    updateFromKeystone();
     //   importBilstein();


      //
     // EibController.importEibach();
      //  importKeystone();
       // checkAlreadyParsedShocks("Bilstein");*/
    }

    private static void updateMounts() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<ItemAttribute> allAttributes = ItemService.getAllItemAttributes(session);
        new ItemService().updateItemAttributes(allAttributes, session);
        session.close();
        HibernateUtil.shutdown();
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
        new ItemService().saveItems(newItems);
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
       /* allItemsForMake.forEach(prodItem->{
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
        });*/
        allKeyItemLinks.removeAll(linksToRemove);
        allKeyItemLinks.forEach(System.out::println);
        session.close();
        HibernateUtil.shutdown();
    }




}
