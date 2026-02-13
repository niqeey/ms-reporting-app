package com.smart.reporting.repository;

import com.smart.reporting.entity.TResultsArchive;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TResultsArchiveRepository extends JpaRepository<TResultsArchive, Integer> {
    List<TResultsArchive> findByEventIdAndCat(String eventId, String cat, Pageable pageable);
    List<TResultsArchive> findByEventIdAndCatAndRank1catGreaterThanOrderByRank1cat(String eventId, String cat, int rank1cat);
    List<TResultsArchive> findByEventIdAndCatAndRank1catGreaterThanOrderByRank1cat(String eventId, String cat, int rank1cat, Pageable pageable);
    List<TResultsArchive> findByEventIdAndBib(String eventId, String bib);
    Optional<TResultsArchive> findByEventIdAndCatAndBib(String eventId, String cat, String bib);
    
    // For Overall Rank (RANK1TOT)
    List<TResultsArchive> findByEventIdAndDistanceAndRank1totGreaterThanOrderByRank1totAsc(String eventId, BigDecimal distance, int rank1tot);
    
    // For Gender Rank (RANK1MIX)
    List<TResultsArchive> findByEventIdAndDistanceAndSexAndRank1mixGreaterThanOrderByRank1mixAsc(String eventId, BigDecimal distance, String sex, int rank1mix);
    
    @Query("SELECT DISTINCT r.sex FROM TResultsArchive r WHERE r.eventId = :eventId AND r.distance = :distance")
    List<String> findDistinctSexByEventIdAndDistance(String eventId, BigDecimal distance);
    
    @Query("SELECT r FROM TResultsArchive r WHERE r.eventId = :eventId AND r.distance = :distance AND r.sex = :sex ORDER BY r.rank1mix ASC")
    List<TResultsArchive> findByEventIdAndDistanceAndSexOrderByRank1mixAsc(String eventId, BigDecimal distance, String sex);
    
    @Query("SELECT COALESCE(MAX(r.pid), 0) FROM TResultsArchive r")
    Integer findMaxPid();

    @Query("SELECT r FROM TResultsArchive r WHERE r.eventId = :eventId AND r.cat = :cat AND r.timefinish > 0 ORDER BY r.lap DESC, (r.timefinish - r.timestart) ASC")
    List<TResultsArchive> findByEventIdAndCatWithLapModeOrderByLapAndNetTime(String eventId, String cat);
}
