package importer;

import importer.entities.CarMergeEntity;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ExcelUtil {
    public static Set<CarMergeEntity> getMergeInfoFromFile(String filePath) {
        Set<CarMergeEntity> result = new HashSet<>();
        Workbook workbook = getWorkBook(filePath);
        result = readWorkbook(workbook);

        return result;
    }

    private static Set<CarMergeEntity> readWorkbook(Workbook workbook) {
        Set<CarMergeEntity> result = new HashSet<>();
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row  = sheet.getRow(i);
            Set<CarMergeEntity> entities = getCarMergeEntityFromRow(row);
            if (!entities.isEmpty()){
                result.addAll(entities);
            }
        }

        return result;
    }

    private static Set<CarMergeEntity> getCarMergeEntityFromRow(Row row) {
        Set<CarMergeEntity> result = new HashSet<>();
        if (row.getCell(0)==null){
            return result; //in case if first cell is empty
        }
        CarMergeEntity rawEntity = getRawEntity(row); //get entity with no skyYear
        if (rawEntity==null){
            return result;
        }
        result = getFinalEntitiesFromRow(rawEntity, row);


        return result;
    }

    private static Set<CarMergeEntity> getFinalEntitiesFromRow(CarMergeEntity rawEntity, Row row) {
        Set<CarMergeEntity> result = new HashSet<>();
        int skyStart = Integer.parseInt(getCellValue(row.getCell(0)));
        int skyFinish = Integer.parseInt(getCellValue(row.getCell(1)));
        for (int i = skyStart; i <= skyFinish; i++) {
            CarMergeEntity entity = new CarMergeEntity(rawEntity);
            entity.setExcelYear(i);
            result.add(entity);
        }

        return result;
    }

    private static CarMergeEntity getRawEntity(Row row) {
        CarMergeEntity result = new CarMergeEntity();
        setSkyMake(row, result);
        setSkyModel(row, result);
        setProdStart(row, result);
        setProdFinish(row, result);
        setProdMake(row, result);
        setProdModel(row, result);
        setCarAttribute(row, result);
        setSubModel(row, result);
        setBody(row, result);
        if (!entityIsValid(result)){
            return null;
        }

        return result;
    }

    private static void setBody(Row row, CarMergeEntity entity) {
        Cell cell = row.getCell(10);
        String body = getCellValue(cell); //will return string value (will convert to string if necessary)
        entity.setProdBody(body);
    }

    private static void setSubModel(Row row, CarMergeEntity entity) {
        Cell cell = row.getCell(9);
        String subModel = getCellValue(cell); //will return string value (will convert to string if necessary)
        entity.setProdSubModel(subModel);
    }

    private static boolean entityIsValid(CarMergeEntity result) {
        if (result.getExcelMake()==null){
            return false;
        }
        if (result.getExcelModel()==null){
            return false;
        }
        if (result.getProdStart()==0){
            return false;
        }
        if (result.getProdFinish()==0){
            return false;
        }
        if (result.getProdMake()==null){
            return false;
        }
        return result.getProdModel() != null;

    }

    private static void setCarAttribute(Row row, CarMergeEntity entity) {
        Cell cell = row.getCell(8);
        String attribute = getCellValue(cell); //will return string value (will convert to string if necessary)
        entity.setProdCarAttribute(attribute);
    }

    private static void setProdModel(Row row, CarMergeEntity entity) {
        Cell cell = row.getCell(7);
        String model = getCellValue(cell); //will return string value (will convert to string if necessary)
        entity.setProdModel(model);
    }

    private static void setProdMake(Row row, CarMergeEntity entity) {
        Cell cell = row.getCell(6);
        String make = getCellValue(cell); //will return string value (will convert to string if necessary)
        entity.setProdMake(make);
    }

    private static void setProdFinish(Row row, CarMergeEntity entity) {
        Cell cell = row.getCell(5);
        String finish = getCellValue(cell); //will return string value (will convert to string if necessary)
        entity.setProdFinish(Integer.parseInt(finish));
    }

    private static void setProdStart(Row row, CarMergeEntity entity) {
        Cell cell = row.getCell(4);
        String start = getCellValue(cell); //will return string value (will convert to string if necessary)
        entity.setProdStart(Integer.parseInt(start));
    }

    private static void setSkyModel(Row row, CarMergeEntity entity) {
        Cell cell = row.getCell(3);
        String model = getCellValue(cell); //will return string value (will convert to string if necessary)
        model = model.trim();
        entity.setExcelModel(model);
    }

    private static void setSkyMake(Row row, CarMergeEntity entity) {
        Cell cell = row.getCell(2);
        String make = getCellValue(cell); //will return string value (will convert to string if necessary)
        entity.setExcelMake(make);
    }

    private static String getCellValue(Cell cell) {
        if (cell==null){
            return null;
        }
        String result = null;
       try {
           result = cell.getStringCellValue();
       }
       catch (Exception e){
           double d = cell.getNumericCellValue();
           int i = (int) d;
           result = i+"";
       }

        return result;
    }

    private static Workbook getWorkBook(String filePath) {
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(new File(filePath));
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return workbook;
    }
}
