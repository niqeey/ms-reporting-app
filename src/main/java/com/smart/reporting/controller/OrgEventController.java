package com.smart.reporting.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.smart.reporting.dto.DeleteEventRequest;
import com.smart.reporting.dto.EventResponse;
import com.smart.reporting.dto.OrgIdRequest;
import com.smart.reporting.model.RunnerResult;
import com.smart.reporting.service.OrgEventService;
import com.smart.reporting.service.EventArchiveService;


@RestController
@RequestMapping("/org/event")
public class OrgEventController {
    
    @Autowired
    private  OrgEventService orgService;

    @Autowired
    private EventArchiveService archiveService;

    @PostMapping("/list")
    public List<EventResponse> getOrgEventList(@RequestBody OrgIdRequest req) {
        return orgService.getEventsByOrgId(req.getOrgId())
                .stream()
                .map(event -> {
                    EventResponse dto = new EventResponse();
                    dto.setId(event.getId());
                    dto.setName(event.getName());
                    dto.setEventDt(event.getEventDt());
                    dto.setLocation(event.getLocation());
                    dto.setCountry(event.getCountry());
                    dto.setArchived(event.getArchived());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @PostMapping("/list/upcoming")
    public List<EventResponse> getUpcomingEventList(@RequestBody OrgIdRequest req) {
        return orgService.getUpcomingEventsByOrgId(req.getOrgId())
                .stream()
                .map(event -> {
                    EventResponse dto = new EventResponse();
                    dto.setId(event.getId());
                    dto.setName(event.getName());
                    dto.setEventDt(event.getEventDt());
                    dto.setLocation(event.getLocation());
                    dto.setCountry(event.getCountry());
                    dto.setArchived(event.getArchived());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @PostMapping("/list/archived")
    public List<EventResponse> getArchivedEventList(@RequestBody OrgIdRequest req) {
        return orgService.getArchivedEventsByOrgId(req.getOrgId())
                .stream()
                .map(event -> {
                    EventResponse dto = new EventResponse();
                    dto.setId(event.getId());
                    dto.setName(event.getName());
                    dto.setEventDt(event.getEventDt());
                    dto.setLocation(event.getLocation());
                    dto.setCountry(event.getCountry());
                    dto.setArchived(event.getArchived());
                    return dto;
                })
                .collect(Collectors.toList());
    }
 
    @PostMapping("/create")
    public ResponseEntity<?> createOrgEvent(@RequestBody EventResponse req, @RequestHeader("OrgId") String orgId) {
        try {
            EventResponse resp = orgService.createOrgEvent(req, orgId);
            return ResponseEntity.ok().body(resp);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Failed to create event: " + ex.getMessage());
        }
    }

    @GetMapping("/results")
    public List<RunnerResult> getOrgEventResults(@RequestParam String eventId) {
        return orgService.getOrgEventResults(eventId);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteOrgEvent(@RequestBody DeleteEventRequest req) {
        try {
            orgService.deleteOrgEvent(req.getEventId());
            return ResponseEntity.ok().body("Event deleted successfully.");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Failed to delete event: " + ex.getMessage());
        }
    }

    @PostMapping("/archive")
    public ResponseEntity<?> archiveOrgEvent(@RequestBody DeleteEventRequest req) {
        try {
            int count = archiveService.archiveEvent(req.getEventId());
            return ResponseEntity.ok().body(Map.of(
                "message", "Event archived successfully",
                "resultsArchived", count
            ));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Failed to archive event: " + ex.getMessage());
        }
    }

    @PostMapping("/unarchive")
    public ResponseEntity<?> unarchiveOrgEvent(@RequestBody DeleteEventRequest req) {
        try {
            int count = archiveService.unarchiveEvent(req.getEventId());
            return ResponseEntity.ok().body(Map.of(
                "message", "Event unarchived successfully",
                "resultsRestored", count
            ));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Failed to unarchive event: " + ex.getMessage());
        }
    }
}
