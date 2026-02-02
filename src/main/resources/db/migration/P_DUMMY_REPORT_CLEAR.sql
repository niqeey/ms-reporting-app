-- Stored procedure to clear all race data (reset to initial state)
DELIMITER $$

DROP PROCEDURE IF EXISTS P_DUMMY_REPORT_CLEAR$$

CREATE PROCEDURE P_DUMMY_REPORT_CLEAR(
    IN p_event_id VARCHAR(36),
    IN p_category VARCHAR(10)
)
BEGIN
    -- Reset all race-related fields to 0 or NULL
    UPDATE results r
    SET 
        -- Reset times
        r.timegun = 0,
        r.timestart = 0,
        r.timefinish = 0,
        r.timecp1 = 0,
        r.timecp2 = 0,
        r.timecp3 = 0,
        r.timecp4 = 0,
        r.timecp5 = 0,
        r.timecp6 = 0,
        r.timecp7 = 0,
        r.timecp8 = 0,
        r.timecp9 = 0,
        r.timecp10 = 0,
        
        -- Reset ranks
        r.rank1cat = 0,
        r.rank1mix = 0,
        r.rank1tot = 0,
        
        -- Reset status flags
        r.nr = 0,
        r.fs = 0,
        r.dnf = 0,
        r.dns = 0,
        r.dq = 0,
        r.nsbf = 0
        
    WHERE r.eventid = p_event_id AND r.cat = p_category;
    
    -- Log completion
    INSERT INTO servicelog(Query, Query_text, log_time)
    VALUES('P_DUMMY_REPORT_CLEAR', 
           CONCAT('Cleared all race data for eventid=', p_event_id, ', cat=', p_category, ', participants=', ROW_COUNT()),
           NOW());
           
END$$

DELIMITER ;
