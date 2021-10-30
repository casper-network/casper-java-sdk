package com.casper.sdk.service.serialization.util;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TtlUtils {

    /**
     * Converts a duration string to a long ms value
     *
     * @param strTtl the string to convert
     * @return the ms value of the duration
     */
    public static long getTtlLong(final String strTtl) {
        if (strTtl != null) {

            final Pattern p = Pattern.compile("\\p{Alpha}");
            final Matcher m = p.matcher(strTtl);
            final int unitIndex;
            if (m.find()) {
                unitIndex = m.start();
            } else {
                unitIndex = strTtl.length() - 1;
            }

            final String unit = strTtl.substring(unitIndex);

            final long value = Long.parseLong(strTtl.substring(0, unitIndex));

            return value * getMultiplier(unit);
        }
        return 0L;
    }

    /**
     * The Time to live is defined as the amount of time for which deploy is considered valid. The ttl serializes in the
     * same manner as the timestamp.
     *
     * @param ttl the time to live in ms
     * @return the ttl as a duration string
     */
    public static String toTtlStr(final long ttl) {
        return Duration.ofMillis(ttl)
                .toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

    private static long getMultiplier(final String unit) {
        switch (unit) {
            case "d":
                return 24 * 60L * 60L * 1000L;
            case "h":
                return 60L * 60L * 1000L;
            case "m":
                return 60L * 1000L;
            case "s":
                return 1000L;
            default:
                return 1L;
        }
    }
}
