package importer.suppliers.summit;

import importer.Controller;
import importer.HibernateUtil;
import importer.Utils;
import importer.entities.ProductionItem;
import importer.service.ItemService;
import importer.suppliers.summit.entities.SumFitAttribute;
import importer.suppliers.summit.entities.SumItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class SumController {
    private static final Logger logger = LogManager.getLogger(SumController.class.getName());

    public void saveSummitToDB(){
        Session sumSession = SumHibernateUtil.getSessionFactory().openSession();
        Set<SumItem> sumItems = new SumService().getAllItems(sumSession);
        Map<Integer, List<SumFitAttribute>> allSumFitAtts = new SumService().getAllFitAttributes(sumSession);
        Set<ProductionItem> prodItems = buildProdItems(sumItems, allSumFitAtts);
       // new ItemService().saveItems(prodItems);

        sumSession.close();
        SumHibernateUtil.shutdown();
        HibernateUtil.shutdown();
    }

    private Set<ProductionItem> buildProdItems(Set<SumItem> sumItems, Map<Integer, List<SumFitAttribute>> allSumFitAtts) {
        Set<ProductionItem> result = new HashSet<>();
        Map<String, String> sumAppNotesMap = getSumAppNotesMap();
        SummitCarValidator validator = new SummitCarValidator();
        Set<String> wrongItemTypes = getWrongItemTypes();
        int counter = 1;
        int total = sumItems.size();
        for (SumItem sumItem: sumItems){
            if (new SumItemSkipper(sumItem).needSkip()) {
                counter++;
                continue;
            }
            if(counter>0){
               /* if (sumItem.getFitments().size()>100){
                    logger.info("built item " + counter + " of total " + total);
                    counter++;
                    continue;
                }*/
                ProductionItem item = new SumItemBuilder(sumItem).buildItem(sumAppNotesMap, validator, allSumFitAtts);
                if (itemTypeValid(item,wrongItemTypes)){
                    result.add(item);
                }
           //     logger.info("built item " + counter + " of total " + total);
            }
            counter++;
        }



        return result;
    }

    private boolean itemTypeValid(ProductionItem item, Set<String> wrontItemTypes) {
        String itemType = item.getItemType();

        return !wrontItemTypes.contains(itemType);
    }

    private Set<String> getWrongItemTypes() {
        Set<String> result = new HashSet<>();

        return result;
    }

    private Map<String, String> getSumAppNotesMap() {
        Map<String, String> result = new HashMap<>();
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(new File("src\\main\\resources\\sum_app_notes.xlsx"));
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
            logger.error("Couldn't open excel file");
            System.exit(1);
        }
        Sheet noteSheet = workbook.getSheetAt(0);
        int qty = noteSheet.getLastRowNum();
        for (int i = 0; i <= qty; i++) {
            Row currentRow = noteSheet.getRow(i);
            String name = currentRow.getCell(1).getStringCellValue();
            String value = currentRow.getCell(0).getStringCellValue();
            result.put(name, value);
        }

        return result;
    }

    public void printFitAttNames(){
        Session session = SumHibernateUtil.getSessionFactory().openSession();
        List<SumFitAttribute> atts = new ArrayList<>();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<SumFitAttribute> crQ = builder.createQuery(SumFitAttribute.class);
        Root<SumFitAttribute> root = crQ.from(SumFitAttribute.class);
        Query q = session.createQuery(crQ);
        atts = q.getResultList();
        Set<String> attNames = new HashSet<>();
        atts.forEach(att->{
            String name = att.getName();
            if (name.equals("Application Notes:")){
                String value = att.getValue();
                String[] split = value.split(";");
                attNames.addAll(Arrays.asList(split));
            }
          //  attNames.add(att.getName());
        });
        attNames.forEach(System.out::println);
        session.close();
        SumHibernateUtil.shutdown();
    }

    public void processSumLogsUnkAppNotes(){
        new SumLogProcesser().processUnkAppNotes();
    }
}
