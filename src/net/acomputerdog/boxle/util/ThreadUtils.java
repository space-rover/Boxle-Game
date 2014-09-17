package net.acomputerdog.boxle.util;

/**
 * Threading-related utilities
 */
public class ThreadUtils {

    /**
     * Syncs a method to a minimum duration.
     *
     * @param methodStartTime   The value of System.currentTimeMillis() when the method was called.
     * @param minDurationMillis The minimum number of milliseconds for the method to take.
     */
    public static void sync(long methodStartTime, long minDurationMillis) {
        sync(methodStartTime, minDurationMillis, false);
    }

    /**
     * Syncs a method to a minimum duration.
     *
     * @param methodStartTime   The value of System.currentTimeMillis() when the method was called.
     * @param minDurationMillis The minimum number of milliseconds for the method to take.
     * @param force             If true, InterruptedExceptions will be suppressed
     */
    public static void sync(long methodStartTime, long minDurationMillis, boolean force) {
        long currTime = System.currentTimeMillis();
        long endTime = minDurationMillis + methodStartTime;
        if (endTime > currTime) {
            do {
                try {
                    Thread.sleep(endTime - currTime);
                } catch (InterruptedException ignored) {
                }
            } while (endTime > currTime && force);
        }
    }
}
