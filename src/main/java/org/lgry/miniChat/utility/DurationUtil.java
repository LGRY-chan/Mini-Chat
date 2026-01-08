package org.lgry.miniChat.utility;

import java.time.Duration;

public class DurationUtil {
    public static String simplizeDuration(Duration duration) {
        if (duration.isNegative() || duration.isZero()) {
            return "Just now";
        }

        long days = duration.toDays();
        if (days > 0) {
            return days + " days";
        }

        long hours = duration.toHours();
        if (hours > 0) {
            return hours + " hours";
        }

        long minutes = duration.toMinutes();
        if (minutes > 0) {
            return minutes + " minutes";
        }

        long seconds = duration.getSeconds();
        if (seconds > 0) {
            return seconds + " seconds";
        }

        return "Just now";
    }
}
