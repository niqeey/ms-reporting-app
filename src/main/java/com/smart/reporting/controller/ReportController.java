package com.smart.reporting.controller;

import com.smart.reporting.dto.EventCategoryResultRequest;
import com.smart.reporting.dto.EventCategoryResultResponse;
import com.smart.reporting.dto.LapResultResponse;
import com.smart.reporting.dto.CategoryResultListWrapper;
import com.smart.reporting.dto.OverallRankRequest;
import com.smart.reporting.dto.GenderRankRequest;
import com.smart.reporting.entity.TEvent;
import com.smart.reporting.entity.TEventCat;
import com.smart.reporting.entity.TOrg;
import com.smart.reporting.entity.TResults;
import com.smart.reporting.service.EventCatService;
import com.smart.reporting.service.EventService;
import com.smart.reporting.service.OrgService;
import com.smart.reporting.service.RaceResultService;
import com.smart.reporting.service.ReportExportService;
import com.smart.reporting.service.StatisticReportService;
import com.smart.reporting.util.TimeFormatUtil;
import com.smart.reporting.util.LapTimeCalculator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private RaceResultService raceResultService;

    @Autowired
    private EventCatService eventCatService;

    @Autowired
    private EventService eventService;

    @Autowired
    private OrgService orgService;

    @Autowired
    private ReportExportService reportExportService;

    @Autowired
    private StatisticReportService statisticReportService;

    @PostMapping("/event/calculate-ranks")
    public ResponseEntity<String> calculateRanks(
        @RequestHeader("OrgId") String orgId,
        @RequestBody EventCategoryResultRequest request) {
        try {
            statisticReportService.calculateRanks(request.getEventId());
            return ResponseEntity.ok("Ranks calculated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to calculate ranks: " + e.getMessage());
        }
    }

    @PostMapping("/event/overall-rank")
    public CategoryResultListWrapper getOverallRank(@RequestBody OverallRankRequest request) {
        List<TResults> results = raceResultService.getResultsByEventAndDistanceOrderByRank1tot(request.getEventId(), request.getDistance());
        
        // Get eventcat from first result to determine mode
        String mode = "TIME";
        List<TEventCat> eventcat = null;
        if (results != null && !results.isEmpty()) {
            String cat = results.get(0).getCat();
            eventcat = eventCatService.getByEventIdAndCat(request.getEventId(), cat);
            if (eventcat != null && !eventcat.isEmpty()) {
                mode = eventcat.get(0).getRacemode();
            }
        }

        if ("LAP".equalsIgnoreCase(mode)) {
            // Return LAP mode response
            final List<TEventCat> finalEventcat = eventcat;
            List<LapResultResponse> lapResponseList = results.stream().map(result -> {
                LapResultResponse dto = new LapResultResponse();
                dto.setName(result.getName());
                dto.setBib(result.getBib());
                dto.setCategory(result.getCategory());
                dto.setEventId(result.getEventId());
                dto.setEventName(finalEventcat.get(0).getCategory());
                dto.setCat(result.getCat());
                // Set rank1Tot for overall ranking
                if (result.getRank1tot() != null && result.getRank1tot() > 0) {
                    dto.setRank1Cat(result.getRank1tot());
                }
                dto.setBonusLap(result.getBonuslap());
                dto.setDqLap(result.getDqLap());
                // Fixed formulas: netTime = timefinish - timegun, officialTime = timefinish - timestart
                dto.setNetTime(result.getTimefinish() != null && result.getTimegun() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimegun()) : null);
                dto.setOfficialTime(result.getTimefinish() != null && result.getTimestart() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimestart()) : null);
                dto.setTimeStart(result.getTimestart() != null ? TimeFormatUtil.intToTimeString(result.getTimestart()) : "0");
                dto.setTimeFinish(result.getTimefinish() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()) : "0");
                dto.setTimeGun(result.getTimegun() != null ? TimeFormatUtil.intToTimeString(result.getTimegun()) : "0");
                dto.setLap(result.getLap());
                dto.setCplist(finalEventcat.get(0).getCplist());
                
                // Set lap time values (time0 to time49) using reflection
                // Check cplist to determine if halflap > 0
                String cplist = finalEventcat.get(0).getCplist();
                boolean includeTimeZero = false;
                if (cplist != null && !cplist.isEmpty()) {
                    String[] cplistParts = cplist.split(",");
                    if (cplistParts.length > 0) {
                        try {
                            int halflap = Integer.parseInt(cplistParts[0].trim());
                            includeTimeZero = halflap > 0;
                        } catch (NumberFormatException e) {
                            // If parsing fails, default to false
                        }
                    }
                }
                int startIndex = includeTimeZero ? 0 : 1;
                // Use lap-1 to determine max lap times to display
                Integer lapCount = result.getLap();
                int maxLap = (lapCount != null && lapCount > 0) ? lapCount - 1 : 0;
                int maxIndex = Math.max(maxLap, 0);
                for (int i = startIndex; i <= maxIndex; i++) {
                    // Calculate lap interval instead of cumulative time
                    String intervalTime = LapTimeCalculator.calculateLapIntervalFormatted(result, i, includeTimeZero);
                    if (intervalTime != null) {
                        dto.setLapTime(i, intervalTime);
                    }
                }
                
                return dto;
            }).collect(Collectors.toList());
            
            return new CategoryResultListWrapper("LAP", lapResponseList);
        } else {
            // Return TIME mode response (default)
            final String cplist = (eventcat != null && !eventcat.isEmpty()) ? eventcat.get(0).getCplist() : null;
            List<EventCategoryResultResponse> responseList = results.stream().map(result -> {
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
                // Fixed formulas: netTime = timefinish - timegun, officialTime = timefinish - timestart
                dto.setNetTime(result.getTimefinish() != null && result.getTimegun() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimegun()) : null);
                dto.setOfficialTime(result.getTimefinish() != null && result.getTimestart() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimestart()) : null);
                dto.setTimeStart(result.getTimestart() != null ? TimeFormatUtil.intToTimeString(result.getTimestart()) : null);
                dto.setTimeFinish(result.getTimefinish() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()) : null);
                dto.setTimeGun(result.getTimegun() != null ? TimeFormatUtil.intToTimeString(result.getTimegun()) : null);
                dto.setTimeCP1(result.getTimecp1() != null ? TimeFormatUtil.intToTimeString(result.getTimecp1()-result.getTimegun()) : null);
                dto.setTimeCP2(result.getTimecp2() != null ? TimeFormatUtil.intToTimeString(result.getTimecp2()-result.getTimegun()) : null);
                dto.setTimeCP3(result.getTimecp3() != null ? TimeFormatUtil.intToTimeString(result.getTimecp3()-result.getTimegun()) : null);
                dto.setTimeCP4(result.getTimecp4() != null ? TimeFormatUtil.intToTimeString(result.getTimecp4()-result.getTimegun()) : null);
                dto.setTimeCP5(result.getTimecp5() != null ? TimeFormatUtil.intToTimeString(result.getTimecp5()-result.getTimegun()) : null);
                dto.setTimeCP6(result.getTimecp6() != null ? TimeFormatUtil.intToTimeString(result.getTimecp6()-result.getTimegun()) : null);
                dto.setTimeCP7(result.getTimecp7() != null ? TimeFormatUtil.intToTimeString(result.getTimecp7()-result.getTimegun()) : null);
                dto.setTimeCP8(result.getTimecp8() != null ? TimeFormatUtil.intToTimeString(result.getTimecp8()-result.getTimegun()) : null);
                dto.setTimeCP9(result.getTimecp9() != null ? TimeFormatUtil.intToTimeString(result.getTimecp9()-result.getTimegun()) : null);
                dto.setTimeCP10(result.getTimecp10() != null ? TimeFormatUtil.intToTimeString(result.getTimecp10()-result.getTimegun()) : null);
                
                // Only set TimeCP values that are in cplist
                if (cplist != null && !cplist.isEmpty()) {
                    String[] cps = cplist.split(",");
                    for (String cp : cps) {
                        String cpTrim = cp.trim();
                        switch (cpTrim) {
                            case "TimeCP1":
                                dto.setTimeCP1(result.getTimecp1() != null ? TimeFormatUtil.intToTimeString(result.getTimecp1()) : null);
                                break;
                            case "TimeCP2":
                                dto.setTimeCP2(result.getTimecp2() != null ? TimeFormatUtil.intToTimeString(result.getTimecp2()) : null);
                                break;
                            case "TimeCP3":
                                dto.setTimeCP3(result.getTimecp3() != null ? TimeFormatUtil.intToTimeString(result.getTimecp3()) : null);
                                break;
                            case "TimeCP4":
                                dto.setTimeCP4(result.getTimecp4() != null ? TimeFormatUtil.intToTimeString(result.getTimecp4()) : null);
                                break;
                            case "TimeCP5":
                                dto.setTimeCP5(result.getTimecp5() != null ? TimeFormatUtil.intToTimeString(result.getTimecp5()) : null);
                                break;
                            case "TimeCP6":
                                dto.setTimeCP6(result.getTimecp6() != null ? TimeFormatUtil.intToTimeString(result.getTimecp6()) : null);
                                break;
                            case "TimeCP7":
                                dto.setTimeCP7(result.getTimecp7() != null ? TimeFormatUtil.intToTimeString(result.getTimecp7()) : null);
                                break;
                            case "TimeCP8":
                                dto.setTimeCP8(result.getTimecp8() != null ? TimeFormatUtil.intToTimeString(result.getTimecp8()) : null);
                                break;
                            case "TimeCP9":
                                dto.setTimeCP9(result.getTimecp9() != null ? TimeFormatUtil.intToTimeString(result.getTimecp9()) : null);
                                break;
                            case "TimeCP10":
                                dto.setTimeCP10(result.getTimecp10() != null ? TimeFormatUtil.intToTimeString(result.getTimecp10()) : null);
                                break;
                        }
                    }
                }
                
                return dto;
            }).collect(Collectors.toList());
            return new CategoryResultListWrapper("TIME", responseList);
        }
    }

    @PostMapping("/event/gender-rank")
    public CategoryResultListWrapper getGenderRank(@RequestBody GenderRankRequest request) {
        System.out.println("DEBUG: EventId=" + request.getEventId() + ", Distance=" + request.getDistance() + ", Gender=" + request.getGender());
        
        // Get results for gender ranking
        List<TResults> results = raceResultService.getResultsByEventAndDistanceAndGenderOrderByRank1mix(
            request.getEventId(), request.getDistance(), request.getGender());
        
        // Get eventcat from first result to determine mode
        String mode = "TIME";
        List<TEventCat> eventcat = null;
        if (results != null && !results.isEmpty()) {
            String cat = results.get(0).getCat();
            eventcat = eventCatService.getByEventIdAndCat(request.getEventId(), cat);
            if (eventcat != null && !eventcat.isEmpty()) {
                mode = eventcat.get(0).getRacemode();
            }
        }

        if ("LAP".equalsIgnoreCase(mode)) {
            // Return LAP mode response
            final List<TEventCat> finalEventcat = eventcat;
            List<LapResultResponse> lapResponseList = results.stream().map(result -> {
                LapResultResponse dto = new LapResultResponse();
                dto.setName(result.getName());
                dto.setBib(result.getBib());
                dto.setCategory(result.getCategory());
                dto.setEventId(result.getEventId());
                dto.setEventName(finalEventcat.get(0).getCategory());
                dto.setCat(result.getCat());
                // Set rank1Mix for gender ranking
                if (result.getRank1mix() != null && result.getRank1mix() > 0) {
                    dto.setRank1Cat(result.getRank1mix());
                }
                dto.setBonusLap(result.getBonuslap());
                dto.setDqLap(result.getDqLap());
                // Fixed formulas: netTime = timefinish - timegun, officialTime = timefinish - timestart
                dto.setNetTime(result.getTimefinish() != null && result.getTimegun() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimegun()) : null);
                dto.setOfficialTime(result.getTimefinish() != null && result.getTimestart() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimestart()) : null);
                dto.setTimeStart(result.getTimestart() != null ? TimeFormatUtil.intToTimeString(result.getTimestart()) : "0");
                dto.setTimeFinish(result.getTimefinish() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()) : "0");
                dto.setTimeGun(result.getTimegun() != null ? TimeFormatUtil.intToTimeString(result.getTimegun()) : "0");
                dto.setLap(result.getLap());
                dto.setCplist(finalEventcat.get(0).getCplist());
                
                // Set lap time values (time0 to time49) using reflection
                // Check cplist to determine if halflap > 0
                String cplist = finalEventcat.get(0).getCplist();
                boolean includeTimeZero = false;
                if (cplist != null && !cplist.isEmpty()) {
                    String[] cplistParts = cplist.split(",");
                    if (cplistParts.length > 0) {
                        try {
                            int halflap = Integer.parseInt(cplistParts[0].trim());
                            includeTimeZero = halflap > 0;
                        } catch (NumberFormatException e) {
                            // If parsing fails, default to false
                        }
                    }
                }
                int startIndex = includeTimeZero ? 0 : 1;
                // Use lap-1 to determine max lap times to display
                Integer lapCount = result.getLap();
                int maxLap = (lapCount != null && lapCount > 0) ? lapCount - 1 : 0;
                int maxIndex = Math.max(maxLap, 0);
                for (int i = startIndex; i <= maxIndex; i++) {
                    // Calculate lap interval instead of cumulative time
                    String intervalTime = LapTimeCalculator.calculateLapIntervalFormatted(result, i, includeTimeZero);
                    if (intervalTime != null) {
                        dto.setLapTime(i, intervalTime);
                    }
                }
                
                return dto;
            }).collect(Collectors.toList());
            
            return new CategoryResultListWrapper("LAP", lapResponseList);
        } else {
            // Return TIME mode response (default)
            List<EventCategoryResultResponse> timeResults = statisticReportService.getGenderRankResults(
                request.getEventId(), request.getDistance(), request.getGender());
            System.out.println("DEBUG: Results count=" + timeResults.size());
            return new CategoryResultListWrapper("TIME", timeResults);
        }
    }

    @PostMapping("/event/overall-rank/xlsx")
    public ResponseEntity<byte[]> downloadOverallRankAsXlsx(
        @RequestHeader("OrgId") String orgId,
        @RequestBody OverallRankRequest request) throws Exception {
        byte[] excelBytes = reportExportService.generateOverallRankExcel(
            request.getEventId(), request.getDistance(), orgId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", 
            "overall_rank_" + request.getDistance() + ".xlsx");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelBytes);
    }

    @PostMapping("/event/gender-rank/xlsx")
    public ResponseEntity<byte[]> downloadGenderRankAsXlsx(
        @RequestHeader("OrgId") String orgId,
        @RequestBody GenderRankRequest request) throws Exception {
        byte[] excelBytes = reportExportService.generateGenderRankExcel(
            request.getEventId(), request.getDistance(), request.getGender(), orgId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", 
            "gender_rank_" + request.getDistance() + "_" + request.getGender() + ".xlsx");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelBytes);
    }

    @PostMapping("/event/category/top")
    public List<EventCategoryResultResponse> getTopResultsByEventAndCategory(@RequestBody EventCategoryResultRequest request) {
        List<TResults> results = raceResultService.getTopResultsByEventAndCat(request.getEventId(), request.getCategory());
        List<TEventCat> eventcat= eventCatService.getByEventIdAndCat(request.getEventId(), request.getCategory());
        List<EventCategoryResultResponse> responseList = results.stream().map(result -> {
            EventCategoryResultResponse dto = new EventCategoryResultResponse();
            dto.setCplist(eventcat.get(0).getCplist());
            dto.setName(result.getName());
            dto.setBib(result.getBib());
            dto.setCategory(result.getCategory());
            dto.setEventId(result.getEventId());
            dto.setEventName(eventcat.get(0).getCategory());
            dto.setCat(result.getCat());
            dto.setRank1Cat(result.getRank1cat());
            dto.setRank1Mix(result.getRank1mix());
            dto.setRank1Tot(result.getRank1tot());
            // Fixed formulas: netTime = timefinish - timegun, officialTime = timefinish - timestart
            dto.setNetTime(result.getTimefinish() != null && result.getTimegun() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimegun()) : null);
            dto.setOfficialTime(result.getTimefinish() != null && result.getTimestart() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimestart()) : null);
            dto.setTimeStart(result.getTimestart() != null ? TimeFormatUtil.intToTimeString(result.getTimestart()) : null);
            dto.setTimeFinish(result.getTimefinish() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()) : null);
            dto.setTimeGun(result.getTimegun() != null ? TimeFormatUtil.intToTimeString(result.getTimegun()) : null);
            dto.setTimeCP1(result.getTimecp1() != null ? TimeFormatUtil.intToTimeString(result.getTimecp1()-result.getTimegun()) : null);
            dto.setTimeCP2(result.getTimecp2() != null ? TimeFormatUtil.intToTimeString(result.getTimecp2()-result.getTimegun()) : null);
            dto.setTimeCP3(result.getTimecp3() != null ? TimeFormatUtil.intToTimeString(result.getTimecp3()-result.getTimegun()) : null);
            dto.setTimeCP4(result.getTimecp4() != null ? TimeFormatUtil.intToTimeString(result.getTimecp4()-result.getTimegun()) : null);
            dto.setTimeCP5(result.getTimecp5() != null ? TimeFormatUtil.intToTimeString(result.getTimecp5()-result.getTimegun()) : null);
            dto.setTimeCP6(result.getTimecp6() != null ? TimeFormatUtil.intToTimeString(result.getTimecp6()-result.getTimegun()) : null);
            dto.setTimeCP7(result.getTimecp7() != null ? TimeFormatUtil.intToTimeString(result.getTimecp7()-result.getTimegun()) : null);
            dto.setTimeCP8(result.getTimecp8() != null ? TimeFormatUtil.intToTimeString(result.getTimecp8()-result.getTimegun()) : null);
            dto.setTimeCP9(result.getTimecp9() != null ? TimeFormatUtil.intToTimeString(result.getTimecp9()-result.getTimegun()) : null);
            dto.setTimeCP10(result.getTimecp10() != null ? TimeFormatUtil.intToTimeString(result.getTimecp10()-result.getTimegun()) : null);
            // Add any additional fields as needed
            return dto;
        }).collect(Collectors.toList());

        // You can add additional processing or logging here if needed
        return responseList;
    }

    @PostMapping("/event/category")
    public CategoryResultListWrapper getResultsByEventAndCategory(@RequestBody EventCategoryResultRequest request) {
        List<TResults> results = raceResultService.getResultsByEventAndCat(request.getEventId(), request.getCategory());
        List<TEventCat> eventcat= eventCatService.getByEventIdAndCat(request.getEventId(), request.getCategory());
        String mode = eventcat.get(0).getRacemode();

        if ("LAP".equalsIgnoreCase(mode)) {
            // Return LAP mode response
            List<LapResultResponse> lapResponseList = results.stream().map(result -> {
                LapResultResponse dto = new LapResultResponse();
                dto.setName(result.getName());
                dto.setBib(result.getBib());
                dto.setCategory(result.getCategory());
                dto.setEventId(result.getEventId());
                dto.setEventName(eventcat.get(0).getCategory());
                dto.setCat(result.getCat());
                // Only set rank1Cat if it's greater than 0
                if (result.getRank1cat() != null && result.getRank1cat() > 0) {
                    dto.setRank1Cat(result.getRank1cat());
                }
                dto.setBonusLap(result.getBonuslap());
                dto.setDqLap(result.getDqLap());
                dto.setNetTime(result.getTimefinish() != null && result.getTimestart() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimestart()) : null);
                dto.setOfficialTime(result.getTimefinish() != null && result.getTimegun() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimegun()) : null);
                dto.setTimeStart(result.getTimestart() != null ? TimeFormatUtil.intToTimeString(result.getTimestart()) : "0");
                dto.setTimeFinish(result.getTimefinish() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()) : "0");
                dto.setTimeGun(result.getTimegun() != null ? TimeFormatUtil.intToTimeString(result.getTimegun()) : "0");
                
                // Calculate lap count dynamically based on highest lap number with data
                Integer maxLap = null;
                
                // Set lap time values (time0 to time49) using reflection
                // Check cplist to determine if halflap > 0
                String cplist = eventcat.get(0).getCplist();
                boolean includeTimeZero = false;
                if (cplist != null && !cplist.isEmpty()) {
                    String[] cplistParts = cplist.split(",");
                    if (cplistParts.length > 0) {
                        try {
                            int halflap = Integer.parseInt(cplistParts[0].trim());
                            includeTimeZero = halflap > 0;
                        } catch (NumberFormatException e) {
                            // If parsing fails, default to false
                        }
                    }
                }
                int startIndex = includeTimeZero ? 0 : 1;
                int maxIndex = startIndex + 50;
                for (int i = startIndex; i < maxIndex; i++) {
                    // Calculate lap interval instead of cumulative time
                    String intervalTime = LapTimeCalculator.calculateLapIntervalFormatted(result, i, includeTimeZero);
                    if (intervalTime != null) {
                        dto.setLapTime(i, intervalTime);
                        // Only count actual laps (time1+), not half-lap (time0)
                        if (i > 0) {
                            maxLap = i;
                        }
                    }
                }
                
                // Set calculated lap count
                dto.setLap(maxLap);
                
                return dto;
            }).collect(Collectors.toList());
            
            return new CategoryResultListWrapper("LAP", lapResponseList);
        } else {
            // Return NORMAL mode response (default)
            List<EventCategoryResultResponse> responseList = results.stream().map(result -> {
                EventCategoryResultResponse dto = new EventCategoryResultResponse();
                dto.setCplist(eventcat.get(0).getCplist());
                dto.setName(result.getName());
                dto.setBib(result.getBib());
                dto.setCategory(result.getCategory());
                dto.setEventId(result.getEventId());
                dto.setEventName(eventcat.get(0).getCategory());
                dto.setCat(result.getCat());
                // Only set rank1Cat if it's greater than 0
                if (result.getRank1cat() != null && result.getRank1cat() > 0) {
                    dto.setRank1Cat(result.getRank1cat());
                }
                dto.setRank1Mix(result.getRank1mix());
                dto.setRank1Tot(result.getRank1tot());
                dto.setNetTime(result.getTimefinish() != null && result.getTimestart() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimestart()) : null);
                dto.setOfficialTime(result.getTimefinish() != null && result.getTimegun() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimegun()) : null);
                dto.setTimeStart(result.getTimestart() != null ? TimeFormatUtil.intToTimeString(result.getTimestart()) : "0");
                dto.setTimeFinish(result.getTimefinish() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()) : "0");
                dto.setTimeGun(result.getTimegun() != null ? TimeFormatUtil.intToTimeString(result.getTimegun()) : "0");
                dto.setTimeCP1(result.getTimecp1() != null ? TimeFormatUtil.intToTimeString(result.getTimecp1()-result.getTimegun()) : null);
                dto.setTimeCP2(result.getTimecp2() != null ? TimeFormatUtil.intToTimeString(result.getTimecp2()-result.getTimegun()) : null);
                dto.setTimeCP3(result.getTimecp3() != null ? TimeFormatUtil.intToTimeString(result.getTimecp3()-result.getTimegun()) : null);
                dto.setTimeCP4(result.getTimecp4() != null ? TimeFormatUtil.intToTimeString(result.getTimecp4()-result.getTimegun()) : null);
                dto.setTimeCP5(result.getTimecp5() != null ? TimeFormatUtil.intToTimeString(result.getTimecp5()-result.getTimegun()) : null);
                dto.setTimeCP6(result.getTimecp6() != null ? TimeFormatUtil.intToTimeString(result.getTimecp6()-result.getTimegun()) : null);
                dto.setTimeCP7(result.getTimecp7() != null ? TimeFormatUtil.intToTimeString(result.getTimecp7()-result.getTimegun()) : null);
                dto.setTimeCP8(result.getTimecp8() != null ? TimeFormatUtil.intToTimeString(result.getTimecp8()-result.getTimegun()) : null);
                dto.setTimeCP9(result.getTimecp9() != null ? TimeFormatUtil.intToTimeString(result.getTimecp9()-result.getTimegun()) : null);
                dto.setTimeCP10(result.getTimecp10() != null ? TimeFormatUtil.intToTimeString(result.getTimecp10()-result.getTimegun()) : null);
                return dto;
            }).collect(Collectors.toList());

            return new CategoryResultListWrapper("NORMAL", responseList);
        }
    }

    @PostMapping("/event/category/top/xlsx")
    public ResponseEntity<byte[]> downloadTopResultsByEventAndCategoryAsXlsx(
        @RequestHeader("OrgId") String orgId,
        @RequestBody EventCategoryResultRequest request) throws Exception {
        List<TResults> results = raceResultService.getTopResultsByEventAndCat(request.getEventId(), request.getCategory());
        List<TEventCat> eventcat = eventCatService.getByEventIdAndCat(request.getEventId(), request.getCategory());
        TEvent event = eventService.getEventById(request.getEventId());
        Optional<TOrg> orgs = orgService.getOrgById(orgId); // Assuming this method exists to fetch organization details    


        String cplist = eventcat.get(0).getCplist();
        String catName = eventcat.get(0).getCategory();
        String eventNm = event.getName();
        String eventLoc = event.getLocation();
        String eventdt = event.getEventDt().toString();
        String eventWeather = event.getWeather(); // Assuming weather is a field in TEvent
        String orgName = orgs.map(TOrg::getAlias).orElse("MyPaceTracker");
        // You may want to fetch these from your event entity/service
        String eventName = eventNm; // Replace with actual event name if available
        String eventDate = eventdt; // Replace with actual date if available
        String title = "Official Result"; // Fixed string
        String location = "Location : "+ eventLoc;     // Replace with actual location if available
        String weather = "Weather : " + eventWeather;       // Replace with actual weather if available

        

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("TopResults");
            
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

            // Row 1: Event Name
            
            Row row1 = sheet.createRow(rowIdx++);
            Cell cell = row1.createCell(0);
            cell.setCellValue(eventName);
            cell.setCellStyle(r1Style);

            // Row 2: Date
            Row row2 = sheet.createRow(rowIdx++);
            Cell cell2 = row2.createCell(0);
            cell2.setCellValue(eventDate);
            cell2.setCellStyle(headingStyle);

            // Row 3: Title (fixed string)
            Row row3 = sheet.createRow(rowIdx++);
            Cell cell3 = row3.createCell(0);
            cell3.setCellValue(title);
            cell3.setCellStyle(headingStyle);

            // Row 4: Location
            Row row4 = sheet.createRow(rowIdx++);
            Cell cell4 = row4.createCell(0);
            cell4.setCellValue(location);
            cell4.setCellStyle(headingStyle);

            // Row 5: Weather
            Row row5 = sheet.createRow(rowIdx++);
            Cell cell5 = row5.createCell(0);
            cell5.setCellValue(weather);
            cell5.setCellStyle(headingStyle);

            // Row 6: Empty
            rowIdx++;

            // Row 7: catName
            Row row7 = sheet.createRow(rowIdx++);
            Cell cell7 = row7.createCell(0);
            cell7.setCellValue(catName);
            cell7.setCellStyle(catStyle);

            // Row 8: Field header (as before)
            Row field = sheet.createRow(rowIdx++);

            // Create header style: white font, dark background
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLACK.getIndex()); // BLACK font
            headerFont.setFontHeightInPoints((short) 11);
            headerFont.setFontName("Aptos Narrow");
            headerStyle.setFont(headerFont);

            // Set background to GREY_80_PERCENT (dark gray, closest to "background 1, darken 35%")
            headerStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Apply style to each header cell
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

            // Dynamically add TimeCP columns based on cplist
            int colIdx = 6;
            if (cplist != null && !cplist.isEmpty()) {
                String[] cps = cplist.split(",");
                for (String cp : cps) {
                    field.createCell(colIdx).setCellValue(cp.trim());
                    field.getCell(colIdx).setCellStyle(headerStyle);
                    colIdx++;
                }
            }

            // Add TimeFinish column after CPs
            field.createCell(colIdx).setCellValue("TimeFinish");
            field.getCell(colIdx).setCellStyle(headerStyle);
            colIdx++;

            // Add Country and NRIC columns
            field.createCell(colIdx).setCellValue("Country");
            field.getCell(colIdx).setCellStyle(headerStyle);
            colIdx++;

            field.createCell(colIdx).setCellValue("NRIC");
            field.getCell(colIdx).setCellStyle(headerStyle);

            // Create data row styles
            CellStyle dataStyleDefault = workbook.createCellStyle(); // No fill (default)
            Font dataFontDefault = workbook.createFont();
            dataFontDefault.setFontName("Aptos Narrow");
            dataFontDefault.setFontHeightInPoints((short) 11);
            dataStyleDefault.setFont(dataFontDefault);

            CellStyle dataStyleAlt = workbook.createCellStyle();
            Font dataFontAlt = workbook.createFont();
            dataFontAlt.setFontName("Aptos Narrow");
            dataFontAlt.setFontHeightInPoints((short) 11);
            dataFontAlt.setColor(IndexedColors.BLACK.getIndex()); // White font
            dataStyleAlt.setFont(dataFontAlt);
            dataStyleAlt.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); // Background 1, darken 15%
            dataStyleAlt.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Data rows (as before)
            for (TResults result : results) {
                Row row = sheet.createRow(rowIdx++);
                int dataColIdx = 0;
                // Alternate style: even rows = default, odd rows = alt
                CellStyle rowStyle = (rowIdx % 2 == 0) ? dataStyleAlt : dataStyleDefault ;

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

                // Dynamically add TimeCP values
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

                // TimeFinish
                Cell finishCell = row.createCell(dataColIdx++);
                finishCell.setCellValue(TimeFormatUtil.intToTimeString(result.getTimefinish()));
                finishCell.setCellStyle(rowStyle);

                // Country
                Cell countryCell = row.createCell(dataColIdx++);
                countryCell.setCellValue(result.getCountry() != null ? result.getCountry() : "");
                countryCell.setCellStyle(rowStyle);

                // NRIC
                Cell nricCell = row.createCell(dataColIdx++);
                nricCell.setCellValue(result.getNric() != null ? result.getNric() : "");
                nricCell.setCellStyle(rowStyle);
            }

            // After writing all data rows, add the footer row
           // rowIdx++; // Optionally add an empty row before the footer
            Row footerRow = sheet.createRow(rowIdx++);
            Cell footerCell = footerRow.createCell(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss.SS a");
            String formattedNow = LocalDateTime.now().format(formatter);
            String footerText = "Timing & Results by "+orgName+"; Printed at " + formattedNow;
            footerCell.setCellValue(footerText);

            // Create normal font, no fill style for footer
            CellStyle footerStyle = workbook.createCellStyle();
            Font footerFont = workbook.createFont();
            footerFont.setFontName("Aptos Narrow");
            footerFont.setFontHeightInPoints((short) 11);
            footerStyle.setFont(footerFont);
            // No fill (default)
            footerCell.setCellStyle(footerStyle);

            // Optionally, merge footer across all columns
            int lastCol = field.getLastCellNum() - 1;
            if (lastCol > 0) {
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(
                    footerRow.getRowNum(), footerRow.getRowNum(), 0, lastCol
                ));
            }

            // After creating the sheet and before writing data rows, set column widths:

            // Column indexes: 0 = A, 1 = B, 2 = C, 3 = D, ...
            // Set column C (index 2) to 350 pixels
            sheet.setColumnWidth(2, 300 * 36); // 1 Excel unit ≈ 1/256th of a character, 1 pixel ≈ 36 units

            // Set columns D (index 3) onwards to 145 pixels
            int totalColumns = field.getLastCellNum(); // or set a fixed max if you know it
            for (int i = 3; i < totalColumns; i++) {
                sheet.setColumnWidth(i, 100 * 36);
            }

            workbook.write(out);
            byte[] bytes = out.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=top_results.xlsx");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(bytes);
        }
    }

    @PostMapping("/event/category/xlsx")
    public ResponseEntity<byte[]> downloadResultsByEventAndCategoryAsXlsx(
        @RequestHeader("OrgId") String orgId,
        @RequestBody EventCategoryResultRequest request) throws Exception {

        List<TResults> results = raceResultService.getResultsByEventAndCat(request.getEventId(), request.getCategory());
        List<TEventCat> eventcat = eventCatService.getByEventIdAndCat(request.getEventId(), request.getCategory());
        TEvent event = eventService.getEventById(request.getEventId());
        Optional<TOrg> orgs = orgService.getOrgById(orgId);

        byte[] bytes = reportExportService.generateResultsXlsx(results, eventcat, event, orgs, "Results");

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=results.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }

    @PostMapping("/event/statistic/xlsx")
    public ResponseEntity<byte[]> downloadStatisticAsXlsx(@RequestBody EventCategoryResultRequest request) throws Exception {
        byte[] bytes = reportExportService.exportStatisticToExcel(request.getEventId());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=statistic.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    @PostMapping("/event/registered/xlsx")
    public ResponseEntity<byte[]> downloadRegisteredAsXlsx(@RequestBody EventCategoryResultRequest request) throws Exception {
        byte[] bytes = reportExportService.exportRegisteredToExcel(request.getEventId());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=registered.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    @PostMapping("/event/started/xlsx")
    public ResponseEntity<byte[]> downloadStartedAsXlsx(@RequestBody EventCategoryResultRequest request) throws Exception {
        byte[] bytes = reportExportService.exportStartedToExcel(request.getEventId());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=started.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    @PostMapping("/event/dns/xlsx")
    public ResponseEntity<byte[]> downloadDnsAsXlsx(@RequestBody EventCategoryResultRequest request) throws Exception {
        byte[] bytes = reportExportService.exportDnsToExcel(request.getEventId());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=did_not_start.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    @PostMapping("/event/finished/xlsx")
    public ResponseEntity<byte[]> downloadFinishedAsXlsx(@RequestBody EventCategoryResultRequest request) throws Exception {
        byte[] bytes = reportExportService.exportFinishedToExcel(request.getEventId());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=finished.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    @PostMapping("/event/dnf/xlsx")
    public ResponseEntity<byte[]> downloadDnfAsXlsx(@RequestBody EventCategoryResultRequest request) throws Exception {
        byte[] bytes = reportExportService.exportDnfToExcel(request.getEventId());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=did_not_finish.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    @PostMapping("/event/nsbf/xlsx")
    public ResponseEntity<byte[]> downloadNsbfAsXlsx(@RequestBody EventCategoryResultRequest request) throws Exception {
        byte[] bytes = reportExportService.exportNsbfToExcel(request.getEventId());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=no_start_but_finished.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    @PostMapping("/event/dq/xlsx")
    public ResponseEntity<byte[]> downloadDqAsXlsx(@RequestBody EventCategoryResultRequest request) throws Exception {
        byte[] bytes = reportExportService.exportDqToExcel(request.getEventId());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=disqualified.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).body(bytes);
    }
}
