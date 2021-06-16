package dps.hoffmann.proxy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ScalingDirection {
    UP("scaling_up"),
    DOWN("scaling_down");

    private final String metricName;
}
