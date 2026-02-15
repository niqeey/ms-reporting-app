package com.smart.reporting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.reporting.dto.*;
import com.smart.reporting.service.*;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
@ContextConfiguration(classes = ReportController.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RaceResultService raceResultService;

    @MockBean
    private EventCatService eventCatService;

    @MockBean
    private EventService eventService;

    @MockBean
    private OrgService orgService;

    @MockBean
    private ReportExportService reportExportService;

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
        private StatisticController statisticController;

        @MockBean
        private PublicLeaderboardController publicLeaderboardController;

    private EventCategoryResultRequest eventCategoryResultRequest;
    private OverallRankRequest overallRankRequest;

    @BeforeEach
    void setUp() {
        eventCategoryResultRequest = new EventCategoryResultRequest();
        eventCategoryResultRequest.setEventId("event123");
                eventCategoryResultRequest.setCategory("10K");

        overallRankRequest = new OverallRankRequest();
        overallRankRequest.setEventId("event123");
                overallRankRequest.setDistance("10.0");
    }

    @Test
    void testCalculateRanks() throws Exception {
        doNothing().when(statisticReportService).calculateRanks(anyString());

        mockMvc.perform(post("/report/event/calculate-ranks")
                .header("OrgId", "org123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventCategoryResultRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Ranks calculated successfully"));

        verify(statisticReportService, times(1)).calculateRanks("event123");
    }

    @Test
    void testCalculateRanksWithError() throws Exception {
        doThrow(new RuntimeException("Database error"))
                .when(statisticReportService).calculateRanks(anyString());

        mockMvc.perform(post("/report/event/calculate-ranks")
                .header("OrgId", "org123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventCategoryResultRequest)))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("Failed to calculate ranks: Database error"));
    }

    @Test
    void testGetOverallRank() throws Exception {
        List<com.smart.reporting.entity.TResults> mockResults = Arrays.asList();
        when(raceResultService.getResultsByEventAndDistanceOrderByRank1tot(anyString(), anyString()))
                .thenReturn(mockResults);

        mockMvc.perform(post("/report/event/overall-rank")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(overallRankRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());

        verify(raceResultService, times(1))
                .getResultsByEventAndDistanceOrderByRank1tot("event123", "10.0");
    }
}
