package importer.suppliers.skyjacker;

import importer.dao.CarDAO;
import importer.entities.ProductionCar;
import org.hibernate.Session;

import java.util.List;

public class SkyService {
    public static boolean modelExists(String rawModelStr, Session session) {
        String model = CarDAO.getExistingModel(rawModelStr);

        return (model==null);
    }

    public static ProductionCar getExistingCar(ProductionCar car, int carYear, Session session) {
        ProductionCar result = null;
        List<ProductionCar> similarCars = CarDAO.getSimilarCarsByMMY(car, carYear);
        if (similarCars.size()>0){
            result = similarCars.get(0);
        }

        return result;
    }
}
