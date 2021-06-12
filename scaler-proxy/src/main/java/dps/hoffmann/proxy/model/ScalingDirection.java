package dps.hoffmann.proxy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ScalingDirection {
    UP("scaling_up"),
    DOWN("scaling_down");

    // todo check if used at all...
    private static final String requestJsonFormat = "{" +
            "\"groupLabels\": " +
            "{\"scale\": \"%s\", " +
            "\"service\": \"%s\"}" +
            "}";


    private final String metricName;

    // todo check if used at all...
    public String getRequestJson(String serviceName) {
        return String.format(requestJsonFormat, this.name().toLowerCase(), serviceName);
    }
}
