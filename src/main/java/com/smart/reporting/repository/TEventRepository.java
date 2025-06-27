package com.smart.reporting.repository;

import com.smart.reporting.entity.TEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TEventRepository extends JpaRepository<TEvent, String> {
    List<TEvent> findByCountry(String country);
    // Add more query methods as needed
}