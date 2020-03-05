package importer.service;

import importer.entities.ItemAttribute;

public class ProdItemAttChecker {
    public void checkAttributeContent(ItemAttribute attribute) {
        checkName(attribute);
    }

    private void checkName(ItemAttribute attribute) {
        String itemAttName = attribute.getItemAttName();
        if (itemAttName==null||itemAttName.length()==0){
            return;
        }
        if (itemAttName.contains("Compressed")){
            itemAttName = itemAttName.replace("Compressed", "Collapsed");
            attribute.setItemAttName(itemAttName);
        }
    }
}
