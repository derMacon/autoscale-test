package dps.hoffmann.proxy.properties;

import dps.hoffmann.proxy.model.RequestMapper;
import dps.hoffmann.proxy.model.ScalingInstruction;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// todo use properties format
@Component
@Getter
public class DelegationProperties {

//    @Autowired
//    private RequestMapper requestMapper;

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

    public String getRequestBody(ScalingInstruction instruction) {
        return String.format(requestJsonFormat,
                instruction.getScalingDirection().name().toLowerCase(),
                instruction.getServiceName());
    }
}
