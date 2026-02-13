DELIMITER $$

DROP PROCEDURE IF EXISTS `P_ASSIGN_RANK1CAT`$$

CREATE PROCEDURE `P_ASSIGN_RANK1CAT`(
    IN in_eventid CHAR(36),
    IN in_cat VARCHAR(100))
BEGIN
    DECLARE race_mode TEXT;
    DECLARE race_cp TEXT;
    DECLARE order_columns TEXT DEFAULT '';
    DECLARE where_conditions TEXT DEFAULT '';
    DECLARE finishing TEXT DEFAULT '';
    DECLARE i INT DEFAULT 1;
    DECLARE row_count_results INT DEFAULT 0;
    DECLARE row_count_cat INT DEFAULT 0;
    
    -- LAP mode variables
    DECLARE lap_halflap INT DEFAULT 0;
    DECLARE lap_fulllap INT DEFAULT 0;
    DECLARE lap_numberOfLaps INT DEFAULT 0;
    DECLARE lap_totalDistance INT DEFAULT 0;
    DECLARE lap_checkpoints INT DEFAULT 0;

    -- Check existence in t_event_cat
    SELECT COUNT(*) INTO row_count_cat 
    FROM t_event_cat 
    WHERE event_id = in_eventid AND cat = in_cat;

    -- Check existence in t_results
    SELECT COUNT(*) INTO row_count_results
    FROM results 
    WHERE EventId = in_eventid AND cat = in_cat;

    -- reset existence in t_results
    UPDATE results set rank1cat=0
    WHERE EventId = in_eventid AND cat = in_cat;

    -- Only proceed if both have data
    IF row_count_cat > 0 AND row_count_results > 0 THEN

        -- Get race mode and cp list
        SELECT racemode, cplist INTO race_mode, race_cp
        FROM t_event_cat
        WHERE cat = in_cat AND event_id = in_eventid;

        IF race_mode = 'LAP' THEN
            -- Parse cplist: halflap,fulllap,numberOfLaps,total (e.g., '200,400,2,1000')
            SET lap_halflap = CAST(SUBSTRING_INDEX(race_cp, ',', 1) AS UNSIGNED);
            SET lap_fulllap = CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(race_cp, ',', 2), ',', -1) AS UNSIGNED);
            SET lap_numberOfLaps = CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(race_cp, ',', 3), ',', -1) AS UNSIGNED);
            SET lap_totalDistance = CAST(SUBSTRING_INDEX(race_cp, ',', -1) AS UNSIGNED);
            
            -- Determine number of checkpoint columns based on half lap
            IF lap_halflap < 1 THEN
                -- No half lap: Use Time1 to Time{numberOfLaps}
                SET lap_checkpoints = lap_numberOfLaps;
            ELSE
                -- Has half lap: Use Time1 to Time{numberOfLaps - 1}
                SET lap_checkpoints = lap_numberOfLaps - 1;
            END IF;
            
            -- Where conditions for lap mode: only non-DQ and non-NR participants
            SET where_conditions = 'DQ <> 1 AND NR <> 1';
            
            -- For LAP mode: rank by lap DESC (most laps first), then by highest lap time ASC (fastest first)
            -- lap column contains the highest lap number the participant completed
            SET finishing = 'lap DESC, timeFinish ASC';
            
        ELSE
            -- Original logic for non-LAP modes (NET/OFFICIAL)
            -- race_cp is comma-separated list, e.g., 'TimeCP1,TimeCP2,TimeFinish'
            SET @cp_list = race_cp;
            SET order_columns = '';
            SET where_conditions = 'DQ <> 1 and NR <> 1 AND TimeStart>0 and Timefinish>0 ';

            WHILE LOCATE(',', @cp_list) > 0 DO
                SET @cp = TRIM(SUBSTRING_INDEX(@cp_list, ',', 1));

                SET order_columns = CONCAT(order_columns, IF(order_columns != '', ',', ''), @cp);
                SET where_conditions = CONCAT(where_conditions, IF(where_conditions != '', ' AND ', ''), @cp, ' > 0');

                SET @cp_list = SUBSTRING(@cp_list, LOCATE(',', @cp_list) + 1);
            END WHILE;

            -- Add last cp
            SET @cp = TRIM(@cp_list);
            SET order_columns = CONCAT(order_columns, IF(order_columns != '', ',', ''), @cp);
            SET where_conditions = CONCAT(where_conditions, IF(where_conditions != '', ' AND ', ''), @cp, ' > 0');
            
            -- Set finishing time based on race mode
            -- For NET mode: rank by lap count (DESC) first, then by net time (ASC)
            IF race_mode = 'NET' THEN
                SET finishing = 'Lap DESC, timeFinish-timestart';
            ELSEIF race_mode = 'OFFICIAL' THEN
                SET finishing = 'timeFinish';
            END IF;
        END IF;

        -- Build final dynamic SQL
        SET @sql_query = CONCAT(
            'WITH ranked AS (',
            ' SELECT pid, ROW_NUMBER() OVER (ORDER BY ', finishing, ') AS new_rank',
            ' FROM results ',
            ' WHERE EventId = "', in_eventid, '" AND cat = "', in_cat, '"',
            IF(where_conditions != '', CONCAT(' AND ', where_conditions), ''),
            ') ',
            'UPDATE results r ',
            'JOIN ranked rk ON r.pid = rk.pid ',
            'SET r.rank1cat = rk.new_rank;'
        );

        -- Log
        INSERT INTO servicelog(Query, Query_text, log_time)
        VALUES('P_ASSIGN_RANK1CAT', @sql_query, NOW());

        -- Execute
        PREPARE stmt FROM @sql_query;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;

    ELSE
        -- log if nothing to update
        INSERT INTO servicelog(Query, Query_text, log_time)
        VALUES('P_ASSIGN_RANK1CAT', CONCAT('Skipped: No data found in t_event_cat or results for eventid=', in_eventid, ' and cat=', in_cat), NOW());
    END IF;
END$$

DELIMITER ;
