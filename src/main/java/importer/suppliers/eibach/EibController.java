package importer.suppliers.eibach;

import importer.Controller;
import importer.HibernateUtil;
import importer.entities.ProductionItem;
import importer.service.ItemService;
import importer.suppliers.bilstein.BilService;
import importer.suppliers.eibach.eib_entities.EibCar;
import importer.suppliers.eibach.eib_entities.EibItem;
import importer.suppliers.eibach.eib_filters.EibItemFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EibController {
    private static final Logger logger = LogManager.getLogger(EibController.class.getName());

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
        Map<Integer, EibCar> carsMap = EibService.getAllCarsMap(eibSession);
        Map<Integer, Integer> itemCarMap = EibService.getItemCarMap(eibSession);
        Map<String, Set<String>> bilMakeModelMap = BilService.getMakeModelMap();
        System.out.println(eibItems.size());
        eibItems.forEach(eibItem -> {
            Integer carID = itemCarMap.get(eibItem.getId());
            EibCar eibCar = carsMap.get(carID);
            result.add(new EibToProdConverter(eibSession, eibItem, eibCar, prodSession).buildProdItem(bilMakeModelMap));
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
