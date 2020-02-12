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

import java.util.*;

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

    public static void setCarYearPeriods(Set<ProductionFitment> prodFits){
        Map<ProductionCar, Set<ProductionFitment>> carGroupMap = groupCars(prodFits);
        carGroupMap.forEach((k,v)-> setPeriodsForSet(v));
    }

    private static Map<ProductionCar, Set<ProductionFitment>> groupCars(Set<ProductionFitment> prodFits) {
        Map<ProductionCar, Set<ProductionFitment>> result = new HashMap<>();
        prodFits.forEach(prodFit->{
            ProductionCar car = prodFit.getCar();
            boolean carPresent = false;
            for (Map.Entry<ProductionCar, Set<ProductionFitment>> entry : result.entrySet()) {
                ProductionCar k = entry.getKey();
                Set<ProductionFitment> v = entry.getValue();
                if (carsDifferOnlyByYear(car, k)) {
                    v.add(prodFit);
                    carPresent = true;
                    break;
                }
            }
            if (!carPresent){
                Set<ProductionFitment> currSet = new HashSet<>();
                currSet.add(prodFit);
                result.put(car, currSet);
            }
        });

        return result;
    }

    private static boolean carsDifferOnlyByYear(ProductionCar car, ProductionCar otherCar) {
        if (!car.getMake().equals(otherCar.getMake())){
            return false;
        }
        if (!car.getModel().equals(otherCar.getModel())){
            return false;
        }
        if (!car.getAttributes().equals(otherCar.getAttributes())){
            return false;
        }
        if (!car.getSubModel().equals(otherCar.getSubModel())){
            return false;
        }

        return car.getDrive().equals(otherCar.getDrive());

    }

    private static void setPeriodsForSet(Set<ProductionFitment> prodFitments) {
        TreeMap<Integer, ProductionFitment> yearMap = new TreeMap<>();
        prodFitments.forEach(productionFitment -> {
            yearMap.put(productionFitment.getCar().getYearStart(), productionFitment);//year start equals year finish at this point
        });
        logger.info("Period Map");
        yearMap.forEach((k,v)->{
            logger.info(k + " " + v.getCar());
            v.getCar().getAttributes().forEach(logger::info);
        });
        Integer yearStart = yearMap.firstKey();
        Integer currentYear = yearStart;
        Integer yearFinish = yearStart;
        Set<ProductionFitment> periodFits = new HashSet<>();
        for (Map.Entry<Integer, ProductionFitment> entry : yearMap.entrySet()) {
            Integer k = entry.getKey();
            ProductionFitment v = entry.getValue();
            if (k.equals(yearStart)) {
                periodFits.add(v);
                continue;
            }
            if (k == currentYear + 1) {
                yearFinish = k;
                currentYear = k;
                periodFits.add(v);
                continue;
            }
            //we get here if break in period is found
            for (ProductionFitment productionFitment : periodFits) {
                ProductionCar car = productionFitment.getCar();
                car.setYearStart(yearStart);
                car.setYearFinish(yearFinish);
            }
            yearStart = k;
            currentYear = yearStart;
            yearFinish = yearStart;
            periodFits = new HashSet<>();
            periodFits.add(v);

        }
        for (ProductionFitment productionFitment : periodFits) {
            ProductionCar car = productionFitment.getCar();
            car.setYearStart(yearStart);
            car.setYearFinish(yearFinish);
        }
    }
}
