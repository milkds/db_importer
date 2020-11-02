package importer.suppliers.eibach.eib_filters;

import importer.suppliers.eibach.eib_entities.EibItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.Set;

public class EibItemFilter {
    private static final Logger logger = LogManager.getLogger(EibItemFilter.class.getName());

    public Set<EibItem> checkItems(Session eibSession, Set<EibItem> eibItems){
        Set<EibItem> validItems = new HashSet<>();
        Set<EibItemChecker> checkers = initCheckers(eibSession);
        for (EibItem eibItem : eibItems) {
            for (EibItemChecker checker : checkers) {
                boolean check = checker.check(eibItem);
                if (!check) {
                    eibItem = null;
                    break;
                }
            }
            if (eibItem != null) {
                validItems.add(eibItem);
            }
        }
        testLog(validItems);

        return validItems;
    }

    private void testLog(Set<EibItem> validItems) {
    }

    private Set<EibItemChecker> initCheckers(Session eibSession) {
        Set<EibItemChecker> result = new HashSet<>();
        result.add(new FitPresenceChecker(eibSession));
     //   result.add(new FitBrandChecker(eibSession));
        result.add(new EibItemTitleChecker(eibSession));
        result.add(new FitBrandModelChecker(eibSession));

        return result;
    }
}
