package com.smart.reporting.service;

import com.smart.reporting.entity.TEvent;
import com.smart.reporting.repository.TEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private TEventRepository tEventRepository;

    public List<TEvent> getAllEvents() {
        return tEventRepository.findAll();
    }

    public List<TEvent> getEventsByCountry(String country) {
        return tEventRepository.findByCountry(country);
    }

    public TEvent getEventById(String id) {
        return tEventRepository.findById(id).orElse(null);
    }

    public TEvent saveEvent(TEvent event) {
        return tEventRepository.save(event);
    }

    public void deleteEvent(String id) {
        tEventRepository.deleteById(id);
    }
}