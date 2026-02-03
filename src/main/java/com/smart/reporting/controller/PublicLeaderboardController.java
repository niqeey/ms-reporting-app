package com.smart.reporting.controller;

import com.smart.reporting.dto.EventResponse;
import com.smart.reporting.dto.LeaderboardRequest;
import com.smart.reporting.dto.LeaderboardResponse;
import com.smart.reporting.dto.OrgIdRequest;
import com.smart.reporting.entity.TEventCat;
import com.smart.reporting.service.EventCatService;
import com.smart.reporting.service.LeaderboardService;
import com.smart.reporting.service.OrgEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/public/leaderboard")
public class PublicLeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private EventCatService eventCatService;

    @Autowired
    private OrgEventService orgEventService;

    /**
     * Get all events for an organization
     * Public endpoint - no authentication required
     */
    @PostMapping("/events")
    public List<EventResponse> getEventList(@RequestBody OrgIdRequest req) {
        return orgEventService.getEventsByOrgId(req.getOrgId())
                .stream()
                .map(event -> {
                    EventResponse dto = new EventResponse();
                    dto.setId(event.getId());
                    dto.setName(event.getName());
                    dto.setEventDt(event.getEventDt());
                    dto.setLocation(event.getLocation());
                    dto.setCountry(event.getCountry());
                    return dto;
                })
                .collect(Collectors.toList());
    }

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
