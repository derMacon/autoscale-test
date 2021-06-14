package dps.hoffmann.proxy.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static dps.hoffmann.proxy.model.RequestType.ScalingInterval.*;
import static dps.hoffmann.proxy.model.ScalingDirection.DOWN;
import static dps.hoffmann.proxy.model.ScalingDirection.UP;

/**
 * requests that can be delegated to the scaler service
 */
@RequiredArgsConstructor
@Getter
public enum RequestType {

    SMALL_UP_SCALE_REQUEST("tooFewConsumers_smallOverhead", UP, SMALL_SCALE_CNT),
    MEDIUM_UP_SCALE_REQUEST("tooFewConsumers_mediumOverhead", UP, MEDIUM_SCALE_CNT),
    LARGE_UP_SCALE_REQUEST("tooFewConsumers_largeOverhead", UP, LARGE_SCALE_CNT),
    SMALL_DOWN_SCALE_REQUEST("tooManyConsumers_smallOverhead", DOWN, SMALL_SCALE_CNT),
    MEDIUM_DOWN_SCALE_REQUEST("tooManyConsumers_mediumOverhead", DOWN, MEDIUM_SCALE_CNT),
    LARGE_DOWN_SCALE_REQUEST("tooManyConsumers_largeOverhead", DOWN, LARGE_SCALE_CNT);

    interface ScalingInterval {
        int SMALL_SCALE_CNT = 5;
        int MEDIUM_SCALE_CNT = 10;
        int LARGE_SCALE_CNT = 15;
    }

    private final String requestName;
    private final ScalingDirection scalingDir;
    private final int scalingInterval;

}
