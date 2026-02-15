package com.smart.reporting.service;

import com.smart.reporting.dto.RaceCategoryRequest;
import com.smart.reporting.dto.RaceCategoryResponse;
import com.smart.reporting.entity.TEventCat;
import com.smart.reporting.repository.TEventCatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RaceSetupServiceTest {

    @Mock
    private TEventCatRepository tEventCatRepository;

    @InjectMocks
    private RaceSetupService raceSetupService;

    private RaceCategoryRequest categoryRequest;
    private TEventCat eventCat;

    @BeforeEach
    void setUp() {
        categoryRequest = new RaceCategoryRequest();
        categoryRequest.setEventId("event123");
        categoryRequest.setCat("10K");
        categoryRequest.setCategory("10 Kilometers");
        categoryRequest.setDistance(new BigDecimal("10.0"));

        eventCat = new TEventCat();
        eventCat.setEventId("event123");
        eventCat.setCat("10K");
        eventCat.setCategory("10 Kilometers");
        eventCat.setDistance(new BigDecimal("10.0"));
    }

    @Test
    void testGetCategoriesByEventSuccess() {
        TEventCat cat1 = new TEventCat();
        cat1.setCat("10K");
        TEventCat cat2 = new TEventCat();
        cat2.setCat("5K");

        when(tEventCatRepository.findByEventId("event123"))
                .thenReturn(Arrays.asList(cat1, cat2));

        List<RaceCategoryResponse> result = raceSetupService.getCategoriesByEvent("event123");

        assertEquals(2, result.size());
        verify(tEventCatRepository, times(1)).findByEventId("event123");
    }

    @Test
    void testGetCategoriesByEventEmpty() {
        when(tEventCatRepository.findByEventId("emptyevent"))
                .thenReturn(Collections.emptyList());

        List<RaceCategoryResponse> result = raceSetupService.getCategoriesByEvent("emptyevent");

        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateCategorySuccess() {
        when(tEventCatRepository.save(any(TEventCat.class))).thenReturn(eventCat);

        RaceCategoryResponse result = raceSetupService.createCategory(categoryRequest);

        assertNotNull(result);
        assertEquals("10K", result.getCat());
        verify(tEventCatRepository, times(1)).save(any(TEventCat.class));
    }

    @Test
    void testCreateCategoryWithDistanceZero() {
        RaceCategoryRequest request = new RaceCategoryRequest();
        request.setEventId("event123");
        request.setCat("Walk");
        request.setDistance(new BigDecimal("0"));

        TEventCat savedCat = new TEventCat();
        savedCat.setDistance(new BigDecimal("0"));

        when(tEventCatRepository.save(any(TEventCat.class))).thenReturn(savedCat);

        RaceCategoryResponse result = raceSetupService.createCategory(request);

        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getDistance());
    }

    @Test
    void testDeleteCategorySuccess() {
        doNothing().when(tEventCatRepository).deleteById(1L);

        raceSetupService.deleteCategory(1L);

        verify(tEventCatRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCategoryNonexistent() {
        doNothing().when(tEventCatRepository).deleteById(999L);

        raceSetupService.deleteCategory(999L);

        verify(tEventCatRepository, times(1)).deleteById(999L);
    }

    @Test
    void testGetCategoriesByEventWithMultipleCategories() {
        List<TEventCat> categories = new java.util.ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TEventCat cat = new TEventCat();
            cat.setCat(i + "K");
            categories.add(cat);
        }

        when(tEventCatRepository.findByEventId("largeEvent"))
                .thenReturn(categories);

        List<RaceCategoryResponse> result = raceSetupService.getCategoriesByEvent("largeEvent");

        assertEquals(10, result.size());
    }
}
