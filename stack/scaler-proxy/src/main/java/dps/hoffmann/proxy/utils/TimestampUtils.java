package dps.hoffmann.proxy.utils;

import dps.hoffmann.proxy.model.ScalingInstruction;

public class TimestampUtils {

    public static int getDuration(ScalingInstruction instruction) {
        return (int) (instruction.getScaleAcknowledgementTimestamp().getTime()
                - instruction.getReceivedRequestTimestamp().getTime());
    }


}
