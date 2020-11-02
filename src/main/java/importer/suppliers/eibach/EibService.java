package importer.suppliers.eibach;

import importer.HibernateUtil;
import importer.suppliers.eibach.eib_entities.EibCar;
import importer.suppliers.eibach.eib_entities.EibItem;
import importer.suppliers.eibach.eib_entities.MetBlock;
import importer.suppliers.eibach.eib_entities.StdBlock;
import importer.suppliers.keystone.entities.ItemCar;
import org.hibernate.Session;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import java.util.*;

public class EibService {
    public static Set<EibItem> getAllItems(Session eibSession) {
        List<EibItem> itemList = EibDAO.getAllItems(eibSession);

        return new HashSet<>(itemList);
    }

    public static Map<Integer, EibCar> getAllCarsMap(Session eibSession) {
        List<EibCar> carList = EibDAO.getAllCars(eibSession);
        Map<Integer, EibCar> result = new HashMap<>();
        carList.forEach(car-> result.put(car.getId(), car));

        return result;
    }

    public static Set<Integer> getMetricIDs(Session eibSession) {
        List<EibItem> items = EibDAO.getItemsFromClass(eibSession, MetBlock.class);
        Set<Integer> result = new HashSet<>();
        items.forEach(item-> result.add(item.getId()));

        return result;
    }

    public static Set<Integer> getStandardIDs(Session eibSession) {
        List<EibItem> items = EibDAO.getItemsFromClass(eibSession, StdBlock.class);
        Set<Integer> result = new HashSet<>();
        items.forEach(item-> result.add(item.getId()));

        return result;
    }

    public static Set<Integer> getItemsIdsExcludingBrands(Set<String> skipBrands, Session eibSession) {
        Set<Integer> result = new HashSet<>();
        List<EibItem> invalidItems = EibDAO.getItemsInvalidBrands(eibSession, skipBrands);
       invalidItems.forEach(eibItem -> {
           result.add(eibItem.getId());
       });

        return result;
    }

    public static Set<Integer> getNoFitItemIDS(Session eibSession) {
        Set<Integer> result = new HashSet<>();
        List<EibItem> allItems = EibDAO.getAllItems(eibSession);
        List<EibItem> allItemsFromCars = EibDAO.allItemsFromCars(eibSession);
        Set<Integer> itemIDS = new HashSet<>();
        Set<Integer> itemFromCarIDs = new HashSet<>();
        allItems.forEach(item-> itemIDS.add(item.getId()));
        allItemsFromCars.forEach(item-> itemFromCarIDs.add(item.getId()));
        itemIDS.forEach(itemID->{
            if (!itemFromCarIDs.contains(itemID)){
                result.add(itemID);
            }
        });

        return result;
    }

    public static Set<Integer> getItemsIdsExcludingBrandsModels(Session eibSession, Map<String, Set<String>> brandModelMap) {
        Set<Integer> result = new HashSet<>();
        Set<Integer> carIDs = new HashSet<>();
        List<EibCar> allCars = EibDAO.getAllCars(eibSession);
        allCars.forEach(car->{
            Set<String> models = brandModelMap.get(car.getMake());
            if (models!=null){
                if (models.size()==0){
                    carIDs.add(car.getId());
                }
                else if (models.contains(car.getModel())){
                    carIDs.add(car.getId());
                }
            }
        });
        List<EibItem> wrongItems = EibDAO.getItemsFromCarIDS(eibSession, carIDs);
        wrongItems.forEach(item-> result.add(item.getId()));

        return result;
    }

    public static Map<Integer, Integer> getItemCarMap(Session eibSession) {
        Map<Integer, Integer> itemCarMap = new HashMap<>();
        Map<Integer, Integer> carItemmap = EibDAO.getCarItemIDmap(eibSession);
        carItemmap.forEach((k,v)->{
            itemCarMap.put(v, k);
        });

        return itemCarMap;
    }
}