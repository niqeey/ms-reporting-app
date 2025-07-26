package com.smart.reporting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.reporting.dto.EventRequest;
import com.smart.reporting.dto.StatisticReportDto;
import com.smart.reporting.dto.StatisticStartListDto;
import com.smart.reporting.dto.StatisticRegListDto;
import com.smart.reporting.dto.StatisticDnfDto;
import com.smart.reporting.dto.StatisticDnsDto;
import com.smart.reporting.dto.StatisticDqDto;
import com.smart.reporting.dto.StatisticFinishedDto;
import com.smart.reporting.dto.StatisticFsDto;
import com.smart.reporting.dto.StatisticNsbfDto;
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

    @PostMapping("/registration")
    public List<StatisticRegListDto> getRegistrationList(@RequestBody EventRequest req) {
        return statisticReportService.getRegistrationList(req.getEventId());
    }

    @PostMapping("/startlist")
    public List<StatisticStartListDto> getStartList(@RequestBody EventRequest req) {
        return statisticReportService.getStartList(req.getEventId());
    }

    @PostMapping("/dns")
    public List<StatisticDnsDto> getDidNotStartList(@RequestBody EventRequest req) {
        return statisticReportService.getDidNotStartList(req.getEventId());
    }

    @PostMapping("/finished")
    public List<StatisticFinishedDto> getFinishedList(@RequestBody EventRequest req) {
        return statisticReportService.getFinishedList(req.getEventId());
    }

    @PostMapping("/dnf")
    public List<StatisticDnfDto> getDidNotFinishList(@RequestBody EventRequest req) {
        return statisticReportService.getDidNotFinishList(req.getEventId());
    }

    @PostMapping("/fs")
    public List<StatisticFsDto> getFalseStartList(@RequestBody EventRequest req) {
        return statisticReportService.getFalseStartList(req.getEventId());
    }

    @PostMapping("/nsbf")
    public List<StatisticNsbfDto> getNoStartButFinishedList(@RequestBody EventRequest req) {
        return statisticReportService.getNoStartButFinishedList(req.getEventId());
    }

    @PostMapping("/dq")
    public List<StatisticDqDto> getDisqualifiedList(@RequestBody EventRequest req) {
        return statisticReportService.getDisqualifiedList(req.getEventId());
    }
}
