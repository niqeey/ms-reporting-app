package com.smart.reporting.service;

import com.smart.reporting.model.RunnerResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<RunnerResult> fetchResults(String year, String type) {
        return jdbcTemplate.execute((ConnectionCallback<List<RunnerResult>>) con -> {
            List<RunnerResult> list = new ArrayList<>();
            try (CallableStatement cs = con.prepareCall("{CALL generate_marathon_result(?, ?)}")) {
                cs.setString(1, year);
                cs.setString(2, type);
                boolean hasResult = cs.execute();
                if (hasResult) {
                    try (ResultSet rs = cs.getResultSet()) {
                        while (rs.next()) {
                            list.add(new RunnerResult(
                                    rs.getInt("rank"),
                                    rs.getString("name"),
                                    rs.getString("time")));
                        }
                    }
                }
            }
            return list;
        });
    }

    public List<Map<String, Object>> getMarathonResults() {
        return jdbcTemplate.queryForList("CALL get_marathon_results()");
    }
}
