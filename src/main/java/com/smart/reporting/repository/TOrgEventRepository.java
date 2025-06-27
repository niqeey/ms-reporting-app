package com.smart.reporting.repository;

import com.smart.reporting.entity.TOrgEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TOrgEventRepository extends JpaRepository<TOrgEvent, String> {
    List<TOrgEvent> findByOrgId(String orgId);
    List<TOrgEvent> findByEventId(String eventId);
}