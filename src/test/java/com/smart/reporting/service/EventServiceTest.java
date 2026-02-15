package com.smart.reporting.service;

import com.smart.reporting.entity.TEvent;
import com.smart.reporting.repository.TEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private TEventRepository tEventRepository;

    @InjectMocks
    private EventService eventService;

    private TEvent testEvent;

    @BeforeEach
    void setUp() {
        testEvent = new TEvent();
        testEvent.setId("event123");
        testEvent.setName("Test Event");
        testEvent.setCountry("USA");
        testEvent.setLocation("New York");
    }

    @Test
    void testGetAllEvents() {
        TEvent event2 = new TEvent();
        event2.setId("event456");
        event2.setName("Another Event");
        
        when(tEventRepository.findAll()).thenReturn(Arrays.asList(testEvent, event2));
        
        List<TEvent> result = eventService.getAllEvents();
        
        assertEquals(2, result.size());
        assertEquals("event123", result.get(0).getId());
        assertEquals("event456", result.get(1).getId());
        verify(tEventRepository, times(1)).findAll();
    }

    @Test
    void testGetAllEventsEmpty() {
        when(tEventRepository.findAll()).thenReturn(Arrays.asList());
        
        List<TEvent> result = eventService.getAllEvents();
        
        assertTrue(result.isEmpty());
        verify(tEventRepository, times(1)).findAll();
    }

    @Test
    void testGetEventsByCountry() {
        when(tEventRepository.findByCountry("USA")).thenReturn(Arrays.asList(testEvent));
        
        List<TEvent> result = eventService.getEventsByCountry("USA");
        
        assertEquals(1, result.size());
        assertEquals("USA", result.get(0).getCountry());
        verify(tEventRepository, times(1)).findByCountry("USA");
    }

    @Test
    void testGetEventsByCountryNoResults() {
        when(tEventRepository.findByCountry("NonExistent")).thenReturn(Arrays.asList());
        
        List<TEvent> result = eventService.getEventsByCountry("NonExistent");
        
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetEventById() {
        when(tEventRepository.findById("event123")).thenReturn(Optional.of(testEvent));
        
        TEvent result = eventService.getEventById("event123");
        
        assertNotNull(result);
        assertEquals("event123", result.getId());
        assertEquals("Test Event", result.getName());
        verify(tEventRepository, times(1)).findById("event123");
    }

    @Test
    void testGetEventByIdNotFound() {
        when(tEventRepository.findById("nonexistent")).thenReturn(Optional.empty());
        
        TEvent result = eventService.getEventById("nonexistent");
        
        assertNull(result);
    }

    @Test
    void testSaveEvent() {
        when(tEventRepository.save(testEvent)).thenReturn(testEvent);
        
        TEvent result = eventService.saveEvent(testEvent);
        
        assertNotNull(result);
        assertEquals("event123", result.getId());
        verify(tEventRepository, times(1)).save(testEvent);
    }

    @Test
    void testSaveEventWithNullFields() {
        TEvent eventWithoutName = new TEvent();
        eventWithoutName.setId("event789");
        
        when(tEventRepository.save(eventWithoutName)).thenReturn(eventWithoutName);
        
        TEvent result = eventService.saveEvent(eventWithoutName);
        
        assertNotNull(result);
        assertEquals("event789", result.getId());
    }

    @Test
    void testDeleteEvent() {
        doNothing().when(tEventRepository).deleteById("event123");
        
        eventService.deleteEvent("event123");
        
        verify(tEventRepository, times(1)).deleteById("event123");
    }

    @Test
    void testDeleteEventNonexistent() {
        doNothing().when(tEventRepository).deleteById("nonexistent");
        
        eventService.deleteEvent("nonexistent");
        
        verify(tEventRepository, times(1)).deleteById("nonexistent");
    }
}
