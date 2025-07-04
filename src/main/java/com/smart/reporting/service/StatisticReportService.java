package com.smart.reporting.service;

import com.smart.reporting.dto.EventRequest;
import com.smart.reporting.dto.StatisticReportDto;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticReportService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<StatisticReportDto> getStatisticReport(EventRequest req) {

        String eventId = req.getEventId();
        List<Object[]> resultList = entityManager
                .createNativeQuery("CALL P_STATISTIC_00_REPORT(:eventid)")
                .setParameter("eventid", eventId)
                .getResultList();

        List<StatisticReportDto> dtos = new ArrayList<>();
        for (Object[] row : resultList) {
            StatisticReportDto dto = new StatisticReportDto();
            dto.setStatistic((String) row[0]);
            dto.setCat((String) row[1]);
            dto.setCategory((String) row[2]);
            dto.setRegistered(((Number) row[3]).intValue());
            dto.setStarted(((Number) row[4]).intValue());
            dto.setDidNotStart(((Number) row[5]).intValue());
            dto.setFinished(((Number) row[6]).intValue());
            dto.setDidNotFinish(((Number) row[7]).intValue());
            dto.setFalseStart(((Number) row[8]).intValue());
            dto.setNoStartButFinished(((Number) row[9]).intValue());
            dtos.add(dto);
        }
        return dtos;
    }
}
