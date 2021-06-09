package dps.hoffmann.proxy.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// todo use properties format
@Component
@Getter
public class DelegationProperties {

    private static final String requestJsonFormat = "{" +
            "\"groupLabels\": " +
            "{\"scale\": \"up\", " +
            "\"service\": \"%s\"}" +
            "}";

    @Value("${delegation.hostname}")
    private String hostname;

    @Value("${delegation.port}")
    private String port;

    @Value("${delegation.lowPrioScaleEndpoint}")
    private String lowPrioScaleEndpoint;

    @Value("${delegation.service}")
    private String service;

    public String getRequestJson() {
        return String.format(requestJsonFormat, service);
    }
}
