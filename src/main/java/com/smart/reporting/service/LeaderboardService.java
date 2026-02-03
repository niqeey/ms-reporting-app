package com.smart.reporting.service;

import com.smart.reporting.dto.LeaderboardResponse;
import com.smart.reporting.util.TimeFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<LeaderboardResponse> getLeaderboardData(String eventId, String category) {
        String sql = "CALL P_LEADERBOARD_REPORT(?, ?)";
        
        return jdbcTemplate.query(sql, 
            new Object[]{eventId, category},
            (rs, rowNum) -> {
                LeaderboardResponse response = new LeaderboardResponse();
                response.setBib(rs.getString("bib"));
                response.setName(rs.getString("name"));
                response.setCategory(rs.getString("category"));
                response.setRankCat(rs.getInt("rankCat"));
                response.setRankMix(rs.getInt("rankMix"));
                response.setRankTot(rs.getInt("rankTot"));
                
                // Convert times from integer milliseconds to HH:MM:SS format
                int timeStart = rs.getInt("timestart");
                int timeFinish = rs.getInt("timefinish");
                int timeGun = rs.getInt("timegun");
                int netTime = rs.getInt("netTime");
                int officialTime = rs.getInt("officialTime");
                
                response.setTimeStart(TimeFormatUtil.intToTimeString(timeStart));
                response.setTimeFinish(TimeFormatUtil.intToTimeString(timeFinish));
                response.setTimeGun(TimeFormatUtil.intToTimeString(timeGun));
                response.setNetTime(TimeFormatUtil.intToTimeString(netTime));
                response.setOfficialTime(TimeFormatUtil.intToTimeString(officialTime));
                
                // Convert checkpoint times (absolute times from database)
                response.setTimeCP1(TimeFormatUtil.intToTimeString(rs.getInt("timecp1") > 0 ? rs.getInt("timecp1") - timeGun : 0));
                response.setTimeCP2(TimeFormatUtil.intToTimeString(rs.getInt("timecp2") > 0 ? rs.getInt("timecp2") - timeGun : 0));
                response.setTimeCP3(TimeFormatUtil.intToTimeString(rs.getInt("timecp3") > 0 ? rs.getInt("timecp3") - timeGun : 0));
                response.setTimeCP4(TimeFormatUtil.intToTimeString(rs.getInt("timecp4") > 0 ? rs.getInt("timecp4") - timeGun : 0));
                response.setTimeCP5(TimeFormatUtil.intToTimeString(rs.getInt("timecp5") > 0 ? rs.getInt("timecp5") - timeGun : 0));
                response.setTimeCP6(TimeFormatUtil.intToTimeString(rs.getInt("timecp6") > 0 ? rs.getInt("timecp6") - timeGun : 0));
                response.setTimeCP7(TimeFormatUtil.intToTimeString(rs.getInt("timecp7") > 0 ? rs.getInt("timecp7") - timeGun : 0));
                response.setTimeCP8(TimeFormatUtil.intToTimeString(rs.getInt("timecp8") > 0 ? rs.getInt("timecp8") - timeGun : 0));
                response.setTimeCP9(TimeFormatUtil.intToTimeString(rs.getInt("timecp9") > 0 ? rs.getInt("timecp9") - timeGun : 0));
                response.setTimeCP10(TimeFormatUtil.intToTimeString(rs.getInt("timecp10") > 0 ? rs.getInt("timecp10") - timeGun : 0));
                
                response.setCplist(rs.getString("cplist"));
                
                return response;
            }
        ).stream()
         .filter(r -> r.getRankCat() > 0) // Filter out records without a rank
         .sorted((r1, r2) -> Integer.compare(r1.getRankCat(), r2.getRankCat())) // Sort by rank (ascending)
         .collect(Collectors.toList());
    }
}
