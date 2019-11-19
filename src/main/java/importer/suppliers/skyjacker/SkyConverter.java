package importer.suppliers.skyjacker;

import importer.entities.*;
import importer.suppliers.skyjacker.sky_entities.Category;
import importer.suppliers.skyjacker.sky_entities.SkyFitment;
import importer.suppliers.skyjacker.sky_entities.SkyShock;
import importer.suppliers.skyjacker.sky_entities.SpecAndKitNote;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.Set;

public class SkyConverter {
    private static final Logger logger = LogManager.getLogger(SkyConverter.class.getName());
    public ProductionItem buildItem(SkyShock shock, Session session) {
        ProductionItem item = new ProductionItem();
        setItemFields(shock, item);
        setFitments(item, shock, session);



        return item;
    }

    private void setFitments(ProductionItem item, SkyShock shock, Session session) {
        Set<SkyFitment> fitments = shock.getSkyFitments();
        Set<ProductionFitment> prodFits = new HashSet<>();
        fitments.forEach(skyFitment -> {
            ProductionFitment fitment = getFitment(skyFitment, session);
            prodFits.add(fitment);
        });
        item.setProductionFitments(prodFits);
    }

    private ProductionFitment getFitment(SkyFitment skyFitment, Session session) {
        ProductionFitment fitment = new ProductionFitment();
        ProductionCar car = buildProductionCar(skyFitment.getFitString(), session);
        Set<FitmentAttribute> attributes = getFitmentAttributes(skyFitment);
        fitment.setCar(car);
        fitment.setFitmentAttributes(attributes);

        return fitment;
    }

    private Set<FitmentAttribute> getFitmentAttributes(SkyFitment skyFitment) {
        return null;
    }

    private ProductionCar buildProductionCar(String fitString, Session session) {
        return null;
    }

    private void setItemFields(SkyShock shock, ProductionItem item) {
        setItemPartNo(shock, item);
        setItemManufacturer(shock, item);
        setItemType(shock, item);
        setItemAttributes(shock, item);
    }

    private void setItemAttributes(SkyShock shock, ProductionItem item) {
        Set<SpecAndKitNote> skNotes = shock.getNotes();
        Set<ItemAttribute> attributes = new HashSet<>();
        skNotes.forEach(note->{
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName(note.getName());
            attribute.setItemAttValue(note.getValue());
            attributes.add(attribute);
            logger.debug(attribute);
        });
        Set<Category> categories = shock.getCategories();
        categories.forEach(category -> {
            ItemAttribute attribute = new ItemAttribute();
            attribute.setItemAttName("Category");
            attribute.setItemAttValue(category.getName());
            attributes.add(attribute);
            logger.debug(attribute);
        });
        item.setItemAttributes(attributes);
    }

    private void setItemType(SkyShock shock, ProductionItem item) {
        item.setItemType("Shock Absorber");
    }

    private void setItemManufacturer(SkyShock shock, ProductionItem item) {
        item.setItemManufacturer("Skyjacker");
    }

    private void setItemPartNo(SkyShock shock, ProductionItem item) {
        item.setItemPartNo(shock.getSku());
    }
}
