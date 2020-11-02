package importer.suppliers.eibach.eib_filters;

import importer.suppliers.eibach.eib_entities.EibItem;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.Set;

public class EibItemTitleChecker implements EibItemChecker {
    private static Set<String> wrongTitleStarts;
    private Session eibSession;

    @Override
    public void init(Session eibSession) {
        initWrongTitleStarts();
    }

    private void initWrongTitleStarts() {
        wrongTitleStarts = new HashSet<>();
        wrongTitleStarts.add("HAT");
        wrongTitleStarts.add("PULLOVER");
        wrongTitleStarts.add("T-SHIRT");
        wrongTitleStarts.add("ZIP HOODIE");
    }

    @Override
    public boolean check(EibItem eibItem) {
        String title = eibItem.getTitle();
      for (String wSt: wrongTitleStarts){
          if (title.startsWith(wSt)){
              return false;
          }
      }

        return true;
    }


    public EibItemTitleChecker(Session eibSession) {
        this.eibSession = eibSession;
        init(eibSession);
    }
}
