package importer.suppliers.summit;

import importer.suppliers.summit.entities.SumItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SumItemSkipper {
    private SumItem sumItem;
    private Map<String, List<String>> skipMap;

    public SumItemSkipper(SumItem sumItem) {
        this.sumItem = sumItem;
        initMap();
    }

    private void initMap() {
        skipMap = new HashMap<>();
        List<String> koniList = getKoniList();
        skipMap.put("Koni", koniList);
    }

    private List<String> getKoniList() {
        List<String> result = new ArrayList<>();
        result.add("9044-1005");
        result.add("88-1457SP1");
        result.add("8805-1006");
        result.add("88-1458SP1");
        result.add("8805-1013");
        result.add("8805-1004");
        result.add("8805-1010");
        result.add("90-2755");
        result.add("88-1641SP3");
        result.add("90-2694");
        result.add("8805-1009");
        result.add("8805-1007");
        result.add("90-2693");
        result.add("8805-1005");


        result.add("8805-1006");

        return result;
    }

    public boolean needSkip() {
        List<String> items = skipMap.get(sumItem.getBrand());
        if (items==null){
            return false;
        }
        return items.contains(sumItem.getPartNo());
    }
}
