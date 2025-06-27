package com.smart.reporting.repository;

import com.smart.reporting.entity.TOrgUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TOrgUserRepository extends JpaRepository<TOrgUser, String> {
    Optional<TOrgUser> findByUsername(String username);
    Optional<TOrgUser> findByUsernameAndPassword(String username, String password);
    List<TOrgUser> findByOrgId(String orgId);
}