package importer.suppliers.eibach;

import importer.HibernateUtil;
import importer.entities.FitmentAttribute;
import importer.entities.ProductionCar;
import importer.entities.ProductionFitment;
import importer.entities.ProductionItem;
import importer.service.FitmentService;
import importer.service.ItemService;
import importer.suppliers.bilstein.BilService;
import importer.suppliers.eibach.eib_entities.EibCar;
import importer.suppliers.eibach.eib_entities.EibItem;
import importer.suppliers.eibach.eib_filters.EibItemFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.*;

public class EibController {
    private static final Logger logger = LogManager.getLogger(EibController.class.getName());


    public static void updateFitNotes(){
        Session eibSession = EibHibernateUtil.getSessionFactory().openSession();
        Session prodSession = HibernateUtil.getSessionFactory().openSession();
        Set<EibItem> eibItems = getItemsForImport(eibSession);
        Set<ProductionItem> prodItems = buildProductionItems(eibItems, eibSession, prodSession);
        Set<ProductionItem> existingEibItems =  ItemService.getAllItemsByMake("Eibach", prodSession);
        updateFits(prodItems, existingEibItems, prodSession);

    }

    private static void updateFits(Set<ProductionItem> newItems, Set<ProductionItem> existingEibItems, Session prodSession) {
        Map<String, ProductionItem> newItemMap = new HashMap<>();
        newItems.forEach(newItem-> newItemMap.put(newItem.getItemPartNo(), newItem));
        existingEibItems.forEach(oldItem->{
            ProductionItem newItem = newItemMap.get(oldItem.getItemPartNo());
            processFits(newItem, oldItem, prodSession);
        });
    }

    private static void processFits(ProductionItem newItem, ProductionItem oldItem, Session prodSession) {
        Map<ProductionCar, ProductionFitment> oldCarFitMap = new HashMap<>();
        oldItem.getProductionFitments().forEach(oldFit-> oldCarFitMap.put(oldFit.getCar(), oldFit));
        newItem.getProductionFitments().forEach(newFit->{
            ProductionFitment oldFit = oldCarFitMap.get(newFit.getCar());
            if  (oldFit==null){
                oldItem.getProductionFitments().forEach(olF->{
                    logger.info(olF.getCar());
                });
                logger.info("Item " + oldItem.getItemPartNo());
                logger.info("New Car " + newFit.getCar());
                System.exit(1);

            }
            Set<FitmentAttribute> oldAtts = oldFit.getFitmentAttributes();
            Set<FitmentAttribute> newAtts = newFit.getFitmentAttributes();
            if (oldAtts!=null&&newAtts!=null){
                if (!oldAtts.equals(newAtts)){
                    oldFit.setFitmentAttributes(newAtts);
                    FitmentService.updateFit(oldFit, prodSession);
                    logger.info("updated fits for " + oldItem.getItemPartNo());
                }
            }
        });

    }

    public static void importEibach(){
        Session eibSession = EibHibernateUtil.getSessionFactory().openSession();
        Session prodSession = HibernateUtil.getSessionFactory().openSession();
        Set<EibItem> eibItems = getItemsForImport(eibSession);
        Set<ProductionItem> prodItems = buildProductionItems(eibItems, eibSession, prodSession);
        new ItemService().saveItems(prodItems);
        eibSession.close();
        prodSession.close();
        EibHibernateUtil.shutdown();
        HibernateUtil.shutdown();
    }

    private static Set<ProductionItem> buildProductionItems(Set<EibItem> eibItems, Session eibSession, Session prodSession) {
        Set<ProductionItem> result = new HashSet<>();
        List<ProductionItem> tmpResult = new ArrayList<>();
        Map<Integer, EibCar> carsMap = EibService.getAllCarsMap(eibSession);
        Map<Integer, Integer> itemCarMap = EibService.getItemCarMap(eibSession);
        Map<String, Set<String>> bilMakeModelMap = BilService.getMakeModelMap();
        System.out.println(eibItems.size());
        eibItems.forEach(eibItem -> {
            Integer carID = itemCarMap.get(eibItem.getId());
            EibCar eibCar = carsMap.get(carID);
            tmpResult.add(new EibToProdConverter(eibSession, eibItem, eibCar, prodSession).buildProdItem(bilMakeModelMap));
        });
        Map<String, List<ProductionItem>> itemMap = groupItems(tmpResult);
        result = joinItems(itemMap);

        return result;
    }

    private static Set<ProductionItem> joinItems(Map<String, List<ProductionItem>> itemMap) {
        Set<ProductionItem> result = new HashSet<>();
        itemMap.forEach((k,v)->{
            int size = v.size();
            if (size==1){
                result.add(v.get(0));
            }
            else {
                ProductionItem basicItem = v.get(0);
                for (int i = 1; i < size; i++) {
                    ProductionItem toMerge = v.get(i);
                    mergeItems(basicItem, toMerge);
                }
                result.add(basicItem);
            }
        });

        return result;
    }

    private static void mergeItems(ProductionItem basicItem, ProductionItem toMerge) {
        basicItem.getItemAttributes().addAll(toMerge.getItemAttributes());
        Set<ProductionFitment> fitsToMerge = toMerge.getProductionFitments();
        fitsToMerge.forEach(fit->{
            fit.setItem(basicItem);
            basicItem.getProductionFitments().add(fit);
        });
        String baseItemType = basicItem.getItemType();
        if (baseItemType==null){
            String mergeItemType = toMerge.getItemType();
            if (mergeItemType!=null){
                basicItem.setItemType(mergeItemType);
            }
        }
    }

    private static Map<String, List<ProductionItem>> groupItems(List<ProductionItem> tmpResult) {
        Map<String, List<ProductionItem>> result = new HashMap<>();
        tmpResult.forEach(item->{
            String part = item.getItemPartNo();
            if (!result.containsKey(part)){
                List<ProductionItem> items = new ArrayList<>();
                items.add(item);
                result.put(part, items);
            }
            else {
                result.get(part).add(item);
            }
        });

        return result;
    }

    private static Set<EibItem> getItemsForImport(Session eibSession) {
        Set<EibItem> eibItems = EibService.getAllItems(eibSession);
        logger.info("Size is " + eibItems.size());
        eibItems = new EibItemFilter().checkItems(eibSession, eibItems);

        return eibItems;
    }

}
