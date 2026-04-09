package com.smart.reporting.controller;

import com.smart.reporting.dto.EventResponse;
import com.smart.reporting.dto.LeaderboardRequest;
import com.smart.reporting.dto.LeaderboardResponse;
import com.smart.reporting.dto.OrgIdRequest;
import com.smart.reporting.dto.LapResultResponse;
import com.smart.reporting.dto.CategoryResultListWrapper;
import com.smart.reporting.dto.EventCategoryResultRequest;
import com.smart.reporting.entity.TEventCat;
import com.smart.reporting.entity.TResults;
import com.smart.reporting.service.EventCatService;
import com.smart.reporting.service.LeaderboardService;
import com.smart.reporting.service.OrgEventService;
import com.smart.reporting.service.RaceResultService;
import com.smart.reporting.util.TimeFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
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

    @Autowired
    private RaceResultService raceResultService;

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

    /**
     * Get LAP mode leaderboard data for a specific event and category
     * Public endpoint - no authentication required
     */
    @PostMapping("/lap/{eventId}")
    public CategoryResultListWrapper getLapLeaderboard(
            @PathVariable String eventId,
            @RequestBody EventCategoryResultRequest request) {
        List<TResults> results = raceResultService.getResultsByEventAndCat(eventId, request.getCategory());
        List<TEventCat> eventcat = eventCatService.getByEventIdAndCat(eventId, request.getCategory());
        
        // Get cplist from eventcat
        String cplist = null;
        if (eventcat != null && eventcat.size() > 0) {
            cplist = eventcat.get(0).getCplist();
        }
        final String finalCplist = cplist;
        
        // Map results to LapResultResponse
        List<LapResultResponse> lapResponseList = results.stream().map(result -> {
            LapResultResponse dto = new LapResultResponse();
            dto.setName(result.getName());
            dto.setBib(result.getBib());
            dto.setCategory(result.getCategory());
            dto.setEventId(result.getEventId());
            if (eventcat != null && eventcat.size() > 0) {
                dto.setEventName(eventcat.get(0).getCategory());
            }
            dto.setCat(result.getCat());
            dto.setLap(result.getLap());
            dto.setBonusLap(result.getBonuslap());
            dto.setDqLap(result.getDqLap());
            // Fixed formulas: netTime = timefinish - timegun, officialTime = timefinish - timestart
            dto.setNetTime(result.getTimefinish() != null && result.getTimegun() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimegun()) : null);
            dto.setOfficialTime(result.getTimefinish() != null && result.getTimestart() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()-result.getTimestart()) : null);
            dto.setTimeStart(result.getTimestart() != null ? TimeFormatUtil.intToTimeString(result.getTimestart()) : "0");
            dto.setTimeFinish(result.getTimefinish() != null ? TimeFormatUtil.intToTimeString(result.getTimefinish()) : "0");
            dto.setTimeGun(result.getTimegun() != null ? TimeFormatUtil.intToTimeString(result.getTimegun()) : "0");
            dto.setCplist(finalCplist);
            
            // Set lap time values as intervals (not cumulative) using LapTimeCalculator
            // Parse cplist to determine if halflap > 0 (if so, include time0)
            boolean includeTimeZero = false;
            if (finalCplist != null && !finalCplist.isEmpty()) {
                String[] cplistParts = finalCplist.split(",");
                if (cplistParts.length > 0) {
                    try {
                        int halflap = Integer.parseInt(cplistParts[0].trim());
                        includeTimeZero = halflap > 0;
                    } catch (NumberFormatException e) {
                        // If parsing fails, default to false
                    }
                }
            }
            
            // If time0 is used, set it as the half-lap time (interval from timestart/timegun to time0)
            if (includeTimeZero) {
                try {
                    Method getter = TResults.class.getMethod("getTime0");
                    Integer time0Value = (Integer) getter.invoke(result);
                    if (time0Value != null && time0Value > 0) {
                        Integer baseTime = result.getTimestart() != null ? result.getTimestart() : result.getTimegun();
                        if (baseTime != null && baseTime > 0) {
                            int halfLapInterval = time0Value - baseTime;
                            dto.setLapTime(0, TimeFormatUtil.intToTimeString(halfLapInterval));
                        }
                    }
                } catch (Exception e) {
                    // Ignore if getter doesn't exist or fails
                }
            }
            
            // Calculate lap intervals for time1 to time49
            int startIndex = 1;
            int maxIndex = 50;
            for (int i = startIndex; i < maxIndex; i++) {
                String lapIntervalTime = com.smart.reporting.util.LapTimeCalculator.calculateLapIntervalFormatted(result, i, includeTimeZero);
                if (lapIntervalTime != null) {
                    dto.setLapTime(i, lapIntervalTime);
                }
            }
            
            return dto;
        }).collect(Collectors.toList());
        
        return new CategoryResultListWrapper("LAP", lapResponseList);
    }
}
