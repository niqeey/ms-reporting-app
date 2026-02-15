package com.smart.reporting.service;

import com.smart.reporting.dto.CsvUploadResponse;
import com.smart.reporting.dto.EventCategoryResponse;
import com.smart.reporting.dto.RaceCategoryRequest;
import com.smart.reporting.dto.RaceCategoryResponse;
import com.smart.reporting.entity.TEvent;
import com.smart.reporting.entity.TEventCat;
import com.smart.reporting.entity.TResults;
import com.smart.reporting.repository.TEventCatRepository;
import com.smart.reporting.repository.TOrgEventRepository;
import com.smart.reporting.repository.TResultsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RaceServiceTest {

    @Mock
    private TEventCatRepository tEventCatRepository;

    @Mock
    private TOrgEventRepository tOrgEventRepository;

    @Mock
    private EventService eventService;

    @Mock
    private TResultsRepository tResultsRepository;

    @InjectMocks
    private RaceService raceService;

    private TEventCat eventCat1;
    private TEventCat eventCat2;
    private TEvent event;
    private RaceCategoryRequest raceCategoryRequest;

    @BeforeEach
    void setUp() {
        eventCat1 = new TEventCat();
        eventCat1.setCatId(1);
        eventCat1.setEventId("event123");
        eventCat1.setCat("10K");
        eventCat1.setCategory("10 Kilometers");
        eventCat1.setDistance(new BigDecimal("10.0"));
        eventCat1.setGender("M");
        eventCat1.setCplist("1,2,3");
        eventCat1.setRacemode("time");
        eventCat1.setTop(10);
        eventCat1.setTopPrize(3);
        eventCat1.setIsResult(1);
        eventCat1.setIsLive(1);

        eventCat2 = new TEventCat();
        eventCat2.setCatId(2);
        eventCat2.setEventId("event123");
        eventCat2.setCat("5K");
        eventCat2.setCategory("5 Kilometers");
        eventCat2.setDistance(new BigDecimal("5.0"));
        eventCat2.setGender("F");
        eventCat2.setIsResult(1);

        event = new TEvent();
        event.setId("event123");
        event.setName("Marathon 2026");

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
        raceCategoryRequest.setIsresult(1);
        raceCategoryRequest.setIslive(1);
    }

    @Test
    void testGetCategoriesByEventResult() {
        when(tEventCatRepository.findByEventIdAndIsResultGreaterThan(anyString(), anyInt()))
                .thenReturn(Arrays.asList(eventCat1, eventCat2));
        when(eventService.getEventById(anyString())).thenReturn(event);

        List<EventCategoryResponse> result = raceService.getCategoriesByEventResult("event123");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("10K", result.get(0).getCat());
        assertEquals("5K", result.get(1).getCat());
        assertEquals("Marathon 2026", result.get(0).getEventName());

        verify(tEventCatRepository, times(1))
                .findByEventIdAndIsResultGreaterThan("event123", 0);
        verify(eventService, times(1)).getEventById("event123");
    }

    @Test
    void testGetCategoriesByEventResultEmptyList() {
        when(tEventCatRepository.findByEventIdAndIsResultGreaterThan(anyString(), anyInt()))
                .thenReturn(Arrays.asList());
        when(eventService.getEventById(anyString())).thenReturn(event);

        List<EventCategoryResponse> result = raceService.getCategoriesByEventResult("event123");

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testCreateCategory() {
        when(tEventCatRepository.findMaxCatId()).thenReturn(5);
        when(tEventCatRepository.save(any(TEventCat.class))).thenReturn(eventCat1);

        RaceCategoryResponse result = raceService.createCategory(raceCategoryRequest);

        assertNotNull(result);
        assertEquals("event123", result.getEventId());
        assertEquals("10K", result.getCat());
        assertEquals("10 Kilometers", result.getCategory());
        assertEquals(new BigDecimal("10.0"), result.getDistance());

        verify(tEventCatRepository, times(1)).findMaxCatId();
        verify(tEventCatRepository, times(1)).save(any(TEventCat.class));
    }

    @Test
    void testCreateCategoryWithNullMaxId() {
        when(tEventCatRepository.findMaxCatId()).thenReturn(null);
        when(tEventCatRepository.save(any(TEventCat.class))).thenReturn(eventCat1);

        RaceCategoryResponse result = raceService.createCategory(raceCategoryRequest);

        assertNotNull(result);
        verify(tEventCatRepository, times(1)).findMaxCatId();
        verify(tEventCatRepository, times(1)).save(any(TEventCat.class));
    }

    @Test
    void testUpdateCategory() {
        raceCategoryRequest.setCatId(1);
        when(tEventCatRepository.findById(anyLong())).thenReturn(Optional.of(eventCat1));
        when(tEventCatRepository.save(any(TEventCat.class))).thenReturn(eventCat1);

        RaceCategoryResponse result = raceService.updateCategory(raceCategoryRequest);

        assertNotNull(result);
        assertEquals("event123", result.getEventId());
        assertEquals("10K", result.getCat());

        verify(tEventCatRepository, times(1)).findById(1L);
        verify(tEventCatRepository, times(1)).save(any(TEventCat.class));
    }

    @Test
    void testUpdateCategoryNotFound() {
        raceCategoryRequest.setCatId(999);
        when(tEventCatRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            raceService.updateCategory(raceCategoryRequest);
        });

        verify(tEventCatRepository, times(1)).findById(999L);
        verify(tEventCatRepository, never()).save(any(TEventCat.class));
    }

    @Test
    void testUpdateTimegun() {
        when(tEventCatRepository.updateTimegunByEventIdAndCat(anyString(), anyString(), anyInt())).thenReturn(1);
        when(tResultsRepository.updateTimegunByEventIdAndCat(anyString(), anyString(), anyInt())).thenReturn(1);

        raceService.updateTimegun("event123", "10K", 36000);

        verify(tEventCatRepository, times(1))
                .updateTimegunByEventIdAndCat("event123", "10K", 36000);
        verify(tResultsRepository, times(1))
                .updateTimegunByEventIdAndCat("event123", "10K", 36000);
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(tEventCatRepository).deleteById(anyLong());

        raceService.deleteCategory(1L);

        verify(tEventCatRepository, times(1)).deleteById(1L);
    }

    @Test
    void testStartRace() {
        TResults result1 = new TResults();
        result1.setEventId("event123");
        result1.setCat("10K");
        result1.setTimegun(3600000);

        TResults result2 = new TResults();
        result2.setEventId("event123");
        result2.setCat("10K");
        result2.setTimegun(3600000);

        when(tResultsRepository.findByEventIdAndCat(anyString(), anyString(), any(org.springframework.data.domain.Pageable.class)))
            .thenReturn(Arrays.asList(result1, result2));
        when(tResultsRepository.save(any(TResults.class))).thenAnswer(invocation -> invocation.getArgument(0));

        int result = raceService.startRace("event123", "10K", 1);

        assertEquals(2, result);
        verify(tResultsRepository, times(2)).save(any(TResults.class));
    }
}
