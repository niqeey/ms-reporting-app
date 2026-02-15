package com.smart.reporting.service;

import com.smart.reporting.dto.LeaderboardResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeaderboardServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private LeaderboardService leaderboardService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetLeaderboardDataWithValidEventAndCategory() {
        List<LeaderboardResponse> expectedList = Arrays.asList(
            createLeaderboardResponse("101", "John Doe", "M", 1, 1, 1, 3600000, 5400000),
            createLeaderboardResponse("102", "Jane Smith", "F", 1, 2, 2, 3700000, 5500000)
        );

        when(jdbcTemplate.query(
            eq("CALL P_LEADERBOARD_REPORT(?, ?)"),
            eq(new Object[]{"event123", "M"}),
            any(RowMapper.class)
        )).thenReturn(expectedList);
        
        List<LeaderboardResponse> result = leaderboardService.getLeaderboardData("event123", "M");
        
        assertEquals(2, result.size());
        assertEquals("101", result.get(0).getBib());
        assertEquals("John Doe", result.get(0).getName());
        verify(jdbcTemplate, times(1)).query(
            eq("CALL P_LEADERBOARD_REPORT(?, ?)"),
            eq(new Object[]{"event123", "M"}),
            any(RowMapper.class)
        );
    }

    @Test
    void testGetLeaderboardDataEmptyResult() {
        when(jdbcTemplate.query(
            eq("CALL P_LEADERBOARD_REPORT(?, ?)"),
            eq(new Object[]{"eventEmpty", "M"}),
            any(RowMapper.class)
        )).thenReturn(Collections.emptyList());
        
        List<LeaderboardResponse> result = leaderboardService.getLeaderboardData("eventEmpty", "M");
        
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetLeaderboardDataWithDifferentCategories() {
        List<LeaderboardResponse> femaleList = Arrays.asList(
            createLeaderboardResponse("201", "Alice", "F", 1, 1, 1, 3600000, 5400000)
        );

        when(jdbcTemplate.query(
            eq("CALL P_LEADERBOARD_REPORT(?, ?)"),
            eq(new Object[]{"event123", "F"}),
            any(RowMapper.class)
        )).thenReturn(femaleList);
        
        List<LeaderboardResponse> result = leaderboardService.getLeaderboardData("event123", "F");
        
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertEquals("F", result.get(0).getCategory());
    }

    @Test
    void testGetLeaderboardDataWithNullCategory() {
        when(jdbcTemplate.query(
            eq("CALL P_LEADERBOARD_REPORT(?, ?)"),
            eq(new Object[]{"event123", null}),
            any(RowMapper.class)
        )).thenReturn(Collections.emptyList());
        
        List<LeaderboardResponse> result = leaderboardService.getLeaderboardData("event123", null);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetLeaderboardDataTimeFormatting() {
        LeaderboardResponse response = new LeaderboardResponse();
        response.setBib("301");
        response.setName("Bob Johnson");
        response.setCategory("M");
        response.setRankCat(1);
        response.setRankMix(1);
        response.setRankTot(1);
        response.setTimeStart("01:00:00");
        response.setTimeFinish("02:30:00");
        response.setNetTime("01:30:00");
        response.setOfficialTime("01:30:00");

        when(jdbcTemplate.query(
            eq("CALL P_LEADERBOARD_REPORT(?, ?)"),
            eq(new Object[]{"event123", "M"}),
            any(RowMapper.class)
        )).thenReturn(Arrays.asList(response));
        
        List<LeaderboardResponse> result = leaderboardService.getLeaderboardData("event123", "M");
        
        assertEquals(1, result.size());
        assertEquals("01:00:00", result.get(0).getTimeStart());
        assertEquals("02:30:00", result.get(0).getTimeFinish());
    }

    private LeaderboardResponse createLeaderboardResponse(String bib, String name, String category,
                                                         int rankCat, int rankMix, int rankTot,
                                                         int netTime, int officialTime) {
        LeaderboardResponse response = new LeaderboardResponse();
        response.setBib(bib);
        response.setName(name);
        response.setCategory(category);
        response.setRankCat(rankCat);
        response.setRankMix(rankMix);
        response.setRankTot(rankTot);
        response.setNetTime(String.format("%02d:%02d:%02d", netTime / 3600000, (netTime % 3600000) / 60000, (netTime % 60000) / 1000));
        response.setOfficialTime(String.format("%02d:%02d:%02d", officialTime / 3600000, (officialTime % 3600000) / 60000, (officialTime % 60000) / 1000));
        return response;
    }
}
