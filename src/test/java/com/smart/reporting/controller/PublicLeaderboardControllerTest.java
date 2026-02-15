package com.smart.reporting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.reporting.dto.LeaderboardRequest;
import com.smart.reporting.dto.LeaderboardResponse;
import com.smart.reporting.dto.OrgIdRequest;
import com.smart.reporting.dto.EventResponse;
import com.smart.reporting.service.LeaderboardService;
import com.smart.reporting.service.EventCatService;
import com.smart.reporting.service.OrgEventService;
import com.smart.reporting.service.RaceResultService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublicLeaderboardController.class)
@ContextConfiguration(classes = PublicLeaderboardController.class)
class PublicLeaderboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LeaderboardService leaderboardService;

    @MockBean
    private EventCatService eventCatService;

    @MockBean
    private OrgEventService orgEventService;

    @MockBean
    private RaceResultService raceResultService;

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
        private StatisticController statisticController;

    private LeaderboardRequest request;
    private List<LeaderboardResponse> mockLeaderboard;

    @BeforeEach
    void setUp() {
        request = new LeaderboardRequest();
        request.setCategory("10K");

        LeaderboardResponse runner1 = new LeaderboardResponse();
        runner1.setBib("101");
        runner1.setName("John Doe");
                runner1.setRankTot(1);
                runner1.setNetTime("00:45:30");

        LeaderboardResponse runner2 = new LeaderboardResponse();
        runner2.setBib("102");
        runner2.setName("Jane Smith");
                runner2.setRankTot(2);
                runner2.setNetTime("00:48:15");

        mockLeaderboard = Arrays.asList(runner1, runner2);
    }

    @Test
    void testGetPublicLeaderboard() throws Exception {
        when(leaderboardService.getLeaderboardData(anyString(), anyString()))
                .thenReturn(mockLeaderboard);

        mockMvc.perform(post("/public/leaderboard/event123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rankTot").value(1))
                .andExpect(jsonPath("$[0].bib").value("101"))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].rankTot").value(2))
                .andExpect(jsonPath("$.length()").value(2));

        verify(leaderboardService, times(1))
                .getLeaderboardData("event123", "10K");
    }

    @Test
    void testGetPublicLeaderboardEmpty() throws Exception {
        when(leaderboardService.getLeaderboardData(anyString(), anyString()))
                .thenReturn(Arrays.asList());

        mockMvc.perform(post("/public/leaderboard/event123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(leaderboardService, times(1))
                .getLeaderboardData("event123", "10K");
    }
}
