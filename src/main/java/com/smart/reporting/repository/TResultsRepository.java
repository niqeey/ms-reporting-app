package com.smart.reporting.repository;

import com.smart.reporting.entity.TResults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
    
    // For Gender Rank (RANK1MIX)
    List<TResults> findByEventIdAndDistanceAndSexAndRank1mixGreaterThanOrderByRank1mixAsc(String eventId, BigDecimal distance, String sex, int rank1mix);
    
    @Query("SELECT DISTINCT r.sex FROM TResults r WHERE r.eventId = :eventId AND r.distance = :distance")
    List<String> findDistinctSexByEventIdAndDistance(String eventId, BigDecimal distance);
    
    @Query("SELECT r FROM TResults r WHERE r.eventId = :eventId AND r.distance = :distance AND r.sex = :sex ORDER BY r.rank1mix ASC")
    List<TResults> findByEventIdAndDistanceAndSexOrderByRank1mixAsc(String eventId, BigDecimal distance, String sex);
    
    @Query("SELECT COALESCE(MAX(r.pid), 0) FROM TResults r")
    Integer findMaxPid();
}