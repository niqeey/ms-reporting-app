package com.smart.reporting.service;

import com.smart.reporting.entity.TEvent;
import com.smart.reporting.entity.TEventCat;
import com.smart.reporting.entity.TResults;
import com.smart.reporting.repository.TResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.smart.reporting.service.EventCatService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RaceResultService {

    @Autowired
    private TResultsRepository tResultsRepository;

    @Autowired
    private EventCatService eventCatService;


    public List<TResults> getResults(String eventId, String cat, int limit) {
        return tResultsRepository.findByEventIdAndCat(eventId, cat, PageRequest.of(0, limit));
    }

    public List<TResults> getResultsByEventAndCat(String eventId, String cat) {
        TEventCat tEventCat= eventCatService.getByEventIdAndCat(eventId, cat)
                .stream()
                .filter(eventCat -> eventCat.getCat().equalsIgnoreCase(cat))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Category not found for event: " + eventId + ", category: " + cat));
        int limit = tEventCat.getTop() > 0 ? tEventCat.getTop() : 10; // Default to 10 if not set
        if (limit <= 0) {
            limit = 10; // Ensure a positive limit
        }
        
        return tResultsRepository.findByEventIdAndCatAndRank1catGreaterThanOrderByRank1cat(eventId, cat, 0);
    }
    public List<TResults> getTopResultsByEventAndCat(String eventId, String cat) {
        TEventCat tEventCat= eventCatService.getByEventIdAndCat(eventId, cat)
                .stream()
                .filter(eventCat -> eventCat.getCat().equalsIgnoreCase(cat))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Category not found for event: " + eventId + ", category: " + cat));
        int limit = tEventCat.getTop() > 0 ? tEventCat.getTop() : 10; // Default to 10 if not set
        if (limit <= 0) {
            limit = 10; // Ensure a positive limit
        }
        
        return tResultsRepository.findByEventIdAndCatAndRank1catGreaterThanOrderByRank1cat(eventId, cat, 0, PageRequest.of(0, limit));
    }

    public List<TResults> getParticipantDetails(String eventId,String bib) {
        return tResultsRepository.findByEventIdAndBib(eventId,bib);
    }
    public List<TResults> getResultsByBib(String eventId,String bib) {
        return tResultsRepository.findByEventIdAndBib(eventId,bib);
    }
    
    // Get results for Overall Rank (RANK1TOT)
    public List<TResults> getResultsByEventAndDistanceOrderByRank1tot(String eventId, String distance) {
        return tResultsRepository.findByEventIdAndDistanceOrderByRank1totAscZerosLast(eventId, new BigDecimal(distance));
    }
    
    // Get results for Gender Rank (RANK1MIX)
    public List<TResults> getResultsByEventAndDistanceAndGenderOrderByRank1mix(String eventId, String distance, String gender) {
        return tResultsRepository.findByEventIdAndDistanceAndSexAndRank1mixGreaterThanOrderByRank1mixAsc(eventId, new BigDecimal(distance), gender, 0);
    }
    
    
}
