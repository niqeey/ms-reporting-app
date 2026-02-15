package com.smart.reporting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.reporting.dto.ParticipantBibDto;
import com.smart.reporting.dto.ParticipantDto;
import com.smart.reporting.entity.TResults;
import com.smart.reporting.service.RaceResultService;
import com.smart.reporting.service.RaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ParticipantController.class)
@ContextConfiguration(classes = ParticipantController.class)
class ParticipantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RaceResultService raceResultService;

        @MockBean
        private RaceService raceService;

        @MockBean
        private LoginController loginController;

        @MockBean
        private OrgEventController orgEventController;

        @MockBean
        private RaceController raceController;

        @MockBean
        private ReportController reportController;

        @MockBean
        private StatisticController statisticController;

        @MockBean
        private PublicLeaderboardController publicLeaderboardController;

        private ParticipantBibDto participantBibDto;
        private TResults participantResult;

    @BeforeEach
    void setUp() {
        participantBibDto = new ParticipantBibDto("event123", "101");

        participantResult = new TResults();
        participantResult.setEventId("event123");
        participantResult.setBib("101");
        participantResult.setCat("10K");
        participantResult.setCategory("10 Kilometers");
        participantResult.setName("John Doe");
        participantResult.setRank1cat(1);
        participantResult.setRank1mix(2);
        participantResult.setRank1tot(3);
        participantResult.setTimegun(3600000);
        participantResult.setTimestart(3605000);
        participantResult.setTimefinish(5405000);
        participantResult.setTimecp1(4200000);
        participantResult.setSex("M");
    }

    @Test
        void testGetParticipantDetails() throws Exception {
        when(raceResultService.getParticipantDetails(anyString(), anyString()))
            .thenReturn(List.of(participantResult));

        mockMvc.perform(post("/participant/details")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(participantBibDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.bib").value("101"))
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.eventId").value("event123"))
            .andExpect(jsonPath("$.cat").value("10K"));

        verify(raceResultService, times(1))
            .getParticipantDetails("event123", "101");
        }

        @Test
        void testUpdateParticipant() throws Exception {
        ParticipantDto participantDto = new ParticipantDto();
        participantDto.setEventId("event123");
        participantDto.setBib("101");
        participantDto.setName("John Doe");
        participantDto.setCategory("10 Kilometers");
        participantDto.setGender("M");
        participantDto.setTimeGun("01:00:00.000");
        participantDto.setTimeStart("01:00:05.000");
        participantDto.setTimeFinish("01:30:05.000");

        when(raceResultService.getParticipantDetails(anyString(), anyString()))
            .thenReturn(List.of(participantResult));
        doNothing().when(raceService).updateParticipantWithEventValidation(any(TResults.class), anyString());

        mockMvc.perform(post("/participant/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(participantDto)))
            .andExpect(status().isOk())
            .andExpect(content().string("Participant updated successfully"));

        verify(raceService, times(1))
            .updateParticipantWithEventValidation(any(TResults.class), eq("event123"));
    }
}
