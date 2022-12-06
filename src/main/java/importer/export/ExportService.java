package importer.export;

import importer.HibernateUtil;
import importer.dao.CarDAO;
import importer.dao.FitmentDAO;
import importer.dao.ItemDAO;
import importer.entities.*;
import importer.entities.links.CarAttributeLink;
import importer.entities.links.FitmentAttributeLink;
import importer.entities.links.ItemAttributeLink;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

class ExportService {
    private static final Logger logger = LogManager.getLogger(ExportService.class.getName());

    private Session session;

    ExportService() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    List<ExportEntity> getExportEntities(String brand) {
        List<ProductionItem> items = getItems(brand);
        ExportEntityBuilder builder = initExportEntityBuilder(items, brand);
        List<ExportEntity> result = new ArrayList<>();
        Instant start = Instant.now();
        items.forEach(item -> {
            result.add(builder.buildExportEntity(item));
        });
        Instant end = Instant.now();
        logger.info("Built all exportEntities in " + Duration.between(start, end));
        shutDown();
        return result;
    }

    private ExportEntityBuilder initExportEntityBuilder(List<ProductionItem> items, String brand) {
        Instant start = Instant.now();
        ExportEntityBuilder result = new ExportEntityBuilder();
        setDividers(result);
        result.setItems(items);
        List<ItemAttribute> itemAttributes = getItemAttributes();
        List<ItemAttributeLink> itemAttributeLinks = getItemAttributeLinks();
        result.initItemAttMap(itemAttributes, itemAttributeLinks);
        initItemPics(result, brand);
        initFits(result, items);
        initCars(result);

        Instant end = Instant.now();
        logger.info("Entity Builder initiated in " + Duration.between(start, end));
        return result;
    }

    private void setDividers(ExportEntityBuilder result) {
        try (InputStream input = new FileInputStream("src\\main\\resources\\exportProperties.xml")) {
           /* Properties prop = new Properties();
            prop.load(input);
            result.setATT_DIVIDER(prop.getProperty("att_divider"));
            result.setGEN_DIVIDER(prop.getProperty("gen_divider"));*/
           result.setATT_DIVIDER(": ");
           result.setGEN_DIVIDER("; ");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initCars(ExportEntityBuilder builder) {
        List<ProductionCar> cars = getCars(builder);
        builder.setCars(cars);
        setCarAttributes( builder);
    }

    private void setCarAttributes(ExportEntityBuilder builder) {
        Instant start = Instant.now();
        List<CarAttribute> attributes = new ArrayList<>();
        Set<Integer> carIDS = new HashSet<>();
        builder.getCars().forEach(car -> carIDS.add(car.getCarID()));
        List<CarAttributeLink> links = CarDAO.getCarAttributeLinks(carIDS, session);
        Instant end = Instant.now();
        logger.info("Got car attribute links in " + Duration.between(start, end));
        logger.info("Total links " + links.size());
        start = Instant.now();
        Set<Integer> attIDs = new HashSet<>();
        links.forEach(link->attIDs.add(link.getAttID()));
        attributes = CarDAO.getCarAttributes(attIDs, session);
        end = Instant.now();
        logger.info("Got car attributes in " + Duration.between(start, end));
        logger.info("Total car attributes " + attributes.size());
        builder.setCarAttributeMap(attributes, links);
    }

    private List<ProductionCar> getCars(ExportEntityBuilder builder) {
        Instant start = Instant.now();
        List<ProductionCar> result = new ArrayList<>();
        List<ProductionFitment> fitments = builder.getFits();
        Set<Integer> carIDS = new HashSet<>();
        fitments.forEach(fit->{
            carIDS.add(fit.getCar().getCarID());
        });
        result = CarDAO.getCarsByIDs(session, carIDS);
        Instant end = Instant.now();
        logger.info("Got Cars in " + Duration.between(start, end));
        logger.info("Total cars " + result.size());

        return result;
    }

    private void initFits(ExportEntityBuilder result, List<ProductionItem> items) {
        List<ProductionFitment> fitsByItems = getFits(items);
        List<FitmentAttribute> fitAttributes = getFitAttributes(fitsByItems);
        List<FitmentAttributeLink> links = getFitAttributeLinks(fitsByItems);
        result.setFits(fitsByItems);
        result.setFitAttributeMap(fitAttributes, links); //fit attributes all, but links only for needed fit
    }

    private List<FitmentAttributeLink> getFitAttributeLinks(List<ProductionFitment> allFits) {
        Instant start = Instant.now();
        Set<Integer>fitIDs = new HashSet<>();
        allFits.forEach(fit->{
            fitIDs.add(fit.getFitmentID());
        });
        List<FitmentAttributeLink> result = FitmentDAO.getFitAttLinksByFitIDs(session, fitIDs);
        Instant end = Instant.now();
        logger.info("Got fitment attribute links for selected fit ids " + Duration.between(start, end));

        return result;
    }

    private List<FitmentAttribute> getFitAttributes(List<ProductionFitment> allFits) {
        Instant start = Instant.now();
        List<FitmentAttribute> result = FitmentDAO.getAllFitAttributes(session);
        Instant end = Instant.now();
        logger.info("Got all fitment attributes in " + Duration.between(start, end));

        return result;
    }

    private List<ProductionFitment> getFits(List<ProductionItem> items) {
        Instant start = Instant.now();
        List<ProductionFitment> result;
      //  Set<Integer> itemIDS = getItemIDs(items);
        result = FitmentDAO.getAllFitsForItems(session, items);
        Instant end = Instant.now();
        logger.info("Got all fits in " + Duration.between(start, end));

        return result;
    }

    private Set<Integer> getItemIDs(List<ProductionItem> items) {
        Set<Integer> result = new HashSet<>();
        items.forEach(item -> result.add(item.getItemID()));

        return result;
    }

    private void initItemPics(ExportEntityBuilder result, String brand) {
        Instant start = Instant.now();
        List<Object[]> picIds = ItemDAO.getItempicItemIDsArray(session); //0 - itemID; 1 - fileName String
        result.setItemPicMap(picIds, brand);
        Instant end = Instant.now();
        logger.info(Duration.between(start, end));
        logger.info("total itempics quantity is " + picIds.size());
    }

    private List<ItemAttributeLink> getItemAttributeLinks() {
        Instant start = Instant.now();
        List<ItemAttributeLink> result = ItemDAO.getAllItemAttributeLinks(session);
        Instant end = Instant.now();
        logger.info (Duration.between(start, end));

        return result;
    }

    private List<ItemAttribute> getItemAttributes() {
        Instant start = Instant.now();
        List<ItemAttribute> result = ItemDAO.getAllItemAttributes(session);
        Instant end = Instant.now();
        logger.info (Duration.between(start, end));

        return result;
    }

    private List<ProductionItem> getItems(String brand) {
        Instant start = Instant.now();
        List<ProductionItem> result = ItemDAO.getAllItemsByMake(brand,session);
        Instant end = Instant.now();
        logger.info("Got all items in " + Duration.between(start, end));

        return result;
    }


    private void shutDown(){
        session.close();
        HibernateUtil.shutdown();
    }


}
