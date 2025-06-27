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
        cat.setGuntime(req.getGuntime());
        cat.setGuntime1(req.getGuntime1());
        cat.setGuntime2(req.getGuntime2());
        cat.setGuntime3(req.getGuntime3());
        cat.setGuntime4(req.getGuntime4());
        cat.setGuntime5(req.getGuntime5());
        cat.setGuntime6(req.getGuntime6());
        return cat;
    }

    // Helper: Entity to DTO
    private RaceCategoryResponse toResponse(TEventCat cat) {
        RaceCategoryResponse resp = new RaceCategoryResponse();
        resp.setId(cat.getId());
        resp.setEventId(cat.getEventId());
        resp.setCat(cat.getCat());
        resp.setCategory(cat.getCategory());
        resp.setDistance(cat.getDistance());
        resp.setRace(cat.getRace());
        resp.setGender(cat.getGender());
        resp.setGuntime(cat.getGuntime());
        resp.setGuntime1(cat.getGuntime1());
        resp.setGuntime2(cat.getGuntime2());
        resp.setGuntime3(cat.getGuntime3());
        resp.setGuntime4(cat.getGuntime4());
        resp.setGuntime5(cat.getGuntime5());
        resp.setGuntime6(cat.getGuntime6());
        return resp;
    }
}