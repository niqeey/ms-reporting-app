package com.smart.reporting.service;

import com.smart.reporting.entity.TOrg;
import com.smart.reporting.repository.TOrgRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrgServiceTest {

    @Mock
    private TOrgRepository tOrgRepository;

    @InjectMocks
    private OrgService orgService;

    private TOrg testOrg;

    @BeforeEach
    void setUp() {
        testOrg = new TOrg();
        testOrg.setId("org123");
        testOrg.setOrgName("Test Organization");
        testOrg.setOwner("Test Owner");
    }

    @Test
    void testGetOrgByIdSuccess() {
        when(tOrgRepository.findById("org123")).thenReturn(Optional.of(testOrg));
        
        Optional<TOrg> result = orgService.getOrgById("org123");
        
        assertTrue(result.isPresent());
        assertEquals("org123", result.get().getId());
        assertEquals("Test Organization", result.get().getOrgName());
        verify(tOrgRepository, times(1)).findById("org123");
    }

    @Test
    void testGetOrgByIdNotFound() {
        when(tOrgRepository.findById("nonexistent")).thenReturn(Optional.empty());
        
        Optional<TOrg> result = orgService.getOrgById("nonexistent");
        
        assertFalse(result.isPresent());
        verify(tOrgRepository, times(1)).findById("nonexistent");
    }

    @Test
    void testGetOrgByIdWithNullId() {
        when(tOrgRepository.findById(null)).thenReturn(Optional.empty());
        
        Optional<TOrg> result = orgService.getOrgById(null);
        
        assertFalse(result.isPresent());
    }

    @Test
    void testGetOrgByIdMultipleCalls() {
        when(tOrgRepository.findById("org123")).thenReturn(Optional.of(testOrg));
        
        Optional<TOrg> result1 = orgService.getOrgById("org123");
        Optional<TOrg> result2 = orgService.getOrgById("org123");
        
        assertTrue(result1.isPresent());
        assertTrue(result2.isPresent());
        assertEquals(result1.get().getId(), result2.get().getId());
        verify(tOrgRepository, times(2)).findById("org123");
    }
}
