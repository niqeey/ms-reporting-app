package com.smart.reporting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.reporting.dto.*;
import com.smart.reporting.service.RaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RaceController.class)
@ContextConfiguration(classes = RaceController.class)
class RaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RaceService raceService;

        @MockBean
        private LoginController loginController;

        @MockBean
        private OrgEventController orgEventController;

        @MockBean
        private ParticipantController participantController;

        @MockBean
        private ReportController reportController;

        @MockBean
        private StatisticController statisticController;

        @MockBean
        private PublicLeaderboardController publicLeaderboardController;

    private EventCategoryRequest eventCategoryRequest;
    private RaceCategoryRequest raceCategoryRequest;
    private List<EventCategoryResponse> eventCategoryResponses;
    private RaceCategoryResponse raceCategoryResponse;

    @BeforeEach
    void setUp() {
        eventCategoryRequest = new EventCategoryRequest();
        eventCategoryRequest.setEventId("event123");

        EventCategoryResponse response1 = new EventCategoryResponse(
                "event123",
                "Marathon 2026",
                1L,
                "10K",
                "10 Kilometers",
                new BigDecimal("10.0"),
                "M",
                "1,2,3",
                "time",
                10,
                3,
                1,
                1,
                false
        );

        EventCategoryResponse response2 = new EventCategoryResponse(
                "event123",
                "Marathon 2026",
                2L,
                "5K",
                "5 Kilometers",
                new BigDecimal("5.0"),
                "F",
                "1,2,3",
                "time",
                10,
                3,
                1,
                1,
                false
        );

        eventCategoryResponses = Arrays.asList(response1, response2);

        raceCategoryRequest = new RaceCategoryRequest();
        raceCategoryRequest.setEventId("event123");
        raceCategoryRequest.setCat("10K");
        raceCategoryRequest.setCategory("10 Kilometers");
        raceCategoryRequest.setDistance(new BigDecimal("10.0"));
        raceCategoryRequest.setGender("M");
        raceCategoryRequest.setCheckpointlist("1,2,3");
        raceCategoryRequest.setRaceMode("time");
        raceCategoryRequest.setToplist(10);
        raceCategoryRequest.setTopprize(3);

        raceCategoryResponse = new RaceCategoryResponse();
        raceCategoryResponse.setCatId(1);
        raceCategoryResponse.setEventId("event123");
        raceCategoryResponse.setCat("10K");
        raceCategoryResponse.setCategory("10 Kilometers");
        raceCategoryResponse.setDistance(new BigDecimal("10.0"));
        raceCategoryResponse.setGender("M");
    }

    @Test
    void testGetCategoriesByEvent() throws Exception {
        when(raceService.getCategoriesByEventResult(anyString())).thenReturn(eventCategoryResponses);

        mockMvc.perform(post("/race/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventCategoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventId").value("event123"))
                .andExpect(jsonPath("$[0].cat").value("10K"))
                .andExpect(jsonPath("$[1].cat").value("5K"))
                .andExpect(jsonPath("$.length()").value(2));

        verify(raceService, times(1)).getCategoriesByEventResult("event123");
    }

    @Test
    void testCreateCategory() throws Exception {
        when(raceService.createCategory(any(RaceCategoryRequest.class))).thenReturn(raceCategoryResponse);

        mockMvc.perform(post("/race/category/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(raceCategoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.catId").value(1))
                .andExpect(jsonPath("$.eventId").value("event123"))
                .andExpect(jsonPath("$.cat").value("10K"))
                .andExpect(jsonPath("$.category").value("10 Kilometers"));

        verify(raceService, times(1)).createCategory(any(RaceCategoryRequest.class));
    }

    @Test
    void testUpdateCategory() throws Exception {
        raceCategoryRequest.setCatId(1);
        when(raceService.updateCategory(any(RaceCategoryRequest.class))).thenReturn(raceCategoryResponse);

        mockMvc.perform(post("/race/category/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(raceCategoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.catId").value(1))
                .andExpect(jsonPath("$.eventId").value("event123"));

        verify(raceService, times(1)).updateCategory(any(RaceCategoryRequest.class));
    }

    @Test
    void testUpdateTimegun() throws Exception {
        raceCategoryRequest.setTimegun(36000);
        doNothing().when(raceService).updateTimegun(anyString(), anyString(), anyInt());

        mockMvc.perform(post("/race/category/timegun")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(raceCategoryRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Timegun updated successfully"));

        verify(raceService, times(1)).updateTimegun("event123", "10K", 36000);
    }

    @Test
    void testUpdateTimegunWithMissingParameters() throws Exception {
        RaceCategoryRequest invalidRequest = new RaceCategoryRequest();
        invalidRequest.setEventId("event123");
        // Missing cat and timegun

        mockMvc.perform(post("/race/category/timegun")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("eventId, cat, and timegun are required"));
    }

    @Test
    void testDeleteCategory() throws Exception {
        doNothing().when(raceService).deleteCategory(anyLong());

        mockMvc.perform(delete("/race/category")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Category deleted successfully."));

        verify(raceService, times(1)).deleteCategory(1L);
    }

    @Test
    void testUploadCsv() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "participants.csv",
                "text/csv",
                "bib,name,category\n101,John Doe,10K".getBytes()
        );

        CsvUploadResponse response = new CsvUploadResponse(10, 9, 1, "Upload successful");
        when(raceService.uploadParticipantsCsv(any(), anyString(), anyString())).thenReturn(response);

        mockMvc.perform(multipart("/race/category/upload-csv")
                .file(file)
                .param("eventId", "event123")
                .param("cat", "10K"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRows").value(10))
                .andExpect(jsonPath("$.inserted").value(9))
                .andExpect(jsonPath("$.updated").value(1))
                .andExpect(jsonPath("$.message").value("Upload successful"));

        verify(raceService, times(1)).uploadParticipantsCsv(any(), eq("event123"), eq("10K"));
    }

    @Test
    void testUploadCsvWithError() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "participants.csv",
                "text/csv",
                "invalid,data".getBytes()
        );

        when(raceService.uploadParticipantsCsv(any(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Invalid CSV format"));

        mockMvc.perform(multipart("/race/category/upload-csv")
                .file(file)
                .param("eventId", "event123")
                .param("cat", "10K"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Invalid CSV format"));
    }

    @Test
    void testStartRace() throws Exception {
        StartRaceRequest startRaceRequest = new StartRaceRequest();
        startRaceRequest.setEventId("event123");
        startRaceRequest.setCat("10K");
        startRaceRequest.setHalflap(5);

        when(raceService.startRace(anyString(), anyString(), anyInt())).thenReturn(50);

        mockMvc.perform(post("/race/category/start-race")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(startRaceRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Race started. Updated 50 participants."));

        verify(raceService, times(1)).startRace("event123", "10K", 5);
    }

    @Test
    void testStartRaceWithMissingParameters() throws Exception {
        StartRaceRequest invalidRequest = new StartRaceRequest();
        invalidRequest.setEventId("event123");
        // Missing cat and halflap

        mockMvc.perform(post("/race/category/start-race")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("eventId, cat, and halflap are required"));
    }
}
