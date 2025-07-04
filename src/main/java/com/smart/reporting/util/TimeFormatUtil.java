package com.smart.reporting.util;

public class TimeFormatUtil {
    /**
     * Converts an integer time (milliseconds) to a string in HH:mm:ss.SSS format.
     * Follows the logic:
     *   HH = n / 3600000
     *   mm = (n % 3600000) / 60000
     *   ss = (n % 60000) / 1000
     *   SSS = n % 1000
     */
    public static String intToTimeString(Integer n) {
        if (n == null || n <= 0) return null;
        int hours = n / 3600000;
        int minutes = (n % 3600000) / 60000;
        int seconds = (n % 60000) / 1000;
        int millis = n % 1000;
        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
    }
}
