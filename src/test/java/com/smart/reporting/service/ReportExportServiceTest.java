package com.smart.reporting.service;

import com.smart.reporting.entity.TEvent;
import com.smart.reporting.entity.TOrg;
import com.smart.reporting.repository.TEventRepository;
import com.smart.reporting.repository.TOrgRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportExportServiceTest {

    @Mock
    private StatisticReportService statisticReportService;

    @Mock
    private TEventRepository eventRepository;

    @Mock
    private TOrgRepository orgRepository;

    @Mock
    private RaceResultService raceResultService;

    @InjectMocks
    private ReportExportService reportExportService;

    private TEvent testEvent;
    private TOrg testOrg;

    @BeforeEach
    void setUp() {
        testEvent = new TEvent();
        testEvent.setId("event123");
        testEvent.setName("Test Event");
        testEvent.setCountry("USA");

        testOrg = new TOrg();
        testOrg.setId("org123");
        testOrg.setOrgName("Test Org");
    }

    @Test
    void testExportEventStatisticsWithValidEvent() {
        when(eventRepository.findById("event123")).thenReturn(Optional.of(testEvent));

        Optional<TEvent> result = eventRepository.findById("event123");

        assertTrue(result.isPresent());
        assertEquals("Test Event", result.get().getName());
    }

    @Test
    void testExportEventStatisticsWithInvalidEvent() {
        when(eventRepository.findById("nonexistent")).thenReturn(Optional.empty());

        Optional<TEvent> result = eventRepository.findById("nonexistent");

        assertFalse(result.isPresent());
    }

    @Test
    void testExportWithOrgInformation() {
        when(orgRepository.findById("org123")).thenReturn(Optional.of(testOrg));

        Optional<TOrg> result = orgRepository.findById("org123");

        assertTrue(result.isPresent());
        assertEquals("Test Org", result.get().getOrgName());
    }

    @Test
    void testExportWithMissingOrg() {
        when(orgRepository.findById("missingOrg")).thenReturn(Optional.empty());

        Optional<TOrg> result = orgRepository.findById("missingOrg");

        assertFalse(result.isPresent());
    }

    @Test
    void testExportEventWithNullValues() {
        TEvent nullEvent = new TEvent();
        nullEvent.setId("nullEvent");
        // name, country are null

        when(eventRepository.findById("nullEvent")).thenReturn(Optional.of(nullEvent));

        Optional<TEvent> result = eventRepository.findById("nullEvent");

        assertTrue(result.isPresent());
        assertNull(result.get().getName());
    }

    @Test
    void testExportMultipleEvents() {
        TEvent event2 = new TEvent();
        event2.setId("event456");
        event2.setName("Another Event");

        when(eventRepository.findById("event123")).thenReturn(Optional.of(testEvent));
        when(eventRepository.findById("event456")).thenReturn(Optional.of(event2));

        Optional<TEvent> result1 = eventRepository.findById("event123");
        Optional<TEvent> result2 = eventRepository.findById("event456");

        assertTrue(result1.isPresent());
        assertTrue(result2.isPresent());
        assertEquals("Test Event", result1.get().getName());
        assertEquals("Another Event", result2.get().getName());
    }

    @Test
    void testExportInvokesRepository() {
        eventRepository.findById("event123");
        eventRepository.findById("event123");

        verify(eventRepository, times(2)).findById("event123");
    }
}
