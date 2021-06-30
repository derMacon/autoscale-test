package dps.hoffmann.proxy.utils;

import dps.hoffmann.proxy.model.ScalingInstruction;

import java.sql.Time;
import java.sql.Timestamp;

public class TimestampUtils {

    public static int getDuration(ScalingInstruction instruction) {
        return (int) (instruction.getScaleAcknowledgementTimestamp().getTime()
                - instruction.getReceivedRequestTimestamp().getTime());
    }

    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

}
