package importer.suppliers.summit;

import importer.entities.ProductionCar;

public class NoSuchModelException extends Throwable {
    public NoSuchModelException(ProductionCar car) {
    }
}
