package com.smart.reporting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.reporting.dto.EventRequest;
import com.smart.reporting.dto.StatisticReportDto;
import com.smart.reporting.service.StatisticReportService;

@RestController
@RequestMapping("/statistic")
public class StatisticController {
    @Autowired
    private StatisticReportService statisticReportService;

    @PostMapping("/full")
    public List<StatisticReportDto> getStatistic(@RequestBody EventRequest req) {
        return statisticReportService.getStatisticReport(req);
    }
}
