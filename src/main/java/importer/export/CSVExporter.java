package importer.export;

import com.opencsv.CSVWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

class CSVExporter {
    private static final Logger logger = LogManager.getLogger(CSVExporter.class.getName());
    private static final String FILE_PATH = "src\\main\\resources\\export.csv";
    void exportToCSV(List<ExportCSVEntity> csvExportList) {
        Instant start = Instant.now();
        List<String[]> data = generateData(csvExportList);
        Instant end = Instant.now();
        logger.info ("generated data for csv in " + Duration.between(start, end));
        start = Instant.now();
        saveToFile(data);
        end = Instant.now();
        logger.info ("saved data to csv in " + Duration.between(start, end));
    }

    private List<String[]> generateData(List<ExportCSVEntity> csvExportList) {
        List<String[]> result = new ArrayList<>();
        csvExportList.forEach(csvEntity->{
            String[] dataSet = new String[14];
            ExportEntity exportEntity = csvEntity.getExportEntity();
            dataSet[0] = csvEntity.getId();
            dataSet[1] = exportEntity.getPartNo();
            dataSet[2] = exportEntity.getItemType();
            dataSet[3] = exportEntity.getBrand();
            dataSet[4] = exportEntity.getExtendedLength();
            dataSet[5] = exportEntity.getCollapsedLength();
            dataSet[6] = exportEntity.getUpperMount();
            dataSet[7] = exportEntity.getLowerMount();
            dataSet[8] = exportEntity.getItemAttributes();
            dataSet[9] = exportEntity.getCarCategories();
            dataSet[10] = csvEntity.getTitle();
            dataSet[11] = csvEntity.getShortDesc();
            dataSet[12] = csvEntity.getLongDesc();
            dataSet[13] = csvEntity.getAllAttributes();

            result.add(dataSet);
        });

        return result;
    }

    void saveToFile( List<String[]> data){
        File file = new File(FILE_PATH);

        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter with '^' as separator
            CSVWriter writer = new CSVWriter(outputfile, '^',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);

            // create a List which contains String array
            writer.writeAll(data);

            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
