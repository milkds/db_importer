package importer.entities;

import importer.dao.ItemDAO;
import importer.entities.links.ItemAttributeLink;
import importer.service.ItemService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class ItemSaver {
    private Session session;
    private static final Logger logger = LogManager.getLogger(ItemSaver.class.getName());
    private static Map<String, ItemAttribute> itemAttMap; //k = attName+AttValue/ v = itemAttribute
    private Set<ItemAttribute> attributesToUpdate;
    private Set<ItemAttribute> newItemAttributes;

    public ItemSaver(Session session) {
        this.session = session;
        itemAttMap = ItemService.getItemAttMap(session);
        attributesToUpdate = new HashSet<>();
        newItemAttributes = new HashSet<>();
    }


    public void saveItems(Set<ProductionItem> newItems) {
        Instant start = Instant.now();
        populateAttributeItems();
        Instant finish = Instant.now();
        logger.info("populated Item Attributes with items in " + Duration.between(start,finish));
        start = Instant.now();
        prepareItemAttributes(newItems);
        finish = Instant.now();
        logger.info("prepared Item Attributes in " + Duration.between(start,finish));
        start = Instant.now();
        ItemDAO.updateItemAttributes(session, new ArrayList<>(attributesToUpdate));
        finish = Instant.now();
        logger.info("updated Item Attributes in " + Duration.between(start,finish));
        start = Instant.now();
        ItemDAO.saveItemAttributes(session, newItemAttributes);
        finish = Instant.now();
        logger.info("saved Item Attributes in " + Duration.between(start,finish));
        start = Instant.now();
        ItemDAO.saveItems(session, newItems);
        finish = Instant.now();
        logger.info("saved Items in " + Duration.between(start,finish));
    }

    private void populateAttributeItems() {
        Instant start = Instant.now();
        List<ProductionItem> allItems = ItemDAO.getAllItemsExistingSession(session);
        Instant finish = Instant.now();
        logger.info("got All Production Items in " + Duration.between(start,finish));
        start = Instant.now();
        List<ItemAttributeLink> itemAttributeLinkList = ItemDAO.getAllItemAttributeLinks(session);
        finish = Instant.now();
        logger.info("got all Prod Item Attribute Links in " + Duration.between(start,finish));
        start = Instant.now();
        //k = Attribute id, v = Set of Items for this attribute
        Map<Integer, Set<ProductionItem>> attIDItemMap = groupItemsByAttributes(allItems, itemAttributeLinkList);
        finish = Instant.now();
         logger.info("grouped Items with Item attributes at " + Duration.between(start,finish));
        start = Instant.now();
        itemAttMap.forEach((k,v)->{
            Set<ProductionItem> curItems = attIDItemMap.get(v.getItemAttID());
            v.setItems(curItems);
        });
        finish = Instant.now();
        logger.info("populated Item attributes with Items at " + Duration.between(start,finish));
    }

    private Map<Integer, ProductionItem> getItemIdMap(List<ProductionItem> allItems) {
        Map<Integer, ProductionItem> result = new HashMap<>();
        allItems.forEach(item->{
            result.put(item.getItemID(), item);
        });

        return result;
    }

    private Map<Integer, Set<ProductionItem>> groupItemsByAttributes(List<ProductionItem> items, List<ItemAttributeLink> links) {
        Map<Integer, Set<ProductionItem>> result = new HashMap<>();
        //k = item id. v = item for this id.
        Map<Integer, ProductionItem> itemIDMap = getItemIdMap(items);
        links.forEach(link->{
            int attID = link.getAttID();
            Set<ProductionItem> curItems = result.computeIfAbsent(attID, k -> new HashSet<>());
            curItems.add(itemIDMap.get(link.getItemID()));
        });
      return result;
    }

    private void prepareItemAttributes(Set<ProductionItem> items) {
        items.forEach(item->{
            Set<ItemAttribute> currentAttributes = item.getItemAttributes();
            Set<ItemAttribute> finalAttributes = new HashSet<>();
            currentAttributes.forEach(attribute -> {
                String key = attribute.getItemAttName()+attribute.getItemAttValue();
                ItemAttribute existingAttribute = itemAttMap.get(key);
                if (existingAttribute==null){
                    finalAttributes.add(attribute);
                    newItemAttributes.add(attribute);
                    itemAttMap.put(key,attribute);
                }
                else {
                    finalAttributes.add(existingAttribute);
                    int id = existingAttribute.getItemAttID();
                    if (id != 0){
                        attributesToUpdate.add(existingAttribute);
                    }
                    existingAttribute.getItems().add(item);
                }
            });
            item.setItemAttributes(finalAttributes);
        });
    }
}
