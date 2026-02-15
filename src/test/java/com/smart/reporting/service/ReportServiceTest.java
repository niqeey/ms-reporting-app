package com.smart.reporting.service;

import com.smart.reporting.model.RunnerResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ReportService reportService;

    private List<RunnerResult> mockRunnerResults;
    private List<Map<String, Object>> mockMapResults;

    @BeforeEach
    void setUp() {
        mockRunnerResults = Arrays.asList(
                new RunnerResult(1, "John Doe", "01:30:45"),
                new RunnerResult(2, "Jane Smith", "01:35:20"),
                new RunnerResult(3, "Bob Johnson", "01:40:15")
        );

        Map<String, Object> result1 = new HashMap<>();
        result1.put("rank", 1);
        result1.put("name", "John Doe");
        result1.put("time", "01:30:45");

        Map<String, Object> result2 = new HashMap<>();
        result2.put("rank", 2);
        result2.put("name", "Jane Smith");
        result2.put("time", "01:35:20");

        mockMapResults = Arrays.asList(result1, result2);
    }

    @Test
    void testFetchResults() {
        when(jdbcTemplate.execute(any(ConnectionCallback.class))).thenReturn(mockRunnerResults);

        List<RunnerResult> result = reportService.fetchResults("2026", "marathon");

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals(1, result.get(0).getRank());
        assertEquals("01:30:45", result.get(0).getTime());

        verify(jdbcTemplate, times(1)).execute(any(ConnectionCallback.class));
    }

    @Test
    void testFetchResultsEmptyList() {
        when(jdbcTemplate.execute(any(ConnectionCallback.class))).thenReturn(Arrays.asList());

        List<RunnerResult> result = reportService.fetchResults("2026", "marathon");

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(jdbcTemplate, times(1)).execute(any(ConnectionCallback.class));
    }

    @Test
    void testGetMarathonResults() {
        when(jdbcTemplate.queryForList(anyString())).thenReturn(mockMapResults);

        List<Map<String, Object>> result = reportService.getMarathonResults();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).get("name"));
        assertEquals(1, result.get(0).get("rank"));

        verify(jdbcTemplate, times(1)).queryForList("CALL get_marathon_results()");
    }

    @Test
    void testGetMarathonResultsEmpty() {
        when(jdbcTemplate.queryForList(anyString())).thenReturn(Arrays.asList());

        List<Map<String, Object>> result = reportService.getMarathonResults();

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(jdbcTemplate, times(1)).queryForList("CALL get_marathon_results()");
    }
}
