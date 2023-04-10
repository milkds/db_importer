package importer.suppliers.keystone;

import importer.HibernateUtil;
import importer.entities.ItemAttribute;
import importer.entities.ProductionItem;
import importer.entities.ShockParameters;
import importer.service.ItemService;
import importer.suppliers.keystone.entities.KeyItem;
import importer.suppliers.keystone.entities.KeyItemSpec;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.*;

public class KeySupplier {

    private static final Logger logger = LogManager.getLogger(KeySupplier.class.getName());

    public void updateShockLengths(){
        Session keySession = KeyHibernateUtil.getSession();
        Session prodSession = HibernateUtil.getSessionFactory().openSession();
        Set<KeyItem> items = KeyService.getAllItems(keySession);
        Map<String, ProductionItem> prodItemMap = getProdItemMap(prodSession);

        int counter = 0;
        int total = items.size();

        for (KeyItem keyItem: items){
            Set<KeyItemSpec> specs = new HashSet<>(keyItem.getSpecs());
            KeyItemSpec extLength = null;
            KeyItemSpec colLength = null;
            for (KeyItemSpec spec : specs) {
                String name = spec.getSpecName();
                if (name.equals("Extended Length (IN)")) {
                    extLength = spec;
                }
                else if (name.equals("Compressed Length (IN)")) {
                    colLength = spec;
                }
            }
            if (extLength!=null||colLength!=null){
                ProductionItem item = prodItemMap.get(keyItem.getPartNo());
                if (item!=null){
                    Set<ItemAttribute> prodItemAttributes = item.getItemAttributes();
                    boolean hasCollapsedLength = false;
                    boolean hasExtendedLength = false;
                    for (ItemAttribute attribute: prodItemAttributes){
                        String name = attribute.getItemAttName();
                        if (name.equals("Collapsed Length (IN)")){
                            hasCollapsedLength = true;
                        }
                        else if (name.equals("Extended Length (IN)")){
                            hasExtendedLength = true;
                        }
                    }
                    boolean updateNeeded = false;
                    if (!hasCollapsedLength&&colLength!=null){
                        prodItemAttributes.add(getLengthAttribute("Compressed", colLength));
                        updateNeeded = true;
                    }
                    if (!hasExtendedLength&&extLength!=null){
                        prodItemAttributes.add(getLengthAttribute("Extended", extLength));
                        updateNeeded = true;
                    }
                    if (updateNeeded){
                      new ItemService().updateItem(item, prodSession);
                    }

                    counter++;
                    logger.info("Checked item " + counter + " of total " + total);
                }
            }
        }

        keySession.close();
        prodSession.close();
        HibernateUtil.shutdown();
        KeyHibernateUtil.shutdown();
    }

    private ItemAttribute getLengthAttribute(String lengthType, KeyItemSpec spec) {
        String name = lengthType + " length";
        String value = spec.getSpecValue();
        if (value.contains(" Inch")){
            value = value.replace(" Inch", "");
        }
        ItemAttribute result = new ItemAttribute();
        result.setItemAttName(name);
        result.setItemAttValue(value);

        return result;
    }

    private Map<String, ProductionItem> getProdItemMap(Session prodSession) {
        Set<ProductionItem> productionItems = ItemService.getAllItemsByMake("Bilstein", prodSession);
        Map<String, ProductionItem> prodItemMap = new HashMap<>();
        productionItems.forEach(prodItem-> prodItemMap.put(prodItem.getItemPartNo(), prodItem));

        return prodItemMap;
    }

    public void setLengthAndMounts(Session keySession, Session prodSession){
        Set<KeyItem> keyItems = KeyService.getAllItems(keySession);
        Map<String, ProductionItem> prodItemMap = getProdItemMap(prodSession);
        Set<ProductionItem> prodItemsToUpdate = checkItemsForUpdates(keyItems, prodItemMap);
        prodItemsToUpdate.forEach(prodItem-> new ItemService().updateItem(prodItem, prodSession));
    }

    private Set<ProductionItem> checkItemsForUpdates(Set<KeyItem> keyItems, Map<String, ProductionItem> prodItemMap) {
        Set<ProductionItem> result = new HashSet<>();
        int count = 0;
        int total = keyItems.size();
        for (KeyItem keyItem : keyItems) {
            logger.debug("Checking item " + keyItem);
            ProductionItem currentProdItem = prodItemMap.get(keyItem.getPartNo());
            if (currentProdItem!=null){
               // boolean updateNeeded = iterateSpecs(currentProdItem, keyItem);
                boolean updateNeeded = true;
                if (updateNeeded){
                    result.add(currentProdItem);
                }
            }

            count++;
            logger.info("Checked item " + count + " of total " + total);
        }

        return result;
    }

    private boolean iterateSpecs(ProductionItem currentProdItem, KeyItem keyItem) {
        boolean result = false;
      /*  List<KeyItemSpec> keySpecs = keyItem.getSpecs();
        for (KeyItemSpec spec : keySpecs) {
            String name = spec.getSpecName();
            if (name.equals("Extended Length (IN)")) {
                ShockParameters params = currentProdItem.getParams();
                if (params.getExtLength() == 0) {
                    double extLength = getLength(spec.getSpecValue());
                    params.setExtLength(extLength);
                    if (extLength>0){
                        addItemAttribute("Extended Length", extLength+"", currentProdItem);
                        result = true;
                    }
                }
            }
            if (name.equals("Compressed Length (IN)")) {
                ShockParameters params = currentProdItem.getParams();
                if (params.getColLength() == 0) {
                    double comLength = getLength(spec.getSpecValue());
                    params.setColLength(comLength);
                    if (comLength>0){
                        addItemAttribute("Compressed Length", comLength+"", currentProdItem);
                        result = true;
                    }
                }
            }
            if (name.equals("Upper Mounting Style")){
                ShockParameters params = currentProdItem.getParams();
                if (params.getUpperMount()==null){
                    params.setUpperMount(spec.getSpecValue());
                    addItemAttribute("Upper Mount", spec.getSpecValue(), currentProdItem);
                    result = true;
                }
            }
            if (name.equals("Lower Mounting Style")){
                ShockParameters params = currentProdItem.getParams();
                if (params.getLowerMount()==null){
                    params.setLowerMount(spec.getSpecValue());
                    addItemAttribute("Lower Mount", spec.getSpecValue(), currentProdItem);
                    result = true;
                }
            }
        }*/

        return result;
    }

    private void addItemAttribute(String name, String value, ProductionItem item) {
        ItemAttribute attribute = new ItemAttribute();
        attribute.setItemAttName(name);
        attribute.setItemAttValue(value);
        item.getItemAttributes().add(attribute);
    }

    private double getLength(String specValue) {
        specValue = specValue.replace(" Inch", "");
        if (specValue.contains("/")){
            specValue = specValue.replace("-", ".");
            specValue = modifyFractions(specValue);
        }
        double result = 0d;
        try {
            result = Double.parseDouble(specValue);
        }
        catch (NumberFormatException ignored){}

        return result;
    }

    private String modifyFractions(String specValue) {
        String fraction = StringUtils.substringAfter(specValue, ".");
        String[] split = fraction.split("/");
        double num = Integer.parseInt(split[0]);
        double den = Integer.parseInt(split[1]);
        double dec = num/den;
        String doub = dec+"";
        doub = StringUtils.substringAfter(doub, ".");
        String result = specValue.replace(fraction, doub);

        return result;
    }
}
