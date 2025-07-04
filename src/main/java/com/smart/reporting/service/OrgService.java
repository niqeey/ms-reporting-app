package com.smart.reporting.service;

import com.smart.reporting.entity.TOrg;
import com.smart.reporting.repository.TOrgRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrgService {
    private static final Logger logger = LoggerFactory.getLogger(OrgService.class);

    @Autowired
    private TOrgRepository tOrgRepository;

    public Optional<TOrg> getOrgById(String id) {
        return tOrgRepository.findById(id);
    }
}
