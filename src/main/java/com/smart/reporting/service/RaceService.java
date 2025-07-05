package com.smart.reporting.service;

import com.smart.reporting.dto.EventCategoryResponse;
import com.smart.reporting.dto.RaceCategoryRequest;
import com.smart.reporting.dto.RaceCategoryResponse;
import com.smart.reporting.entity.TEventCat;
import com.smart.reporting.repository.TEventCatRepository;
import com.smart.reporting.repository.TOrgEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import com.smart.reporting.entity.TEvent;

@Service
public class RaceService {

    @Autowired
    private TEventCatRepository tEventCatRepository;

    @Autowired
    private TOrgEventRepository tOrgEventRepository;

    @Autowired
    private EventService eventService;

    public List<EventCategoryResponse> getCategoriesByEventResult(String eventId) {
        List<TEventCat> categories = tEventCatRepository.findByEventIdAndIsResultGreaterThan(eventId, 0);
        System.out.println("DEBUG: getCategoriesByEvent called with eventId = " + eventId);
        TEvent event = eventService.getEventById(eventId);

        return categories.stream()
                .map(cat -> new EventCategoryResponse(
                        cat.getEventId(),
                        event != null ? event.getName() : null,
                        cat.getCatId() != null ? cat.getCatId().longValue() : null,
                        cat.getCat(),
                        cat.getCategory(),
                        cat.getDistance(),
                        cat.getGender(),
                        cat.getCplist(),
                        cat.getRacemode(),
                        cat.getTop() != null ? cat.getTop() : 0,
                        cat.getIsLive() != null ? cat.getIsLive() : 0,
                        cat.getIsResult() != null ? cat.getIsResult() : 0
                ))
                .collect(Collectors.toList());
    }

    public RaceCategoryResponse createCategory(RaceCategoryRequest req) {
        // Dummy implementation, replace with real logic
        return new RaceCategoryResponse();
    }

    public void deleteCategory(Long id) {
        // Dummy implementation, replace with real logic
    }
}