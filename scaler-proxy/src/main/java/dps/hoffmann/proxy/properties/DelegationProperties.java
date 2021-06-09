package dps.hoffmann.proxy.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// todo use properties format
@Component
@Getter
public class DelegationProperties {

    @Value("${delegation.hostname}")
    private String hostname;

    @Value("${delegation.port}")
    private String port;

    @Value("${delegation.endpoint}")
    private String endpoint;

    @Value("${delegation.service}")
    private String service;

}
