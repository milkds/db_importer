package importer.service;

import importer.dao.CarDAO;
import importer.entities.CarAttribute;
import importer.entities.CarMergeEntity;
import importer.entities.ProductionCar;
import importer.entities.ProductionFitment;
import importer.suppliers.skyjacker.SkyDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class CarService {
    private static final Logger logger = LogManager.getLogger(CarService.class.getName());
    public void saveCar(ProductionFitment fitment, Session session) {
        ProductionCar car = fitment.getCar();
        car = checkCarExistence(car, session);
        if (car.getCarID()==0){
            prepareCarAttributes(car, session);
            CarDAO.saveCar(car, session);
        }
        logger.debug(car);
        fitment.setCar(car);
    }

    private void prepareCarAttributes(ProductionCar car, Session session) {
        Set<CarAttribute> attributes = car.getAttributes();
        Set<CarAttribute> checkedAttributes = new HashSet<>();
        attributes.forEach(attribute->{
            CarAttribute checkedAtt = CarDAO.checkAttribute(attribute, session);
            checkedAttributes.add(Objects.requireNonNullElse(checkedAtt, attribute));
        });
        car.setAttributes(checkedAttributes);
    }

    private ProductionCar checkCarExistence(ProductionCar car, Session session) {
        List<ProductionCar> similarCars = CarDAO.getSimilarCars(car, session);
        for (ProductionCar dbCar : similarCars) {
            if (dbCar.equals(car)) {
                return dbCar;
            }
        }

        return car;
    }

    public static CarMergeEntity getCarMergeEntity(ProductionCar car) {
        CarMergeEntity result = null;
        List<CarMergeEntity> entities = CarDAO.getMergeEntities(car);
        if (entities.size()!=0){
            result = entities.get(0);
        }

        return result;
    }
}
