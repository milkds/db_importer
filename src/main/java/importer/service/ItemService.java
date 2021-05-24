package importer.service;

import importer.HibernateUtil;
import importer.dao.ItemDAO;
import importer.entities.ItemAttribute;
import importer.entities.ItemPic;
import importer.entities.ProductionItem;
import importer.entities.ShockParameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

public class ItemService {
    private static final Logger logger = LogManager.getLogger(ItemService.class.getName());

    public static Set<ProductionItem> getAllItemsByMake(String itemMake, Session prodSession) {
        List<ProductionItem> itemList = ItemDAO.getAllItemsByMake(itemMake, prodSession);

        return new HashSet<>(itemList);
    }

    public static List<ItemAttribute> getAllItemAttributes(Session session) {
        List<ItemAttribute> result = ItemDAO.getAllItemAttributes(session);
        return result;
    }

    public void updateItemAttributes(List<ItemAttribute> allAttributes, Session session) {
        Set<String> correctMounts = getCorrectMounts();
        Map<String, String> mountMap = getMountMap();
        allAttributes.forEach(attribute -> {
            String name = attribute.getItemAttName();
            if (!filterMount(name)){ return;}
            String value = attribute.getItemAttValue();
            if (correctMounts.contains(value)){ return;}
            String correctValue = mountMap.get(value);
            if (correctValue==null){
                logger.error("Unknown Mount " + value);
                System.exit(1);
            }
            attribute.setItemAttValue(correctValue);
            ItemDAO.updateItemAttribute(session, attribute);
            logger.info("corrected " + value + " to " + correctValue);
        });

    }

    private boolean filterMount(String name) {
        if (name==null){
            return false;
        }
        if (name.length()==0){
            return false;
        }
        if (!name.equals("Upper Mount Full")&&!name.equals("Lower Mount Full")){
            return false;
        }

        return true;
    }

    public void updateItem(ProductionItem item, Session prodSession) {
        checkMounts(item);
        checkLengths(item);
        ItemDAO.updateItem(item, prodSession);
    }

    public static void updateItemAttributes(String oldVal, String newVal) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<ItemAttribute> itemAtts = ItemDAO.getItemAttributesByName(session, oldVal);
        itemAtts.forEach(itemAttribute -> {
            itemAttribute.setItemAttName(newVal);
            ItemDAO.updateItemAttribute(session, itemAttribute);
        });
        session.close();
    }

    public static Set<String> getAllPicLinks() {
        Set<String> result = new HashSet<>();
        result = ItemDAO.getAllPicLinks();
        result.remove("https://productdesk.cart.bilsteinus.com/media/products/bilstein/image_generic_02_1.jpg");
        result.remove("NO IMG LINK");

        return result;
    }

    public static List<ProductionItem> getAllItems() {
        List<ProductionItem> result = new ArrayList<>();
        result = ItemDAO.getAllItems();

        return result;
    }

    public static void updateItemPics(Set<ItemPic> pics) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        pics.forEach(pic-> ItemDAO.updateItemPic(pic, session));
        session.close();
    }

    public void saveItems(Set<ProductionItem> newItems) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        int counter = 0;
        int total = newItems.size();
        for (ProductionItem item : newItems) {
             prepareItemAttributes(item, session);
             ItemDAO.saveItem(item, session);
             item.getProductionFitments().forEach(fitment->{
                 fitment.setItem(item);
                 new FitmentService().saveFitment(fitment, session);
             });
             counter++;
            logger.info("Saved item " + counter + " of total " + total);
        }
        session.close();




         /* Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();

            transaction.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }*/
    }

    private void prepareItemAttributes(ProductionItem item, Session session) {
        checkMounts(item);
        checkLengths(item);
        Set<ItemAttribute> attributes = item.getItemAttributes();
        Set<ItemAttribute> checkedAttributes = new HashSet<>();
        attributes.forEach(attribute->{
            new ProdItemAttChecker().checkAttributeContent(attribute);
            ItemAttribute checkedAtt = ItemDAO.checkAttribute (attribute, session); //checks existence
            checkedAttributes.add(Objects.requireNonNullElse(checkedAtt, attribute));
        });
        item.setItemAttributes(checkedAttributes);
    }

    private void checkLengths(ProductionItem item) {
        ShockParameters params = item.getParams();
        if (params==null){
            return;
        }
        double colLength = params.getColLength();
        if (colLength!=0){
            item.getItemAttributes().add(new ItemAttribute("Fully Collapsed Length", colLength+""));
        }
        double extLength = params.getExtLength();
        if (extLength!=0){
            item.getItemAttributes().add(new ItemAttribute("Fully Extended Length", extLength+""));
        }
    }

    private void checkMounts(ProductionItem item) {
        ShockParameters params = item.getParams();
        if (params==null){
            return;
        }
        String upperMount = params.getUpperMount();
        if (upperMount!=null&&upperMount.length()>0){
            upperMount = checkMount(upperMount);
            item.getItemAttributes().add(new ItemAttribute("Upper Mount Full", upperMount));
        }
        String lowerMount = params.getLowerMount();
        if (lowerMount!=null&&lowerMount.length()>0){
            item.getItemAttributes().add(new ItemAttribute("Lower Mount Full", lowerMount));
        }
    }

    private String checkMount(String mount) {
        Set<String> correctMounts = getCorrectMounts();
        if (correctMounts.contains(mount)){
            return mount;
        }
        Map<String, String> mountMap = getMountMap(); //k = wrong value, v = correct value
        String correct = mountMap.get(mount);
        if (correct==null){
            logger.error("Unknown mount " + mount);
            System.exit(1);
        }

        return correct;
    }

    private Map<String, String> getMountMap() {
        Map<String, String> result = new HashMap<>();
        result.put("Eye","Eyelet");
        result.put("Bar-Pin","Bar Pin");
        result.put("Cross Pin","Bar Pin");
        result.put("Cross Pin Mount","Bar Pin");
        for (int i = 1; i < 12; i++) {
            result.put("Cross Pin Mount-XP"+i, "Bar Pin");
        }
        result.put("SS1","Stem");
        result.put("SS2","Stem");
        result.put("SS3","Stem");
        result.put("SS4","Stem");
        result.put("SS6","Stem");
        result.put("SS7","Stem");
        result.put("Stem Mount","Stem");
        result.put("Stem Mount-S2","Stem");
        result.put("Stem Mount-S4","Stem");
        result.put("Stem Mount-S5","Stem");
        result.put("T-bar","T-Bar");

        return result;
    }

    private Set<String> getCorrectMounts() {
        Set<String> result = new HashSet<>();
        result.add("Axle Mount");
        result.add("B11");
        result.add("B4");
        result.add("B8");
        result.add("Ball");
        result.add("Ball Joint");
        result.add("Bar Pin");
        result.add("Base");
        result.add("Base Cup");
        result.add("Bolt-On");
        result.add("Bracket");
        result.add("Cantilever");
        result.add("Clevis");
        result.add("Clevis Bracket");
        result.add("Clevis/ Eyelet");
        result.add("Clip");
        result.add("Crossbar");
        result.add("Double Stud");
        result.add("Double Welded Loop");
        result.add("Electrical Plastic Bearing");
        result.add("Eyelet");
        result.add("Eyelet/ Stud");
        result.add("Factory");
        result.add("Fork");
        result.add("Heim");
        result.add("Housing");
        result.add("Key Hole");
        result.add("Loop");
        result.add("Loop Bushing");
        result.add("Loop Mount-L1");
        result.add("Loop Mount-L2");
        result.add("Loop Mount-L3");
        for (int i = 4; i <93 ; i++) {
            result.add("Loop Sleeve-LS"+i);
        }
        result.add("Metal Ball");
        result.add("OEM");
        result.add("Offset Bracket");
        result.add("Pin");
        result.add("Plastic Ball");
        result.add("Plastic Ball, Electric");
        result.add("Sleeve Mount");
        result.add("Snapeye");
        result.add("Special Mount");
        result.add("Stem");
        result.add("Stem Plate");
        result.add("Strut");
        result.add("Stud");
        result.add("T-Bar");
        result.add("Threaded");
        result.add("Tie Rod Clamp");
        result.add("Top Mount");
        result.add("U Bracket");
        result.add("Yoke");

        return result;
    }


}
