package com.smart.reporting.controller;

import com.smart.reporting.dto.LeaderboardRequest;
import com.smart.reporting.dto.LeaderboardResponse;
import com.smart.reporting.entity.TEventCat;
import com.smart.reporting.service.EventCatService;
import com.smart.reporting.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/leaderboard")
public class PublicLeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private EventCatService eventCatService;

    /**
     * Get leaderboard data for a specific event and category
     * Public endpoint - no authentication required
     */
    @PostMapping("/{eventId}")
    public List<LeaderboardResponse> getLeaderboard(
            @PathVariable String eventId,
            @RequestBody LeaderboardRequest request) {
        return leaderboardService.getLeaderboardData(eventId, request.getCategory());
    }

    /**
     * Get all categories for an event (for the category buttons)
     * Public endpoint - no authentication required
     */
    @GetMapping("/{eventId}/categories")
    public List<TEventCat> getEventCategories(@PathVariable String eventId) {
        return eventCatService.getByEventId(eventId);
    }
}
