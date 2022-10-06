package importer.export;

import java.util.List;

public class ExportController {

    public void exportToCSV(String brand){
        List<ExportEntity> entityList = new ExportService().getExportEntities(brand);
    }
}
