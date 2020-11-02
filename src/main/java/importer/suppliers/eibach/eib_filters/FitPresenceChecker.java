package importer.suppliers.eibach.eib_filters;

import importer.suppliers.eibach.EibService;
import importer.suppliers.eibach.eib_entities.EibItem;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.Set;

public class FitPresenceChecker implements EibItemChecker {
    Set<Integer> noFitItemIDS = new HashSet<>();//ids of items without fits
    Session eibSession;
    @Override
    public void init(Session eibSession) {
        noFitItemIDS = EibService.getNoFitItemIDS(eibSession);
    }

    @Override
    public boolean check(EibItem eibItem) {
      if (noFitItemIDS.contains(eibItem.getId())){
          return false;
      }

        return true;
    }

    public FitPresenceChecker(Session eibSession) {
        this.eibSession = eibSession;
        init(eibSession);
    }
}
