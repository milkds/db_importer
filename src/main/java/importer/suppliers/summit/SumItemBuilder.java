package importer.suppliers.summit;

import importer.entities.*;
import importer.suppliers.summit.entities.SumFitAttribute;
import importer.suppliers.summit.entities.SumItem;
import importer.suppliers.summit.entities.SumItemAttribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SumItemBuilder {
    private SumItem sumItem;
    private ProductionItem result;
    private static final Logger logger = LogManager.getLogger(SumItemBuilder.class.getName());
    public SumItemBuilder(SumItem sumItem) {
        this.sumItem = sumItem;
    }


    public ProductionItem buildItem(Map<String, String> sumAppNotesMap, SummitCarValidator validator, Map<Integer, List<SumFitAttribute>> allSumFitAtts) {
        result = new ProductionItem();
        setPartNo();
        setItemMake();
        setItemType();
        setParams();//will initiate blank params object
        setItemAttributes();
        setItemPics();
        setFits(sumAppNotesMap, validator, allSumFitAtts);

        return result;
    }

    private void setFits(Map<String, String> sumAppNotesMap, SummitCarValidator validator, Map<Integer, List<SumFitAttribute>> allSumFitAtts) {
        Instant start = Instant.now();
        Set<ProductionFitment> prodFits = new SumFitBuilder(sumItem, sumAppNotesMap).buildFits(result, validator,allSumFitAtts);
        prodFits.forEach(prodFit->{
            prodFit.setItem(result);
            result.getProductionFitments().add(prodFit);
        });
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
       // logger.info("Built fits in " + timeElapsed + " ms");
    }

    private void setItemPics() {
        String itemPics = sumItem.getPicUrls();
        if (itemPics==null||itemPics.length()==0){
            return;
        }
        String[] split = itemPics.split("div");
        for (String picUrl: split){
            ItemPic iPic = new ItemPic();
            iPic.setActual(picUrl.endsWith("true"));
            if (iPic.isActual()){
                picUrl = picUrl.replace("_true", "");
            }
            else {
                picUrl =  picUrl.replace("_false", "");
            }
            iPic.setPicUrl(picUrl);
            iPic.setItem(result);
            result.getPics().add(iPic);
        }
    }

    private void setParams() {
        ShockParameters params = new ShockParameters();
        result.setParams(params);
        params.setItem(result);
    }

    private void setItemAttributes() {
        processAttField();
        result.getItemAttributes().add(new ItemAttribute("Short Description", sumItem.getShortDesc()));
        result.getItemAttributes().add(new ItemAttribute("Description", sumItem.getDescription()));
    }

    private void processVideoField() {
        //срус
      /*  String videoLinks = sumItem.getVideoUrls();
        if (videoLinks==null||videoLinks.length()==0){
            return;
        }
        String[] links = videoLinks.split("div");*/
    }

    private void processAttField() {
        List<SumItemAttribute> sumAtts = sumItem.getAttributes();
        Map<String, String> attsToCheck = getAttsToCheck(); //k = att name, v = correct att name.
        sumAtts.forEach(sumAtt->{
            String attName = sumAtt.getName();
            String attValue = sumAtt.getValue();
            if (attsToCheck.containsKey(attName)){
                String mapV = attsToCheck.get(attName);
                if (mapV.length()!=0){  //we skip attributes with zero length
                    result.getItemAttributes().add(processItemAtt(attValue, mapV));
                }
            }
            else {
                result.getItemAttributes().add(new ItemAttribute(attName, attValue));
            }
        });
    }

    private ItemAttribute processItemAtt(String attValue, String mapV) {
        if (mapV.contains("Mount")){
            if (mapV.contains("Upper")){
                result.getParams().setUpperMount(attValue);
            }
            else {
                result.getParams().setLowerMount(attValue);
            }
        }
        else {
            attValue = attValue.replace(" in.","");
            attValue = attValue.replace(" in","");
            if (mapV.contains("Collapsed")){
                result.getParams().setColLength(Double.parseDouble(attValue));
            }
            else {
                result.getParams().setExtLength(Double.parseDouble(attValue));
            }
        }

        return new ItemAttribute(mapV, attValue);
    }

    private Map<String, String> getAttsToCheck() {
        Map<String, String> result = new HashMap<>();
        result.put("Summit Racing Part Number:","");
        result.put("UPC:","");
        result.put("In-Store Pickup:","");
        result.put("Collapsed Length (in):","Collapsed Length");
        result.put("Collapsed Length (in.):","Collapsed Length");
        result.put("Extended Length (in):","Extended Length");
        result.put("Extended Length (in.):","Extended Length");
        result.put("Lower Mount:","Lower Mount");
        result.put("Upper Mount:","Upper Mount");

        return result;
    }

    private void setItemType() {
        result.setItemType(sumItem.getItemType());
    }

    private void setItemMake() {
        result.setItemManufacturer(sumItem.getBrand());
    }

    private void setPartNo() {
        result.setItemPartNo(sumItem.getPartNo());
    }
}
