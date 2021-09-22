package de.dps.quarkusconsumer.utils;

import java.sql.Timestamp;

public class PaymentUtils {

    /**
     * Creates a timestamp with the current system time
     * @return new timestamp instance
     */
    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

}
