package dps.hoffmann.proxy.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(ignoreUnknownFields = false, prefix = "scaling")
public class ScalingProperties {
    private int qb0;
    private int qb1;
    private int qb2;
    private int cb0;
    private int cb1;
    private int cb2;
    private int cb3;

    public int getHighestContainerBound() {
        return cb3;
    }
}
