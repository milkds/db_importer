package importer.suppliers.summit;

import importer.Utils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class SumLogProcesser {


    public void processUnkAppNotes() {
        List<String> allLines = Utils.readErrLog();
        List<String> errLines = getAllUnkAppNotesLines(allLines);
        Set<AppNotePartKeeper> processedLines = getProcessedLines(errLines); //k = unknown part, v = car + item + fullNote
        printProcessedLines(processedLines);
    }

    private void printProcessedLines(Set<AppNotePartKeeper> processedLines) {
        processedLines.forEach(line->{
            //System.out.println(line.getUpart() + " ::: " + line.getItemPart() + " ::: " + line.getCarPart() + " ::: " + line.getFullAppNotePart());
            System.out.println(line.getUpart());
        });
    }

    private  Set<AppNotePartKeeper> getProcessedLines(List<String> errLines) {
        Set<AppNotePartKeeper> result = new HashSet<>();
        errLines.forEach(line->{
            AppNotePartKeeper current = new AppNotePartKeeper();
            String uPart = StringUtils.substringAfter(line, "Unknown part is ");
            String carPart = StringUtils.substringBetween(line, "at car ProductionCar{carID=0, ", "}. Unknown part is");
            String itemPart = StringUtils.substringBetween(line, "at item ::: "," ::: ");
            String fullAppNotePart = StringUtils.substringBetween(line, itemPart +" ::: "," at car");
            current.setUpart(uPart);
            current.setCarPart(carPart);
            current.setItemPart(itemPart);
            current.setFullAppNotePart(fullAppNotePart);
            result.add(current);
        });

        return result;
    }

    private List<String> getAllUnkAppNotesLines(List<String> allLines) {
        List<String> result = new ArrayList<>();
        allLines.forEach(line->{
            if (line.length()>0&&line.contains("Unknown Application Note at item")){
                result.add(line);
            }
        });
        return result;
    }

    private class AppNotePartKeeper {
        private String upart;
        private String carPart;
        private String itemPart;
        private String fullAppNotePart;

        public void setUpart(String upart) {
            this.upart = upart;
        }

        public String getUpart() {
            return upart;
        }

        public void setCarPart(String carPart) {
            this.carPart = carPart;
        }

        public String getCarPart() {
            return carPart;
        }

        public void setItemPart(String itemPart) {
            this.itemPart = itemPart;
        }

        public String getItemPart() {
            return itemPart;
        }

        public void setFullAppNotePart(String fullAppNotePart) {
            
            this.fullAppNotePart = fullAppNotePart;
        }

        public String getFullAppNotePart() {
            return fullAppNotePart;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AppNotePartKeeper that = (AppNotePartKeeper) o;
            return Objects.equals(upart, that.upart);
        }

        @Override
        public int hashCode() {
            return Objects.hash(upart);
        }
    }
}
