package importer.export;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ExcelExporter {
    private static final String FILE_PATH = "src\\main\\resources\\table.xls";
    private static final Logger logger = LogManager.getLogger(ExcelExporter.class.getName());
    private Workbook workbook;
    private Sheet sheet;
    private int rowCounter=0;
    private int cellCounter=0;
    private int overload = 0;
    private int itemsOverLoaded = 0;
    private int itemsOverLoadedOver200k = 0;
    private int maxOverload = 0;


    void exportToExcel(List<ExportEntity> entityList)  {
         Instant start = Instant.now();
          setFirstRow();
          entityList.forEach(entity -> {
              setCommonRowPart(entity);
              setFits(entity);
            //  cellCounter=0;
             // rowCounter++;
          });
          saveToFile();
        Instant end = Instant.now();
        logger.info ("saved excel table in " + Duration.between(start, end));
    }

    private void setCommonRowPart(ExportEntity entity) {
        Row currentRow = sheet.getRow(rowCounter);
        if (currentRow==null){
            currentRow = sheet.createRow(rowCounter);
        }
        Cell currentCell = currentRow.createCell(cellCounter++);
        currentCell.setCellValue(entity.getPartNo());
        currentCell = currentRow.createCell(cellCounter++);
        currentCell.setCellValue(entity.getItemType());
        currentCell = currentRow.createCell(cellCounter++);
        currentCell.setCellValue(entity.getBrand());
        currentCell = currentRow.createCell(cellCounter++);
        currentCell.setCellValue(entity.getExtendedLength());
        currentCell = currentRow.createCell(cellCounter++);
        currentCell.setCellValue(entity.getCollapsedLength());
        currentCell = currentRow.createCell(cellCounter++);
        currentCell.setCellValue(entity.getUpperMount());
        currentCell = currentRow.createCell(cellCounter++);
        currentCell.setCellValue(entity.getLowerMount());
        //   currentRow.createCell(cellCounter++);
        currentCell = currentRow.createCell(cellCounter++);
        currentCell.setCellValue(entity.getItemAttributes());
        currentCell = currentRow.createCell(cellCounter++);
        currentCell.setCellValue(entity.getCarCategories());
    }

    private void setFits(ExportEntity entity) {
        List<ExportFitEntity> fitEntities = entity.getFitEntities();
        Row currentRow = sheet.getRow(rowCounter++);

        int currentCellCounter = cellCounter;
        for (ExportFitEntity fitEntity : fitEntities) {
            Cell currentCell = currentRow.createCell(cellCounter++);
            currentCell.setCellValue(fitEntity.getLiftStart());
            currentCell = currentRow.createCell(cellCounter++);
            currentCell.setCellValue(fitEntity.getLiftFinish());
            currentCell = currentRow.createCell(cellCounter++);
            currentCell.setCellValue(fitEntity.getPosition());
            currentCell = currentRow.createCell(cellCounter++);
            currentCell.setCellValue(fitEntity.getCarMake());
            currentCell = currentRow.createCell(cellCounter++);
            currentCell.setCellValue(fitEntity.getCarModel());
            currentCell = currentRow.createCell(cellCounter++);
            currentCell.setCellValue(fitEntity.getCarSubModel());
            currentCell = currentRow.createCell(cellCounter++);
            currentCell.setCellValue(fitEntity.getDrive());
            currentCell = currentRow.createCell(cellCounter++);
            currentCell.setCellValue(fitEntity.getYearStart());
            currentCell = currentRow.createCell(cellCounter++);
            currentCell.setCellValue(fitEntity.getYearFinish());
            currentCell = currentRow.createCell(cellCounter++);
            currentCell.setCellValue(fitEntity.getAllOtherFitAttributes());
            currentCell = currentRow.createCell(cellCounter++);
            currentCell.setCellValue(fitEntity.getAllOtherFitAttributes());
            currentCell = currentRow.createCell(cellCounter++);
            currentCell.setCellValue(fitEntity.getCarLiftAttributes());
            currentCell = currentRow.createCell(cellCounter++);
            currentCell.setCellValue(fitEntity.getCarYearAttributes());

            currentRow = sheet.createRow(rowCounter++);
            cellCounter = currentCellCounter;
        }
        cellCounter = 0;
        rowCounter--;
       // rowCounter++;
    }


    private void setFirstRow() {
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(cellCounter++);
        setCommonFieldsFirstRow(row, cell);
        setFitFirstRow(row, cell);

        cellCounter = 0;
        rowCounter = 1;
    }

    private void setFitFirstRow(Row row, Cell cell) {
        cell.setCellValue("Lift_Start");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Lift_Finish");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Position");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Car_Make");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Car_Model");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Car_SubModel");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Car_Drive");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Year_Start");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Year_Finish");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Fit_Attributes");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Car_Attributes");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Car_Lift_Attributes");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Car_Year_Attributes");
    }

    private void setCommonFieldsFirstRow(Row row, Cell cell) {
        cell.setCellValue("Item_SKU");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Item_Type");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Item_Manufacturer");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Item_ExtendedLength");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Item_CollapsedLength");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Item_UpperMount");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Item_LowerMount");
        cell = row.createCell(cellCounter++);/*
        cell.setCellValue("Item_Features");
        cell = row.createCell(cellCounter++);*/
        cell.setCellValue("Item_Attributes");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Categories");
        cell = row.createCell(cellCounter++);
    }

    private void saveToFile() {
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(FILE_PATH);
            workbook.write(fileOut);
            fileOut.close();

            // Closing the workbook
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void savetocsv(){
            try {
                // create a writer
                Writer writer = Files.newBufferedWriter(Paths.get("src\\main\\resources\\book_new3.csv"));

                // write CSV file
                CSVPrinter printer = CSVFormat.DEFAULT.withDelimiter(';').withHeader("ID", "Name", "Program", "University").print(writer);

                printer.printRecord(1, "John Mike", "Engineering", "MIT");
                printer.printRecord(2, "Jovan Krovoski", "Medical", "Harvard");
                printer.printRecord(3, "Lando Mata", "Computer Science", "TU Berlin");
                printer.printRecord(4, "Emma Ali", "Mathematics", "Oxford");

                // flush the stream
                printer.flush();

                // close the writer
                writer.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    ExcelExporter() {
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet();
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public void exportToCSV(List<ExportCSVEntity> csvExportList) {
        Instant start = Instant.now();
        setFirstRowCSV();
        csvExportList.forEach(exportCSVEntity -> {
            ExportEntity exportEntity = exportCSVEntity.getExportEntity();
            setCommonRowPart(exportEntity);
            setCSVrowPart(exportCSVEntity);
        });
        saveToFile();
        Instant end = Instant.now();
        logger.info ("saved excel CSV table in " + Duration.between(start, end));
        logger.info("Total overload " + overload);
        logger.info("Medium overload " + overload/itemsOverLoaded);
        logger.info("Max overload " + maxOverload);
        logger.info("Total overloaded items over 200k " + itemsOverLoadedOver200k);
    }

    private void setCSVrowPart(ExportCSVEntity exportCSVEntity) {
        Row currentRow = sheet.getRow(rowCounter);
        Cell currentCell = currentRow.createCell(cellCounter++);
        currentCell.setCellValue(exportCSVEntity.getTitle());
        currentCell = currentRow.createCell(cellCounter++);
        String short_desc = exportCSVEntity.getShortDesc();
        if (short_desc.length()>32767){
            short_desc = "";
        }
        currentCell.setCellValue(short_desc);
        currentCell = currentRow.createCell(cellCounter++);
        String long_desc = exportCSVEntity.getShortDesc();
        if (long_desc.length()>32767){
            long_desc = "";
        }
        currentCell.setCellValue(long_desc);
        currentCell = currentRow.createCell(cellCounter++);
        String attributes = exportCSVEntity.getAllAttributes();
        if (attributes.length()>32767){
            int curOver = attributes.length()-32767;
            if (curOver>200000){
                itemsOverLoadedOver200k++;
            }
            if (maxOverload<curOver){
                maxOverload = curOver;
            }
            overload = overload+curOver;
            itemsOverLoaded++;
            attributes = attributes.substring(0, 32767);
           // attributes = "";
        }
        currentCell.setCellValue(attributes);

        rowCounter++;
        cellCounter = 0;
    }

    private void setFirstRowCSV() {
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(cellCounter++);
        setCommonFieldsFirstRow(row, cell);
        setCSVfirstRowFields(row, cell);
        cellCounter = 0;
        rowCounter = 1;
    }

    private void setCSVfirstRowFields(Row row, Cell cell) {
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Title");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Short Description");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Long Description");
        cell = row.createCell(cellCounter++);
        cell.setCellValue("Attributes");
    }
}
