package com.smart.reporting.controller;

import com.smart.reporting.model.RunnerResult;
import com.smart.reporting.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/report")
    public List<RunnerResult> report(@RequestParam String year,
                                     @RequestParam String type) {
        return reportService.fetchResults(year, type);
    }

    @GetMapping("/export/excel")
    public void exportExcel(@RequestParam String year,
                            @RequestParam String type,
                            HttpServletResponse response) throws Exception {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=marathon-results.xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        // TODO: Implement workbook generation with Apache POI
    }

    @GetMapping("/export/pdf")
    public void exportPdf(@RequestParam String year,
                          @RequestParam String type,
                          HttpServletResponse response) throws Exception {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=marathon-results.pdf");
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        // TODO: Implement PDF generation with OpenPDF
    }
    @GetMapping("/results")
    public List<Map<String, Object>> fetchResults() {
        return reportService.getMarathonResults();
    }
    @GetMapping("/test")
    public String hello() {
        return "Hello, API is up!";
    }
}
