package com.smart.reporting.repository;

import com.smart.reporting.entity.TOrg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TOrgRepository extends JpaRepository<TOrg, String> {
    List<TOrg> findByIsActive(Boolean isActive);

    // Use Optional<TOrg> for findById, or just use the inherited JpaRepository method
    Optional<TOrg> findById(String id);
}