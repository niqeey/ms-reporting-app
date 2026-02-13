-- Stored procedure for public leaderboard report by event and category
DELIMITER $$

DROP PROCEDURE IF EXISTS P_LEADERBOARD_REPORT$$

CREATE PROCEDURE P_LEADERBOARD_REPORT(
    IN p_event_id VARCHAR(36),
    IN p_category VARCHAR(10)
)
BEGIN
    SELECT 
        r.bib,
        r.name,
        r.category,
        r.rank1cat AS rankCat,
        r.rank1mix AS rankMix,
        r.rank1tot AS rankTot,
        r.timestart,
        r.timefinish,
        r.timegun,
        (r.timefinish - r.timestart) AS netTime,
        (r.timefinish - r.timegun) AS officialTime,
        r.timecp1,
        r.timecp2,
        r.timecp3,
        r.timecp4,
        r.timecp5,
        r.timecp6,
        r.timecp7,
        r.timecp8,
        r.timecp9,
        r.timecp10,
        r.sex,
        r.lap,
        ec.cplist
    FROM t_results r
    LEFT JOIN t_event_cat ec ON r.eventid = ec.event_id AND r.cat = ec.cat
    WHERE r.eventid = p_event_id
        AND r.cat = p_category
        AND r.timefinish IS NOT NULL
        AND r.timefinish > 0
        AND r.dnf = 0
        AND r.dq = 0
    ORDER BY r.rank1cat ASC;
END$$

DELIMITER ;
