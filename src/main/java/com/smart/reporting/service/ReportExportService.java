package com.smart.reporting.service;

import com.smart.reporting.dto.*;
import com.smart.reporting.entity.TEvent;
import com.smart.reporting.entity.TEventCat;
import com.smart.reporting.entity.TOrg;
import com.smart.reporting.entity.TResults;
import com.smart.reporting.repository.TEventRepository;
import com.smart.reporting.repository.TOrgRepository;
import com.smart.reporting.util.TimeFormatUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportExportService {

    @Autowired
    private StatisticReportService statisticReportService;

    @Autowired
    private TEventRepository eventRepository;

    @Autowired
    private TOrgRepository orgRepository;

    @Autowired
    private RaceResultService raceResultService;

    @Autowired
    private EventCatService eventCatService;

    public byte[] generateResultsXlsx(
            List<TResults> results,
            List<TEventCat> eventcat,
            TEvent event,
            Optional<TOrg> orgs,
            String sheetName
    ) throws Exception {
        String cplist = eventcat.get(0).getCplist();
        String raceMode = eventcat.get(0).getRacemode();
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
        boolean isLapMode = "LAP".equalsIgnoreCase(raceMode);

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
            
            // For LAP mode, generate lap columns based on cplist
            // For TIME mode, use cplist as checkpoint names
            if (isLapMode && cplist != null && !cplist.isEmpty()) {
                String[] cplistParts = cplist.split(",");
                int halflap = 0;
                int lapCount = 0;
                
                if (cplistParts.length >= 3) {
                    try {
                        halflap = Integer.parseInt(cplistParts[0].trim());
                        lapCount = Integer.parseInt(cplistParts[2].trim());
                    } catch (NumberFormatException e) {
                        // Use default values
                    }
                }
                
                // Add "1/2 Lap" column if halflap > 0
                if (halflap > 0) {
                    field.createCell(colIdx).setCellValue("1/2 Lap");
                    field.getCell(colIdx).setCellStyle(headerStyle);
                    colIdx++;
                }
                
                // Add lap columns (Lap 1, Lap 2, etc.)
                for (int i = 1; i <= lapCount; i++) {
                    field.createCell(colIdx).setCellValue("Lap " + i);
                    field.getCell(colIdx).setCellStyle(headerStyle);
                    colIdx++;
                }
            } else if (!isLapMode && cplist != null && !cplist.isEmpty()) {
                // TIME mode: treat cplist as checkpoint names
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

                // Handle LAP mode vs TIME mode columns
                if (isLapMode && cplist != null && !cplist.isEmpty()) {
                    String[] cplistParts = cplist.split(",");
                    int halflap = 0;
                    int lapCount = 0;
                    
                    if (cplistParts.length >= 3) {
                        try {
                            halflap = Integer.parseInt(cplistParts[0].trim());
                            lapCount = Integer.parseInt(cplistParts[2].trim());
                        } catch (NumberFormatException e) {
                            // Use default values
                        }
                    }
                    
                    // Add time0 (half-lap) if halflap > 0
                    if (halflap > 0) {
                        Cell halfLapCell = row.createCell(dataColIdx++);
                        try {
                            Method getter = TResults.class.getMethod("getTime0");
                            Integer timeValue = (Integer) getter.invoke(result);
                            halfLapCell.setCellValue(TimeFormatUtil.intToTimeString(timeValue));
                        } catch (Exception e) {
                            halfLapCell.setCellValue("");
                        }
                        halfLapCell.setCellStyle(rowStyle);
                    }
                    
                    // Add lap times (time1, time2, etc.)
                    for (int i = 1; i <= lapCount; i++) {
                        Cell lapCell = row.createCell(dataColIdx++);
                        try {
                            Method getter = TResults.class.getMethod("getTime" + i);
                            Integer timeValue = (Integer) getter.invoke(result);
                            lapCell.setCellValue(TimeFormatUtil.intToTimeString(timeValue));
                        } catch (Exception e) {
                            lapCell.setCellValue("");
                        }
                        lapCell.setCellStyle(rowStyle);
                    }
                } else if (!isLapMode && cplist != null && !cplist.isEmpty()) {
                    // TIME mode: use checkpoint fields
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

    public byte[] exportStatisticToExcel(String eventId) throws Exception {
        List<StatisticReportDto> data = statisticReportService.getStatisticReport(new com.smart.reporting.dto.EventRequest(eventId));
        TEvent event = eventRepository.findById(eventId).orElse(null);
        TOrg org = orgRepository.findByIsActive(true).stream().findFirst().orElse(null);
        return generateStatisticExcel(data, "Statistics", event, org);
    }

    public byte[] exportRegisteredToExcel(String eventId) throws Exception {
        List<StatisticRegListDto> data = statisticReportService.getRegistrationList(eventId);
        TEvent event = eventRepository.findById(eventId).orElse(null);
        TOrg org = orgRepository.findByIsActive(true).stream().findFirst().orElse(null);
        return generateListExcel(data, "Registered", new String[]{"Item", "Category", "Bib", "Name"}, event, org, "Registration List");
    }

    public byte[] exportStartedToExcel(String eventId) throws Exception {
        List<StatisticStartListDto> data = statisticReportService.getStartList(eventId);
        TEvent event = eventRepository.findById(eventId).orElse(null);
        TOrg org = orgRepository.findByIsActive(true).stream().findFirst().orElse(null);
        return generateStartListExcel(data, "Started", event, org);
    }

    public byte[] exportDnsToExcel(String eventId) throws Exception {
        List<StatisticDnsDto> data = statisticReportService.getDidNotStartList(eventId);
        TEvent event = eventRepository.findById(eventId).orElse(null);
        TOrg org = orgRepository.findByIsActive(true).stream().findFirst().orElse(null);
        return generateListExcel(data, "Did Not Start", new String[]{"Item", "Category", "Bib", "Name"}, event, org, "Did Not Start List");
    }

    public byte[] exportFinishedToExcel(String eventId) throws Exception {
        List<StatisticFinishedDto> data = statisticReportService.getFinishedList(eventId);
        TEvent event = eventRepository.findById(eventId).orElse(null);
        TOrg org = orgRepository.findByIsActive(true).stream().findFirst().orElse(null);
        return generateFinishedListExcel(data, "Finished", event, org);
    }

    public byte[] exportDnfToExcel(String eventId) throws Exception {
        List<StatisticDnfDto> data = statisticReportService.getDidNotFinishList(eventId);
        TEvent event = eventRepository.findById(eventId).orElse(null);
        TOrg org = orgRepository.findByIsActive(true).stream().findFirst().orElse(null);
        return generateDnfListExcel(data, "Did Not Finish", event, org);
    }

    public byte[] exportNsbfToExcel(String eventId) throws Exception {
        List<StatisticNsbfDto> data = statisticReportService.getNoStartButFinishedList(eventId);
        TEvent event = eventRepository.findById(eventId).orElse(null);
        TOrg org = orgRepository.findByIsActive(true).stream().findFirst().orElse(null);
        return generateFinishedListExcel(data, "No Start But Finished", event, org);
    }

    public byte[] exportDqToExcel(String eventId) throws Exception {
        List<StatisticDqDto> data = statisticReportService.getDisqualifiedList(eventId);
        TEvent event = eventRepository.findById(eventId).orElse(null);
        TOrg org = orgRepository.findByIsActive(true).stream().findFirst().orElse(null);
        return generateDqListExcel(data, "Disqualified", event, org);
    }

    private byte[] generateStatisticExcel(List<StatisticReportDto> data, String sheetName, TEvent event, TOrg org) throws Exception {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(sheetName);
            int rowIdx = 0;

            // Styles
            CellStyle r1Style = createTitleStyle(workbook);
            CellStyle headingStyle = createHeadingStyle(workbook);
            CellStyle fieldStyle = createFieldHeaderStyle(workbook);
            CellStyle footerStyle = createFooterStyle(workbook);

            // Header rows
            if (event != null) {
                rowIdx = addEventHeader(sheet, event, org, "Statistics Report", rowIdx, r1Style, headingStyle);
                rowIdx++; // Empty row
            }

            // Field headers
            Row headerRow = sheet.createRow(rowIdx++);
            String[] headers = {"Cat", "Category", "Registered", "Started", "Did Not Start", "Finished", "Did Not Finish", "False Start", "No Start But Finished", "Disqualified"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(fieldStyle);
            }

            // Data row styles
            CellStyle dataStyleDefault = createDataStyle(workbook, false);
            CellStyle dataStyleAlt = createDataStyle(workbook, true);

            // Data rows
            for (StatisticReportDto dto : data) {
                Row row = sheet.createRow(rowIdx++);
                CellStyle rowStyle = (rowIdx % 2 == 0) ? dataStyleAlt : dataStyleDefault;
                
                Cell c0 = row.createCell(0);
                c0.setCellValue(dto.getCat());
                c0.setCellStyle(rowStyle);
                
                Cell c1 = row.createCell(1);
                c1.setCellValue(dto.getCategory());
                c1.setCellStyle(rowStyle);
                
                Cell c2 = row.createCell(2);
                c2.setCellValue(dto.getRegistered());
                c2.setCellStyle(rowStyle);
                
                Cell c3 = row.createCell(3);
                c3.setCellValue(dto.getStarted());
                c3.setCellStyle(rowStyle);
                
                Cell c4 = row.createCell(4);
                c4.setCellValue(dto.getDidNotStart());
                c4.setCellStyle(rowStyle);
                
                Cell c5 = row.createCell(5);
                c5.setCellValue(dto.getFinished());
                c5.setCellStyle(rowStyle);
                
                Cell c6 = row.createCell(6);
                c6.setCellValue(dto.getDidNotFinish());
                c6.setCellStyle(rowStyle);
                
                Cell c7 = row.createCell(7);
                c7.setCellValue(dto.getFalseStart());
                c7.setCellStyle(rowStyle);
                
                Cell c8 = row.createCell(8);
                c8.setCellValue(dto.getNoStartButFinished());
                c8.setCellStyle(rowStyle);
                
                Cell c9 = row.createCell(9);
                c9.setCellValue(dto.getDisqualified());
                c9.setCellStyle(rowStyle);
            }

            // Footer
            addFooter(sheet, rowIdx, headers.length - 1, org, footerStyle);

            // Column widths
            sheet.setColumnWidth(0, 50 * 36);
            sheet.setColumnWidth(1, 250 * 36);
            for (int i = 2; i < headers.length; i++) {
                sheet.setColumnWidth(i, 100 * 36);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private byte[] generateListExcel(List<?> data, String sheetName, String[] headers, TEvent event, TOrg org, String title) throws Exception {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(sheetName);
            int rowIdx = 0;

            // Styles
            CellStyle r1Style = createTitleStyle(workbook);
            CellStyle headingStyle = createHeadingStyle(workbook);
            CellStyle fieldStyle = createFieldHeaderStyle(workbook);
            CellStyle footerStyle = createFooterStyle(workbook);

            // Header rows
            if (event != null) {
                rowIdx = addEventHeader(sheet, event, org, title, rowIdx, r1Style, headingStyle);
                rowIdx++; // Empty row
            }

            // Field headers
            Row headerRow = sheet.createRow(rowIdx++);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(fieldStyle);
            }

            // Data row styles
            CellStyle dataStyleDefault = createDataStyle(workbook, false);
            CellStyle dataStyleAlt = createDataStyle(workbook, true);

            // Data rows
            for (Object obj : data) {
                Row row = sheet.createRow(rowIdx++);
                CellStyle rowStyle = (rowIdx % 2 == 0) ? dataStyleAlt : dataStyleDefault;
                
                if (obj instanceof StatisticRegListDto) {
                    StatisticRegListDto dto = (StatisticRegListDto) obj;
                    Cell c0 = row.createCell(0);
                    c0.setCellValue(dto.getItem());
                    c0.setCellStyle(rowStyle);
                    
                    Cell c1 = row.createCell(1);
                    c1.setCellValue(dto.getCategory());
                    c1.setCellStyle(rowStyle);
                    
                    Cell c2 = row.createCell(2);
                    c2.setCellValue(dto.getBib());
                    c2.setCellStyle(rowStyle);
                    
                    Cell c3 = row.createCell(3);
                    c3.setCellValue(dto.getName());
                    c3.setCellStyle(rowStyle);
                } else if (obj instanceof StatisticDnsDto) {
                    StatisticDnsDto dto = (StatisticDnsDto) obj;
                    Cell c0 = row.createCell(0);
                    c0.setCellValue(dto.getItem());
                    c0.setCellStyle(rowStyle);
                    
                    Cell c1 = row.createCell(1);
                    c1.setCellValue(dto.getCategory());
                    c1.setCellStyle(rowStyle);
                    
                    Cell c2 = row.createCell(2);
                    c2.setCellValue(dto.getBib());
                    c2.setCellStyle(rowStyle);
                    
                    Cell c3 = row.createCell(3);
                    c3.setCellValue(dto.getName());
                    c3.setCellStyle(rowStyle);
                }
            }

            // Footer
            addFooter(sheet, rowIdx, headers.length - 1, org, footerStyle);

            // Column widths
            sheet.setColumnWidth(0, 50 * 36);
            sheet.setColumnWidth(1, 150 * 36);
            sheet.setColumnWidth(2, 80 * 36);
            sheet.setColumnWidth(3, 250 * 36);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private byte[] generateStartListExcel(List<StatisticStartListDto> data, String sheetName, TEvent event, TOrg org) throws Exception {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(sheetName);
            int rowIdx = 0;

            // Styles
            CellStyle r1Style = createTitleStyle(workbook);
            CellStyle headingStyle = createHeadingStyle(workbook);
            CellStyle fieldStyle = createFieldHeaderStyle(workbook);
            CellStyle footerStyle = createFooterStyle(workbook);

            // Header rows
            if (event != null) {
                rowIdx = addEventHeader(sheet, event, org, "Start List", rowIdx, r1Style, headingStyle);
                rowIdx++; // Empty row
            }

            // Field headers
            Row headerRow = sheet.createRow(rowIdx++);
            String[] headers = {"Item", "Category", "Bib", "Name", "Time Start", "Time Gun"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(fieldStyle);
            }

            // Data row styles
            CellStyle dataStyleDefault = createDataStyle(workbook, false);
            CellStyle dataStyleAlt = createDataStyle(workbook, true);

            // Data rows
            for (StatisticStartListDto dto : data) {
                Row row = sheet.createRow(rowIdx++);
                CellStyle rowStyle = (rowIdx % 2 == 0) ? dataStyleAlt : dataStyleDefault;
                
                Cell c0 = row.createCell(0);
                c0.setCellValue(dto.getItem());
                c0.setCellStyle(rowStyle);
                
                Cell c1 = row.createCell(1);
                c1.setCellValue(dto.getCategory());
                c1.setCellStyle(rowStyle);
                
                Cell c2 = row.createCell(2);
                c2.setCellValue(dto.getBib());
                c2.setCellStyle(rowStyle);
                
                Cell c3 = row.createCell(3);
                c3.setCellValue(dto.getName());
                c3.setCellStyle(rowStyle);
                
                Cell c4 = row.createCell(4);
                c4.setCellValue("'" + dto.getTimeStart());
                c4.setCellStyle(rowStyle);
                
                Cell c5 = row.createCell(5);
                c5.setCellValue("'" + dto.getTimeGun());
                c5.setCellStyle(rowStyle);
            }

            // Footer
            addFooter(sheet, rowIdx, headers.length - 1, org, footerStyle);

            // Column widths
            sheet.setColumnWidth(0, 50 * 36);
            sheet.setColumnWidth(1, 150 * 36);
            sheet.setColumnWidth(2, 80 * 36);
            sheet.setColumnWidth(3, 250 * 36);
            sheet.setColumnWidth(4, 100 * 36);
            sheet.setColumnWidth(5, 100 * 36);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private byte[] generateFinishedListExcel(List<?> data, String sheetName, TEvent event, TOrg org) throws Exception {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(sheetName);
            int rowIdx = 0;

            // Styles
            CellStyle r1Style = createTitleStyle(workbook);
            CellStyle headingStyle = createHeadingStyle(workbook);
            CellStyle fieldStyle = createFieldHeaderStyle(workbook);
            CellStyle footerStyle = createFooterStyle(workbook);

            // Header rows
            if (event != null) {
                String title = sheetName.equals("Finished") ? "Finished List" : "No Start But Finished List";
                rowIdx = addEventHeader(sheet, event, org, title, rowIdx, r1Style, headingStyle);
                rowIdx++; // Empty row
            }

            // Field headers
            Row headerRow = sheet.createRow(rowIdx++);
            String[] headers = {"Item", "Category", "Bib", "Name", "Time Finish", "Time Gun"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(fieldStyle);
            }

            // Data row styles
            CellStyle dataStyleDefault = createDataStyle(workbook, false);
            CellStyle dataStyleAlt = createDataStyle(workbook, true);

            // Data rows
            for (Object obj : data) {
                Row row = sheet.createRow(rowIdx++);
                CellStyle rowStyle = (rowIdx % 2 == 0) ? dataStyleAlt : dataStyleDefault;
                
                if (obj instanceof StatisticFinishedDto) {
                    StatisticFinishedDto dto = (StatisticFinishedDto) obj;
                    Cell c0 = row.createCell(0);
                    c0.setCellValue(dto.getItem());
                    c0.setCellStyle(rowStyle);
                    
                    Cell c1 = row.createCell(1);
                    c1.setCellValue(dto.getCategory());
                    c1.setCellStyle(rowStyle);
                    
                    Cell c2 = row.createCell(2);
                    c2.setCellValue(dto.getBib());
                    c2.setCellStyle(rowStyle);
                    
                    Cell c3 = row.createCell(3);
                    c3.setCellValue(dto.getName());
                    c3.setCellStyle(rowStyle);
                    
                    Cell c4 = row.createCell(4);
                    c4.setCellValue("'" + dto.getTimeFinish());
                    c4.setCellStyle(rowStyle);
                    
                    Cell c5 = row.createCell(5);
                    c5.setCellValue("'" + dto.getTimeGun());
                    c5.setCellStyle(rowStyle);
                } else if (obj instanceof StatisticNsbfDto) {
                    StatisticNsbfDto dto = (StatisticNsbfDto) obj;
                    Cell c0 = row.createCell(0);
                    c0.setCellValue(dto.getItem());
                    c0.setCellStyle(rowStyle);
                    
                    Cell c1 = row.createCell(1);
                    c1.setCellValue(dto.getCategory());
                    c1.setCellStyle(rowStyle);
                    
                    Cell c2 = row.createCell(2);
                    c2.setCellValue(dto.getBib());
                    c2.setCellStyle(rowStyle);
                    
                    Cell c3 = row.createCell(3);
                    c3.setCellValue(dto.getName());
                    c3.setCellStyle(rowStyle);
                    
                    Cell c4 = row.createCell(4);
                    c4.setCellValue("'" + dto.getTimeFinish());
                    c4.setCellStyle(rowStyle);
                    
                    Cell c5 = row.createCell(5);
                    c5.setCellValue("'" + dto.getTimeGun());
                    c5.setCellStyle(rowStyle);
                }
            }

            // Footer
            addFooter(sheet, rowIdx, headers.length - 1, org, footerStyle);

            // Column widths
            sheet.setColumnWidth(0, 50 * 36);
            sheet.setColumnWidth(1, 150 * 36);
            sheet.setColumnWidth(2, 80 * 36);
            sheet.setColumnWidth(3, 250 * 36);
            sheet.setColumnWidth(4, 100 * 36);
            sheet.setColumnWidth(5, 100 * 36);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private byte[] generateDnfListExcel(List<StatisticDnfDto> data, String sheetName, TEvent event, TOrg org) throws Exception {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(sheetName);
            int rowIdx = 0;

            // Styles
            CellStyle r1Style = createTitleStyle(workbook);
            CellStyle headingStyle = createHeadingStyle(workbook);
            CellStyle fieldStyle = createFieldHeaderStyle(workbook);
            CellStyle footerStyle = createFooterStyle(workbook);

            // Header rows
            if (event != null) {
                rowIdx = addEventHeader(sheet, event, org, "Did Not Finish List", rowIdx, r1Style, headingStyle);
                rowIdx++; // Empty row
            }

            // Field headers
            Row headerRow = sheet.createRow(rowIdx++);
            String[] headers = {"Item", "Category", "Bib", "Name", "Time Start", "Time Gun"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(fieldStyle);
            }

            // Data row styles
            CellStyle dataStyleDefault = createDataStyle(workbook, false);
            CellStyle dataStyleAlt = createDataStyle(workbook, true);

            // Data rows
            for (StatisticDnfDto dto : data) {
                Row row = sheet.createRow(rowIdx++);
                CellStyle rowStyle = (rowIdx % 2 == 0) ? dataStyleAlt : dataStyleDefault;
                
                Cell c0 = row.createCell(0);
                c0.setCellValue(dto.getItem());
                c0.setCellStyle(rowStyle);
                
                Cell c1 = row.createCell(1);
                c1.setCellValue(dto.getCategory());
                c1.setCellStyle(rowStyle);
                
                Cell c2 = row.createCell(2);
                c2.setCellValue(dto.getBib());
                c2.setCellStyle(rowStyle);
                
                Cell c3 = row.createCell(3);
                c3.setCellValue(dto.getName());
                c3.setCellStyle(rowStyle);
                
                Cell c4 = row.createCell(4);
                c4.setCellValue("'" + dto.getTimeStart());
                c4.setCellStyle(rowStyle);
                
                Cell c5 = row.createCell(5);
                c5.setCellValue("'" + dto.getTimeGun());
                c5.setCellStyle(rowStyle);
            }

            // Footer
            addFooter(sheet, rowIdx, headers.length - 1, org, footerStyle);

            // Column widths
            sheet.setColumnWidth(0, 50 * 36);
            sheet.setColumnWidth(1, 150 * 36);
            sheet.setColumnWidth(2, 80 * 36);
            sheet.setColumnWidth(3, 250 * 36);
            sheet.setColumnWidth(4, 100 * 36);
            sheet.setColumnWidth(5, 100 * 36);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private byte[] generateDqListExcel(List<StatisticDqDto> data, String sheetName, TEvent event, TOrg org) throws Exception {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(sheetName);
            int rowIdx = 0;

            // Styles
            CellStyle r1Style = createTitleStyle(workbook);
            CellStyle headingStyle = createHeadingStyle(workbook);
            CellStyle fieldStyle = createFieldHeaderStyle(workbook);
            CellStyle footerStyle = createFooterStyle(workbook);

            // Header rows
            if (event != null) {
                rowIdx = addEventHeader(sheet, event, org, "Disqualified List", rowIdx, r1Style, headingStyle);
                rowIdx++; // Empty row
            }

            // Field headers
            Row headerRow = sheet.createRow(rowIdx++);
            String[] headers = {"Item", "Category", "Bib", "Name", "Time Finish", "Time Gun", "Reason"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(fieldStyle);
            }

            // Data row styles
            CellStyle dataStyleDefault = createDataStyle(workbook, false);
            CellStyle dataStyleAlt = createDataStyle(workbook, true);

            // Data rows
            for (StatisticDqDto dto : data) {
                Row row = sheet.createRow(rowIdx++);
                CellStyle rowStyle = (rowIdx % 2 == 0) ? dataStyleAlt : dataStyleDefault;
                
                Cell c0 = row.createCell(0);
                c0.setCellValue(dto.getItem());
                c0.setCellStyle(rowStyle);
                
                Cell c1 = row.createCell(1);
                c1.setCellValue(dto.getCategory());
                c1.setCellStyle(rowStyle);
                
                Cell c2 = row.createCell(2);
                c2.setCellValue(dto.getBib());
                c2.setCellStyle(rowStyle);
                
                Cell c3 = row.createCell(3);
                c3.setCellValue(dto.getName());
                c3.setCellStyle(rowStyle);
                
                Cell c4 = row.createCell(4);
                c4.setCellValue("'" + dto.getTimeFinish());
                c4.setCellStyle(rowStyle);
                
                Cell c5 = row.createCell(5);
                c5.setCellValue("'" + dto.getTimeGun());
                c5.setCellStyle(rowStyle);
                
                Cell c6 = row.createCell(6);
                c6.setCellValue(dto.getRemark() != null ? dto.getRemark() : "");
                c6.setCellStyle(rowStyle);
            }

            // Footer
            addFooter(sheet, rowIdx, headers.length - 1, org, footerStyle);

            // Column widths
            sheet.setColumnWidth(0, 50 * 36);
            sheet.setColumnWidth(1, 150 * 36);
            sheet.setColumnWidth(2, 80 * 36);
            sheet.setColumnWidth(3, 250 * 36);
            sheet.setColumnWidth(4, 100 * 36);
            sheet.setColumnWidth(5, 100 * 36);
            sheet.setColumnWidth(6, 300 * 36);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    // Helper methods for creating consistent styles and headers
    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 18);
        font.setFontName("Aptos Narrow");
        style.setFont(font);
        return style;
    }

    private CellStyle createHeadingStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(false);
        font.setFontHeightInPoints((short) 14);
        font.setFontName("Aptos Narrow");
        style.setFont(font);
        return style;
    }

    private CellStyle createFieldHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setFontHeightInPoints((short) 11);
        font.setFontName("Aptos Narrow");
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private CellStyle createFooterStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Aptos Narrow");
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        return style;
    }

    private int addEventHeader(Sheet sheet, TEvent event, TOrg org, String title, int rowIdx, CellStyle r1Style, CellStyle headingStyle) {
        String eventName = event.getName();
        String eventDate = event.getEventDt() != null ? event.getEventDt().toString() : "";
        String location = "Location : " + (event.getLocation() != null ? event.getLocation() : "");
        String weather = "Weather : " + (event.getWeather() != null ? event.getWeather() : "");

        Row row1 = sheet.createRow(rowIdx++);
        Cell cell1 = row1.createCell(0);
        cell1.setCellValue(eventName);
        cell1.setCellStyle(r1Style);

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

        return rowIdx;
    }

    private void addFooter(Sheet sheet, int rowIdx, int lastCol, TOrg org, CellStyle footerStyle) {
        String orgName = (org != null && org.getAlias() != null) ? org.getAlias() : "MyPaceTracker";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss.SS a");
        String formattedNow = LocalDateTime.now().format(formatter);
        String footerText = "Timing & Results by " + orgName + "; Printed at " + formattedNow;

        Row footerRow = sheet.createRow(rowIdx);
        Cell footerCell = footerRow.createCell(0);
        footerCell.setCellValue(footerText);
        footerCell.setCellStyle(footerStyle);

        if (lastCol > 0) {
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(
                    footerRow.getRowNum(), footerRow.getRowNum(), 0, lastCol
            ));
        }
    }

    private CellStyle createDataStyle(Workbook workbook, boolean alternateRow) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Aptos Narrow");
        font.setFontHeightInPoints((short) 11);
        if (alternateRow) {
            font.setColor(IndexedColors.BLACK.getIndex());
            style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        style.setFont(font);
        return style;
    }

    public byte[] generateOverallRankExcel(String eventId, String distance, String orgId) throws Exception {
        TEvent event = eventRepository.findById(eventId).orElse(null);
        Optional<TOrg> orgs = orgRepository.findById(orgId);
        TOrg org = orgs.orElse(null);
        
        // Get results from database
        List<TResults> dbResults = raceResultService.getResultsByEventAndDistanceOrderByRank1tot(eventId, distance);
        
        // Determine mode
        String mode = "TIME";
        List<TEventCat> eventcat = null;
        if (dbResults != null && !dbResults.isEmpty()) {
            String cat = dbResults.get(0).getCat();
            eventcat = eventCatService.getByEventIdAndCat(eventId, cat);
            if (eventcat != null && !eventcat.isEmpty()) {
                mode = eventcat.get(0).getRacemode();
            }
        }

        if ("LAP".equalsIgnoreCase(mode)) {
            // LAP mode Excel generation
            return generateOverallRankLapExcel(dbResults, eventcat, event, org, distance);
        } else {
            // TIME mode Excel generation (original logic)
            return generateOverallRankTimeExcel(dbResults, eventcat, event, org, distance);
        }
    }

    private byte[] generateOverallRankLapExcel(List<TResults> results, List<TEventCat> eventcat, TEvent event, TOrg org, String distance) throws Exception {
        String cplist = eventcat.get(0).getCplist();
        
        // Parse cplist for LAP mode (halflap, fulllap, numberOfLaps, totalDistance)
        boolean includeHalfLap = false;
        if (cplist != null && !cplist.isEmpty()) {
            String[] cplistParts = cplist.split(",");
            if (cplistParts.length > 0) {
                try {
                    int halflap = Integer.parseInt(cplistParts[0].trim());
                    includeHalfLap = halflap > 0;
                } catch (NumberFormatException e) {
                    // Default to false
                }
            }
        }

        // Calculate maxLapCount as the maximum displayable lap (lap - 1)
        int maxLapCount = 0;
        for (TResults result : results) {
            if (result.getLap() != null && result.getLap() > 0) {
                int displayLap = result.getLap() - 1;  // lap - 1
                if (displayLap > maxLapCount) {
                    maxLapCount = displayLap;
                }
            }
        }

        // Sort by rank1tot, rank1mix, rank1cat, lap (zeros last)
        List<TResults> sortedResults = results.stream()
            .sorted((a, b) -> {
                // Non-zero ranks first
                Integer aRank1tot = a.getRank1tot() != null && a.getRank1tot() > 0 ? a.getRank1tot() : Integer.MAX_VALUE;
                Integer bRank1tot = b.getRank1tot() != null && b.getRank1tot() > 0 ? b.getRank1tot() : Integer.MAX_VALUE;
                if (!aRank1tot.equals(bRank1tot)) return aRank1tot.compareTo(bRank1tot);
                
                Integer aRank1mix = a.getRank1mix() != null && a.getRank1mix() > 0 ? a.getRank1mix() : Integer.MAX_VALUE;
                Integer bRank1mix = b.getRank1mix() != null && b.getRank1mix() > 0 ? b.getRank1mix() : Integer.MAX_VALUE;
                if (!aRank1mix.equals(bRank1mix)) return aRank1mix.compareTo(bRank1mix);
                
                Integer aRank1cat = a.getRank1cat() != null && a.getRank1cat() > 0 ? a.getRank1cat() : Integer.MAX_VALUE;
                Integer bRank1cat = b.getRank1cat() != null && b.getRank1cat() > 0 ? b.getRank1cat() : Integer.MAX_VALUE;
                if (!aRank1cat.equals(bRank1cat)) return aRank1cat.compareTo(bRank1cat);
                
                Integer aLap = a.getLap() != null && a.getLap() > 0 ? a.getLap() : Integer.MAX_VALUE;
                Integer bLap = b.getLap() != null && b.getLap() > 0 ? b.getLap() : Integer.MAX_VALUE;
                return aLap.compareTo(bLap);
            })
            .collect(Collectors.toList());

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("OverallRank");
            
            CellStyle r1Style = createTitleStyle(workbook);
            CellStyle headingStyle = createHeadingStyle(workbook);
            CellStyle headerStyle = createFieldHeaderStyle(workbook);
            CellStyle footerStyle = createFooterStyle(workbook);
            CellStyle dataStyleDefault = createDataStyle(workbook, false);
            CellStyle dataStyleAlt = createDataStyle(workbook, true);
            
            int rowIdx = 0;
            rowIdx = addEventHeader(sheet, event, org, "Overall Rank - " + distance, rowIdx, r1Style, headingStyle);
            rowIdx++; // Empty row
            
            // Build headers for LAP mode
            List<String> headers = new java.util.ArrayList<>();
            headers.add("Overall Rank");
            headers.add("Gender Rank");
            headers.add("Category Rank");
            headers.add("Lap");
            headers.add("Bib");
            headers.add("Name");
            headers.add("Category");
            headers.add("TimeStart");
            headers.add("Official Time");
            headers.add("Net Time");
            
            if (includeHalfLap) {
                headers.add("1/2 Lap");
            }
            // Create headers for each displayable lap (1 to maxLapCount)
            for (int i = 1; i <= maxLapCount; i++) {
                headers.add("Lap " + i);
            }
            
            headers.add("TimeFinish");

            Row headerRow = sheet.createRow(rowIdx++);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }
            
            // Add data rows
            for (TResults result : sortedResults) {
                boolean alternate = (rowIdx % 2 == 0);
                CellStyle rowStyle = alternate ? dataStyleAlt : dataStyleDefault;
                Row row = sheet.createRow(rowIdx++);
                
                int colIdx = 0;

                // Overall Rank
                Cell c0 = row.createCell(colIdx++);
                c0.setCellValue(result.getRank1tot() != null ? result.getRank1tot() : 0);
                c0.setCellStyle(rowStyle);

                // Gender Rank
                Cell c1 = row.createCell(colIdx++);
                c1.setCellValue(result.getRank1mix() != null ? result.getRank1mix() : 0);
                c1.setCellStyle(rowStyle);

                // Category Rank
                Cell c2 = row.createCell(colIdx++);
                c2.setCellValue(result.getRank1cat() != null ? result.getRank1cat() : 0);
                c2.setCellStyle(rowStyle);

                // Lap
                Cell c3 = row.createCell(colIdx++);
                c3.setCellValue(result.getLap() != null ? result.getLap() : 0);
                c3.setCellStyle(rowStyle);

                // Bib
                Cell c4 = row.createCell(colIdx++);
                c4.setCellValue(result.getBib() != null ? result.getBib() : "");
                c4.setCellStyle(rowStyle);

                // Name
                Cell c5 = row.createCell(colIdx++);
                c5.setCellValue(result.getName() != null ? result.getName() : "");
                c5.setCellStyle(rowStyle);

                // Category
                Cell c6 = row.createCell(colIdx++);
                c6.setCellValue(result.getCategory() != null ? result.getCategory() : "");
                c6.setCellStyle(rowStyle);

                // TimeStart
                Cell c7 = row.createCell(colIdx++);
                c7.setCellValue(result.getTimestart() != null ? TimeFormatUtil.intToTimeString(result.getTimestart()) : "");
                c7.setCellStyle(rowStyle);

                // Official Time (timefinish - timegun)
                Cell c8 = row.createCell(colIdx++);
                String officialTime = "";
                if (result.getTimefinish() != null && result.getTimegun() != null) {
                    officialTime = TimeFormatUtil.intToTimeString(result.getTimefinish() - result.getTimegun());
                }
                c8.setCellValue(officialTime);
                c8.setCellStyle(rowStyle);

                // Net Time (timefinish - timestart)
                Cell c9 = row.createCell(colIdx++);
                String netTime = "";
                if (result.getTimefinish() != null && result.getTimestart() != null) {
                    netTime = TimeFormatUtil.intToTimeString(result.getTimefinish() - result.getTimestart());
                }
                c9.setCellValue(netTime);
                c9.setCellStyle(rowStyle);

                // Lap times (display from time(startIndex) to time(lap-1))
                Integer lapCount = result.getLap();
                int displayMaxLap = (lapCount != null && lapCount > 0) ? lapCount - 1 : 0;
                int startIndex = includeHalfLap ? 0 : 1;
                
                // Add time0 if needed
                if (includeHalfLap) {
                    try {
                        Method getter = TResults.class.getMethod("getTime0");
                        Integer timeValue = (Integer) getter.invoke(result);
                        Cell lapCell = row.createCell(colIdx++);
                        if (timeValue != null) {
                            lapCell.setCellValue(TimeFormatUtil.intToTimeString(timeValue));
                        } else {
                            lapCell.setCellValue("");
                        }
                        lapCell.setCellStyle(rowStyle);
                    } catch (Exception e) {
                        Cell lapCell = row.createCell(colIdx++);
                        lapCell.setCellValue("");
                        lapCell.setCellStyle(rowStyle);
                    }
                }
                
                // Add lap times 1 to maxLapCount
                for (int i = 1; i <= maxLapCount; i++) {
                    try {
                        Method getter = TResults.class.getMethod("getTime" + i);
                        Integer timeValue = (Integer) getter.invoke(result);
                        Cell lapCell = row.createCell(colIdx++);
                        // Only display if this lap was completed (i <= displayMaxLap)
                        if (i <= displayMaxLap && timeValue != null) {
                            lapCell.setCellValue(TimeFormatUtil.intToTimeString(timeValue));
                        } else {
                            lapCell.setCellValue("");
                        }
                        lapCell.setCellStyle(rowStyle);
                    } catch (Exception e) {
                        Cell lapCell = row.createCell(colIdx++);
                        lapCell.setCellValue("");
                        lapCell.setCellStyle(rowStyle);
                    }
                }

                // TimeFinish
                Cell finishCell = row.createCell(colIdx++);
                finishCell.setCellValue(result.getTimefinish() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()) : "");
                finishCell.setCellStyle(rowStyle);
            }

            addFooter(sheet, rowIdx, headers.size() - 1, org, footerStyle);

            // Auto-size columns
            int totalColumns = headers.size();
            for (int i = 0; i < totalColumns; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, Math.max(sheet.getColumnWidth(i), 120 * 36));
            }
            
            workbook.write(out);
            return out.toByteArray();
        }
    }

    private byte[] generateOverallRankTimeExcel(List<TResults> dbResults, List<TEventCat> eventcat, TEvent event, TOrg org, String distance) throws Exception {
        // Convert to EventCategoryResultResponse
        final String cplist = (eventcat != null && !eventcat.isEmpty()) ? eventcat.get(0).getCplist() : null;
        List<EventCategoryResultResponse> results = dbResults.stream().map(result -> {
            EventCategoryResultResponse dto = new EventCategoryResultResponse();
            dto.setCplist(cplist);
            dto.setName(result.getName());
            dto.setBib(result.getBib());
            dto.setCategory(result.getCategory());
            dto.setEventId(result.getEventId());
            dto.setCat(result.getCat());
            dto.setRank1Cat(result.getRank1cat());
            dto.setRank1Mix(result.getRank1mix());
            dto.setRank1Tot(result.getRank1tot());
            dto.setNetTime(result.getTimefinish() != null && result.getTimestart() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimestart()) : null);
            dto.setOfficialTime(result.getTimefinish() != null && result.getTimegun() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimegun()) : null);
            dto.setTimeStart(result.getTimestart() != null ? TimeFormatUtil.intToTimeString(result.getTimestart()) : null);
            dto.setTimeFinish(result.getTimefinish() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()) : null);
            dto.setTimeCP1(result.getTime1() != null ? TimeFormatUtil.intToTimeString(result.getTime1()) : null);
            dto.setTimeCP2(result.getTime2() != null ? TimeFormatUtil.intToTimeString(result.getTime2()) : null);
            dto.setTimeCP3(result.getTime3() != null ? TimeFormatUtil.intToTimeString(result.getTime3()) : null);
            dto.setTimeCP4(result.getTime4() != null ? TimeFormatUtil.intToTimeString(result.getTime4()) : null);
            dto.setTimeCP5(result.getTime5() != null ? TimeFormatUtil.intToTimeString(result.getTime5()) : null);
            dto.setTimeCP6(result.getTime6() != null ? TimeFormatUtil.intToTimeString(result.getTime6()) : null);
            dto.setTimeCP7(result.getTime7() != null ? TimeFormatUtil.intToTimeString(result.getTime7()) : null);
            dto.setTimeCP8(result.getTime8() != null ? TimeFormatUtil.intToTimeString(result.getTime8()) : null);
            dto.setTimeCP9(result.getTime9() != null ? TimeFormatUtil.intToTimeString(result.getTime9()) : null);
            dto.setTimeCP10(result.getTime10() != null ? TimeFormatUtil.intToTimeString(result.getTime10()) : null);
            return dto;
        }).collect(Collectors.toList());
        
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("OverallRank");
            
            CellStyle r1Style = createTitleStyle(workbook);
            CellStyle headingStyle = createHeadingStyle(workbook);
            CellStyle headerStyle = createFieldHeaderStyle(workbook);
            CellStyle footerStyle = createFooterStyle(workbook);
            CellStyle dataStyleDefault = createDataStyle(workbook, false);
            CellStyle dataStyleAlt = createDataStyle(workbook, true);
            
            int rowIdx = 0;
            rowIdx = addEventHeader(sheet, event, org, "Overall Rank - " + distance, rowIdx, r1Style, headingStyle);
            rowIdx++; // Empty row
            
            List<String> headers = new java.util.ArrayList<>();
            headers.add("Overall Rank");
            headers.add("Gender Rank");
            headers.add("Category Rank");
            headers.add("Bib");
            headers.add("Name");
            headers.add("Category");
            headers.add("Official Time");
            headers.add("Net Time");
            headers.add("TimeStart");

            if (cplist != null && !cplist.isEmpty()) {
                String[] cps = cplist.split(",");
                for (String cp : cps) {
                    String cpTrim = cp.trim();
                    if (!cpTrim.isEmpty()) {
                        headers.add(cpTrim);
                    }
                }
            }

            headers.add("TimeFinish");

            Row headerRow = sheet.createRow(rowIdx++);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }
            
            for (EventCategoryResultResponse dto : results) {
                boolean alternate = (rowIdx % 2 == 0);
                CellStyle rowStyle = alternate ? dataStyleAlt : dataStyleDefault;
                Row row = sheet.createRow(rowIdx++);
                
                int colIdx = 0;

                Cell c0 = row.createCell(colIdx++);
                c0.setCellValue(dto.getRank1Tot());
                c0.setCellStyle(rowStyle);

                Cell c1 = row.createCell(colIdx++);
                c1.setCellValue(dto.getRank1Mix());
                c1.setCellStyle(rowStyle);

                Cell c2 = row.createCell(colIdx++);
                c2.setCellValue(dto.getRank1Cat());
                c2.setCellStyle(rowStyle);

                Cell c3 = row.createCell(colIdx++);
                c3.setCellValue(dto.getBib() != null ? dto.getBib() : "");
                c3.setCellStyle(rowStyle);

                Cell c4 = row.createCell(colIdx++);
                c4.setCellValue(dto.getName() != null ? dto.getName() : "");
                c4.setCellStyle(rowStyle);

                Cell c5 = row.createCell(colIdx++);
                c5.setCellValue(dto.getCat() != null ? dto.getCat() : "");
                c5.setCellStyle(rowStyle);

                Cell c6 = row.createCell(colIdx++);
                c6.setCellValue(dto.getOfficialTime() != null ? dto.getOfficialTime() : "");
                c6.setCellStyle(rowStyle);

                Cell c7 = row.createCell(colIdx++);
                c7.setCellValue(dto.getNetTime() != null ? dto.getNetTime() : "");
                c7.setCellStyle(rowStyle);

                Cell c8 = row.createCell(colIdx++);
                c8.setCellValue(dto.getTimeStart() != null ? dto.getTimeStart() : "");
                c8.setCellStyle(rowStyle);

                if (cplist != null && !cplist.isEmpty()) {
                    String[] cps = cplist.split(",");
                    for (String cp : cps) {
                        String cpTrim = cp.trim();
                        String value = "";
                        switch (cpTrim) {
                            case "TimeCP1": value = dto.getTimeCP1(); break;
                            case "TimeCP2": value = dto.getTimeCP2(); break;
                            case "TimeCP3": value = dto.getTimeCP3(); break;
                            case "TimeCP4": value = dto.getTimeCP4(); break;
                            case "TimeCP5": value = dto.getTimeCP5(); break;
                            case "TimeCP6": value = dto.getTimeCP6(); break;
                            case "TimeCP7": value = dto.getTimeCP7(); break;
                            case "TimeCP8": value = dto.getTimeCP8(); break;
                            case "TimeCP9": value = dto.getTimeCP9(); break;
                            case "TimeCP10": value = dto.getTimeCP10(); break;
                            default: value = ""; break;
                        }
                        Cell cpCell = row.createCell(colIdx++);
                        cpCell.setCellValue(value != null ? value : "");
                        cpCell.setCellStyle(rowStyle);
                    }
                }

                Cell finishCell = row.createCell(colIdx++);
                finishCell.setCellValue(dto.getTimeFinish() != null ? dto.getTimeFinish() : "");
                finishCell.setCellStyle(rowStyle);
            }

            addFooter(sheet, rowIdx, headers.size() - 1, org, footerStyle);

            int totalColumns = headers.size();
            for (int i = 1; i < totalColumns; i++) {
                sheet.autoSizeColumn(i);
            }

            sheet.setColumnWidth(0, 80 * 36);  // Overall Rank - Fixed width, no auto-size
            sheet.setColumnWidth(1, Math.max(sheet.getColumnWidth(1), 80 * 36));  // Gender Rank
            sheet.setColumnWidth(2, Math.max(sheet.getColumnWidth(2), 80 * 36));  // Category Rank
            sheet.setColumnWidth(3, Math.max(sheet.getColumnWidth(3), 80 * 36));  // Bib
            sheet.setColumnWidth(4, Math.max(sheet.getColumnWidth(4), 250 * 36)); // Name
            sheet.setColumnWidth(5, Math.max(sheet.getColumnWidth(5), 150 * 36)); // Category
            sheet.setColumnWidth(6, Math.max(sheet.getColumnWidth(6), 120 * 36)); // Official Time
            sheet.setColumnWidth(7, Math.max(sheet.getColumnWidth(7), 120 * 36)); // Net Time
            sheet.setColumnWidth(8, Math.max(sheet.getColumnWidth(8), 120 * 36)); // TimeStart

            for (int i = 9; i < totalColumns; i++) {
                sheet.setColumnWidth(i, Math.max(sheet.getColumnWidth(i), 120 * 36));
            }
            
            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] generateGenderRankExcel(String eventId, String distance, String gender, String orgId) throws Exception {
        TEvent event = eventRepository.findById(eventId).orElse(null);
        Optional<TOrg> orgs = orgRepository.findById(orgId);
        TOrg org = orgs.orElse(null);
        
        // Get results from database
        List<TResults> dbResults = raceResultService.getResultsByEventAndDistanceAndGenderOrderByRank1mix(eventId, distance, gender);
        
        // Determine mode
        String mode = "TIME";
        List<TEventCat> eventcat = null;
        if (dbResults != null && !dbResults.isEmpty()) {
            String cat = dbResults.get(0).getCat();
            eventcat = eventCatService.getByEventIdAndCat(eventId, cat);
            if (eventcat != null && !eventcat.isEmpty()) {
                mode = eventcat.get(0).getRacemode();
            }
        }

        if ("LAP".equalsIgnoreCase(mode)) {
            // LAP mode Excel generation
            return generateGenderRankLapExcel(dbResults, eventcat, event, org, distance, gender);
        } else {
            // TIME mode Excel generation (original logic)
            return generateGenderRankTimeExcel(dbResults, eventcat, event, org, distance, gender);
        }
    }

    private byte[] generateGenderRankLapExcel(List<TResults> results, List<TEventCat> eventcat, TEvent event, TOrg org, String distance, String gender) throws Exception {
        String cplist = eventcat.get(0).getCplist();
        
        // Parse cplist for LAP mode (halflap, fulllap, numberOfLaps, totalDistance)
        boolean includeHalfLap = false;
        if (cplist != null && !cplist.isEmpty()) {
            String[] cplistParts = cplist.split(",");
            if (cplistParts.length > 0) {
                try {
                    int halflap = Integer.parseInt(cplistParts[0].trim());
                    includeHalfLap = halflap > 0;
                } catch (NumberFormatException e) {
                    // Default to false
                }
            }
        }

        // Calculate maxLapCount as the maximum displayable lap (lap - 1)
        int maxLapCount = 0;
        for (TResults result : results) {
            if (result.getLap() != null && result.getLap() > 0) {
                int displayLap = result.getLap() - 1;  // lap - 1
                if (displayLap > maxLapCount) {
                    maxLapCount = displayLap;
                }
            }
        }

        // Sort by rank1mix, rank1cat, lap (zeros last)
        List<TResults> sortedResults = results.stream()
            .sorted((a, b) -> {
                // Non-zero ranks first
                Integer aRank1mix = a.getRank1mix() != null && a.getRank1mix() > 0 ? a.getRank1mix() : Integer.MAX_VALUE;
                Integer bRank1mix = b.getRank1mix() != null && b.getRank1mix() > 0 ? b.getRank1mix() : Integer.MAX_VALUE;
                if (!aRank1mix.equals(bRank1mix)) return aRank1mix.compareTo(bRank1mix);
                
                Integer aRank1cat = a.getRank1cat() != null && a.getRank1cat() > 0 ? a.getRank1cat() : Integer.MAX_VALUE;
                Integer bRank1cat = b.getRank1cat() != null && b.getRank1cat() > 0 ? b.getRank1cat() : Integer.MAX_VALUE;
                if (!aRank1cat.equals(bRank1cat)) return aRank1cat.compareTo(bRank1cat);
                
                Integer aLap = a.getLap() != null && a.getLap() > 0 ? a.getLap() : Integer.MAX_VALUE;
                Integer bLap = b.getLap() != null && b.getLap() > 0 ? b.getLap() : Integer.MAX_VALUE;
                return aLap.compareTo(bLap);
            })
            .collect(Collectors.toList());

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("GenderRank");
            
            CellStyle r1Style = createTitleStyle(workbook);
            CellStyle headingStyle = createHeadingStyle(workbook);
            CellStyle headerStyle = createFieldHeaderStyle(workbook);
            CellStyle footerStyle = createFooterStyle(workbook);
            CellStyle dataStyleDefault = createDataStyle(workbook, false);
            CellStyle dataStyleAlt = createDataStyle(workbook, true);
            
            int rowIdx = 0;
            rowIdx = addEventHeader(sheet, event, org, "Gender Rank - " + distance + " (" + gender + ")", rowIdx, r1Style, headingStyle);
            rowIdx++; // Empty row
            
            // Build headers for LAP mode
            List<String> headers = new java.util.ArrayList<>();
            headers.add("Rank");
            headers.add("Category Rank");
            headers.add("Lap");
            headers.add("Bib");
            headers.add("Name");
            headers.add("Category");
            headers.add("TimeStart");
            headers.add("Official Time");
            headers.add("Net Time");
            
            if (includeHalfLap) {
                headers.add("1/2 Lap");
            }
            // Create headers for each displayable lap (1 to maxLapCount)
            for (int i = 1; i <= maxLapCount; i++) {
                headers.add("Lap " + i);
            }
            
            headers.add("TimeFinish");

            Row headerRow = sheet.createRow(rowIdx++);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }
            
            // Add data rows
            for (TResults result : sortedResults) {
                boolean alternate = (rowIdx % 2 == 0);
                CellStyle rowStyle = alternate ? dataStyleAlt : dataStyleDefault;
                Row row = sheet.createRow(rowIdx++);
                
                int colIdx = 0;

                // Rank (Gender Rank)
                Cell c0 = row.createCell(colIdx++);
                c0.setCellValue(result.getRank1mix() != null ? result.getRank1mix() : 0);
                c0.setCellStyle(rowStyle);

                // Category Rank
                Cell c1 = row.createCell(colIdx++);
                c1.setCellValue(result.getRank1cat() != null ? result.getRank1cat() : 0);
                c1.setCellStyle(rowStyle);

                // Lap
                Cell c2 = row.createCell(colIdx++);
                c2.setCellValue(result.getLap() != null ? result.getLap() : 0);
                c2.setCellStyle(rowStyle);

                // Bib
                Cell c3 = row.createCell(colIdx++);
                c3.setCellValue(result.getBib() != null ? result.getBib() : "");
                c3.setCellStyle(rowStyle);

                // Name
                Cell c4 = row.createCell(colIdx++);
                c4.setCellValue(result.getName() != null ? result.getName() : "");
                c4.setCellStyle(rowStyle);

                // Category
                Cell c5 = row.createCell(colIdx++);
                c5.setCellValue(result.getCategory() != null ? result.getCategory() : "");
                c5.setCellStyle(rowStyle);

                // TimeStart
                Cell c6 = row.createCell(colIdx++);
                c6.setCellValue(result.getTimestart() != null ? TimeFormatUtil.intToTimeString(result.getTimestart()) : "");
                c6.setCellStyle(rowStyle);

                // Official Time (timefinish - timegun)
                Cell c7 = row.createCell(colIdx++);
                String officialTime = "";
                if (result.getTimefinish() != null && result.getTimegun() != null) {
                    officialTime = TimeFormatUtil.intToTimeString(result.getTimefinish() - result.getTimegun());
                }
                c7.setCellValue(officialTime);
                c7.setCellStyle(rowStyle);

                // Net Time (timefinish - timestart)
                Cell c8 = row.createCell(colIdx++);
                String netTime = "";
                if (result.getTimefinish() != null && result.getTimestart() != null) {
                    netTime = TimeFormatUtil.intToTimeString(result.getTimefinish() - result.getTimestart());
                }
                c8.setCellValue(netTime);
                c8.setCellStyle(rowStyle);

                // Lap times (display from time(startIndex) to time(lap-1))
                Integer lapCount = result.getLap();
                int displayMaxLap = (lapCount != null && lapCount > 0) ? lapCount - 1 : 0;
                int startIndex = includeHalfLap ? 0 : 1;
                
                // Add time0 if needed
                if (includeHalfLap) {
                    try {
                        Method getter = TResults.class.getMethod("getTime0");
                        Integer timeValue = (Integer) getter.invoke(result);
                        Cell lapCell = row.createCell(colIdx++);
                        if (timeValue != null) {
                            lapCell.setCellValue(TimeFormatUtil.intToTimeString(timeValue));
                        } else {
                            lapCell.setCellValue("");
                        }
                        lapCell.setCellStyle(rowStyle);
                    } catch (Exception e) {
                        Cell lapCell = row.createCell(colIdx++);
                        lapCell.setCellValue("");
                        lapCell.setCellStyle(rowStyle);
                    }
                }
                
                // Add lap times 1 to maxLapCount
                for (int i = 1; i <= maxLapCount; i++) {
                    try {
                        Method getter = TResults.class.getMethod("getTime" + i);
                        Integer timeValue = (Integer) getter.invoke(result);
                        Cell lapCell = row.createCell(colIdx++);
                        // Only display if this lap was completed (i <= displayMaxLap)
                        if (i <= displayMaxLap && timeValue != null) {
                            lapCell.setCellValue(TimeFormatUtil.intToTimeString(timeValue));
                        } else {
                            lapCell.setCellValue("");
                        }
                        lapCell.setCellStyle(rowStyle);
                    } catch (Exception e) {
                        Cell lapCell = row.createCell(colIdx++);
                        lapCell.setCellValue("");
                        lapCell.setCellStyle(rowStyle);
                    }
                }

                // TimeFinish
                Cell finishCell = row.createCell(colIdx++);
                finishCell.setCellValue(result.getTimefinish() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()) : "");
                finishCell.setCellStyle(rowStyle);
            }

            addFooter(sheet, rowIdx, headers.size() - 1, org, footerStyle);

            // Auto-size columns
            int totalColumns = headers.size();
            for (int i = 0; i < totalColumns; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, Math.max(sheet.getColumnWidth(i), 120 * 36));
            }
            
            workbook.write(out);
            return out.toByteArray();
        }
    }

    private byte[] generateGenderRankTimeExcel(List<TResults> dbResults, List<TEventCat> eventcat, TEvent event, TOrg org, String distance, String gender) throws Exception {
        // Convert to EventCategoryResultResponse
        final String cplist = (eventcat != null && !eventcat.isEmpty()) ? eventcat.get(0).getCplist() : null;
        List<EventCategoryResultResponse> results = dbResults.stream().map(result -> {
            EventCategoryResultResponse dto = new EventCategoryResultResponse();
            dto.setCplist(cplist);
            dto.setName(result.getName());
            dto.setBib(result.getBib());
            dto.setCategory(result.getCategory());
            dto.setEventId(result.getEventId());
            dto.setCat(result.getCat());
            dto.setRank1Cat(result.getRank1cat());
            dto.setRank1Mix(result.getRank1mix());
            dto.setRank1Tot(result.getRank1tot());
            dto.setNetTime(result.getTimefinish() != null && result.getTimestart() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimestart()) : null);
            dto.setOfficialTime(result.getTimefinish() != null && result.getTimegun() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimegun()) : null);
            dto.setTimeStart(result.getTimestart() != null ? TimeFormatUtil.intToTimeString(result.getTimestart()) : null);
            dto.setTimeFinish(result.getTimefinish() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()) : null);
            dto.setTimeCP1(result.getTime1() != null ? TimeFormatUtil.intToTimeString(result.getTime1()) : null);
            dto.setTimeCP2(result.getTime2() != null ? TimeFormatUtil.intToTimeString(result.getTime2()) : null);
            dto.setTimeCP3(result.getTime3() != null ? TimeFormatUtil.intToTimeString(result.getTime3()) : null);
            dto.setTimeCP4(result.getTime4() != null ? TimeFormatUtil.intToTimeString(result.getTime4()) : null);
            dto.setTimeCP5(result.getTime5() != null ? TimeFormatUtil.intToTimeString(result.getTime5()) : null);
            dto.setTimeCP6(result.getTime6() != null ? TimeFormatUtil.intToTimeString(result.getTime6()) : null);
            dto.setTimeCP7(result.getTime7() != null ? TimeFormatUtil.intToTimeString(result.getTime7()) : null);
            dto.setTimeCP8(result.getTime8() != null ? TimeFormatUtil.intToTimeString(result.getTime8()) : null);
            dto.setTimeCP9(result.getTime9() != null ? TimeFormatUtil.intToTimeString(result.getTime9()) : null);
            dto.setTimeCP10(result.getTime10() != null ? TimeFormatUtil.intToTimeString(result.getTime10()) : null);
            return dto;
        }).collect(Collectors.toList());
        
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("GenderRank");
            
            CellStyle r1Style = createTitleStyle(workbook);
            CellStyle headingStyle = createHeadingStyle(workbook);
            CellStyle headerStyle = createFieldHeaderStyle(workbook);
            CellStyle footerStyle = createFooterStyle(workbook);
            CellStyle dataStyleDefault = createDataStyle(workbook, false);
            CellStyle dataStyleAlt = createDataStyle(workbook, true);
            
            int rowIdx = 0;
            rowIdx = addEventHeader(sheet, event, org, "Gender Rank - " + distance + " (" + gender + ")", rowIdx, r1Style, headingStyle);
            rowIdx++; // Empty row
            
            List<String> headers = new java.util.ArrayList<>();
            headers.add("Rank");
            headers.add("Category Rank");
            headers.add("Bib");
            headers.add("Name");
            headers.add("Category");
            headers.add("Official Time");
            headers.add("Net Time");
            headers.add("TimeStart");

            if (cplist != null && !cplist.isEmpty()) {
                String[] cps = cplist.split(",");
                for (String cp : cps) {
                    String cpTrim = cp.trim();
                    if (!cpTrim.isEmpty()) {
                        headers.add(cpTrim);
                    }
                }
            }

            headers.add("TimeFinish");

            Row headerRow = sheet.createRow(rowIdx++);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }
            
            for (EventCategoryResultResponse dto : results) {
                boolean alternate = (rowIdx % 2 == 0);
                CellStyle rowStyle = alternate ? dataStyleAlt : dataStyleDefault;
                Row row = sheet.createRow(rowIdx++);
                
                int colIdx = 0;

                Cell c0 = row.createCell(colIdx++);
                c0.setCellValue(dto.getRank1Mix());
                c0.setCellStyle(rowStyle);

                Cell c1 = row.createCell(colIdx++);
                c1.setCellValue(dto.getRank1Cat());
                c1.setCellStyle(rowStyle);

                Cell c2 = row.createCell(colIdx++);
                c2.setCellValue(dto.getBib() != null ? dto.getBib() : "");
                c2.setCellStyle(rowStyle);

                Cell c3 = row.createCell(colIdx++);
                c3.setCellValue(dto.getName() != null ? dto.getName() : "");
                c3.setCellStyle(rowStyle);

                Cell c4 = row.createCell(colIdx++);
                c4.setCellValue(dto.getCat() != null ? dto.getCat() : "");
                c4.setCellStyle(rowStyle);

                Cell c5 = row.createCell(colIdx++);
                c5.setCellValue(dto.getOfficialTime() != null ? dto.getOfficialTime() : "");
                c5.setCellStyle(rowStyle);

                Cell c6 = row.createCell(colIdx++);
                c6.setCellValue(dto.getNetTime() != null ? dto.getNetTime() : "");
                c6.setCellStyle(rowStyle);

                Cell c7 = row.createCell(colIdx++);
                c7.setCellValue(dto.getTimeStart() != null ? dto.getTimeStart() : "");
                c7.setCellStyle(rowStyle);

                if (cplist != null && !cplist.isEmpty()) {
                    String[] cps = cplist.split(",");
                    for (String cp : cps) {
                        String cpTrim = cp.trim();
                        String value = "";
                        switch (cpTrim) {
                            case "TimeCP1": value = dto.getTimeCP1(); break;
                            case "TimeCP2": value = dto.getTimeCP2(); break;
                            case "TimeCP3": value = dto.getTimeCP3(); break;
                            case "TimeCP4": value = dto.getTimeCP4(); break;
                            case "TimeCP5": value = dto.getTimeCP5(); break;
                            case "TimeCP6": value = dto.getTimeCP6(); break;
                            case "TimeCP7": value = dto.getTimeCP7(); break;
                            case "TimeCP8": value = dto.getTimeCP8(); break;
                            case "TimeCP9": value = dto.getTimeCP9(); break;
                            case "TimeCP10": value = dto.getTimeCP10(); break;
                            default: value = ""; break;
                        }
                        Cell cpCell = row.createCell(colIdx++);
                        cpCell.setCellValue(value != null ? value : "");
                        cpCell.setCellStyle(rowStyle);
                    }
                }

                Cell finishCell = row.createCell(colIdx++);
                finishCell.setCellValue(dto.getTimeFinish() != null ? dto.getTimeFinish() : "");
                finishCell.setCellStyle(rowStyle);
            }

            addFooter(sheet, rowIdx, headers.size() - 1, org, footerStyle);

            int totalColumns = headers.size();
            for (int i = 1; i < totalColumns; i++) {
                sheet.autoSizeColumn(i);
            }

            sheet.setColumnWidth(0, 80 * 36);  // Rank - Fixed width, no auto-size
            sheet.setColumnWidth(1, Math.max(sheet.getColumnWidth(1), 80 * 36));  // Category Rank
            sheet.setColumnWidth(2, Math.max(sheet.getColumnWidth(2), 80 * 36));  // Bib
            sheet.setColumnWidth(3, Math.max(sheet.getColumnWidth(3), 250 * 36)); // Name
            sheet.setColumnWidth(4, Math.max(sheet.getColumnWidth(4), 150 * 36)); // Category
            sheet.setColumnWidth(5, Math.max(sheet.getColumnWidth(5), 120 * 36)); // Official Time
            sheet.setColumnWidth(6, Math.max(sheet.getColumnWidth(6), 120 * 36)); // Net Time
            sheet.setColumnWidth(7, Math.max(sheet.getColumnWidth(7), 120 * 36)); // TimeStart

            for (int i = 8; i < totalColumns; i++) {
                sheet.setColumnWidth(i, Math.max(sheet.getColumnWidth(i), 120 * 36));
            }
            
            workbook.write(out);
            return out.toByteArray();
        }
    }
}