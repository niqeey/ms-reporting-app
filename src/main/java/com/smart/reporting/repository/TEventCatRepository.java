package com.smart.reporting.repository;

import com.smart.reporting.entity.TEventCat;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TEventCatRepository extends JpaRepository<TEventCat, Long> {
    List<TEventCat> findByEventId(String eventId);
    List<TEventCat> findByEventIdAndIsResultGreaterThan(String eventId, int isResult);
    List<TEventCat> findByEventIdAndIsResult(String eventId, int isResult);
    List<TEventCat> findByEventIdAndIsResultAndCat(String eventId, int isResult, String b);
    List<TEventCat> findByEventIdAndCat(String eventId, String v);

    @Transactional
    @Modifying
    @Query("UPDATE TEventCat e SET e.timegun = :timegun WHERE e.eventId = :eventId AND e.cat = :cat")
    int updateTimegunByEventIdAndCat(String eventId, String cat, Integer timegun);
    
    @Query("SELECT COALESCE(MAX(e.catId), 0) FROM TEventCat e")
    Integer findMaxCatId();
}