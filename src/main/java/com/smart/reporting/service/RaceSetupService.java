package com.smart.reporting.service;

import com.smart.reporting.dto.RaceCategoryRequest;
import com.smart.reporting.dto.RaceCategoryResponse;
import com.smart.reporting.entity.TEventCat;
import com.smart.reporting.repository.TEventCatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RaceSetupService {

    @Autowired
    private TEventCatRepository tEventCatRepository;

    public List<RaceCategoryResponse> getCategoriesByEvent(String eventId) {
        return tEventCatRepository.findByEventId(eventId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public RaceCategoryResponse createCategory(RaceCategoryRequest req) {
        TEventCat cat = toEntity(req);
        TEventCat saved = tEventCatRepository.save(cat);
        return toResponse(saved);
    }

    public void deleteCategory(Long id) {
        tEventCatRepository.deleteById(id);
    }

    // Helper: DTO to Entity
    private TEventCat toEntity(RaceCategoryRequest req) {
        TEventCat cat = new TEventCat();
        cat.setEventId(req.getEventId());
        cat.setCat(req.getCat());
        cat.setCategory(req.getCategory());
        cat.setDistance(req.getDistance());
        cat.setRace(req.getRace());
        cat.setGender(req.getGender());
        cat.setTimegun(req.getTimegun());
        cat.setTimegun1(req.getTimegun1());
        cat.setTimegun2(req.getTimegun2());
        cat.setTimegun3(req.getTimegun3());
        cat.setTimegun4(req.getTimegun4());
        cat.setTimegun5(req.getTimegun5());
        cat.setTimegun6(req.getTimegun6());
        cat.setTop(req.getToplist());
        cat.setTopPrize(req.getTopprize());
        return cat;
    }

    // Helper: Entity to DTO
    private RaceCategoryResponse toResponse(TEventCat cat) {
        RaceCategoryResponse resp = new RaceCategoryResponse();
        resp.setCatId(cat.getCatId());
        resp.setEventId(cat.getEventId());
        resp.setCat(cat.getCat());
        resp.setCategory(cat.getCategory());
        resp.setDistance(cat.getDistance());
        resp.setRace(cat.getRace());
        resp.setGender(cat.getGender());
        resp.setTimegun(cat.getTimegun());
        resp.setTimegun1(cat.getTimegun1());
        resp.setTimegun2(cat.getTimegun2());
        resp.setTimegun3(cat.getTimegun3());
        resp.setTimegun4(cat.getTimegun4());
        resp.setTimegun5(cat.getTimegun5());
        resp.setTimegun6(cat.getTimegun6());
        resp.setToplist(cat.getTop() != null ? cat.getTop() : 0);
        resp.setTopprize(cat.getTopPrize() != null ? cat.getTopPrize() : 0);
        return resp;
    }
}