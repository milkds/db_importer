package importer.export;

import importer.dao.ItemDAO;
import importer.entities.ItemAttribute;
import importer.entities.ProductionItem;
import importer.entities.links.ItemAttributeLink;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportEntityBuilder {
    private static final Logger logger = LogManager.getLogger(ExportEntityBuilder.class.getName());
    private List<ProductionItem> items;
    private Map<Integer, List<ItemAttribute>> itemAttMap; //k = item ID, v = List of attributes for this item

    void initItemAttMap(List<ItemAttribute> itemAttributes, List<ItemAttributeLink> itemAttributeLinks) {
        Instant start = Instant.now();
        itemAttMap = new HashMap<>();
        Map<Integer, ItemAttribute> tempAttMap = getTempItemAttMap(itemAttributes);//k = att id, v = itemAtt
        items.forEach(item -> {
            int itemID = item.getItemID();
            itemAttributeLinks.forEach(link->{
                if (link.getItemID() == itemID){
                    List<ItemAttribute> attributes = itemAttMap.computeIfAbsent(itemID, k -> new ArrayList<>());
                    attributes.add(tempAttMap.get(link.getAttID()));
                }
            });
        });

        Instant end = Instant.now();
        logger.info (Duration.between(start, end));
    }

    private Map<Integer, ItemAttribute> getTempItemAttMap(List<ItemAttribute> itemAttributes) {
        Map<Integer, ItemAttribute> result = new HashMap<>();
        itemAttributes.forEach(attribute -> result.put(attribute.getItemAttID(), attribute));

        return result;
    }

    public void setItems(List<ProductionItem> items) {
        this.items = items;
    }
}
