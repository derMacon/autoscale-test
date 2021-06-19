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
    private int ql0;
    private int ql1;
    private int ql2;
    private int cl0;
    private int cl1;
    private int cl2;
    private int cl3;
}
