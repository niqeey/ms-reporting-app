package com.smart.reporting.service;

import com.smart.reporting.dto.EventRequest;
import com.smart.reporting.dto.EventCategoryResultResponse;
import com.smart.reporting.dto.StatisticDnfDto;
import com.smart.reporting.dto.StatisticDnsDto;
import com.smart.reporting.dto.StatisticDqDto;
import com.smart.reporting.dto.StatisticFinishedDto;
import com.smart.reporting.dto.StatisticFsDto;
import com.smart.reporting.dto.StatisticNsbfDto;
import com.smart.reporting.dto.StatisticRegListDto;
import com.smart.reporting.dto.StatisticReportDto;
import com.smart.reporting.dto.StatisticStartListDto;
import com.smart.reporting.entity.TResults;
import com.smart.reporting.entity.TEventCat;
import com.smart.reporting.repository.TResultsRepository;
import com.smart.reporting.util.TimeFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticReportService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TResultsRepository tResultsRepository;
    
    @Autowired
    private EventCatService eventCatService;

    public List<Object[]> callStoredProcedure(String procedureName, String eventId) {
        return entityManager
                .createNativeQuery("CALL " + procedureName + "(:eventid)")
                .setParameter("eventid", eventId)
                .getResultList();
    }

    @Transactional
    public void calculateRanks(String eventId) {
        // Get all categories for this event
        List<Object[]> categories = entityManager
                .createNativeQuery("SELECT DISTINCT cat, distance FROM t_event_cat WHERE event_id = :eventid")
                .setParameter("eventid", eventId)
                .getResultList();
        
        // Get all genders for RANK1MIX
        List<String> genders = entityManager
                .createNativeQuery("SELECT DISTINCT sex FROM results WHERE EventId = :eventid AND sex IS NOT NULL")
                .setParameter("eventid", eventId)
                .getResultList();
        
        // Calculate RANK1CAT for each category
        for (Object[] catRow : categories) {
            String cat = (String) catRow[0];
            entityManager
                    .createNativeQuery("CALL P_ASSIGN_RANK1CAT(:eventid, :cat)")
                    .setParameter("eventid", eventId)
                    .setParameter("cat", cat)
                    .executeUpdate();
        }
        
        // Get distinct distances for RANK1TOT and RANK1MIX
        List<BigDecimal> distances = entityManager
                .createNativeQuery("SELECT DISTINCT distance FROM t_event_cat WHERE event_id = :eventid AND distance IS NOT NULL")
                .setParameter("eventid", eventId)
                .getResultList();
        
        // Calculate RANK1TOT for each distance
        for (BigDecimal distance : distances) {
            entityManager
                    .createNativeQuery("CALL P_ASSIGN_RANK1TOT(:eventid, :distance)")
                    .setParameter("eventid", eventId)
                    .setParameter("distance", distance)
                    .executeUpdate();
        }
        
        // Calculate RANK1MIX for each distance and gender combination
        for (BigDecimal distance : distances) {
            for (String gender : genders) {
                entityManager
                        .createNativeQuery("CALL P_ASSIGN_RANK1MIX(:eventid, :distance, :gender)")
                        .setParameter("eventid", eventId)
                        .setParameter("distance", distance)
                        .setParameter("gender", gender)
                        .executeUpdate();
            }
        }
    }

    public List<StatisticReportDto> getStatisticReport(EventRequest req) {
        String eventId = req.getEventId();
        List<Object[]> resultList = callStoredProcedure("P_STATISTIC_00_REPORT", eventId);

        List<StatisticReportDto> dtos = new ArrayList<>();
        for (Object[] row : resultList) {
            StatisticReportDto dto = new StatisticReportDto();
            dto.setStatistic((String) row[0]);
            dto.setCat((String) row[1]);
            dto.setCategory((String) row[2]);
            dto.setRegistered(row[3] != null ? ((Number) row[3]).intValue() : 0);
            dto.setStarted(row[4] != null ? ((Number) row[4]).intValue() : 0);
            dto.setDidNotStart(row[5] != null ? ((Number) row[5]).intValue() : 0);
            dto.setFinished(row[6] != null ? ((Number) row[6]).intValue() : 0);
            dto.setDidNotFinish(row[7] != null ? ((Number) row[7]).intValue() : 0);
            dto.setFalseStart(row[8] != null ? ((Number) row[8]).intValue() : 0);
            dto.setNoStartButFinished(row[9] != null ? ((Number) row[9]).intValue() : 0);
            dto.setDisqualified(row[10] != null ? ((Number) row[10]).intValue() : 0);
            if (row.length > 10 && row[11] != null) {
                dto.setDistance(((Number) row[11]).intValue());
            } else {
                dto.setDistance(0); // Default value if distance is not provided
            }
            dtos.add(dto);
        }
        return dtos;
    }

    public List<StatisticRegListDto> getRegistrationList(String eventId) {
        List<Object[]> resultList = callStoredProcedure("P_STATISTIC_00_REGLIST", eventId);

        List<StatisticRegListDto> dtos = new ArrayList<>();
        for (Object[] row : resultList) {
            StatisticRegListDto dto = new StatisticRegListDto();
            dto.setItem((String) row[0]); // Column 0: Item
            dto.setCategory((String) row[1]); // Column 1: Category
            dto.setBib((String) row[2]); // Column 2: Bib
            dto.setName((String) row[3]); // Column 3: Name
            dtos.add(dto);
        }
        return dtos;
    }

    public List<StatisticStartListDto> getStartList(String eventId) {
        List<Object[]> resultList = callStoredProcedure("P_STATISTIC_01_STARTLIST", eventId);

        List<StatisticStartListDto> dtos = new ArrayList<>();
        for (Object[] row : resultList) {
            StatisticStartListDto dto = new StatisticStartListDto();
            dto.setItem((String) row[0]); // Column 0: Item
            dto.setCategory((String) row[1]); // Column 1: Category
            dto.setBib((String) row[2]); // Column 2: Bib
            dto.setName((String) row[3]); // Column 3: Name
            dto.setTimeStart((String) row[4]); // Column 4: TimeStart
            dto.setTimeGun((String) row[5]); // Column 5: TimeGun
            dtos.add(dto);
        }
        return dtos;
    }

    public List<StatisticDnsDto> getDidNotStartList(String eventId) {
        List<Object[]> resultList = callStoredProcedure("P_STATISTIC_02_DNS", eventId);

        List<StatisticDnsDto> dtos = new ArrayList<>();
        for (Object[] row : resultList) {
            StatisticDnsDto dto = new StatisticDnsDto();
            dto.setItem((String) row[0]); // Column 0: Item
            dto.setCategory((String) row[1]); // Column 1: Category
            dto.setBib((String) row[2]); // Column 2: Bib
            dto.setName((String) row[3]); // Column 3: Name
            dto.setTimeStart((String) row[4]); // Column 4: TimeStart
            dto.setTimeGun((String) row[5]); // Column 5: TimeGun
            dtos.add(dto);
        }
        return dtos;
    }

    public List<StatisticFinishedDto> getFinishedList(String eventId) {
        List<Object[]> resultList = callStoredProcedure("P_STATISTIC_03_FINISHED", eventId);

        List<StatisticFinishedDto> dtos = new ArrayList<>();
        for (Object[] row : resultList) {
            StatisticFinishedDto dto = new StatisticFinishedDto();
            dto.setItem((String) row[0]); // Column 0: Item
            dto.setCategory((String) row[1]); // Column 1: Category
            dto.setBib((String) row[2]); // Column 2: Bib
            dto.setName((String) row[3]); // Column 3: Name
            dto.setTimeStart((String) row[4]); // Column 4: TimeStart
            dto.setTimeGun((String) row[5]); // Column 5: TimeGun
            dto.setTimeFinish((String) row[6]); // Column 6: TimeFinish
            dtos.add(dto);
        }
        return dtos;
    }

    public List<StatisticDnfDto> getDidNotFinishList(String eventId) {
        List<Object[]> resultList = callStoredProcedure("P_STATISTIC_04_DNF", eventId);

        List<StatisticDnfDto> dtos = new ArrayList<>();
        for (Object[] row : resultList) {
            StatisticDnfDto dto = new StatisticDnfDto();
            dto.setItem((String) row[0]); // Column 0: Item
            dto.setCategory((String) row[1]); // Column 1: Category
            dto.setBib((String) row[2]); // Column 2: Bib
            dto.setName((String) row[3]); // Column 3: Name
            dto.setTimeStart((String) row[4]); // Column 4: TimeStart
            dto.setTimeGun((String) row[5]); // Column 5: TimeGun
            dto.setTimeFinish((String) row[6]); // Column 6: TimeFinish
            dtos.add(dto);
        }
        return dtos;
    }

    public List<StatisticFsDto> getFalseStartList(String eventId) {
        List<Object[]> resultList = callStoredProcedure("P_STATISTIC_05_FS", eventId);

        List<StatisticFsDto> dtos = new ArrayList<>();
        for (Object[] row : resultList) {
            StatisticFsDto dto = new StatisticFsDto();
            dto.setItem((String) row[0]); // Column 0: Item
            dto.setCategory((String) row[1]); // Column 1: Category
            dto.setBib((String) row[2]); // Column 2: Bib
            dto.setName((String) row[3]); // Column 3: Name
            dto.setTimeStart((String) row[4]); // Column 4: TimeStart
            dto.setTimeGun((String) row[5]); // Column 5: TimeGun
            dtos.add(dto);
        }
        return dtos;
    }

    public List<StatisticNsbfDto> getNoStartButFinishedList(String eventId) {
        List<Object[]> resultList = callStoredProcedure("P_STATISTIC_06_NSBF", eventId);

        List<StatisticNsbfDto> dtos = new ArrayList<>();
        for (Object[] row : resultList) {
            StatisticNsbfDto dto = new StatisticNsbfDto();
            dto.setItem((String) row[0]); // Column 0: Item
            dto.setCategory((String) row[1]); // Column 1: Category
            dto.setBib((String) row[2]); // Column 2: Bib
            dto.setName((String) row[3]); // Column 3: Name
            dto.setTimeStart((String) row[4]); // Column 4: TimeStart
            dto.setTimeGun((String) row[5]); // Column 5: TimeGun
            dto.setTimeFinish((String) row[6]); // Column 6: TimeFinish
            dtos.add(dto);
        }
        return dtos;
    }

    public List<StatisticDqDto> getDisqualifiedList(String eventId) {
        List<Object[]> resultList = callStoredProcedure("P_STATISTIC_07_DQ", eventId);

        List<StatisticDqDto> dtos = new ArrayList<>();
        for (Object[] row : resultList) {
            StatisticDqDto dto = new StatisticDqDto();
            dto.setItem((String) row[0]); // Column 0: Item
            dto.setCategory((String) row[1]); // Column 1: Category
            dto.setBib((String) row[2]); // Column 2: Bib
            dto.setName((String) row[3]); // Column 3: Name
            dto.setTimeStart((String) row[4]); // Column 4: TimeStart
            dto.setTimeGun((String) row[5]); // Column 5: TimeGun
            dto.setTimeFinish((String) row[6]); // Column 6: TimeFinish
            dto.setRemark((String) row[7]); // Column 7: Remark
            dtos.add(dto);
        }
        return dtos;
    }

    public List<EventCategoryResultResponse> getOverallRankResults(String eventId, String distance) {
        List<TResults> results = tResultsRepository.findByEventIdAndDistanceAndRank1totGreaterThanOrderByRank1totAsc(eventId, new BigDecimal(distance), -1);
        
        // Sort: ascending order with zeros at the end
        results.sort((a, b) -> {
            int aRank = a.getRank1tot() != null ? a.getRank1tot() : 0;
            int bRank = b.getRank1tot() != null ? b.getRank1tot() : 0;
            
            // Put zeros at the end
            if (aRank == 0 && bRank == 0) return 0;
            if (aRank == 0) return 1;
            if (bRank == 0) return -1;
            
            // Sort non-zero values in ascending order
            return Integer.compare(aRank, bRank);
        });
        
        // Get cplist from eventcat - use the cat from first result
        String cplist = null;
        if (results != null && !results.isEmpty()) {
            String cat = results.get(0).getCat();
            List<TEventCat> eventcat = eventCatService.getByEventIdAndCat(eventId, cat);
            if (eventcat != null && !eventcat.isEmpty()) {
                cplist = eventcat.get(0).getCplist();
            }
        }
        
        final String finalCplist = cplist;
        return results.stream().map(result -> {
            EventCategoryResultResponse dto = new EventCategoryResultResponse();
            dto.setRank1Tot(result.getRank1tot());
            dto.setRank1Mix(result.getRank1mix());
            dto.setRank1Cat(result.getRank1cat());
            dto.setBib(result.getBib());
            dto.setName(result.getName());
            dto.setCat(result.getCat());
            dto.setCplist(finalCplist);
            dto.setOfficialTime(TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimegun()));
            dto.setNetTime(TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimestart()));
            dto.setTimeStart(TimeFormatUtil.intToTimeString(result.getTimestart()));
            dto.setTimeFinish(TimeFormatUtil.intToTimeString(result.getTimefinish()));
            
            // Only set TimeCP values that are in cplist
            if (finalCplist != null && !finalCplist.isEmpty()) {
                String[] cps = finalCplist.split(",");
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
    }

    public List<EventCategoryResultResponse> getGenderRankResults(String eventId, String distance, String gender) {
        System.out.println("DEBUG StatisticReportService: EventId=" + eventId + ", Distance=" + distance + ", Gender=" + gender);
        
        // Debug: Check what distinct Sex values exist in the database
        List<String> distinctSexValues = tResultsRepository.findDistinctSexByEventIdAndDistance(eventId, new BigDecimal(distance));
        System.out.println("DEBUG: Distinct Sex values in database: " + distinctSexValues);
        
        // Changed: Remove rank1mix > 0 filter to get all results
        List<TResults> results = tResultsRepository.findByEventIdAndDistanceAndSexOrderByRank1mixAsc(eventId, new BigDecimal(distance), gender);
        System.out.println("DEBUG StatisticReportService: Raw results count=" + results.size());
        if (results.size() > 0) {
            System.out.println("DEBUG: First result Sex value='" + results.get(0).getSex() + "', rank1mix=" + results.get(0).getRank1mix());
        }
        
        // Sort: ascending order with zeros at the end
        results.sort((a, b) -> {
            int aRank = a.getRank1mix() != null ? a.getRank1mix() : 0;
            int bRank = b.getRank1mix() != null ? b.getRank1mix() : 0;
            
            // Put zeros at the end
            if (aRank == 0 && bRank == 0) return 0;
            if (aRank == 0) return 1;
            if (bRank == 0) return -1;
            
            // Sort non-zero values in ascending order
            return Integer.compare(aRank, bRank);
        });
        
        // Get cplist from eventcat - use the cat from first result
        String cplist = null;
        if (results != null && !results.isEmpty()) {
            String cat = results.get(0).getCat();
            List<TEventCat> eventcat = eventCatService.getByEventIdAndCat(eventId, cat);
            if (eventcat != null && !eventcat.isEmpty()) {
                cplist = eventcat.get(0).getCplist();
            }
        }
        
        final String finalCplist = cplist;
        // Filter out results where rank1mix is null or 0
        return results.stream()
                .filter(result -> result.getRank1mix() != null && result.getRank1mix() > 0)
                .map(result -> {
            EventCategoryResultResponse dto = new EventCategoryResultResponse();
            dto.setRank1Mix(result.getRank1mix());
            dto.setRank1Cat(result.getRank1cat());
            dto.setBib(result.getBib());
            dto.setName(result.getName());
            dto.setCat(result.getCat());
            dto.setCplist(finalCplist);
            dto.setOfficialTime(TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimegun()));
            dto.setNetTime(TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimestart()));
            dto.setTimeStart(TimeFormatUtil.intToTimeString(result.getTimestart()));
            dto.setTimeFinish(TimeFormatUtil.intToTimeString(result.getTimefinish()));
            
            // Only set TimeCP values that are in cplist
            if (finalCplist != null && !finalCplist.isEmpty()) {
                String[] cps = finalCplist.split(",");
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
    }
}
