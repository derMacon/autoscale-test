package dps.hoffmann.proxy.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static dps.hoffmann.proxy.model.RequestType.*;

@RequiredArgsConstructor
@Getter
public enum NodeMetric {
        SCALINGTIME_NODE_UP_SMALL(SMALL_UP_SCALE_REQUEST),
        SCALINGTIME_NODE_UP_MEDIUM(MEDIUM_UP_SCALE_REQUEST),
        SCALINGTIME_NODE_UP_LARGE(LARGE_UP_SCALE_REQUEST),
        SCALINGTIME_NODE_DOWN_SMALL(SMALL_DOWN_SCALE_REQUEST),
        SCALINGTIME_NODE_DOWN_MEDIUM(MEDIUM_DOWN_SCALE_REQUEST),
        SCALINGTIME_NODE_DOWN_LARGE(LARGE_DOWN_SCALE_REQUEST);

        private final RequestType requestType;


        public String toMeterRegistryName() {
                return this.name().toLowerCase().replaceAll("_", ".");
        }


        // todo fix this indent
        public static NodeMetric findMetric(RequestType type) {
                for (NodeMetric metric : NodeMetric.values()) {
                        if (metric.requestType == type) {
                                return metric;
                        }
                }
                return null;
        }
}
