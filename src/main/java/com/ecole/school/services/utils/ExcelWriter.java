package com.ecole.school.services.utils;

import com.ecole.school.web.POJO.RecapProgrammeModule;
import com.ecole.school.web.POJO.RecapReferentiel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ExcelWriter {

    // generate an excel file model for uploading programs
    public void generateReferentielUploadModel() throws IOException {

        List<String> headers = Arrays.asList("Périodes", "Unité d'enseignement",
                "Intitulé EC", "Code Ec", "Coef", "CM", "TP", "TD", "VHP", "TPE", "VHT", "Credit");

        // define the workbook
        Workbook workbook = new XSSFWorkbook();

        // create a sheet with the workbook and set a name for it
        Sheet sheet = workbook.createSheet("Dev Web & Mobile S1 & S2");

        // create a row at the top of the sheet
        Row title = sheet.createRow(0);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.size()));

        // define cell style for title
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);

        // define font for titles style
        XSSFFont titleFont = ((XSSFWorkbook) workbook).createFont();
        titleFont.setFontName("Arial");
        titleFont.setFontHeightInPoints((short) 14);
        titleFont.setBold(true);

        // set font to titles style
        titleStyle.setFont(titleFont);

        // create cell for title and assign it style
        Cell titleCell = title.createCell(0);
        titleCell.setCellValue("Semestre 1 : Dev Web & Mobile");
        titleCell.setCellStyle(titleStyle);

        // define headers of the table
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // define font for titles style
        XSSFFont headerFont = ((XSSFWorkbook) workbook).createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(1);
        Cell headerCell;
        for (String head : headers) {
            headerCell = headerRow.createCell(headers.indexOf(head));
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue(head);
            sheet.autoSizeColumn(headers.indexOf(head));
        }

        // define content of the table
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        // define font for titles style
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);

        Row row = sheet.createRow(2);
        Cell periodeCell = row.createCell(0);
        periodeCell.setCellValue("Semestre 1");
        periodeCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress
                (2, 10, 0, 0));

        Cell ueCell = row.createCell(1);
        ueCell.setCellValue("OMA 1101");
        ueCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress
                (2, 4, 1, 1));
        Cell creditCell = row.createCell(11);
        creditCell.setCellValue(50);
        creditCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress
                (2, 4, 11, 11));

        Cell ecCell = row.createCell(2);
        ecCell.setCellValue("Algorithme 1");
        ecCell.setCellStyle(style);
        Cell coefCell = row.createCell(3);
        coefCell.setCellValue("1OMA 1101");
        coefCell.setCellStyle(style);

        Row row1 = sheet.createRow(3);
        ecCell = row1.createCell(2);
        ecCell.setCellValue("Mathematique 1");
        ecCell.setCellStyle(style);
        coefCell = row1.createCell(3);
        coefCell.setCellValue("2OMA 1101");
        coefCell.setCellStyle(style);


        Row row2 = sheet.createRow(4);
        ecCell = row2.createCell(2);
        ecCell.setCellValue("Programmation web 1");
        ecCell.setCellStyle(style);
        coefCell = row2.createCell(3);
        coefCell.setCellValue("3OMA 1101");
        coefCell.setCellStyle(style);

        Row row3 = sheet.createRow(5);
        Cell totalCell = row3.createCell(1);
        totalCell.setCellStyle(style);
        totalCell.setCellValue("Total OMA 1101");
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 1,
                2));


        /*Row row4 = sheet.createRow(6);
        ueCell = row4.createCell(1);
        ueCell.setCellValue("CGE 1102");
        ueCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress
                (5, 8, 1, 1));
*/

        /*List<RecapReferentiel> recapReferentiels = new ArrayList<>();
        int cpt = 2;
        for (RecapReferentiel recap : recapReferentiels) {
            Row row = sheet.createRow(cpt);

            Cell periodeCell = row.createCell(0);
            periodeCell.setCellValue(recap.getSemestre().getLibelle());
            sheet.addMergedRegion(new CellRangeAddress
                    (cpt, recap.getRecapProgrammeModules().size(), 0, 0));

            for (RecapProgrammeModule rp : recap.getRecapProgrammeModules()) {
                Cell ueCell = row.createCell(1);
                ueCell.setCellValue(rp.getProgrammeUE().getCode());
            }

            cpt++;
        }*/

        // write content into a file and save it in a directory
        String dossier = System.getProperty("user.home") + "/ecole221files/referentiel/model/";
        File chemin = new File(dossier);
        if (!chemin.exists()) {
            try {
                if (chemin.mkdirs())
                    System.out.println("directory created successfully");
                else System.out.println("directory was not created");
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        String path = chemin.getAbsolutePath();
        String fileLocation = path + "/Referentiel_upload_Model.xlsx";
        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
