-- Stored procedure to generate dummy race data for testing
DELIMITER $$

DROP PROCEDURE IF EXISTS P_DUMMY_REPORT$$

CREATE PROCEDURE P_DUMMY_REPORT(
    IN p_event_id VARCHAR(36),
    IN p_category VARCHAR(10),
    IN p_timegun INT
)
BEGIN
    DECLARE v_cplist TEXT;
    DECLARE v_cp_count INT DEFAULT 0;
    DECLARE v_cp_name VARCHAR(20);
    DECLARE v_remaining TEXT;
    DECLARE v_comma_pos INT;
    
    -- Get cplist from t_event_cat
    SELECT cplist INTO v_cplist
    FROM t_event_cat
    WHERE event_id = p_event_id AND cat = p_category
    LIMIT 1;
    
    -- Count checkpoints
    IF v_cplist IS NOT NULL AND v_cplist != '' THEN
        SET v_cp_count = (LENGTH(v_cplist) - LENGTH(REPLACE(v_cplist, ',', '')) + 1);
    END IF;
    
    -- Update results table for all participants in this event/category
    UPDATE results r
    SET 
        -- Set gun time
        r.timegun = p_timegun,
        
        -- Set start time (randomly 1-60 seconds after gun time)
        r.timestart = p_timegun + FLOOR(1000 + RAND() * 59000),
        
        -- Set checkpoint times (20-25 minutes gap between each)
        r.timecp1 = CASE 
            WHEN v_cp_count >= 1 THEN p_timegun + FLOOR(1200000 + RAND() * 300000) -- 20-25 min
            ELSE 0 
        END,
        
        r.timecp2 = CASE 
            WHEN v_cp_count >= 2 THEN r.timecp1 + FLOOR(1200000 + RAND() * 300000) -- +20-25 min
            ELSE 0 
        END,
        
        r.timecp3 = CASE 
            WHEN v_cp_count >= 3 THEN r.timecp2 + FLOOR(1200000 + RAND() * 300000)
            ELSE 0 
        END,
        
        r.timecp4 = CASE 
            WHEN v_cp_count >= 4 THEN r.timecp3 + FLOOR(1200000 + RAND() * 300000)
            ELSE 0 
        END,
        
        r.timecp5 = CASE 
            WHEN v_cp_count >= 5 THEN r.timecp4 + FLOOR(1200000 + RAND() * 300000)
            ELSE 0 
        END,
        
        r.timecp6 = CASE 
            WHEN v_cp_count >= 6 THEN r.timecp5 + FLOOR(1200000 + RAND() * 300000)
            ELSE 0 
        END,
        
        r.timecp7 = CASE 
            WHEN v_cp_count >= 7 THEN r.timecp6 + FLOOR(1200000 + RAND() * 300000)
            ELSE 0 
        END,
        
        r.timecp8 = CASE 
            WHEN v_cp_count >= 8 THEN r.timecp7 + FLOOR(1200000 + RAND() * 300000)
            ELSE 0 
        END,
        
        r.timecp9 = CASE 
            WHEN v_cp_count >= 9 THEN r.timecp8 + FLOOR(1200000 + RAND() * 300000)
            ELSE 0 
        END,
        
        r.timecp10 = CASE 
            WHEN v_cp_count >= 10 THEN r.timecp9 + FLOOR(1200000 + RAND() * 300000)
            ELSE 0 
        END,
        
        -- Set finish time (20-30 minutes after last checkpoint or start if no checkpoints)
        r.timefinish = CASE
            WHEN v_cp_count >= 10 THEN r.timecp10 + FLOOR(1200000 + RAND() * 600000)
            WHEN v_cp_count >= 9 THEN r.timecp9 + FLOOR(1200000 + RAND() * 600000)
            WHEN v_cp_count >= 8 THEN r.timecp8 + FLOOR(1200000 + RAND() * 600000)
            WHEN v_cp_count >= 7 THEN r.timecp7 + FLOOR(1200000 + RAND() * 600000)
            WHEN v_cp_count >= 6 THEN r.timecp6 + FLOOR(1200000 + RAND() * 600000)
            WHEN v_cp_count >= 5 THEN r.timecp5 + FLOOR(1200000 + RAND() * 600000)
            WHEN v_cp_count >= 4 THEN r.timecp4 + FLOOR(1200000 + RAND() * 600000)
            WHEN v_cp_count >= 3 THEN r.timecp3 + FLOOR(1200000 + RAND() * 600000)
            WHEN v_cp_count >= 2 THEN r.timecp2 + FLOOR(1200000 + RAND() * 600000)
            WHEN v_cp_count >= 1 THEN r.timecp1 + FLOOR(1200000 + RAND() * 600000)
            ELSE r.timestart + FLOOR(1200000 + RAND() * 600000)
        END,
        
        -- Clear status flags
        r.dnf = 0,
        r.dq = 0,
        r.dns = 0,
        r.fs = 0,
        r.nsbf = 0,
        r.nr = 0
        
    WHERE r.eventid = p_event_id AND r.cat = p_category;
    
    -- Log completion
    INSERT INTO servicelog(Query, Query_text, log_time)
    VALUES('P_DUMMY_REPORT', 
           CONCAT('Generated dummy data for eventid=', p_event_id, ', cat=', p_category, ', participants=', ROW_COUNT()),
           NOW());
           
END$$

DELIMITER ;
