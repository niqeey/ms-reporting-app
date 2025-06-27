package com.smart.reporting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.smart.reporting.dto.EventResponse;
import com.smart.reporting.entity.TEvent;
import com.smart.reporting.entity.TOrgEvent;
import com.smart.reporting.model.RunnerResult;
import com.smart.reporting.repository.TEventRepository;
import com.smart.reporting.repository.TOrgEventRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class OrgEventService {

    private static final Logger logger = LoggerFactory.getLogger(OrgEventService.class);


    @Autowired
    private JdbcTemplate jdbcTemplate;

     @Autowired
    private TEventRepository tEventRepository;

    @Autowired
    private TOrgEventRepository tOrgEventRepository;

    public EventResponse createOrgEvent(EventResponse req, String orgId) {
        // 1. Create new TEvent
        TEvent event = new TEvent();
        event.setId(java.util.UUID.randomUUID().toString());
        event.setName(req.getName());
        event.setEventDt(req.getEventDt());
        event.setLocation(req.getLocation());
        event.setCountry(req.getCountry());
        TEvent savedEvent = tEventRepository.save(event);

        // 2. Link to TOrgEvent
        TOrgEvent orgEvent = new TOrgEvent();
        orgEvent.setId(java.util.UUID.randomUUID().toString());
        orgEvent.setOrgId(orgId);
        orgEvent.setEventId(savedEvent.getId());
        tOrgEventRepository.save(orgEvent);

        // 3. Prepare response
        EventResponse resp = new EventResponse();
        resp.setId(savedEvent.getId());
        resp.setName(savedEvent.getName());
        resp.setEventDt(savedEvent.getEventDt());
        resp.setLocation(savedEvent.getLocation());
        resp.setCountry(savedEvent.getCountry());
        return resp;
    }

    public List<TEvent> getEventsByOrgId(String orgId) {
    logger.info("Fetching events for Org ID: {}", orgId);
    String sql = """
        SELECT 
            e.id, 
            e.name, 
            e.event_dt AS eventDt, 
            e.location, 
            e.country
        FROM t_event e
        JOIN t_org_event oe ON e.id = oe.event_id
        JOIN t_org o ON o.id = oe.org_id
        WHERE o.id = ?
        order by e.event_dt
        """;
    List<TEvent> events = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TEvent.class), orgId);
    logger.info("sql: {}", sql);
    logger.info("Found {} events for Org ID: {}", events.size(), orgId);
    return events;
}
    

    

    public List<TEvent> getEventsByOrgIdAndYear(String orgId, String year) {
        logger.info("Fetching events for Org ID: {} and Year: {}", orgId, year);
        String sql = """
            SELECT e.*
            FROM t_event e
            JOIN t_org_event oe ON e.id = oe.event_id
            JOIN t_org o ON o.id = oe.org_id
            WHERE o.id = ? AND e.year = ?
            """;
        List<TEvent> events = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TEvent.class), orgId, year);
        logger.debug("Found {} events for Org ID: {} and Year: {}", events.size(), orgId, year);
        return events;
    }

    public List<TEvent> getAllEvents() {
        logger.info("Fetching all events");
        String sql = "SELECT * FROM t_event";
        List<TEvent> events = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TEvent.class));
        logger.debug("Found {} events in total", events.size());
        return events;
    }

    public TEvent getEventById(String eventId) {
        logger.info("Fetching event by ID: {}", eventId);
        String sql = "SELECT * FROM t_event WHERE id = ?";
        TEvent event = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(TEvent.class), eventId);
        logger.debug("Fetched event: {}", event);
        return event;
    }

    public List<RunnerResult> getOrgEventResults(String eventId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrgEventResults'");
    }

    public void deleteOrgEvent(String eventId) {
        // Delete link in TOrgEvent first (if exists)
        tOrgEventRepository.deleteAll(tOrgEventRepository.findByEventId(eventId));
        // Then delete the event itself
        tEventRepository.deleteById(eventId);
    }
}