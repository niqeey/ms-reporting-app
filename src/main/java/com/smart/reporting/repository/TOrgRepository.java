package com.smart.reporting.repository;

import com.smart.reporting.entity.TOrg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TOrgRepository extends JpaRepository<TOrg, String> {
    // You can add custom query methods here if needed
}