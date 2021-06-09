package dps.hoffmann.proxy.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * requests that can be delegated to the scaler service
 */
@RequiredArgsConstructor
@Getter
public enum RequestType {
    SMALL_SCALE_REQUEST("tooFewConsumers_lowOverhead");

    private final String requestContent;
}
