package net.acomputerdog.boxle.util;

import net.acomputerdog.core.logger.CLogger;

/**
 * Threading-related utilities
 */
public class ThreadUtils {

    private static CLogger logger = new CLogger("ThreadUtils", true, true);

    /**
     * Syncs a method to a minimum duration.
     *
     * @param methodStartTime   The value of System.currentTimeMillis() when the method was called.
     * @param duration The minimum number of milliseconds for the method to take.
     */
    public static void sync(long methodStartTime, long duration) {
        sync(methodStartTime, duration, false);
    }

    /**
     * Syncs a method to a minimum duration.
     *
     * @param methodStartTime   The value of System.currentTimeMillis() when the method was called.
     * @param duration The minimum number of milliseconds for the method to take.
     * @param force             If true, InterruptedExceptions will be suppressed
     */
    public static void sync(long methodStartTime, long duration, boolean force) {
        if (duration <= 0) {
            return;
        }
        long currTime = System.currentTimeMillis(); //only calculate current time once, otherwise math can be thrown off
        long endTime = duration + methodStartTime; //time that the method wants to end after
        if (endTime > currTime) { //if the method completed too fast, then sleep.
            sleep(endTime - currTime);
        }
    }

    /**
     * Makes the current thread sleep for the specified duration.  InterruptedExceptions will be suppressed.
     *
     * @param duration The duration to sleep for.
     */
    public static void sleep(long duration) {
        try {
            sleep(duration, true);
        } catch (InterruptedException e) {
            logger.logError("Caught InterruptedException, this should be impossible!", e);
        }
    }

    /**
     * Makes the current thread sleep for the specified duration. InterruptedExceptions will be suppressed if force is true.
     *
     * @param duration The duration to sleep for.
     * @param force    If true, InterruptedExceptions will be suppressed
     * @throws InterruptedException Throws InterruptedException if force is false, and the thread is interrupted.
     */
    public static void sleep(long duration, boolean force) throws InterruptedException {
        if (duration <= 0) {
            return;
        }
        long endTime = System.currentTimeMillis() + duration; //time at which to wake up
        do {
            try {
                Thread.sleep(endTime - System.currentTimeMillis()); //sleep any time remaining from sleep duration
            } catch (InterruptedException e) {
                if (!force) { //if force is true, suppress InterruptedException
                    throw e;
                }
            }
        }
        while (System.currentTimeMillis() < endTime && force);  // if time is left and force is true, sleep for remaining time.
    }
}
