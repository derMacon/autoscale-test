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

    @Value("${delegation.endpoint}")
    private String endpoint;

    @Value("${delegation.service}")
    private String service;

    public String getRequestBody() {
        return String.format(requestJsonFormat, service);
    }
}
