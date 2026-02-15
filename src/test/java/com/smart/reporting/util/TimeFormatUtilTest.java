package com.smart.reporting.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeFormatUtilTest {

    @Test
    void testIntToTimeStringWithValidValue() {
        // Test converting 3,665,000 ms (1 hour, 1 minute, 5 seconds)
        String result = TimeFormatUtil.intToTimeString(3665000);
        assertEquals("01:01:05.000", result);
    }

    @Test
    void testIntToTimeStringWithZero() {
        String result = TimeFormatUtil.intToTimeString(0);
        assertNull(result);
    }

    @Test
    void testIntToTimeStringWithLargeValue() {
        // Test converting 86,399,000 ms (23:59:59)
        String result = TimeFormatUtil.intToTimeString(86399000);
        assertEquals("23:59:59.000", result);
    }

    @Test
    void testIntToTimeStringWithSmallValue() {
        // Test converting 125,000 ms (0:02:05)
        String result = TimeFormatUtil.intToTimeString(125000);
        assertEquals("00:02:05.000", result);
    }

    @Test
    void testIntToTimeStringWithExactHour() {
        // Test converting 3,600,000 ms (1 hour exactly)
        String result = TimeFormatUtil.intToTimeString(3600000);
        assertEquals("01:00:00.000", result);
    }

    @Test
    void testIntToTimeStringWithExactMinute() {
        // Test converting 60,000 ms (1 minute exactly)
        String result = TimeFormatUtil.intToTimeString(60000);
        assertEquals("00:01:00.000", result);
    }

    @Test
    void testTimeStringToIntWithValidFormat() {
        // Test converting "01:01:05.000" to 3,665,000 ms
        Integer result = TimeFormatUtil.timeStringToInt("01:01:05.000");
        assertEquals(3665000, result);
    }

    @Test
    void testTimeStringToIntWithZero() {
        Integer result = TimeFormatUtil.timeStringToInt("00:00:00.000");
        assertEquals(0, result);
    }

    @Test
    void testTimeStringToIntWithInvalidFormat() {
        Integer result = TimeFormatUtil.timeStringToInt("invalid");
        assertNull(result);
    }

    @Test
    void testTimeStringToIntWithNull() {
        Integer result = TimeFormatUtil.timeStringToInt(null);
        assertNull(result);
    }

    @Test
    void testTimeStringToIntWithEmptyString() {
        Integer result = TimeFormatUtil.timeStringToInt("");
        assertNull(result);
    }

    @Test
    void testIntToTimeStringRoundTrip() {
        int originalMillis = 5432000; // 1:30:32.000
        String timeString = TimeFormatUtil.intToTimeString(originalMillis);
        Integer convertedBack = TimeFormatUtil.timeStringToInt(timeString);

        assertEquals(originalMillis, convertedBack);
    }
}
