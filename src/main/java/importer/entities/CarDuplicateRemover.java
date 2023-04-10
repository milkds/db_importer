package importer.entities;

import importer.suppliers.summit.SumController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class CarDuplicateRemover {
    private static final Logger logger = LogManager.getLogger(SumController.class.getName());
    private Map<String, ProductionCar> checkedCars = new HashMap<>();
    public void removeCarDuplicates(Set<ProductionItem> items) {
        logger.info("removing car duplicates");
        items.forEach(item->{
            Set<ProductionFitment> fitments = item.getProductionFitments();
            fitments.forEach(currentFit->{
                ProductionCar car = currentFit.getCar();
                String carLine = getCarLine(car);
                ProductionCar existingCar = checkedCars.get(carLine);
                if (existingCar==null){
                    checkedCars.put(carLine,car);
                }
                else {
                    currentFit.setCar(existingCar);
                }
            });
        });
        logger.info("Car duplicates removed");
    }

    private String getCarLine(ProductionCar car) {
        String separator = ":::";
        String mainCarline = getMainCarLine(car, separator);
        List<CarAttribute> attributes = car.getAttributes();
        String attributeCarline = getAttributeCarLine(attributes,separator);

        return mainCarline+attributeCarline;
    }

    private String getAttributeCarLine(List<CarAttribute> attributes, String separator) {
        StringBuilder sb = new StringBuilder();
        Set<String> attributeLines = new HashSet<>();
        attributes.forEach(attribute -> {
            String attLine = attribute.getCarAttName()+separator + attribute.getCarAttValue()+separator;
            attributeLines.add(attLine);
        });
        attributeLines.forEach(sb::append);

        return sb.toString();
    }

    private String getMainCarLine(ProductionCar car, String separator) {
        StringBuilder sb = new StringBuilder();
        sb.append(car.getYearStart()).append(separator);
        sb.append(car.getYearFinish()).append(separator);
        sb.append(car.getMake()).append(separator);
        sb.append(car.getModel()).append(separator);
        sb.append(car.getSubModel()).append(separator);
        sb.append(car.getDrive()).append(separator);

        return sb.toString();
    }
}
