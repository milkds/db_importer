package importer.suppliers.bilstein;

import importer.suppliers.bilstein.bilstein_entities.BilCar;
import org.hibernate.Session;

import java.util.*;

public class BilService {
    public static Map<String, Set<String>> getMakeModelMap() {
        Session bilSession = BilHibernateUtil.getSession();
        Map<String, Set<String>> result = new HashMap<>();
        List<BilCar> bilCars = BilsteinDAO.getAllCars(bilSession);
        bilCars.forEach(car->{
            String make = car.getMake();
            if (result.containsKey(make)){
                result.get(make).add(car.getModel());
            }
            else {
                Set<String> models = new HashSet<>();
                models.add(car.getModel());
                result.put(make, models);
            }
        });
        BilHibernateUtil.shutdown();

        return result;
    }
}
