package com.smart.reporting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.reporting.dto.EventRequest;
import com.smart.reporting.dto.StatisticReportDto;
import com.smart.reporting.service.StatisticReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatisticController.class)
@ContextConfiguration(classes = StatisticController.class)
class StatisticControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StatisticReportService statisticReportService;

    @MockBean
    private LoginController loginController;

    @MockBean
    private OrgEventController orgEventController;

    @MockBean
    private ParticipantController participantController;

    @MockBean
    private RaceController raceController;

    @MockBean
    private ReportController reportController;

    @MockBean
    private PublicLeaderboardController publicLeaderboardController;

    private EventRequest request;
    private List<StatisticReportDto> response;

    @BeforeEach
    void setUp() {
        request = new EventRequest();
        request.setEventId("event123");

        StatisticReportDto stat1 = new StatisticReportDto();
        stat1.setCat("10K");
        stat1.setCategory("10 Kilometers");
        stat1.setRegistered(150);
        stat1.setFinished(145);
        
        response = Arrays.asList(stat1);
    }

    @Test
    void testGetEventStatistics() throws Exception {
        when(statisticReportService.getStatisticReport(any(EventRequest.class))).thenReturn(response);

        mockMvc.perform(post("/statistic/full")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cat").value("10K"))
                .andExpect(jsonPath("$[0].category").value("10 Kilometers"))
                .andExpect(jsonPath("$[0].registered").value(150))
                .andExpect(jsonPath("$[0].finished").value(145));

        verify(statisticReportService, times(1)).getStatisticReport(any(EventRequest.class));
    }

    @Test
    void testGetEventStatisticsEmpty() throws Exception {
        when(statisticReportService.getStatisticReport(any(EventRequest.class))).thenReturn(Arrays.asList());

        mockMvc.perform(post("/statistic/full")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(statisticReportService, times(1)).getStatisticReport(any(EventRequest.class));
    }
}
