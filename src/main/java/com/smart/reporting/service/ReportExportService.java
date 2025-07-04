package com.smart.reporting.service;

import com.smart.reporting.entity.TEvent;
import com.smart.reporting.entity.TEventCat;
import com.smart.reporting.entity.TOrg;
import com.smart.reporting.entity.TResults;
import com.smart.reporting.util.TimeFormatUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ReportExportService {

    public byte[] generateResultsXlsx(
            List<TResults> results,
            List<TEventCat> eventcat,
            TEvent event,
            Optional<TOrg> orgs,
            String sheetName
    ) throws Exception {
        String cplist = eventcat.get(0).getCplist();
        String catName = eventcat.get(0).getCategory();
        String eventNm = event.getName();
        String eventLoc = event.getLocation();
        String eventdt = event.getEventDt().toString();
        String eventWeather = event.getWeather();
        String orgName = orgs.map(TOrg::getAlias).orElse("MyPaceTracker");
        String eventName = eventNm;
        String eventDate = eventdt;
        String title = "Official Result";
        String location = "Location : " + eventLoc;
        String weather = "Weather : " + eventWeather;

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(sheetName);

            // Styles (same as before)
            CellStyle r1Style = workbook.createCellStyle();
            Font r1Font = workbook.createFont();
            r1Font.setBold(true);
            r1Font.setFontHeightInPoints((short) 18);
            r1Font.setFontName("Aptos Narrow");
            r1Style.setFont(r1Font);

            CellStyle headingStyle = workbook.createCellStyle();
            Font headingFont = workbook.createFont();
            headingFont.setBold(false);
            headingFont.setFontHeightInPoints((short) 14);
            headingFont.setFontName("Aptos Narrow");
            headingStyle.setFont(headingFont);

            CellStyle catStyle = workbook.createCellStyle();
            Font catFont = workbook.createFont();
            catFont.setFontHeightInPoints((short) 11);
            catFont.setFontName("Aptos Narrow");
            catFont.setBold(true);
            catStyle.setFont(catFont);

            int rowIdx = 0;

            // Header rows
            Row row1 = sheet.createRow(rowIdx++);
            Cell cell = row1.createCell(0);
            cell.setCellValue(eventName);
            cell.setCellStyle(r1Style);

            Row row2 = sheet.createRow(rowIdx++);
            Cell cell2 = row2.createCell(0);
            cell2.setCellValue(eventDate);
            cell2.setCellStyle(headingStyle);

            Row row3 = sheet.createRow(rowIdx++);
            Cell cell3 = row3.createCell(0);
            cell3.setCellValue(title);
            cell3.setCellStyle(headingStyle);

            Row row4 = sheet.createRow(rowIdx++);
            Cell cell4 = row4.createCell(0);
            cell4.setCellValue(location);
            cell4.setCellStyle(headingStyle);

            Row row5 = sheet.createRow(rowIdx++);
            Cell cell5 = row5.createCell(0);
            cell5.setCellValue(weather);
            cell5.setCellStyle(headingStyle);

            rowIdx++; // Empty row

            Row row7 = sheet.createRow(rowIdx++);
            Cell cell7 = row7.createCell(0);
            cell7.setCellValue(catName);
            cell7.setCellStyle(catStyle);

            // Field header
            Row field = sheet.createRow(rowIdx++);
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLACK.getIndex());
            headerFont.setFontHeightInPoints((short) 11);
            headerFont.setFontName("Aptos Narrow");
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            field.createCell(0).setCellValue("Rank1Cat");
            field.getCell(0).setCellStyle(headerStyle);
            field.createCell(1).setCellValue("Bib");
            field.getCell(1).setCellStyle(headerStyle);
            field.createCell(2).setCellValue("Name");
            field.getCell(2).setCellStyle(headerStyle);
            field.createCell(3).setCellValue("OfficialTime");
            field.getCell(3).setCellStyle(headerStyle);
            field.createCell(4).setCellValue("NetTime");
            field.getCell(4).setCellStyle(headerStyle);
            field.createCell(5).setCellValue("TimeStart");
            field.getCell(5).setCellStyle(headerStyle);

            int colIdx = 6;
            if (cplist != null && !cplist.isEmpty()) {
                String[] cps = cplist.split(",");
                for (String cp : cps) {
                    field.createCell(colIdx).setCellValue(cp.trim());
                    field.getCell(colIdx).setCellStyle(headerStyle);
                    colIdx++;
                }
            }
            field.createCell(colIdx).setCellValue("TimeFinish");
            field.getCell(colIdx).setCellStyle(headerStyle);

            // Data row styles
            CellStyle dataStyleDefault = workbook.createCellStyle();
            Font dataFontDefault = workbook.createFont();
            dataFontDefault.setFontName("Aptos Narrow");
            dataFontDefault.setFontHeightInPoints((short) 11);
            dataStyleDefault.setFont(dataFontDefault);

            CellStyle dataStyleAlt = workbook.createCellStyle();
            Font dataFontAlt = workbook.createFont();
            dataFontAlt.setFontName("Aptos Narrow");
            dataFontAlt.setFontHeightInPoints((short) 11);
            dataFontAlt.setColor(IndexedColors.BLACK.getIndex());
            dataStyleAlt.setFont(dataFontAlt);
            dataStyleAlt.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            dataStyleAlt.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Data rows
            for (TResults result : results) {
                Row row = sheet.createRow(rowIdx++);
                int dataColIdx = 0;
                CellStyle rowStyle = (rowIdx % 2 == 0) ? dataStyleAlt : dataStyleDefault;

                Cell c0 = row.createCell(dataColIdx++);
                c0.setCellValue(result.getRank1cat());
                c0.setCellStyle(rowStyle);

                Cell c1 = row.createCell(dataColIdx++);
                c1.setCellValue(result.getBib());
                c1.setCellStyle(rowStyle);

                Cell c2 = row.createCell(dataColIdx++);
                c2.setCellValue(result.getName());
                c2.setCellStyle(rowStyle);

                Cell c3 = row.createCell(dataColIdx++);
                c3.setCellValue(TimeFormatUtil.intToTimeString(result.getTimefinish() - result.getTimegun()));
                c3.setCellStyle(rowStyle);

                Cell c4 = row.createCell(dataColIdx++);
                c4.setCellValue(TimeFormatUtil.intToTimeString(result.getTimefinish() - result.getTimestart()));
                c4.setCellStyle(rowStyle);

                Cell c5 = row.createCell(dataColIdx++);
                c5.setCellValue(TimeFormatUtil.intToTimeString(result.getTimestart()));
                c5.setCellStyle(rowStyle);

                if (cplist != null && !cplist.isEmpty()) {
                    String[] cps = cplist.split(",");
                    for (String cp : cps) {
                        String cpTrim = cp.trim();
                        Integer cpValue = null;
                        switch (cpTrim) {
                            case "TimeCP1": cpValue = result.getTimecp1(); break;
                            case "TimeCP2": cpValue = result.getTimecp2(); break;
                            case "TimeCP3": cpValue = result.getTimecp3(); break;
                            case "TimeCP4": cpValue = result.getTimecp4(); break;
                            case "TimeCP5": cpValue = result.getTimecp5(); break;
                            case "TimeCP6": cpValue = result.getTimecp6(); break;
                            case "TimeCP7": cpValue = result.getTimecp7(); break;
                            case "TimeCP8": cpValue = result.getTimecp8(); break;
                            case "TimeCP9": cpValue = result.getTimecp9(); break;
                            case "TimeCP10": cpValue = result.getTimecp10(); break;
                        }
                        Cell cpCell = row.createCell(dataColIdx++);
                        cpCell.setCellValue(TimeFormatUtil.intToTimeString(cpValue != null ? cpValue - result.getTimegun() : null));
                        cpCell.setCellStyle(rowStyle);
                    }
                }

                Cell finishCell = row.createCell(dataColIdx);
                finishCell.setCellValue(TimeFormatUtil.intToTimeString(result.getTimefinish()));
                finishCell.setCellStyle(rowStyle);
            }

            // Footer row
            Row footerRow = sheet.createRow(rowIdx++);
            Cell footerCell = footerRow.createCell(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss.SS a");
            String formattedNow = LocalDateTime.now().format(formatter);
            String footerText = "Timing & Results by " + orgName + "; Printed at " + formattedNow;
            footerCell.setCellValue(footerText);

            CellStyle footerStyle = workbook.createCellStyle();
            Font footerFont = workbook.createFont();
            footerFont.setFontName("Aptos Narrow");
            footerFont.setFontHeightInPoints((short) 11);
            footerStyle.setFont(footerFont);
            footerCell.setCellStyle(footerStyle);

            int lastCol = field.getLastCellNum() - 1;
            if (lastCol > 0) {
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(
                        footerRow.getRowNum(), footerRow.getRowNum(), 0, lastCol
                ));
            }

            // Set column widths
            sheet.setColumnWidth(2, 300 * 36);
            int totalColumns = field.getLastCellNum();
            for (int i = 3; i < totalColumns; i++) {
                sheet.setColumnWidth(i, 100 * 36);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }
}