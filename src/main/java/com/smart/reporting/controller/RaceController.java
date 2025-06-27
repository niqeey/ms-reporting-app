package com.smart.reporting.controller;

import com.smart.reporting.dto.RaceCategoryRequest;
import com.smart.reporting.dto.RaceCategoryResponse;
import com.smart.reporting.service.RaceSetupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/race")
public class RaceController {

    @Autowired
    private RaceSetupService raceSetupService;

    // Get all race categories for an event
    @GetMapping("/categories")
    public List<RaceCategoryResponse> getCategoriesByEvent(@RequestParam String eventId) {
        return raceSetupService.getCategoriesByEvent(eventId);
    }

    // Create a new race category
    @PostMapping("/category")
    public ResponseEntity<RaceCategoryResponse> createCategory(@RequestBody RaceCategoryRequest req) {
        RaceCategoryResponse resp = raceSetupService.createCategory(req);
        return ResponseEntity.ok(resp);
    }

    // Delete a race category by id
    @DeleteMapping("/category")
    public ResponseEntity<?> deleteCategory(@RequestParam Long id) {
        raceSetupService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully.");
    }
}