package importer.service;

import importer.entities.ProductionCar;

public class ProdCarChecker {
    public void checkCarFields(ProductionCar car) {
        checkDrive(car);
    }

    private void checkDrive(ProductionCar car) {
        String drive = car.getDrive();
        if (drive==null){
            return;
        }
        if (drive.length()==0){
            car.setDrive("N/A");
        }
        switch (drive){
            case "2WD/4WD" :
            case "ALL" :
                car.setDrive("N/A"); break;
            case "4 x 2" :
            case "4 X 2" :
                car.setDrive("RWD"); break;
            case "4 x 4" :
            case "4 X 4" :
                car.setDrive("4WD"); break;
        }
    }
}
