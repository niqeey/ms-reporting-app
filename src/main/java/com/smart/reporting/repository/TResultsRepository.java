package com.smart.reporting.repository;

import com.smart.reporting.entity.TResults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TResultsRepository extends JpaRepository<TResults, Integer> {
    List<TResults> findByEventIdAndCat(String eventId, String cat, Pageable pageable);
    List<TResults> findByEventIdAndCatAndRank1catGreaterThanOrderByRank1cat(String eventId, String cat, int rank1cat);
    List<TResults> findByEventIdAndCatAndRank1catGreaterThanOrderByRank1cat(String eventId, String cat, int rank1cat, Pageable pageable);
    List<TResults> findByEventIdAndBib(String eventId,String bib);
    Optional<TResults> findByEventIdAndCatAndBib(String eventId, String cat, String bib);
    
    // For Overall Rank (RANK1TOT)
    List<TResults> findByEventIdAndDistanceAndRank1totGreaterThanOrderByRank1totAsc(String eventId, BigDecimal distance, int rank1tot);
    
    // For Overall Rank with 0 values last
    @Query("SELECT r FROM TResults r WHERE r.eventId = :eventId AND r.distance = :distance ORDER BY CASE WHEN r.rank1tot = 0 THEN 1 ELSE 0 END, r.rank1tot ASC")
    List<TResults> findByEventIdAndDistanceOrderByRank1totAscZerosLast(String eventId, BigDecimal distance);
    
    // For Gender Rank (RANK1MIX)
    List<TResults> findByEventIdAndDistanceAndSexAndRank1mixGreaterThanOrderByRank1mixAsc(String eventId, BigDecimal distance, String sex, int rank1mix);
    
    @Query("SELECT DISTINCT r.sex FROM TResults r WHERE r.eventId = :eventId AND r.distance = :distance")
    List<String> findDistinctSexByEventIdAndDistance(String eventId, BigDecimal distance);
    
    @Query("SELECT r FROM TResults r WHERE r.eventId = :eventId AND r.distance = :distance AND r.sex = :sex ORDER BY r.rank1mix ASC")
    List<TResults> findByEventIdAndDistanceAndSexOrderByRank1mixAsc(String eventId, BigDecimal distance, String sex);
    
    @Query("SELECT COALESCE(MAX(r.pid), 0) FROM TResults r")
    Integer findMaxPid();

    @Query("SELECT r FROM TResults r WHERE r.eventId = :eventId AND r.cat = :cat AND r.time1 > 0 ORDER BY r.lap DESC, (r.timefinish - r.timestart) ASC")
    List<TResults> findByEventIdAndCatWithLapModeOrderByLapAndNetTime(String eventId, String cat);

    @Transactional
    @Modifying
    @Query("UPDATE TResults r SET r.timegun = :timegun WHERE r.eventId = :eventId AND r.cat = :cat")
    int updateTimegunByEventIdAndCat(String eventId, String cat, Integer timegun);
}