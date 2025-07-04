package com.smart.reporting.repository;

import com.smart.reporting.entity.TResults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TResultsRepository extends JpaRepository<TResults, Integer> {
    List<TResults> findByEventIdAndCat(String eventId, String cat, Pageable pageable);
    List<TResults> findByEventIdAndCatAndRank1catGreaterThanOrderByRank1cat(String eventId, String cat, int rank1cat);
    List<TResults> findByEventIdAndCatAndRank1catGreaterThanOrderByRank1cat(String eventId, String cat, int rank1cat, Pageable pageable);
}