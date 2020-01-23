package importer;

import importer.entities.ShockParameters;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Utils {

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
}
