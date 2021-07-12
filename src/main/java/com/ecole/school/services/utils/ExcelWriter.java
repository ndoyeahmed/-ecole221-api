package com.ecole.school.services.utils;

import com.ecole.school.models.*;
import com.ecole.school.models.Module;
import com.ecole.school.services.parametrages.ParametrageModuleUEService;
import com.ecole.school.services.parametrages.ParametrageReferentielService;
import com.ecole.school.services.parametrages.ParametrageSpecialiteService;
import com.ecole.school.web.POJO.RecapProgrammeModule;
import com.ecole.school.web.POJO.RecapReferentiel;
import com.ecole.school.web.POJO.ReferentielUploadRecap;
import lombok.extern.java.Log;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Log
@Component
public class ExcelWriter {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private ParametrageSpecialiteService parametrageSpecialiteService;
    private ParametrageReferentielService parametrageReferentielService;
    private ParametrageModuleUEService parametrageModuleUEService;

    public ExcelWriter(ParametrageModuleUEService parametrageModuleUEService, ParametrageSpecialiteService parametrageSpecialiteService,
                       ParametrageReferentielService parametrageReferentielService) {
        this.parametrageModuleUEService = parametrageModuleUEService;
        this.parametrageReferentielService = parametrageReferentielService;
        this.parametrageSpecialiteService = parametrageSpecialiteService;
    }

    /**
     * generate an excel file model for uploading programs
     * @throws IOException e
     */
    public void generateReferentielUploadModel() throws IOException {

        List<String> headers = Arrays.asList("Périodes", "Unité d'enseignement",
                "Intitulé EC", "Coef", "CM", "TP", "TD", "VHP", "TPE", "VHT", "Credit");

        // define the workbook
        Workbook workbook = new XSSFWorkbook();

        // create a sheet with the workbook and set a name for it
        Sheet sheet = workbook.createSheet("Dev Web & Mobile S1 & S2");

        Row ref = sheet.createRow(0);
        Cell refCell = ref.createCell(0);
        refCell.setCellValue("Code Niveau:");

        refCell = ref.createCell(2);
        refCell.setCellValue("Code Specialite:");

        refCell = ref.createCell(4);
        refCell.setCellValue("Annee:");

        refCell = ref.createCell(6);
        refCell.setCellValue("Credit:");

        refCell = ref.createCell(8);
        refCell.setCellValue("VHT:");

        refCell = ref.createCell(10);
        refCell.setCellValue("Description:");


        // create a row
        Row title = sheet.createRow(1);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, headers.size()-1));

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
        titleCell.setCellValue("Programme");
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

        Row headerRow = sheet.createRow(2);
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

        Row row = sheet.createRow(3);
        Cell periodeCell = row.createCell(0);
        periodeCell.setCellValue("Semestre 1");
        periodeCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress
                (3, 11, 0, 0));

        Cell ueCell = row.createCell(1);
        ueCell.setCellValue("Connaissance général");
        ueCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress
                (3, 5, 1, 1));
        Cell creditCell = row.createCell(10);
        creditCell.setCellValue(50);
        creditCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress
                (3, 5, 10, 10));

        Cell ecCell = row.createCell(2);
        ecCell.setCellValue("Algorithme 1");
        ecCell.setCellStyle(style);

        Row row1 = sheet.createRow(4);
        ecCell = row1.createCell(2);
        ecCell.setCellValue("Mathematique 1");
        ecCell.setCellStyle(style);

        Row row2 = sheet.createRow(5);
        ecCell = row2.createCell(2);
        ecCell.setCellValue("Programmation web 1");
        ecCell.setCellStyle(style);


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


    /**
     * Verify if it's an excel file or not
     * @param file to test
     * @return boolean true if it's an excel file false if not
     */
    public boolean hasExcelFormat(MultipartFile file) {

        return TYPE.equals(file.getContentType());
    }

    /**
     * Convert excel file to list recap referentiel
     * @param is excel InputStream
     * @return list recap referentiel
     */
    public ReferentielUploadRecap excelToRecapReferentiel(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            Referentiel referentiel = null;
            Semestre semestre = null;
            UE ue;
            Module module;
            ProgrammeUE programmeUE = new ProgrammeUE();
            RecapReferentiel recapReferentiel = new RecapReferentiel();
            List<RecapProgrammeModule> recapProgrammeModuleList = new ArrayList<>();
            ProgrammeModule programmeModule;
            List<ProgrammeModule> programmeModuleList = new ArrayList<>();
            List<RecapReferentiel> recapReferentielList = new ArrayList<>();
            List<String> errorList = new ArrayList<>();
            int cpt = 0;
            while (sheetIterator.hasNext()) {
                Sheet sheet = sheetIterator.next();

                Iterator<Row> rows = sheet.iterator();
                int rowNumber = 0;
                // reinitialise isNewSemestre and isNewProgrammeUe
                boolean isNewSemestre = false;
                boolean isNewProgrammeUE = false;
                RecapProgrammeModule recapProgrammeModule = new RecapProgrammeModule();


                while (rows.hasNext()) {
                    Row currentRow = rows.next();
                    if (rowNumber == 0) {
                         // Operations on the first row to get the referentiel object value
                        Iterator<Cell> cellsInRow = currentRow.iterator();
                        int cellIdx = 0;
                        referentiel = new Referentiel();
                        while (cellsInRow.hasNext()) {
                            Cell currentCell = cellsInRow.next();
                            switch (cellIdx) {
                                case 1:
                                    if (currentCell != null && !currentCell.getStringCellValue().equals("")) {
                                        Niveau niveau = parametrageSpecialiteService.findNiveauByLibelle(currentCell.getStringCellValue());
                                        if (niveau != null) {
                                            referentiel.setNiveau(niveau);
                                        } else {
                                            errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + ", niveau incorrect");
                                        }
                                    } else {
                                        errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " est obligatoire");
                                    }
                                    break;
                                case 3:
                                    if (currentCell != null && !currentCell.getStringCellValue().equals("")) {
                                        Specialite specialite = parametrageSpecialiteService.findSpecialiteByLibelle(currentCell.getStringCellValue());
                                        if (specialite != null) {
                                            referentiel.setSpecialite(specialite);
                                        } else {
                                            errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + ", spécialité incorrect");
                                        }
                                    } else {
                                        errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " est obligatoire");
                                    }
                                    break;
                                case 5:
                                    if (currentCell != null) {
                                        try {
                                            referentiel.setAnnee((int) currentCell.getNumericCellValue());
                                        } catch (Exception e) {
                                            errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " saisir une valeur numérique");
                                            log.severe(e.getMessage());
                                        }
                                    } else {
                                        errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " est obligatoire");
                                    }
                                    break;
                                case 7:
                                    if (currentCell != null) {
                                        try {
                                            referentiel.setCredit((int) currentCell.getNumericCellValue());
                                        } catch (Exception e) {
                                            errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " saisir une valeur numérique");
                                            log.severe(e.getMessage());
                                        }
                                    } else {
                                        errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " est obligatoire");
                                    }
                                    break;
                                case 9:
                                    if (currentCell != null) {
                                        try {
                                            referentiel.setVolumeHeureTotal((int) currentCell.getNumericCellValue());
                                        } catch (Exception e) {
                                            errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " saisir une valeur numérique");
                                            log.severe(e.getMessage());
                                        }
                                    } else {
                                        errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " est obligatoire");
                                    }
                                    break;
                                case 11:
                                    if (currentCell != null && !currentCell.getStringCellValue().equals("")) {
                                        referentiel.setDescription(currentCell.getStringCellValue());
                                    } else errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " est obligatoire");
                                    break;
                                default:
                                    break;
                            }
                            cellIdx++;
                        }
                        rowNumber++;
                    } else {
                        if (rowNumber != 1 && rowNumber != 2) {
                            // get list cell of the current row
                            Iterator<Cell> cellsInRow = currentRow.iterator();

                            int cellIdx = 0;
                            cpt++;
                            programmeModule = new ProgrammeModule();
                            while (cellsInRow.hasNext()) {
                                Cell currentCell = cellsInRow.next();
                                switch (cellIdx) {
                                    case 0:
                                        if (currentCell != null && !currentCell.getStringCellValue().equals("")) {
                                            isNewSemestre = true;
                                            semestre = parametrageSpecialiteService
                                                    .findSemestreByLibelle(currentCell.getStringCellValue());
                                            if (semestre != null) {
                                                programmeUE.setSemestre(semestre);
                                            }  else {
                                                errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " semestre syntaxe incorrecte");
                                            }
                                        } else {
                                            isNewSemestre = false;
                                        }
                                        break;
                                    case 1:
                                        if (currentCell != null && !currentCell.getStringCellValue().equals("")) {
                                            isNewProgrammeUE = true;
                                            ue = parametrageModuleUEService
                                                    .findUEByCode(currentCell.getStringCellValue());
                                            if (ue != null) {
                                                programmeUE.setUe(ue);
                                            }  errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " UE syntaxe incorrecte");
                                        } else {
                                            isNewProgrammeUE = false;
                                        }
                                        break;
                                    case 2:
                                        if (currentCell != null && currentCell.getStringCellValue() != null) {
                                            module = parametrageModuleUEService
                                                    .findModuleByLibelle(currentCell.getStringCellValue());
                                            if (module != null) {
                                                programmeModule.setModule(module);
                                            } else {
                                                errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " module syntaxe incorrecte");
                                            }
                                        } else  errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " module obligatoire");
                                        break;
                                    case 3:
                                        try {
                                            if (currentCell != null && currentCell.getNumericCellValue() != 0) {
                                                programmeModule.setCoef((int) currentCell.getNumericCellValue());
                                            } else errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " coef obligatoire");
                                        } catch (Exception e) {
                                            errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " saisir un entier");
                                            log.severe(e.getMessage());
                                        }
                                        break;
                                    case 4:
                                        try {
                                            if (currentCell != null && currentCell.getNumericCellValue() != 0) {
                                                programmeModule.setCm((int) currentCell.getNumericCellValue());
                                            } else errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " CM obligatoire");
                                        } catch (Exception e) {
                                            errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " saisir un entier");
                                            log.severe(e.getMessage());
                                        }
                                        break;
                                    case 5:
                                        try {
                                            if (currentCell != null && currentCell.getNumericCellValue() != 0) {
                                                programmeModule.setTp((int) currentCell.getNumericCellValue());
                                            } else errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " TP obligatoire");
                                        } catch (Exception e) {
                                            errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " saisir un entier");
                                            log.severe(e.getMessage());
                                        }
                                        break;
                                    case 6:
                                        try {
                                            if (currentCell != null && currentCell.getNumericCellValue() != 0) {
                                                programmeModule.setTd((int) currentCell.getNumericCellValue());
                                            } else errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " TD obligatoire");
                                        } catch (Exception e) {
                                            errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " saisir un entier");
                                            log.severe(e.getMessage());
                                        }
                                        break;
                                    case 7:
                                        try {
                                            if (currentCell != null && currentCell.getNumericCellValue() != 0) {
                                                programmeModule.setVhp((int) currentCell.getNumericCellValue());
                                            } else errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " VHP obligatoire");
                                        } catch (Exception e) {
                                            errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " saisir un entier");
                                            log.severe(e.getMessage());
                                        }
                                        break;
                                    case 8:
                                        try {
                                            if (currentCell != null && currentCell.getNumericCellValue() != 0) {
                                                programmeModule.setTpe((int) currentCell.getNumericCellValue());
                                            } else errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " TPE obligatoire");
                                        } catch (Exception e) {
                                            errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " saisir un entier");
                                            log.severe(e.getMessage());
                                        }
                                        break;
                                    case 9:
                                        try {
                                            if (currentCell != null && currentCell.getNumericCellValue() != 0) {
                                                programmeModule.setVht((int) currentCell.getNumericCellValue());
                                            } else errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " VHT obligatoire");
                                        } catch (Exception e) {
                                            errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " saisir un entier");
                                            log.severe(e.getMessage());
                                        }
                                        break;
                                    case 10:
                                        try {
                                            if (currentCell != null && currentCell.getNumericCellValue() != 0) {
                                                programmeModule.setNbreCreditModule((int) currentCell.getNumericCellValue());
                                            } else {
                                                programmeModule.setNbreCreditModule(0);
                                            }
                                        } catch (Exception e) {
                                            errorList.add("Ligne " + (rowNumber+1) + " colonne " + (cellIdx+1) + " saisir un entier");
                                            log.severe(e.getMessage());
                                        }
                                        break;
                                    default:
                                        break;

                                }
                                cellIdx++;
                            }

                            if (isNewSemestre && isNewProgrammeUE && cpt == 1) {
                                programmeModuleList.add(programmeModule);
                                recapProgrammeModule.setProgrammeUE(programmeUE);
                                recapProgrammeModule.setProgrammeModuleList(programmeModuleList);

                                recapReferentiel.setSemestre(semestre);
                            } else if (isNewSemestre && isNewProgrammeUE && cpt > 1) {
                                // recap ref list
                                recapProgrammeModule.setProgrammeModuleList(programmeModuleList);
                                recapProgrammeModuleList.add(recapProgrammeModule);
                                recapReferentiel.setListRecapProgrammeModule(recapProgrammeModuleList);
                                recapReferentielList.add(recapReferentiel);

                                // recap programme module
                                recapReferentiel = new RecapReferentiel();
                                recapProgrammeModule = new RecapProgrammeModule();
                                programmeModuleList = new ArrayList<>();
                                recapProgrammeModuleList = new ArrayList<>();
                                programmeModuleList.add(programmeModule);

                                recapProgrammeModule.setProgrammeUE(programmeUE);
                                recapProgrammeModule.setProgrammeModuleList(programmeModuleList);

                                recapReferentiel.setSemestre(semestre);
                            } else if (!isNewSemestre && isNewProgrammeUE && cpt > 1) {
                                recapProgrammeModule.setProgrammeModuleList(programmeModuleList);
                                recapProgrammeModuleList.add(recapProgrammeModule);
                                recapProgrammeModule = new RecapProgrammeModule();
                                programmeModuleList = new ArrayList<>();
                                programmeModuleList.add(programmeModule);

                                recapProgrammeModule.setProgrammeUE(programmeUE);
                                recapProgrammeModule.setProgrammeModuleList(programmeModuleList);
                            } else if (!isNewSemestre && !isNewProgrammeUE && cpt > 1) {
                                programmeModuleList.add(programmeModule);
                            }
                        }
                        rowNumber++;
                    }
                }
                recapProgrammeModule.setProgrammeModuleList(programmeModuleList);
                recapProgrammeModuleList.add(recapProgrammeModule);
                recapReferentiel.setListRecapProgrammeModule(recapProgrammeModuleList);
                recapReferentielList.add(recapReferentiel);
            }

            workbook.close();

            ReferentielUploadRecap referentielUploadRecap = new ReferentielUploadRecap();
            referentielUploadRecap.setReferentiel(referentiel);
            referentielUploadRecap.setErrorList(errorList);
            referentielUploadRecap.setRecapReferentielList(recapReferentielList);
            return referentielUploadRecap;
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

}
