package importer;

import importer.entities.ShockParameters;
import importer.service.ItemService;
import org.apache.commons.lang3.StringUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Utils {
    public static final String SAVED_PICS = "src\\main\\resources\\savedPics.txt";

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
        Set<String> allPics = ItemService.getAllPicLinks();
        List<String> savedPics = new ArrayList<>();
        try {
            savedPics = Files.readAllLines(Paths.get(SAVED_PICS),
                    Charset.defaultCharset());
        } catch (Exception e) {
            e.printStackTrace();
        }
        allPics.removeAll(savedPics);
        int total = allPics.size();
        int counter = 0;
        for (String picUrl : allPics) {
            savePic(picUrl);
            counter++;
            System.out.println("saved pic " + counter + " of total " + total);
        }
    }

    private static void savePic(String picUrl) {
        String pName = getRawPicName(picUrl);
        pName =  "C:/pics/"+pName;
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
        else if (picUrl.contains("skyjacker")){
            result = StringUtils.substringAfterLast(picUrl, "/");
            result = "sky/" + result;
        }

        return result;
    }


}
