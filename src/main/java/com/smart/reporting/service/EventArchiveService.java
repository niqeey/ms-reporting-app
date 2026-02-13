package com.smart.reporting.service;

import com.smart.reporting.entity.TResults;
import com.smart.reporting.entity.TResultsArchive;
import com.smart.reporting.entity.TEvent;
import com.smart.reporting.repository.TResultsRepository;
import com.smart.reporting.repository.TResultsArchiveRepository;
import com.smart.reporting.repository.TEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class EventArchiveService {

    private static final Logger logger = LoggerFactory.getLogger(EventArchiveService.class);

    @Autowired
    private TResultsRepository resultsRepository;

    @Autowired
    private TResultsArchiveRepository resultsArchiveRepository;

    @Autowired
    private TEventRepository eventRepository;

    /**
     * Archive an event - copies all results from results table to results_archive table
     * and marks the event as archived. This is a 100% data copy operation.
     * 
     * @param eventId The ID of the event to archive
     * @return The number of results archived
     */
    @Transactional
    public int archiveEvent(String eventId) {
        logger.info("Starting archive process for event ID: {}", eventId);

        // 1. Get the event and verify it exists
        TEvent event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found: " + eventId));

        if (Boolean.TRUE.equals(event.getArchived())) {
            logger.warn("Event {} is already archived", eventId);
            throw new RuntimeException("Event is already archived");
        }

        // 2. Get all results for this event
        List<TResults> results = resultsRepository.findAll().stream()
            .filter(r -> eventId.equals(r.getEventId()))
            .toList();

        if (results.isEmpty()) {
            logger.warn("No results found for event {}, proceeding with archive anyway", eventId);
        }

        // 3. Copy each result to archive table (100% copy)
        int archivedCount = 0;
        for (TResults result : results) {
            TResultsArchive archivedResult = copyToArchive(result);
            resultsArchiveRepository.save(archivedResult);
            archivedCount++;
        }

        // 4. Delete results from active table
        if (!results.isEmpty()) {
            resultsRepository.deleteAll(results);
            logger.info("Deleted {} results from active results table", archivedCount);
        }

        // 5. Mark event as archived
        event.setArchived(true);
        eventRepository.save(event);

        logger.info("Successfully archived event {} with {} results", eventId, archivedCount);
        return archivedCount;
    }

    /**
     * Unarchive an event - copies results from results_archive back to results table
     * and marks the event as not archived.
     * 
     * @param eventId The ID of the event to unarchive
     * @return The number of results unarchived
     */
    @Transactional
    public int unarchiveEvent(String eventId) {
        logger.info("Starting unarchive process for event ID: {}", eventId);

        // 1. Get the event and verify it exists
        TEvent event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found: " + eventId));

        if (!Boolean.TRUE.equals(event.getArchived())) {
            logger.warn("Event {} is not archived", eventId);
            throw new RuntimeException("Event is not archived");
        }

        // 2. Get all archived results for this event
        List<TResultsArchive> archivedResults = resultsArchiveRepository.findAll().stream()
            .filter(r -> eventId.equals(r.getEventId()))
            .toList();

        if (archivedResults.isEmpty()) {
            logger.warn("No archived results found for event {}", eventId);
        }

        // 3. Copy each archived result back to active table
        int unarchivedCount = 0;
        for (TResultsArchive archivedResult : archivedResults) {
            TResults result = copyFromArchive(archivedResult);
            resultsRepository.save(result);
            unarchivedCount++;
        }

        // 4. Delete results from archive table
        if (!archivedResults.isEmpty()) {
            resultsArchiveRepository.deleteAll(archivedResults);
            logger.info("Deleted {} results from archive table", unarchivedCount);
        }

        // 5. Mark event as not archived
        event.setArchived(false);
        eventRepository.save(event);

        logger.info("Successfully unarchived event {} with {} results", eventId, unarchivedCount);
        return unarchivedCount;
    }

    /**
     * Copy a TResults entity to TResultsArchive (100% copy of all fields)
     */
    private TResultsArchive copyToArchive(TResults source) {
        TResultsArchive target = new TResultsArchive();
        
        target.setPid(source.getPid());
        target.setEventId(source.getEventId());
        target.setChipCode(source.getChipCode());
        target.setBib(source.getBib());
        target.setCat(source.getCat());
        target.setSubCat(source.getSubCat());
        target.setCategory(source.getCategory());
        target.setName(source.getName());
        target.setChiName(source.getChiName());
        target.setTeamNo(source.getTeamNo());
        target.setTeam(source.getTeam());
        target.setTeam1K(source.getTeam1K());
        target.setAge(source.getAge());
        target.setNationality(source.getNationality());
        target.setCountry(source.getCountry());
        target.setSn(source.getSn());
        target.setRace(source.getRace());
        target.setRank1cat(source.getRank1cat());
        target.setRank1mix(source.getRank1mix());
        target.setRank1tot(source.getRank1tot());
        target.setRank1team(source.getRank1team());
        target.setRank2team(source.getRank2team());
        target.setLap(source.getLap());
        target.setBonuslap(source.getBonuslap());
        target.setDqLap(source.getDqLap());
        target.setNr(source.getNr());
        target.setDns(source.getDns());
        target.setDnf(source.getDnf());
        target.setDq(source.getDq());
        target.setFs(source.getFs());
        target.setNsbf(source.getNsbf());
        target.setRemark(source.getRemark());
        target.setTimeteam(source.getTimeteam());
        target.setTimegun(source.getTimegun());
        target.setTimestart(source.getTimestart());
        target.setTimefinish(source.getTimefinish());
        target.setTimecp1(source.getTimecp1());
        target.setTimestart1k(source.getTimestart1k());
        target.setTimefinish1k(source.getTimefinish1k());
        target.setTimeteam1k(source.getTimeteam1k());
        target.setTimegun1k(source.getTimegun1k());
        target.setSex(source.getSex());
        target.setRank1cat1k(source.getRank1cat1k());
        target.setRank1mix1k(source.getRank1mix1k());
        target.setRank1tot1k(source.getRank1tot1k());
        target.setTimecp2(source.getTimecp2());
        target.setTimecp3(source.getTimecp3());
        target.setTimecp4(source.getTimecp4());
        target.setTimecp5(source.getTimecp5());
        target.setTimecp6(source.getTimecp6());
        target.setTimecp7(source.getTimecp7());
        target.setTimecp8(source.getTimecp8());
        target.setTimecp9(source.getTimecp9());
        target.setTimecp10(source.getTimecp10());
        
        // Copy all time1 to time100 fields
        target.setTime1(source.getTime1());
        target.setTime2(source.getTime2());
        target.setTime3(source.getTime3());
        target.setTime4(source.getTime4());
        target.setTime5(source.getTime5());
        target.setTime6(source.getTime6());
        target.setTime7(source.getTime7());
        target.setTime8(source.getTime8());
        target.setTime9(source.getTime9());
        target.setTime10(source.getTime10());
        target.setTime11(source.getTime11());
        target.setTime12(source.getTime12());
        target.setTime13(source.getTime13());
        target.setTime14(source.getTime14());
        target.setTime15(source.getTime15());
        target.setTime16(source.getTime16());
        target.setTime17(source.getTime17());
        target.setTime18(source.getTime18());
        target.setTime19(source.getTime19());
        target.setTime20(source.getTime20());
        target.setTime21(source.getTime21());
        target.setTime22(source.getTime22());
        target.setTime23(source.getTime23());
        target.setTime24(source.getTime24());
        target.setTime25(source.getTime25());
        target.setTime26(source.getTime26());
        target.setTime27(source.getTime27());
        target.setTime28(source.getTime28());
        target.setTime29(source.getTime29());
        target.setTime30(source.getTime30());
        target.setTime31(source.getTime31());
        target.setTime32(source.getTime32());
        target.setTime33(source.getTime33());
        target.setTime34(source.getTime34());
        target.setTime35(source.getTime35());
        target.setTime36(source.getTime36());
        target.setTime37(source.getTime37());
        target.setTime38(source.getTime38());
        target.setTime39(source.getTime39());
        target.setTime40(source.getTime40());
        target.setTime41(source.getTime41());
        target.setTime42(source.getTime42());
        target.setTime43(source.getTime43());
        target.setTime44(source.getTime44());
        target.setTime45(source.getTime45());
        target.setTime46(source.getTime46());
        target.setTime47(source.getTime47());
        target.setTime48(source.getTime48());
        target.setTime49(source.getTime49());
        target.setTime50(source.getTime50());
        target.setTime51(source.getTime51());
        target.setTime52(source.getTime52());
        target.setTime53(source.getTime53());
        target.setTime54(source.getTime54());
        target.setTime55(source.getTime55());
        target.setTime56(source.getTime56());
        target.setTime57(source.getTime57());
        target.setTime58(source.getTime58());
        target.setTime59(source.getTime59());
        target.setTime60(source.getTime60());
        target.setTime61(source.getTime61());
        target.setTime62(source.getTime62());
        target.setTime63(source.getTime63());
        target.setTime64(source.getTime64());
        target.setTime65(source.getTime65());
        target.setTime66(source.getTime66());
        target.setTime67(source.getTime67());
        target.setTime68(source.getTime68());
        target.setTime69(source.getTime69());
        target.setTime70(source.getTime70());
        target.setTime71(source.getTime71());
        target.setTime72(source.getTime72());
        target.setTime73(source.getTime73());
        target.setTime74(source.getTime74());
        target.setTime75(source.getTime75());
        target.setTime76(source.getTime76());
        target.setTime77(source.getTime77());
        target.setTime78(source.getTime78());
        target.setTime79(source.getTime79());
        target.setTime80(source.getTime80());
        target.setTime81(source.getTime81());
        target.setTime82(source.getTime82());
        target.setTime83(source.getTime83());
        target.setTime84(source.getTime84());
        target.setTime85(source.getTime85());
        target.setTime86(source.getTime86());
        target.setTime87(source.getTime87());
        target.setTime88(source.getTime88());
        target.setTime89(source.getTime89());
        target.setTime90(source.getTime90());
        target.setTime91(source.getTime91());
        target.setTime92(source.getTime92());
        target.setTime93(source.getTime93());
        target.setTime94(source.getTime94());
        target.setTime95(source.getTime95());
        target.setTime96(source.getTime96());
        target.setTime97(source.getTime97());
        target.setTime98(source.getTime98());
        target.setTime99(source.getTime99());
        target.setTime100(source.getTime100());
        
        target.setBib1(source.getBib1());
        target.setNo(source.getNo());
        target.setBib2(source.getBib2());
        target.setName2(source.getName2());
        target.setDob(source.getDob());
        target.setName3(source.getName3());
        target.setDistance(source.getDistance());
        target.setNric(source.getNric());
        target.setCompany(source.getCompany());
        
        return target;
    }

    /**
     * Copy a TResultsArchive entity to TResults (100% copy of all fields)
     */
    private TResults copyFromArchive(TResultsArchive source) {
        TResults target = new TResults();
        
        target.setPid(source.getPid());
        target.setEventId(source.getEventId());
        target.setChipCode(source.getChipCode());
        target.setBib(source.getBib());
        target.setCat(source.getCat());
        target.setSubCat(source.getSubCat());
        target.setCategory(source.getCategory());
        target.setName(source.getName());
        target.setChiName(source.getChiName());
        target.setTeamNo(source.getTeamNo());
        target.setTeam(source.getTeam());
        target.setTeam1K(source.getTeam1K());
        target.setAge(source.getAge());
        target.setNationality(source.getNationality());
        target.setCountry(source.getCountry());
        target.setSn(source.getSn());
        target.setRace(source.getRace());
        target.setRank1cat(source.getRank1cat());
        target.setRank1mix(source.getRank1mix());
        target.setRank1tot(source.getRank1tot());
        target.setRank1team(source.getRank1team());
        target.setRank2team(source.getRank2team());
        target.setLap(source.getLap());
        target.setBonuslap(source.getBonuslap());
        target.setDqLap(source.getDqLap());
        target.setNr(source.getNr());
        target.setDns(source.getDns());
        target.setDnf(source.getDnf());
        target.setDq(source.getDq());
        target.setFs(source.getFs());
        target.setNsbf(source.getNsbf());
        target.setRemark(source.getRemark());
        target.setTimeteam(source.getTimeteam());
        target.setTimegun(source.getTimegun());
        target.setTimestart(source.getTimestart());
        target.setTimefinish(source.getTimefinish());
        target.setTimecp1(source.getTimecp1());
        target.setTimestart1k(source.getTimestart1k());
        target.setTimefinish1k(source.getTimefinish1k());
        target.setTimeteam1k(source.getTimeteam1k());
        target.setTimegun1k(source.getTimegun1k());
        target.setSex(source.getSex());
        target.setRank1cat1k(source.getRank1cat1k());
        target.setRank1mix1k(source.getRank1mix1k());
        target.setRank1tot1k(source.getRank1tot1k());
        target.setTimecp2(source.getTimecp2());
        target.setTimecp3(source.getTimecp3());
        target.setTimecp4(source.getTimecp4());
        target.setTimecp5(source.getTimecp5());
        target.setTimecp6(source.getTimecp6());
        target.setTimecp7(source.getTimecp7());
        target.setTimecp8(source.getTimecp8());
        target.setTimecp9(source.getTimecp9());
        target.setTimecp10(source.getTimecp10());
        
        // Copy all time1 to time100 fields
        target.setTime1(source.getTime1());
        target.setTime2(source.getTime2());
        target.setTime3(source.getTime3());
        target.setTime4(source.getTime4());
        target.setTime5(source.getTime5());
        target.setTime6(source.getTime6());
        target.setTime7(source.getTime7());
        target.setTime8(source.getTime8());
        target.setTime9(source.getTime9());
        target.setTime10(source.getTime10());
        target.setTime11(source.getTime11());
        target.setTime12(source.getTime12());
        target.setTime13(source.getTime13());
        target.setTime14(source.getTime14());
        target.setTime15(source.getTime15());
        target.setTime16(source.getTime16());
        target.setTime17(source.getTime17());
        target.setTime18(source.getTime18());
        target.setTime19(source.getTime19());
        target.setTime20(source.getTime20());
        target.setTime21(source.getTime21());
        target.setTime22(source.getTime22());
        target.setTime23(source.getTime23());
        target.setTime24(source.getTime24());
        target.setTime25(source.getTime25());
        target.setTime26(source.getTime26());
        target.setTime27(source.getTime27());
        target.setTime28(source.getTime28());
        target.setTime29(source.getTime29());
        target.setTime30(source.getTime30());
        target.setTime31(source.getTime31());
        target.setTime32(source.getTime32());
        target.setTime33(source.getTime33());
        target.setTime34(source.getTime34());
        target.setTime35(source.getTime35());
        target.setTime36(source.getTime36());
        target.setTime37(source.getTime37());
        target.setTime38(source.getTime38());
        target.setTime39(source.getTime39());
        target.setTime40(source.getTime40());
        target.setTime41(source.getTime41());
        target.setTime42(source.getTime42());
        target.setTime43(source.getTime43());
        target.setTime44(source.getTime44());
        target.setTime45(source.getTime45());
        target.setTime46(source.getTime46());
        target.setTime47(source.getTime47());
        target.setTime48(source.getTime48());
        target.setTime49(source.getTime49());
        target.setTime50(source.getTime50());
        target.setTime51(source.getTime51());
        target.setTime52(source.getTime52());
        target.setTime53(source.getTime53());
        target.setTime54(source.getTime54());
        target.setTime55(source.getTime55());
        target.setTime56(source.getTime56());
        target.setTime57(source.getTime57());
        target.setTime58(source.getTime58());
        target.setTime59(source.getTime59());
        target.setTime60(source.getTime60());
        target.setTime61(source.getTime61());
        target.setTime62(source.getTime62());
        target.setTime63(source.getTime63());
        target.setTime64(source.getTime64());
        target.setTime65(source.getTime65());
        target.setTime66(source.getTime66());
        target.setTime67(source.getTime67());
        target.setTime68(source.getTime68());
        target.setTime69(source.getTime69());
        target.setTime70(source.getTime70());
        target.setTime71(source.getTime71());
        target.setTime72(source.getTime72());
        target.setTime73(source.getTime73());
        target.setTime74(source.getTime74());
        target.setTime75(source.getTime75());
        target.setTime76(source.getTime76());
        target.setTime77(source.getTime77());
        target.setTime78(source.getTime78());
        target.setTime79(source.getTime79());
        target.setTime80(source.getTime80());
        target.setTime81(source.getTime81());
        target.setTime82(source.getTime82());
        target.setTime83(source.getTime83());
        target.setTime84(source.getTime84());
        target.setTime85(source.getTime85());
        target.setTime86(source.getTime86());
        target.setTime87(source.getTime87());
        target.setTime88(source.getTime88());
        target.setTime89(source.getTime89());
        target.setTime90(source.getTime90());
        target.setTime91(source.getTime91());
        target.setTime92(source.getTime92());
        target.setTime93(source.getTime93());
        target.setTime94(source.getTime94());
        target.setTime95(source.getTime95());
        target.setTime96(source.getTime96());
        target.setTime97(source.getTime97());
        target.setTime98(source.getTime98());
        target.setTime99(source.getTime99());
        target.setTime100(source.getTime100());
        
        target.setBib1(source.getBib1());
        target.setNo(source.getNo());
        target.setBib2(source.getBib2());
        target.setName2(source.getName2());
        target.setDob(source.getDob());
        target.setName3(source.getName3());
        target.setDistance(source.getDistance());
        target.setNric(source.getNric());
        target.setCompany(source.getCompany());
        
        return target;
    }
}
