package dps.hoffmann.springconsumer.utils;

import java.sql.Timestamp;

public class TimestampUtils {

    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Integer diff(Timestamp t1, Timestamp t2) {
        if (t1 == null || t2 == null) {
            return null;
        }

        return (int) Math.abs(t1.getTime() - t2.getTime());
    }

}
