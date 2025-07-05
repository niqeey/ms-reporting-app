package com.smart.reporting.controller;

import com.smart.reporting.dto.EventCategoryRequest;
import com.smart.reporting.dto.EventCategoryResponse;
import com.smart.reporting.dto.RaceCategoryRequest;
import com.smart.reporting.dto.RaceCategoryResponse;
import com.smart.reporting.service.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/race")
public class RaceController {

    @Autowired
    private RaceService raceService;

    // Get all race categories for an event
    @PostMapping("/categories")
    public List<EventCategoryResponse> getCategoriesByEvent(@RequestBody EventCategoryRequest req) {
        System.out.println("DEBUG: getCategoriesByEvent called with eventId = " + req.getEventId());
    
        return raceService.getCategoriesByEventResult(req.getEventId());
    }

    // Create a new race category
    @PostMapping("/category/create")
    public ResponseEntity<RaceCategoryResponse> createCategory(@RequestBody RaceCategoryRequest req) {
        RaceCategoryResponse resp = raceService.createCategory(req);
        return ResponseEntity.ok(resp);
    }

    // Delete a race category by id
    @DeleteMapping("/category")
    public ResponseEntity<?> deleteCategory(@RequestParam Long id) {
        raceService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully.");
    }
}