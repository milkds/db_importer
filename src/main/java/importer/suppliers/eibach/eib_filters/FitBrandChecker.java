package importer.suppliers.eibach.eib_filters;

import importer.suppliers.eibach.EibService;
import importer.suppliers.eibach.eib_entities.EibCar;
import importer.suppliers.eibach.eib_entities.EibItem;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FitBrandChecker implements EibItemChecker {
    Set<String> skipBrands = new HashSet<>();
    private Set<Integer> itemIDS;
    @Override
    public void init(Session eibSession) {
        fillSkipBrands();
        itemIDS = EibService.getItemsIdsExcludingBrands(skipBrands, eibSession);
    }

    private void fillSkipBrands() {
        skipBrands.add("YAMAHA");
        skipBrands.add("POLARIS");
        skipBrands.add("KAWASAKI");
        skipBrands.add("KTM");
        skipBrands.add("CAN-AM");
        skipBrands.add("TEXTRON");
        skipBrands.add("SUZUKI");
        skipBrands.add("Universal");
    }

    @Override
    public boolean check(EibItem eibItem) {
       if (itemIDS.contains(eibItem.getId())){
           return false;
       }
        return true;
    }

    public FitBrandChecker(Session eibSession) {
        init(eibSession);
    }
}
