package importer.entities;

import importer.dao.CarDAO;
import importer.entities.links.CarAttributeLink;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class CarSaver {
    private static final Logger logger = LogManager.getLogger(CarSaver.class.getName());
    private Session session;
    private List<ProductionCar> carsToSave;
    private Map<Integer,ProductionCar> carMap; //k = car id. v = car
    private Map<Integer, List<ProductionCar>> carsByAttMap; //k = att id. v = Set of cars for this attribute
    private Map<String, CarAttribute> carAttMap; //k = att name + att value. v = attribute

    public CarSaver(Session session) {
        Instant start = Instant.now();
        this.session = session;
        carsToSave = new ArrayList<>();
        carMap = getCarAttMap(session);
        Instant finish = Instant.now();
        logger.info("Initiated Car Saver in " + Duration.between(start,finish));
    }

    private Map<Integer, ProductionCar> getCarAttMap(Session session) {
        Map<Integer, ProductionCar> result = new HashMap<>();
        List<ProductionCar> allCars = CarDAO.getAllCars(session);
        allCars.forEach(car->{
            result.put(car.getCarID(), car);
        });

        return result;
    }

    public void saveCars(Set<ProductionItem> newItems) {
        Instant start = Instant.now();
        carsToSave = getCarsToSave(newItems);
        Instant finish = Instant.now();
        logger.info("Got list of cars to save " + Duration.between(start,finish));
        start = Instant.now();
        carsByAttMap = getCarsByAttMap();
        finish = Instant.now();
        logger.info("Grouped cars by attributes " + Duration.between(start,finish));
        start = Instant.now();
        populateCarAtts(carsByAttMap);
        finish = Instant.now();
        logger.info("Got list of cars to save " + Duration.between(start,finish));
        browseCars();
    }

    private void browseCars() {
        List<ProductionCar> checkedCars = new ArrayList<>();
        Set<CarAttribute> attsToUpdate = new HashSet<>();
        Set<CarAttribute> attsToSave = new HashSet<>();

        carsToSave.forEach(currentCar->{
            List<CarAttribute> currentAtts = currentCar.getAttributes();
            List<CarAttribute> finalAtts = new ArrayList<>();
            currentAtts.forEach(curAtt->{
                String key = curAtt.getCarAttName()+curAtt.getCarAttValue();
                CarAttribute existingAtt = carAttMap.get(key);
                if (existingAtt==null){
                //    logger.info("new car attribute " + curAtt);
                    carAttMap.put(key, curAtt);
                    attsToSave.add(curAtt);
                    finalAtts.add(curAtt);
                }
                else {
              //      logger.info("existing car attribute " + existingAtt);
                    int id = existingAtt.getCarAttID();
                    if (id!=0){
                        attsToUpdate.add(existingAtt);
                    }
                    existingAtt.getProductionCars().add(currentCar);
                    finalAtts.add(existingAtt);
                }
            });
            currentCar.setAttributes(finalAtts);
            checkedCars.add(currentCar);
        });
        updateEntities(attsToUpdate);
        saveEntities(attsToSave, checkedCars);
    }

    private void saveEntities(Set<CarAttribute> attsToSave, List<ProductionCar> checkedCars) {
        Instant start = Instant.now();
        CarDAO.saveAttributes(session, attsToSave);
        Instant finish = Instant.now();
        logger.info("Saved car attributes in " + Duration.between(start, finish));
        start = Instant.now();
        CarDAO.saveCars(session, checkedCars);
        finish = Instant.now();
        logger.info("Saved cars in " + Duration.between(start, finish));
    }

    private void updateEntities(Set<CarAttribute> attsToUpdate) {
        Instant start = Instant.now();
        CarDAO.updateAttributes(session, attsToUpdate);
        Instant finish = Instant.now();
        logger.info("Updated car attributes in " + Duration.between(start, finish));
    }

    private void populateCarAtts(Map<Integer, List<ProductionCar>> carsByAttMap) {
        carAttMap = new HashMap<>();
        List<CarAttribute> allCarAtts = CarDAO.getAllCarAttributes(session);
        allCarAtts.forEach(att->{
            att.setProductionCars(new ArrayList<>(carsByAttMap.get(att.getCarAttID())));
            carAttMap.put(att.getCarAttName()+att.getCarAttValue(),att);
        });
    }

    private Map<Integer, List<ProductionCar>> getCarsByAttMap() {
       Map<Integer, List<ProductionCar>> result = new HashMap<>();
       List<CarAttributeLink> carAttLinks = CarDAO.getAllCarAttributeLinks(session);
       carAttLinks.forEach(link->{
           int attID = link.getAttID();
           List<ProductionCar> cars = result.computeIfAbsent(attID, k -> new ArrayList<>());
           int carID = link.getCarID();
           ProductionCar curCar = carMap.get(carID);
           cars.add(curCar);
       });

        return result;
    }

    private List<ProductionCar> getCarsToSave(Set<ProductionItem> items) {
        new CarDuplicateRemover().removeCarDuplicates(items);
        List<ProductionCar> result = new ArrayList<>();
        items.forEach(item -> {
            Set<ProductionFitment> fitments = item.getProductionFitments();
            fitments.forEach(fit->{
                result.add(fit.getCar());
            });
        });

        return result;
    }
}
