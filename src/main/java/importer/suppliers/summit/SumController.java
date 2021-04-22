package importer.suppliers.summit;

import importer.Controller;
import importer.HibernateUtil;
import importer.entities.ProductionItem;
import importer.suppliers.summit.entities.SumItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.Set;

public class SumController {
    private static final Logger logger = LogManager.getLogger(SumController.class.getName());

    public void saveSummitToDB(){
        Session sumSession = SumHibernateUtil.getSessionFactory().openSession();
        Set<SumItem> sumItems = new SumService().getAllItems(sumSession);
        Set<ProductionItem> prodItems = buildProdItems(sumItems);
    }

    private Set<ProductionItem> buildProdItems(Set<SumItem> sumItems) {
        Set<ProductionItem> result = new HashSet<>();
        int counter = 1;
        int total = sumItems.size();
        for (SumItem sumItem: sumItems){
            result.add(new SumItemBuilder(sumItem).buildItem());
        }

        return result;
    }
}
