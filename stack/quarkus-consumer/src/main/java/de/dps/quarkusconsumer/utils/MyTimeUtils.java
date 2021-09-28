package de.dps.quarkusconsumer.utils;

import java.sql.Timestamp;

public class MyTimeUtils {

    /**
     * Creates a timestamp with the current system time
     * @return new timestamp instance
     */
    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static void myWait(int millis) {
        // todo sleep needs to happen after value was put in acknowledgement queue
        // not before ...
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
