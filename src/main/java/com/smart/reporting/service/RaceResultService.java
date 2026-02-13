package com.smart.reporting.service;

import com.smart.reporting.entity.TEvent;
import com.smart.reporting.entity.TEventCat;
import com.smart.reporting.entity.TResults;
import com.smart.reporting.entity.TResultsArchive;
import com.smart.reporting.repository.TResultsRepository;
import com.smart.reporting.repository.TResultsArchiveRepository;
import com.smart.reporting.repository.TEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.smart.reporting.service.EventCatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RaceResultService {
    
    private static final Logger logger = LoggerFactory.getLogger(RaceResultService.class);

    @Autowired
    private TResultsRepository tResultsRepository;

    @Autowired
    private TResultsArchiveRepository tResultsArchiveRepository;

    @Autowired
    private TEventRepository tEventRepository;

    @Autowired
    private EventCatService eventCatService;

    /**
     * Check if an event is archived
     */
    private boolean isEventArchived(String eventId) {
        return tEventRepository.findById(eventId)
            .map(event -> Boolean.TRUE.equals(event.getArchived()))
            .orElse(false);
    }

    /**
     * Convert TResultsArchive to TResults for unified response
     */
    private TResults convertArchiveToResults(TResultsArchive archive) {
        TResults result = new TResults();
        result.setPid(archive.getPid());
        result.setEventId(archive.getEventId());
        result.setChipCode(archive.getChipCode());
        result.setBib(archive.getBib());
        result.setCat(archive.getCat());
        result.setSubCat(archive.getSubCat());
        result.setCategory(archive.getCategory());
        result.setName(archive.getName());
        result.setChiName(archive.getChiName());
        result.setTeamNo(archive.getTeamNo());
        result.setTeam(archive.getTeam());
        result.setTeam1K(archive.getTeam1K());
        result.setAge(archive.getAge());
        result.setNationality(archive.getNationality());
        result.setCountry(archive.getCountry());
        result.setSn(archive.getSn());
        result.setRace(archive.getRace());
        result.setRank1cat(archive.getRank1cat());
        result.setRank1mix(archive.getRank1mix());
        result.setRank1tot(archive.getRank1tot());
        result.setRank1team(archive.getRank1team());
        result.setRank2team(archive.getRank2team());
        result.setLap(archive.getLap());
        result.setBonuslap(archive.getBonuslap());
        result.setDqLap(archive.getDqLap());
        result.setNr(archive.getNr());
        result.setDns(archive.getDns());
        result.setDnf(archive.getDnf());
        result.setDq(archive.getDq());
        result.setFs(archive.getFs());
        result.setNsbf(archive.getNsbf());
        result.setRemark(archive.getRemark());
        result.setTimeteam(archive.getTimeteam());
        result.setTimegun(archive.getTimegun());
        result.setTimestart(archive.getTimestart());
        result.setTimefinish(archive.getTimefinish());
        result.setTimecp1(archive.getTimecp1());
        result.setTimestart1k(archive.getTimestart1k());
        result.setTimefinish1k(archive.getTimefinish1k());
        result.setTimeteam1k(archive.getTimeteam1k());
        result.setTimegun1k(archive.getTimegun1k());
        result.setSex(archive.getSex());
        result.setRank1cat1k(archive.getRank1cat1k());
        result.setRank1mix1k(archive.getRank1mix1k());
        result.setRank1tot1k(archive.getRank1tot1k());
        result.setTimecp2(archive.getTimecp2());
        result.setTimecp3(archive.getTimecp3());
        result.setTimecp4(archive.getTimecp4());
        result.setTimecp5(archive.getTimecp5());
        result.setTimecp6(archive.getTimecp6());
        result.setTimecp7(archive.getTimecp7());
        result.setTimecp8(archive.getTimecp8());
        result.setTimecp9(archive.getTimecp9());
        result.setTimecp10(archive.getTimecp10());
        // Copy time0-time100 fields
        result.setTime0(archive.getTime0());
        result.setTime1(archive.getTime1());
        result.setTime2(archive.getTime2());
        result.setTime3(archive.getTime3());
        result.setTime4(archive.getTime4());
        result.setTime5(archive.getTime5());
        result.setTime6(archive.getTime6());
        result.setTime7(archive.getTime7());
        result.setTime8(archive.getTime8());
        result.setTime9(archive.getTime9());
        result.setTime10(archive.getTime10());
        result.setTime11(archive.getTime11());
        result.setTime12(archive.getTime12());
        result.setTime13(archive.getTime13());
        result.setTime14(archive.getTime14());
        result.setTime15(archive.getTime15());
        result.setTime16(archive.getTime16());
        result.setTime17(archive.getTime17());
        result.setTime18(archive.getTime18());
        result.setTime19(archive.getTime19());
        result.setTime20(archive.getTime20());
        result.setTime21(archive.getTime21());
        result.setTime22(archive.getTime22());
        result.setTime23(archive.getTime23());
        result.setTime24(archive.getTime24());
        result.setTime25(archive.getTime25());
        result.setTime26(archive.getTime26());
        result.setTime27(archive.getTime27());
        result.setTime28(archive.getTime28());
        result.setTime29(archive.getTime29());
        result.setTime30(archive.getTime30());
        result.setTime31(archive.getTime31());
        result.setTime32(archive.getTime32());
        result.setTime33(archive.getTime33());
        result.setTime34(archive.getTime34());
        result.setTime35(archive.getTime35());
        result.setTime36(archive.getTime36());
        result.setTime37(archive.getTime37());
        result.setTime38(archive.getTime38());
        result.setTime39(archive.getTime39());
        result.setTime40(archive.getTime40());
        result.setTime41(archive.getTime41());
        result.setTime42(archive.getTime42());
        result.setTime43(archive.getTime43());
        result.setTime44(archive.getTime44());
        result.setTime45(archive.getTime45());
        result.setTime46(archive.getTime46());
        result.setTime47(archive.getTime47());
        result.setTime48(archive.getTime48());
        result.setTime49(archive.getTime49());
        result.setTime50(archive.getTime50());
        result.setTime51(archive.getTime51());
        result.setTime52(archive.getTime52());
        result.setTime53(archive.getTime53());
        result.setTime54(archive.getTime54());
        result.setTime55(archive.getTime55());
        result.setTime56(archive.getTime56());
        result.setTime57(archive.getTime57());
        result.setTime58(archive.getTime58());
        result.setTime59(archive.getTime59());
        result.setTime60(archive.getTime60());
        result.setTime61(archive.getTime61());
        result.setTime62(archive.getTime62());
        result.setTime63(archive.getTime63());
        result.setTime64(archive.getTime64());
        result.setTime65(archive.getTime65());
        result.setTime66(archive.getTime66());
        result.setTime67(archive.getTime67());
        result.setTime68(archive.getTime68());
        result.setTime69(archive.getTime69());
        result.setTime70(archive.getTime70());
        result.setTime71(archive.getTime71());
        result.setTime72(archive.getTime72());
        result.setTime73(archive.getTime73());
        result.setTime74(archive.getTime74());
        result.setTime75(archive.getTime75());
        result.setTime76(archive.getTime76());
        result.setTime77(archive.getTime77());
        result.setTime78(archive.getTime78());
        result.setTime79(archive.getTime79());
        result.setTime80(archive.getTime80());
        result.setTime81(archive.getTime81());
        result.setTime82(archive.getTime82());
        result.setTime83(archive.getTime83());
        result.setTime84(archive.getTime84());
        result.setTime85(archive.getTime85());
        result.setTime86(archive.getTime86());
        result.setTime87(archive.getTime87());
        result.setTime88(archive.getTime88());
        result.setTime89(archive.getTime89());
        result.setTime90(archive.getTime90());
        result.setTime91(archive.getTime91());
        result.setTime92(archive.getTime92());
        result.setTime93(archive.getTime93());
        result.setTime94(archive.getTime94());
        result.setTime95(archive.getTime95());
        result.setTime96(archive.getTime96());
        result.setTime97(archive.getTime97());
        result.setTime98(archive.getTime98());
        result.setTime99(archive.getTime99());
        result.setTime100(archive.getTime100());
        result.setBib1(archive.getBib1());
        result.setNo(archive.getNo());
        result.setBib2(archive.getBib2());
        result.setName2(archive.getName2());
        result.setDob(archive.getDob());
        result.setName3(archive.getName3());
        result.setDistance(archive.getDistance());
        result.setNric(archive.getNric());
        result.setCompany(archive.getCompany());
        return result;
    }


    public List<TResults> getResults(String eventId, String cat, int limit) {
        if (isEventArchived(eventId)) {
            return tResultsArchiveRepository.findByEventIdAndCat(eventId, cat, PageRequest.of(0, limit))
                .stream()
                .map(this::convertArchiveToResults)
                .collect(Collectors.toList());
        }
        return tResultsRepository.findByEventIdAndCat(eventId, cat, PageRequest.of(0, limit));
    }

    public List<TResults> getResultsByEventAndCat(String eventId, String cat) {
        logger.info("getResultsByEventAndCat called with eventId={}, cat={}", eventId, cat);
        
        TEventCat tEventCat= eventCatService.getByEventIdAndCat(eventId, cat)
                .stream()
                .filter(eventCat -> eventCat.getCat().equalsIgnoreCase(cat))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Category not found for event: " + eventId + ", category: " + cat));
        
        logger.info("Found EventCat - racemode={}, cplist={}", tEventCat.getRacemode(), tEventCat.getCplist());
        
        int limit = tEventCat.getTop() > 0 ? tEventCat.getTop() : 10; // Default to 10 if not set
        if (limit <= 0) {
            limit = 10; // Ensure a positive limit
        }
        
        // For LAP mode, fetch records with time1 > 0, even if rank1cat = 0
        String raceMode = tEventCat.getRacemode();
        List<TResults> results;
        
        if (isEventArchived(eventId)) {
            logger.info("Event is archived");
            if ("LAP".equalsIgnoreCase(raceMode)) {
                logger.info("Fetching LAP mode results from archive");
                results = tResultsArchiveRepository.findByEventIdAndCatWithLapModeOrderByLapAndNetTime(eventId, cat)
                    .stream()
                    .map(this::convertArchiveToResults)
                    .collect(Collectors.toList());
            } else {
                logger.info("Fetching standard mode results from archive");
                results = tResultsArchiveRepository.findByEventIdAndCatAndRank1catGreaterThanOrderByRank1cat(eventId, cat, 0)
                    .stream()
                    .map(this::convertArchiveToResults)
                    .collect(Collectors.toList());
            }
        } else {
            logger.info("Event is NOT archived");
            if ("LAP".equalsIgnoreCase(raceMode)) {
                logger.info("Fetching LAP mode results from live table");
                results = tResultsRepository.findByEventIdAndCatWithLapModeOrderByLapAndNetTime(eventId, cat);
            } else {
                logger.info("Fetching standard mode results from live table");
                results = tResultsRepository.findByEventIdAndCatAndRank1catGreaterThanOrderByRank1cat(eventId, cat, 0);
            }
        }
        
        logger.info("Retrieved {} results before LAP filtering", results.size());
        
        // For LAP mode, filter results based on the specific time column for the lap count
        if ("LAP".equalsIgnoreCase(raceMode)) {
            logger.info("Applying LAP mode filtering with cplist: {}", tEventCat.getCplist());
           // results = filterLapModeResults(results, tEventCat.getCplist());
            logger.info("After LAP filtering: {} results remain", results.size());
        }
        
        return results;
    }
    
    public List<TResults> getTopResultsByEventAndCat(String eventId, String cat) {
        TEventCat tEventCat= eventCatService.getByEventIdAndCat(eventId, cat)
                .stream()
                .filter(eventCat -> eventCat.getCat().equalsIgnoreCase(cat))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Category not found for event: " + eventId + ", category: " + cat));
        int limit = tEventCat.getTop() > 0 ? tEventCat.getTop() : 10; // Default to 10 if not set
        if (limit <= 0) {
            limit = 10; // Ensure a positive limit
        }
        
        if (isEventArchived(eventId)) {
            return tResultsArchiveRepository.findByEventIdAndCatAndRank1catGreaterThanOrderByRank1cat(eventId, cat, 0, PageRequest.of(0, limit))
                .stream()
                .map(this::convertArchiveToResults)
                .collect(Collectors.toList());
        }
        return tResultsRepository.findByEventIdAndCatAndRank1catGreaterThanOrderByRank1cat(eventId, cat, 0, PageRequest.of(0, limit));
    }

    public List<TResults> getParticipantDetails(String eventId,String bib) {
        if (isEventArchived(eventId)) {
            return tResultsArchiveRepository.findByEventIdAndBib(eventId, bib)
                .stream()
                .map(this::convertArchiveToResults)
                .collect(Collectors.toList());
        }
        return tResultsRepository.findByEventIdAndBib(eventId,bib);
    }
    
    public List<TResults> getResultsByBib(String eventId,String bib) {
        if (isEventArchived(eventId)) {
            return tResultsArchiveRepository.findByEventIdAndBib(eventId, bib)
                .stream()
                .map(this::convertArchiveToResults)
                .collect(Collectors.toList());
        }
        return tResultsRepository.findByEventIdAndBib(eventId,bib);
    }
    
    // Get results for Overall Rank (RANK1TOT)
    public List<TResults> getResultsByEventAndDistanceOrderByRank1tot(String eventId, String distance) {
        if (isEventArchived(eventId)) {
            return tResultsArchiveRepository.findByEventIdAndDistanceAndRank1totGreaterThanOrderByRank1totAsc(eventId, new BigDecimal(distance), 0)
                .stream()
                .map(this::convertArchiveToResults)
                .collect(Collectors.toList());
        }
        return tResultsRepository.findByEventIdAndDistanceAndRank1totGreaterThanOrderByRank1totAsc(eventId, new BigDecimal(distance), 0);
    }
    
    // Get results for Gender Rank (RANK1MIX)
    public List<TResults> getResultsByEventAndDistanceAndGenderOrderByRank1mix(String eventId, String distance, String gender) {
        if (isEventArchived(eventId)) {
            return tResultsArchiveRepository.findByEventIdAndDistanceAndSexAndRank1mixGreaterThanOrderByRank1mixAsc(eventId, new BigDecimal(distance), gender, 0)
                .stream()
                .map(this::convertArchiveToResults)
                .collect(Collectors.toList());
        }
        return tResultsRepository.findByEventIdAndDistanceAndSexAndRank1mixGreaterThanOrderByRank1mixAsc(eventId, new BigDecimal(distance), gender, 0);
    }

    /**
     * Filter LAP mode results to only include those with valid lap completion.
     * For LAP mode: if halflap=0, check time[lap], if halflap>0, check time[lap+1]
     */
    private List<TResults> filterLapModeResults(List<TResults> results, String cplist) {
        logger.info("filterLapModeResults: input cplist={}, results count={}", cplist, results.size());
        
        if (cplist == null || cplist.isEmpty()) {
            logger.warn("cplist is null or empty, returning all results");
            return results;
        }
        
        // Parse cplist: halflap,fulllap,numberOfLaps,totalDistance
        String[] parts = cplist.split(",");
        logger.info("Parsed cplist into {} parts: {}", parts.length, cplist);
        
        if (parts.length < 3) {
            logger.warn("cplist has less than 3 parts, returning all results");
            return results;
        }
        
        try {
            int halflap = Integer.parseInt(parts[0].trim());
            int numberOfLaps = Integer.parseInt(parts[2].trim());
            
            logger.info("Parsed cplist: halflap={}, numberOfLaps={}", halflap, numberOfLaps);
            
            // Determine which time column index to check
            boolean hasTimeZero = results.stream().anyMatch(r -> r.getTime0() != null && r.getTime0() > 0);
            int baseIndex = (halflap > 0) ? numberOfLaps + 1 : numberOfLaps;
            int timeColumnIndex = hasTimeZero ? baseIndex - 1 : baseIndex;
            if (timeColumnIndex < 0) {
                timeColumnIndex = 0;
            }
            final int timeColumnIndexFinal = timeColumnIndex;
            logger.info("Determined timeColumnIndex={}", timeColumnIndexFinal);
            
            // Filter results where the corresponding time column > 0
            List<TResults> filtered = results.stream()
                .peek(result -> {
                    int timeValue = getTimeColumnValue(result, timeColumnIndexFinal);
                    logger.debug("Result bib={}, name={}, time[{}]={}, passes filter={}", 
                        result.getBib(), result.getName(), timeColumnIndexFinal, timeValue, timeValue > 0);
                })
                .filter(result -> getTimeColumnValue(result, timeColumnIndexFinal) > 0)
                .collect(Collectors.toList());
            
            logger.info("After LAP filtering: {} results remain", filtered.size());
            return filtered;
        } catch (NumberFormatException e) {
            logger.error("Error parsing cplist: {}", cplist, e);
            return results;
        }
    }

    /**
     * Get the value of a time column (time0 through time18) by index
     */
    private Integer getTimeColumnValue(TResults result, int columnIndex) {
        Integer value;
        switch (columnIndex) {
            case 0: value = result.getTime0() != null ? result.getTime0() : 0; break;
            case 1: value = result.getTime1() != null ? result.getTime1() : 0; break;
            case 2: value = result.getTime2() != null ? result.getTime2() : 0; break;
            case 3: value = result.getTime3() != null ? result.getTime3() : 0; break;
            case 4: value = result.getTime4() != null ? result.getTime4() : 0; break;
            case 5: value = result.getTime5() != null ? result.getTime5() : 0; break;
            case 6: value = result.getTime6() != null ? result.getTime6() : 0; break;
            case 7: value = result.getTime7() != null ? result.getTime7() : 0; break;
            case 8: value = result.getTime8() != null ? result.getTime8() : 0; break;
            case 9: value = result.getTime9() != null ? result.getTime9() : 0; break;
            case 10: value = result.getTime10() != null ? result.getTime10() : 0; break;
            case 11: value = result.getTime11() != null ? result.getTime11() : 0; break;
            case 12: value = result.getTime12() != null ? result.getTime12() : 0; break;
            case 13: value = result.getTime13() != null ? result.getTime13() : 0; break;
            case 14: value = result.getTime14() != null ? result.getTime14() : 0; break;
            case 15: value = result.getTime15() != null ? result.getTime15() : 0; break;
            case 16: value = result.getTime16() != null ? result.getTime16() : 0; break;
            case 17: value = result.getTime17() != null ? result.getTime17() : 0; break;
            case 18: value = result.getTime18() != null ? result.getTime18() : 0; break;
            default: value = 0;
        }
        logger.trace("getTimeColumnValue for index {} returned {}", columnIndex, value);
        return value;
    }
}