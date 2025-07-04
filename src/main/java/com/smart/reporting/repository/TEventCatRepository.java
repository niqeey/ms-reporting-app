package com.smart.reporting.repository;

import com.smart.reporting.entity.TEventCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TEventCatRepository extends JpaRepository<TEventCat, Long> {
    List<TEventCat> findByEventId(String eventId);
    List<TEventCat> findByEventIdAndIsResultGreaterThan(String eventId, int isResult);
    List<TEventCat> findByEventIdAndIsResult(String eventId, int isResult);
    List<TEventCat> findByEventIdAndIsResultAndCat(String eventId, int isResult, String b);
    List<TEventCat> findByEventIdAndCat(String eventId, String v);
}