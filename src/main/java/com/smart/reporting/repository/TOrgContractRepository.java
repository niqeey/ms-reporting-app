package com.smart.reporting.repository;

import com.smart.reporting.entity.TOrgContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TOrgContractRepository extends JpaRepository<TOrgContract, String> {
    List<TOrgContract> findByOrgId(String orgId);
    List<TOrgContract> findByIsActive(Boolean isActive);
}