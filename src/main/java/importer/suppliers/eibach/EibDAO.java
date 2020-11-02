package importer.suppliers.eibach;

import importer.suppliers.eibach.eib_entities.EibCar;
import importer.suppliers.eibach.eib_entities.EibItem;
import importer.suppliers.eibach.eib_entities.MetBlock;
import importer.suppliers.keystone.entities.KeyItem;
import importer.suppliers.skyjacker.sky_entities.SkyFitment;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class EibDAO {

    public static List<EibItem> getAllItems(Session eibSession) {
        List<EibItem> allItemList = new ArrayList<>();
        CriteriaBuilder builder = eibSession.getCriteriaBuilder();
        CriteriaQuery<EibItem> crQ = builder.createQuery(EibItem.class);
        Root<EibItem> root = crQ.from(EibItem.class);
        Query q = eibSession.createQuery(crQ);
        allItemList = q.getResultList();

        return allItemList;
    }

    public static List<EibItem> getItemsFromClass(Session eibSession, Class clazz) {
        CriteriaBuilder builder = eibSession.getCriteriaBuilder();
        List<EibItem> result = new ArrayList<>();
        CriteriaQuery<EibItem> crQ = builder.createQuery(EibItem.class);
        Root root = crQ.from(clazz);
        crQ.select(root.get("item"));
        Query q = eibSession.createQuery(crQ);

        return q.getResultList();
    }

    public static List<EibCar> getAllCars(Session eibSession) {
        List<EibCar> allCarList = new ArrayList<>();
        CriteriaBuilder builder = eibSession.getCriteriaBuilder();
        CriteriaQuery<EibCar> crQ = builder.createQuery(EibCar.class);
        Root<EibCar> root = crQ.from(EibCar.class);
        Query q = eibSession.createQuery(crQ);
        allCarList = q.getResultList();

        return allCarList;
    }

    public static List<EibItem> getItemsInvalidBrands(Session eibSession, Set<String> skipBrands) {
        List<EibItem> result = new ArrayList<>();
        CriteriaBuilder builder = eibSession.getCriteriaBuilder();
        CriteriaQuery<EibCar> crQ = builder.createQuery(EibCar.class);
        Root<EibCar> root = crQ.from(EibCar.class);
        crQ.where(root.get("make").in(skipBrands));
        crQ.select(root.get("item"));
        Query q = eibSession.createQuery(crQ);
        result = q.getResultList();

        return result;
    }

    public static List<EibItem> allItemsFromCars(Session eibSession) {
        List<EibItem> result = new ArrayList<>();
        CriteriaBuilder builder = eibSession.getCriteriaBuilder();
        CriteriaQuery<EibCar> crQ = builder.createQuery(EibCar.class);
        Root<EibCar> root = crQ.from(EibCar.class);
        crQ.select(root.get("item"));
        Query q = eibSession.createQuery(crQ);
        result = q.getResultList();

        return result;
    }

    public static List<EibItem> getItemsFromCarIDS(Session eibSession, Set<Integer> carIDs) {
        List<EibItem> result = new ArrayList<>();
        CriteriaBuilder builder = eibSession.getCriteriaBuilder();
        CriteriaQuery<EibCar> crQ = builder.createQuery(EibCar.class);
        Root<EibCar> root = crQ.from(EibCar.class);
        crQ.where(root.get("id").in(carIDs));
        crQ.select(root.get("item"));
        Query q = eibSession.createQuery(crQ);
        result = q.getResultList();

        return result;
    }

    public static Map<Integer, Integer> getCarItemIDmap(Session eibSession) {
        Map<Integer, Integer> result = eibSession
                .createQuery(
                        "select " +
                                "   id as carID, " +
                                "   item.id as itemID " +
                                "from " +
                                "   EibCar ", Tuple.class)
                .getResultList()
                .stream()
                .collect(
                        Collectors.toMap(
                                tuple -> ((Number) tuple.get("carID")).intValue(),
                                tuple -> ((Number) tuple.get("itemID")).intValue()
                        )
                );


        return result;
    }
}
