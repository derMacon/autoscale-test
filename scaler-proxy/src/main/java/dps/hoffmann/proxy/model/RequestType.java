package dps.hoffmann.proxy.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * requests that can be delegated to the scaler service
 */
@RequiredArgsConstructor
@Getter
public enum RequestType {

    SMALL_SCALE_REQUEST("tooFewConsumers_lowOverhead", 2);

    private static final String requestJsonFormat = "{" +
            "\"groupLabels\": " +
            "{\"scale\": \"up\", " +
            "\"by\": \"%d\", " +
            "\"service\": \"%s\"}" +
            "}";


    private final String requestName;
    private final int additionalNodeCount;

    public String getRequestBody(String serviceName) {
        return String.format(requestJsonFormat, additionalNodeCount, serviceName);
    }
}
