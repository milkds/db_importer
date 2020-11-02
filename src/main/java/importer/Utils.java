package importer;

import importer.entities.ItemPic;
import importer.entities.ProductionItem;
import importer.entities.ShockParameters;
import importer.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Utils {
    public static final String SAVED_PICS = "src\\main\\resources\\savedPics.txt";
    private static final Logger logger = LogManager.getLogger(Utils.class.getName());

    public static List<String> readImportFile(){
        List<String> result = new ArrayList<>();
        String fileName = "src\\main\\resources\\importData.txt";
        try {
            result = Files.readAllLines(Paths.get(fileName),
                    Charset.defaultCharset());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean paramsFilled(ShockParameters params) {
        String mount = params.getLowerMount();
        if (mount==null||mount.length()==0){
            return false;
        }
         mount = params.getUpperMount();
        if (mount==null||mount.length()==0){
            return false;
        }
        double length = params.getColLength();
        if (length==0){
            return false;
        }
         length = params.getExtLength();

        return length != 0;
    }

    public static void downloadAllPics() {
       // Set<String> allPics = ItemService.getAllPicLinks();
        List<ProductionItem> allItems = ItemService.getAllItems();
        Set<String> dowloadedPics = getDowloadedPicNameSet();
        int total = allItems.size();
        int counter = 0;
        for (ProductionItem item : allItems) {
            if (picDownloadNeeded(item)) {
                setFileNames(item);
                try {
                    downloadPicsForItem(item, dowloadedPics);
                    ItemService.updateItemPics(item.getPics());
                } catch (IOException e) {
                  logger.error("Couldn't save pic for " + item);
                }
            }
            counter++;
            System.out.println("saved pics for item " + counter + " of total " + total);
        }


      /*  List<String> savedPics = new ArrayList<>();
        try {
            savedPics = Files.readAllLines(Paths.get(SAVED_PICS),
                    Charset.defaultCharset());
        } catch (Exception e) {
            e.printStackTrace();
        }
        allPics.removeAll(savedPics);
        for (String picUrl : allPics) {
            savePic(picUrl);
            counter++;
            System.out.println("saved pic " + counter + " of total " + total);
        }*/
    }

    public static Set<String> getDowloadedPicNameSet() {
        Set<String> result = null;
        String[] pathnames;
        File f = new File("C:/pics/parsed/");
        pathnames = f.list();
        result = new HashSet<>(Arrays.asList(pathnames));

        return result;
    }

    private static void downloadPicsForItem(ProductionItem item, Set<String> dowloadedPics) throws IOException {
        Set<ItemPic> pics = item.getPics();
        for (ItemPic pic : pics) {
            String rawName = pic.getFileName();
            if (dowloadedPics.contains(rawName)){
                continue;
            }
            String fName = "C:/pics/parsed/" + rawName;
            InputStream in = new URL(pic.getPicUrl()).openStream();
            Files.copy(in, Paths.get(fName));
        }
    }

    private static void setFileNames(ProductionItem item) {
        Set<ItemPic> pics = item.getPics();
        String itemMake = item.getItemManufacturer();
        switch (itemMake){
            case "Bilstein" :{
                for (ItemPic pic: pics){
                    String end = "";
                    if (pic.getPicUrl().equals("https://productdesk.cart.bilsteinus.com//media/products/bilstein/image_generic_02_1.jpg")){
                        end = "a";
                    }
                    else {
                        end = StringUtils.substringBetween(pic.getPicUrl(), "_", ".jpg");
                    }
                    if (!end.equals("a")){
                        int index = Integer.parseInt(end);
                        index = index-1;
                        end = index + "";
                    }
                    String fName = itemMake + "-" + item.getItemPartNo() + "-" + end + ".jpg";
                    pic.setFileName(fName);
                }
                break;
            }
            case "Skyjacker" :{
                for (ItemPic pic: pics){
                    String fName = itemMake + "-" + item.getItemPartNo() + "-" + "0" + ".jpg";
                    pic.setFileName(fName);
                }
                break;
            }
            case "FOX" :{
                int counter = 1;
                for (ItemPic pic: pics){
                    if (pics.size()==1){
                        String fName = itemMake + "-" + item.getItemPartNo() + "-" + "0" + ".jpg";
                        pic.setFileName(fName);
                        break;
                    }
                    String picUrl = pic.getPicUrl();
                    if (picUrl.endsWith("pr.png")){
                        String fName = itemMake + "-" + item.getItemPartNo() + "-" + "0" + ".jpg";
                        pic.setFileName(fName);
                        continue;
                    }
                    String fName = itemMake + "-" + item.getItemPartNo() + "-" + counter + ".jpg";
                    pic.setFileName(fName);
                    counter++;
                }
                break;
            }
            case "KYB Shocks": {
                if (pics.size()==1){
                    ItemPic pic = pics.stream().findFirst().orElse(null);
                    String fName = "KYB" + "-" + item.getItemPartNo() + "-" + 0 + ".jpg";
                    pic.setFileName(fName);
                }
                else {
                    //we suppose that shortest item url is main.
                  ItemPic main = null;
                  int size = 5000;
                  for (ItemPic tmp: pics){
                      int curSize = tmp.getPicUrl().length();
                      if (curSize<size){
                          main = tmp;
                          size = curSize;
                      }
                  }
                  int counter = 1;
                    for (ItemPic tmp: pics){
                        if (tmp.equals(main)){
                            String fName = "KYB" + "-" + item.getItemPartNo() + "-" + 0 + ".jpg";
                            tmp.setFileName(fName);
                        }
                        else {
                            String fName = "KYB" + "-" + item.getItemPartNo() + "-" + counter + ".jpg";
                            tmp.setFileName(fName);
                            counter++;
                        }
                    }
                }
                break;
            }
            case "Pro Comp Suspension": {
                if (pics.size()==1){
                    ItemPic pic = pics.stream().findFirst().orElse(null);
                    String fName = "ProComp" + "-" + item.getItemPartNo() + "-" + 0 + ".jpg";
                    pic.setFileName(fName);
                }
                else {
                    ItemPic main = null;
                    int size = 5000;
                    for (ItemPic tmp: pics){
                        int curSize = tmp.getPicUrl().length();
                        if (curSize<size){
                            main = tmp;
                            size = curSize;
                        }
                    }
                    int counter = 1;
                    for (ItemPic tmp: pics){
                        if (tmp.equals(main)){
                            String fName = "ProComp" + "-" + item.getItemPartNo() + "-" + 0 + ".jpg";
                            tmp.setFileName(fName);
                        }
                        else {
                            String fName = "ProComp" + "-" + item.getItemPartNo() + "-" + counter + ".jpg";
                            tmp.setFileName(fName);
                            counter++;
                        }
                    }
                }
                break;
            }
            case "Rancho": {
                if (pics.size()==1){
                    ItemPic pic = pics.stream().findFirst().orElse(null);
                    String fName = "Rancho" + "-" + item.getItemPartNo() + "-" + 0 + ".jpg";
                    pic.setFileName(fName);
                }
                else {
                    ItemPic main = null;
                    int size = 5000;
                    for (ItemPic tmp: pics){
                        int curSize = tmp.getPicUrl().length();
                        if (curSize<size){
                            main = tmp;
                            size = curSize;
                        }
                    }
                    int counter = 1;
                    for (ItemPic tmp: pics){
                        if (tmp.equals(main)){
                            String fName = "Rancho" + "-" + item.getItemPartNo() + "-" + 0 + ".jpg";
                            tmp.setFileName(fName);
                        }
                        else {
                            String fName = "Rancho" + "-" + item.getItemPartNo() + "-" + counter + ".jpg";
                            tmp.setFileName(fName);
                            counter++;
                        }
                    }
                }
                break;
            }

        }

    }

    private static boolean picDownloadNeeded(ProductionItem item) {
        Set<ItemPic> pics = item.getPics();
        int size = pics.size();
        if (size==0){
            return false;
        }
        if (size==1){
            ItemPic pic = pics.stream().findFirst().orElse(null);
            String url = pic.getPicUrl();
            if (url.equals("NO IMG LINK")||url.equals("http")){
                return false;
            }
            if (pic.getFileName()==null){
                return true;
            }
        }
        for (ItemPic pic: pics){
            if (pic.getFileName()!=null){
                return false;
            }
        }

        return true;
    }

    private static void savePic(String picUrl) {
        String pName = getRawPicName(picUrl);
        pName =  "C:/pics/parsed/"+pName;
        try(InputStream in = new URL(picUrl).openStream()){
            Files.copy(in, Paths.get(pName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter fw = new FileWriter(SAVED_PICS,true);
            fw.write(picUrl);
            fw.write(System.lineSeparator());
            fw.close();
        }
        catch(IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    private static String getRawPicName(String picUrl) {
        String result = "";
        if (picUrl.contains("bilstein")){
            result = StringUtils.substringAfter(picUrl, "products/");
        }
        else if (picUrl.contains("ridefox")){
            result = StringUtils.substringAfter(picUrl, "assets/");
            result = "fox/" + result;
        }
        else if (picUrl.contains("KYB/Images/")){
            result = StringUtils.substringAfter(picUrl, "KYB/Images/");
            result = "kyb/" + result;
        }
        else if (picUrl.contains("PRS/Images/")){
            result = StringUtils.substringAfter(picUrl, "PRS/Images/");
            result = "procomp/" + result;
        }
        else if (picUrl.contains("skyjacker")){
            result = StringUtils.substringAfterLast(picUrl, "/");
            result = "sky/" + result;
        }

        return result;
    }


}
