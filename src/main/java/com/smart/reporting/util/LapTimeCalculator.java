package com.smart.reporting.util;

import com.smart.reporting.entity.TResults;
import java.lang.reflect.Method;

/**
 * Utility class for calculating lap times as intervals rather than cumulative values.
 * 
 * Lap time calculations:
 * - Lap 1 = time1 - COALESCE(time0, timestart)
 * - Lap N = timeN - time(N-1)
 */
public class LapTimeCalculator {
    
    /**
     * Calculate the lap time interval for a specific lap number.
     * 
     * @param result The TResults entity containing timing data
     * @param lapNumber The lap number (1-based)
     * @param includeTimeZero Whether to use time0 as the baseline for lap 1
     * @return The lap interval in milliseconds, or null if data is unavailable
     */
    public static Integer calculateLapInterval(TResults result, int lapNumber, boolean includeTimeZero) {
        try {
            if (lapNumber < 1) {
                return null;
            }
            
            // Get the time for this lap
            Method getCurrentLapMethod = TResults.class.getMethod("getTime" + lapNumber);
            Integer currentLapTime = (Integer) getCurrentLapMethod.invoke(result);
            
            if (currentLapTime == null || currentLapTime == 0) {
                return null;
            }
            
            // For lap 1, subtract the starting time
            if (lapNumber == 1) {
                Integer baseTime;
                if (includeTimeZero) {
                    // Try to get time0, fallback to timestart
                    try {
                        Method getTime0Method = TResults.class.getMethod("getTime0");
                        baseTime = (Integer) getTime0Method.invoke(result);
                        if (baseTime == null || baseTime == 0) {
                            baseTime = result.getTimestart();
                        }
                    } catch (Exception e) {
                        baseTime = result.getTimestart();
                    }
                } else {
                    baseTime = result.getTimestart();
                }
                
                if (baseTime == null || baseTime == 0) {
                    return currentLapTime; // Return raw value if no baseline
                }
                
                return currentLapTime - baseTime;
            }
            
            // For lap N > 1, subtract the previous lap time
            Method getPreviousLapMethod = TResults.class.getMethod("getTime" + (lapNumber - 1));
            Integer previousLapTime = (Integer) getPreviousLapMethod.invoke(result);
            
            if (previousLapTime == null || previousLapTime == 0) {
                return null; // Cannot calculate interval without previous lap
            }
            
            return currentLapTime - previousLapTime;
            
        } catch (Exception e) {
            // Method doesn't exist or invocation failed
            return null;
        }
    }
    
    /**
     * Calculate lap interval and format as time string.
     * 
     * @param result The TResults entity containing timing data
     * @param lapNumber The lap number (1-based)
     * @param includeTimeZero Whether to use time0 as the baseline for lap 1
     * @return Formatted time string (HH:mm:ss.SSS) or null if unavailable
     */
    public static String calculateLapIntervalFormatted(TResults result, int lapNumber, boolean includeTimeZero) {
        Integer interval = calculateLapInterval(result, lapNumber, includeTimeZero);
        return interval != null ? TimeFormatUtil.intToTimeString(interval) : null;
    }
}
