package com.smart.reporting.service;

import com.smart.reporting.entity.TEventCat;
import com.smart.reporting.repository.TEventCatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventCatService {

    @Autowired
    private TEventCatRepository tEventCatRepository;

    public List<TEventCat> getByEventId(String eventId) {
        return tEventCatRepository.findByEventId(eventId);
    }

    public List<TEventCat> getByEventIdAndIsResultGreaterThan(String eventId, int isResult) {
        return tEventCatRepository.findByEventIdAndIsResultGreaterThan(eventId, isResult);
    }
    
    public List<TEventCat> getByEventIdAndCat(String eventId, String cat) {
        return tEventCatRepository.findByEventIdAndCat(eventId, cat);
    }
        
}