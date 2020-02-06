package importer.suppliers.skyjacker;

import importer.dao.CarDAO;
import importer.entities.ProductionCar;
import importer.entities.CarMergeEntity;
import org.hibernate.Session;

import java.util.List;

public class SkyService {

    public static ProductionCar getExistingCar(ProductionCar car, int carYear) {
        ProductionCar result = null;
        if (car.getModel().equals("NO MODEL")){
            return result;
        }
        List<ProductionCar> similarCars = CarDAO.getSimilarCarsByMMY(car, carYear);
        if (similarCars.size()>0){
            result = similarCars.get(0);
        }

        return result;
    }

}
