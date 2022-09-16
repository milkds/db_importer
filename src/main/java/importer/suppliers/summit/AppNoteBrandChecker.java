package importer.suppliers.summit;

import importer.entities.ProductionCar;
import importer.entities.ProductionFitment;
import importer.entities.ProductionItem;
import importer.suppliers.summit.brandcheckers.FoxAppNoteChecker;

public class AppNoteBrandChecker {
    private ProductionCar car;
    private ProductionFitment fit;
    private ProductionItem prodItem;

    private boolean processingNeeded = false;
    public AppNoteBrandChecker(ProductionCar car, ProductionFitment fit, ProductionItem prodItem) {
        this.car = car;
        this.fit = fit;
        this.prodItem = prodItem;
    }

    //returns true, if appNote needs further check
    public void checkAppNote(String appNote) {
        String brand = prodItem.getItemManufacturer();
        if (brand.equals("Fox Racing Shox - Truck & Offroad")){
            processingNeeded = new FoxAppNoteChecker().checkAppNote(car, fit, prodItem, appNote);
        }
    }



    public ProductionCar getCar() {
        return car;
    }

    public void setCar(ProductionCar car) {
        this.car = car;
    }

    public ProductionFitment getFit() {
        return fit;
    }

    public void setFit(ProductionFitment fit) {
        this.fit = fit;
    }

    public ProductionItem getProdItem() {
        return prodItem;
    }

    public void setProdItem(ProductionItem prodItem) {
        this.prodItem = prodItem;
    }

    public boolean processingNeeded() {
        return processingNeeded;
    }

    public void setProcessingNeeded(boolean processingNeeded) {
        this.processingNeeded = processingNeeded;
    }
}
