package importer.suppliers.eibach.eib_filters;

import importer.suppliers.eibach.EibService;
import importer.suppliers.eibach.eib_entities.EibItem;
import org.hibernate.Session;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class FitBrandModelChecker implements EibItemChecker {
     private Map<String, Set<String>> brandModelMap;
     private Set<Integer> itemIDs;

     FitBrandModelChecker(Session eibSession) {
       init(eibSession);
    }

     @Override
     public void init(Session eibSession) {
         brandModelMap = new HashMap<>();
         addHonda(brandModelMap);
         addWholeBrands(brandModelMap);
         itemIDs = EibService.getItemsIdsExcludingBrandsModels(eibSession, brandModelMap);
     }

    private void addWholeBrands(Map<String, Set<String>> brandModelMap) {
         brandModelMap.put("YAMAHA", new HashSet<>());
         brandModelMap.put("POLARIS", new HashSet<>());
         brandModelMap.put("KAWASAKI", new HashSet<>());
         brandModelMap.put("KTM", new HashSet<>());
         brandModelMap.put("CAN-AM", new HashSet<>());
         brandModelMap.put("TEXTRON", new HashSet<>());
         brandModelMap.put("SUZUKI", new HashSet<>());
         brandModelMap.put("Universal", new HashSet<>());
    }

    private void addHonda(Map<String, Set<String>> brandModelMap) {
        Set<String> hondaSet = new HashSet<>();
        hondaSet.add("CR125");
        hondaSet.add("CR125R");
        hondaSet.add("CR250");
        hondaSet.add("CR250R");
        hondaSet.add("CR480R");
        hondaSet.add("CR500");
        hondaSet.add("CR500R");
        hondaSet.add("CR80R");
        hondaSet.add("CR80RB Expert");
        hondaSet.add("CR85R");
        hondaSet.add("CR85RB Expert");
        hondaSet.add("CRF150R");
        hondaSet.add("CRF150R Expert");
        hondaSet.add("CRF250R");
        hondaSet.add("CRF250RX");
        hondaSet.add("CRF250X");
        hondaSet.add("CRF450R");
        hondaSet.add("CRF450RWE");
        hondaSet.add("CRF450RX");
        hondaSet.add("CRF450X");
        hondaSet.add("Talon 1000R");
        hondaSet.add("XR400");
        hondaSet.add("XR400R");
        hondaSet.add("XR500R");
        hondaSet.add("XR600");
        hondaSet.add("XR600R");
        hondaSet.add("XR650L");
        hondaSet.add("XR650R");
        brandModelMap.put("HONDA", hondaSet);
    }

    @Override
     public boolean check(EibItem eibItem) {
        int itemID = eibItem.getId();
        if (itemIDs.contains(itemID)){
            return false;
        }

        return true;
     }
 }
