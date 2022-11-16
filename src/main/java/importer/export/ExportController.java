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

public class ExportController {
    private static final Logger logger = LogManager.getLogger(ExportController.class.getName());

    public void exportToExcel(String brand) throws IOException {
        List<ExportEntity> entityList = new ExportService().getExportEntities(brand);
   //     List<ExportEntity> entityList = new ArrayList<>();
        new ExcelExporter().exportToExcel(entityList);
    }

    public void exportToCSV(String brand){
        Instant start = Instant.now();
        ExportService service = new ExportService();
        List<ExportEntity> entityList = service.getExportEntities(brand);
        List<ExportCSVEntity> csvExportList = new ExportEntityToCSVConverter().getCSVEntityList(entityList);
       // new ExcelExporter().exportToCSV(csvExportList);
        new CSVExporter().exportToCSV(csvExportList);
        Instant end = Instant.now();
        logger.info ("whole process took " + Duration.between(start, end));
    }

    public void testCSVexport (){
        File file = new File("src\\main\\resources\\export.csv");

        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter with '|' as separator
            CSVWriter writer = new CSVWriter(outputfile, '|',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);

            // create a List which contains String array
            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[] { "Name", "Class", "Marks" });
            data.add(new String[] { "Aman", "10", "620" });
            data.add(new String[] { "Suraj", "10", "630" });
            writer.writeAll(data);

            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
