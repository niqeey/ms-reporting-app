package com.smart.reporting.repository;

import com.smart.reporting.entity.TResults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TResultsRepository extends JpaRepository<TResults, Integer> {
    List<TResults> findByEventIdAndCat(String eventId, String cat, Pageable pageable);
    List<TResults> findByEventIdAndCatAndRank1catGreaterThanOrderByRank1cat(String eventId, String cat, int rank1cat);
    List<TResults> findByEventIdAndCatAndRank1catGreaterThanOrderByRank1cat(String eventId, String cat, int rank1cat, Pageable pageable);
    List<TResults> findByEventIdAndBib(String eventId,String bib);
    Optional<TResults> findByEventIdAndCatAndBib(String eventId, String cat, String bib);
    
    @Query("SELECT COALESCE(MAX(r.pid), 0) FROM TResults r")
    Integer findMaxPid();
}