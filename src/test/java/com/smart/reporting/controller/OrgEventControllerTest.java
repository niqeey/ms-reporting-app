package com.smart.reporting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.reporting.dto.OrgIdRequest;
import com.smart.reporting.entity.TEvent;
import com.smart.reporting.service.OrgEventService;
import com.smart.reporting.service.EventArchiveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrgEventController.class)
@ContextConfiguration(classes = OrgEventController.class)
class OrgEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrgEventService orgEventService;

    @MockBean
    private EventArchiveService eventArchiveService;

    @MockBean
    private LoginController loginController;

    @MockBean
    private ParticipantController participantController;

    @MockBean
    private RaceController raceController;

    @MockBean
    private ReportController reportController;

    @MockBean
    private StatisticController statisticController;

    @MockBean
    private PublicLeaderboardController publicLeaderboardController;

    private OrgIdRequest orgIdRequest;
    private List<TEvent> mockEvents;

    @BeforeEach
    void setUp() {
        orgIdRequest = new OrgIdRequest();
        orgIdRequest.setOrgId("org123");

        TEvent event1 = new TEvent();
        event1.setId("event123");
        event1.setName("Marathon 2026");
        event1.setEventDt(new Date());
        event1.setLocation("City Park");
        event1.setCountry("USA");

        TEvent event2 = new TEvent();
        event2.setId("event124");
        event2.setName("5K Run 2026");
        event2.setEventDt(new Date());
        event2.setLocation("Beach Area");
        event2.setCountry("USA");

        mockEvents = Arrays.asList(event1, event2);
    }

    @Test
    void testGetEventsByOrganization() throws Exception {
        when(orgEventService.getEventsByOrgId(anyString())).thenReturn(mockEvents);

        mockMvc.perform(post("/org/event/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orgIdRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("event123"))
                .andExpect(jsonPath("$[0].name").value("Marathon 2026"))
                .andExpect(jsonPath("$[1].id").value("event124"))
                .andExpect(jsonPath("$.length()").value(2));

        verify(orgEventService, times(1)).getEventsByOrgId("org123");
    }

    @Test
    void testGetEventsByOrganizationEmpty() throws Exception {
        when(orgEventService.getEventsByOrgId(anyString())).thenReturn(Arrays.asList());

        mockMvc.perform(post("/org/event/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orgIdRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(orgEventService, times(1)).getEventsByOrgId("org123");
    }
}
