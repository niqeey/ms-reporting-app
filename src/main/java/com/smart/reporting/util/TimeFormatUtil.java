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

    /**
     * Converts a time string in HH:mm:ss.SSS or HH:mm:ss format to milliseconds (integer).
     * Returns null if the string is null, empty, or cannot be parsed.
     */
    public static Integer timeStringToInt(String timeString) {
        if (timeString == null || timeString.trim().isEmpty() || timeString.equals("-")) {
            return null;
        }
        
        try {
            String[] parts = timeString.split(":");
            if (parts.length < 2) {
                return null;
            }
            
            int hours = Integer.parseInt(parts[0].trim());
            int minutes = Integer.parseInt(parts[1].trim());
            
            int seconds = 0;
            int millis = 0;
            
            if (parts.length >= 3) {
                String[] secondsParts = parts[2].split("\\.");
                seconds = Integer.parseInt(secondsParts[0].trim());
                if (secondsParts.length > 1) {
                    millis = Integer.parseInt(secondsParts[1].trim());
                }
            }
            
            return hours * 3600000 + minutes * 60000 + seconds * 1000 + millis;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
