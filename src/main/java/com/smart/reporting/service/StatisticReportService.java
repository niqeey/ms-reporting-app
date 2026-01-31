package com.smart.reporting.service;

import com.smart.reporting.dto.EventRequest;
import com.smart.reporting.dto.StatisticDnfDto;
import com.smart.reporting.dto.StatisticDnsDto;
import com.smart.reporting.dto.StatisticDqDto;
import com.smart.reporting.dto.StatisticFinishedDto;
import com.smart.reporting.dto.StatisticFsDto;
import com.smart.reporting.dto.StatisticNsbfDto;
import com.smart.reporting.dto.StatisticRegListDto;
import com.smart.reporting.dto.StatisticReportDto;
import com.smart.reporting.dto.StatisticStartListDto;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticReportService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> callStoredProcedure(String procedureName, String eventId) {
        return entityManager
                .createNativeQuery("CALL " + procedureName + "(:eventid)")
                .setParameter("eventid", eventId)
                .getResultList();
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
}
