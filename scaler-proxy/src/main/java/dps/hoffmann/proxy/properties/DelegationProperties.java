package dps.hoffmann.proxy.properties;

import dps.hoffmann.proxy.model.ScalingDirection;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// todo use properties format
@Component
@Getter
public class DelegationProperties {

    private static final String requestJsonFormat = "{" +
            "\"groupLabels\": " +
            "{\"scale\": \"%s\", " +
            "\"service\": \"%s\"}" +
            "}";

    @Value("${delegation.hostname}")
    private String hostname;

    @Value("${delegation.port}")
    private String port;

    @Value("${delegation.endpoint}")
    private String endpoint;

    @Value("${delegation.service}")
    private String service;

    public String getRequestBody(ScalingDirection scalingDirection) {
        return String.format(requestJsonFormat,
                scalingDirection.name().toLowerCase(),
                service);
    }
}
